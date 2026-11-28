package com.hanzijourney.app.data.progress

import com.hanzijourney.app.domain.progress.Achievement
import com.hanzijourney.app.domain.progress.AchievementContext
import com.hanzijourney.app.domain.progress.AchievementRules
import com.hanzijourney.app.domain.progress.ContentCatalog
import com.hanzijourney.app.domain.progress.DailyMission
import com.hanzijourney.app.domain.progress.DailyMissionPlanner
import com.hanzijourney.app.domain.progress.LessonCompletion
import com.hanzijourney.app.domain.progress.LessonProgress
import com.hanzijourney.app.domain.progress.LevelRules
import com.hanzijourney.app.domain.progress.MissionTypes
import com.hanzijourney.app.domain.progress.ProgressSnapshot
import com.hanzijourney.app.domain.progress.StreakCalculator
import com.hanzijourney.app.domain.progress.XpEvent
import com.hanzijourney.app.domain.progress.XpRules
import com.hanzijourney.app.domain.review.ReviewItem
import com.hanzijourney.app.domain.review.ReviewScheduler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import java.time.Clock
import java.time.Instant
import java.time.LocalDate

class ProgressRepository(
    private val store: ProgressStore,
    private val levelRules: LevelRules = LevelRules.Default,
    private val clock: Clock = Clock.systemDefaultZone()
) {
    /**
     * Saves a finished lesson and everything that follows from it, in one transaction:
     * practice day, first-completion XP, newly learned words, daily-mission progress (and mission XP),
     * and achievements. Every XP award uses a fixed event id, so replays or repeated calls never award it twice.
     */
    suspend fun completeLesson(
        lessonId: String,
        correctFirstTry: Int,
        totalExercises: Int,
        vocabularyIds: List<String>,
        catalog: ContentCatalog
    ): LessonCompletion {
        val now = Instant.now(clock).toEpochMilli()
        val today = LocalDate.now(clock).toString()
        return store.atomically {
            // Missions are planned before this lesson's words count as learned.
            ensureMissions(today, catalog)

            val existing = getLesson(lessonId)
            saveLesson(
                LessonProgress(
                    lessonId = lessonId,
                    firstCompletedAt = existing?.firstCompletedAt ?: now,
                    lastCompletedAt = now,
                    bestCorrect = maxOf(existing?.bestCorrect ?: 0, correctFirstTry),
                    totalExercises = totalExercises,
                    timesCompleted = (existing?.timesCompleted ?: 0) + 1
                )
            )
            markPracticeDay(today)
            val lessonXpStored = insertXpEvent(
                XpEvent(XpRules.lessonFirstEventId(lessonId), XpRules.KIND_LESSON_FIRST, XpRules.LESSON_FIRST_COMPLETION, now, today)
            )

            val newWords = vocabularyIds.distinct().count { id ->
                val isNew = markWordLearned(id, now)
                // A newly learned word joins the review schedule (first review due 10 minutes later).
                if (isNew) createReviewItem(ReviewScheduler.newItem(id, now))
                isNew
            }

            val completedMissions = mutableListOf<DailyMission>()
            for (mission in getMissions(today)) {
                if (mission.done) continue
                val step = when (mission.type) {
                    MissionTypes.COMPLETE_LESSON -> 1
                    MissionTypes.NEW_WORDS -> newWords
                    else -> 0
                }
                if (step == 0) continue
                val progress = minOf(mission.target, mission.progress + step)
                if (progress >= mission.target) {
                    val done = mission.copy(progress = progress, completedAt = now)
                    saveMission(done)
                    val rewardStored = insertXpEvent(
                        XpEvent(DailyMissionPlanner.rewardEventId(mission.id), "daily_mission", mission.rewardXp, now, today)
                    )
                    if (rewardStored) completedMissions += done
                } else {
                    saveMission(mission.copy(progress = progress))
                }
            }

            val unlockedNow = unlockEarned(now, catalog)

            LessonCompletion(
                firstTime = existing == null,
                xpAwarded = if (lessonXpStored) XpRules.LESSON_FIRST_COMPLETION else 0,
                completedMissions = completedMissions,
                newAchievements = unlockedNow
            )
        }
    }

    /** Result of one review answer. [xpAwarded] is 0 when forgotten or when this answer was already paid. */
    data class ReviewOutcome(val xpAwarded: Int, val newAchievements: List<Achievement>)

    /**
     * Saves one review answer right away (so a session interrupted by closing the app loses nothing):
     * moves the word on the schedule, marks today as a practice day, awards 1 XP for a remembered card
     * (event id per session and word, so it is paid once) and checks achievements.
     */
    suspend fun recordReview(sessionId: String, vocabularyId: String, remembered: Boolean): ReviewOutcome {
        val now = Instant.now(clock).toEpochMilli()
        val today = LocalDate.now(clock).toString()
        return store.atomically {
            val item = getReviewItem(vocabularyId) ?: return@atomically ReviewOutcome(0, emptyList())
            saveReviewItem(ReviewScheduler.answer(item, remembered, now))
            markPracticeDay(today)
            val paid = remembered && insertXpEvent(
                XpEvent(XpRules.reviewEventId(sessionId, vocabularyId), XpRules.KIND_REVIEW, XpRules.REVIEW_REMEMBERED, now, today)
            )
            ReviewOutcome(if (paid) XpRules.REVIEW_REMEMBERED else 0, unlockEarned(now, null))
        }
    }

    /** Words due for review now, most overdue first. */
    suspend fun dueReviewItems(limit: Int = ReviewScheduler.SESSION_SIZE): List<ReviewItem> =
        store.getDueReviewItems(Instant.now(clock).toEpochMilli(), limit)

    /** Number of words due now; re-read every 30 seconds so it follows the clock while a screen is open. */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeDueCount(): Flow<Int> = flow {
        while (true) {
            emit(Instant.now(clock).toEpochMilli())
            delay(30_000)
        }
    }.flatMapLatest { now -> store.observeDueCount(now) }

    private suspend fun ProgressStore.unlockEarned(now: Long, catalog: ContentCatalog?): List<Achievement> {
        val completedIds = getCompletedLessonIds()
        val bestStreak = StreakCalculator.compute(parseDays(getPracticeDays()), LocalDate.now(clock)).best
        return AchievementRules.unlocked(
            AchievementContext(
                completedLessons = completedIds.size,
                learnedWords = getLearnedWordIds().size,
                chaptersCompleted = catalog?.let { AchievementRules.chaptersCompleted(it, completedIds) } ?: 0,
                bestStreak = bestStreak,
                totalReviews = getTotalReviews()
            )
        ).filter { unlockAchievement(it.id, now) }
    }

    /** Plans today's missions if none exist yet. A day's missions are never re-planned once created. */
    suspend fun ensureTodayMissions(catalog: ContentCatalog) {
        val today = LocalDate.now(clock).toString()
        store.atomically { ensureMissions(today, catalog) }
    }

    private suspend fun ProgressStore.ensureMissions(today: String, catalog: ContentCatalog) {
        if (getMissions(today).isNotEmpty()) return
        val unlearned = (catalog.wordIds - getLearnedWordIds()).size
        DailyMissionPlanner.plan(catalog.lessonIds.size, unlearned).forEach { plan ->
            saveMission(
                DailyMission(
                    id = DailyMissionPlanner.missionId(today, plan.type),
                    localDate = today,
                    type = plan.type,
                    target = plan.target,
                    progress = 0,
                    rewardXp = plan.rewardXp,
                    completedAt = null
                )
            )
        }
    }

    fun observeCompletedLessonIds(): Flow<Set<String>> = store.observeCompletedLessonIds()

    fun observeSnapshot(): Flow<ProgressSnapshot> {
        val today = LocalDate.now(clock)
        val base = combine(
            store.observeTotalXp(),
            store.observeCompletedLessonIds(),
            store.observePracticeDays()
        ) { totalXp, completed, days -> Triple(totalXp, completed, days) }
        return combine(
            base,
            store.observeLearnedWordCount(),
            store.observeMissions(today.toString()),
            store.observeUnlockedAchievementIds(),
            store.observeTotalReviews()
        ) { (totalXp, completed, days), words, missions, achievements, reviews ->
            ProgressSnapshot(
                totalXp = totalXp,
                level = levelRules.info(totalXp),
                streak = StreakCalculator.compute(parseDays(days), LocalDate.now(clock)),
                completedLessonIds = completed,
                learnedWordCount = words,
                missions = missions,
                unlockedAchievements = achievements.mapNotNull { Achievement.fromId(it) }.toSet(),
                totalReviews = reviews
            )
        }
    }

    private fun parseDays(days: Set<String>): Set<LocalDate> =
        days.mapNotNull { runCatching { LocalDate.parse(it) }.getOrNull() }.toSet()
}
