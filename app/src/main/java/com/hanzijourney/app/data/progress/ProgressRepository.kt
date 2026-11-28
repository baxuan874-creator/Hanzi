package com.hanzijourney.app.data.progress

import com.hanzijourney.app.domain.progress.LessonCompletion
import com.hanzijourney.app.domain.progress.LessonProgress
import com.hanzijourney.app.domain.progress.LevelRules
import com.hanzijourney.app.domain.progress.ProgressSnapshot
import com.hanzijourney.app.domain.progress.StreakCalculator
import com.hanzijourney.app.domain.progress.XpEvent
import com.hanzijourney.app.domain.progress.XpRules
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.Clock
import java.time.Instant
import java.time.LocalDate

class ProgressRepository(
    private val store: ProgressStore,
    private val levelRules: LevelRules = LevelRules.Default,
    private val clock: Clock = Clock.systemDefaultZone()
) {
    /**
     * Saves a finished lesson, marks today as a practice day and awards first-completion XP.
     * Everything happens in one transaction. XP uses a fixed event id per lesson, so replaying the lesson,
     * or calling this twice, can never award it again.
     */
    suspend fun completeLesson(lessonId: String, correctFirstTry: Int, totalExercises: Int): LessonCompletion {
        val now = Instant.now(clock).toEpochMilli()
        val today = LocalDate.now(clock).toString()
        return store.atomically {
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
            val stored = insertXpEvent(
                XpEvent(
                    eventId = XpRules.lessonFirstEventId(lessonId),
                    kind = XpRules.KIND_LESSON_FIRST,
                    amount = XpRules.LESSON_FIRST_COMPLETION,
                    createdAt = now,
                    localDate = today
                )
            )
            LessonCompletion(firstTime = existing == null, xpAwarded = if (stored) XpRules.LESSON_FIRST_COMPLETION else 0)
        }
    }

    fun observeCompletedLessonIds(): Flow<Set<String>> = store.observeCompletedLessonIds()

    fun observeSnapshot(): Flow<ProgressSnapshot> = combine(
        store.observeTotalXp(),
        store.observeCompletedLessonIds(),
        store.observePracticeDays()
    ) { totalXp, completed, days ->
        val parsed = days.mapNotNull { runCatching { LocalDate.parse(it) }.getOrNull() }.toSet()
        ProgressSnapshot(
            totalXp = totalXp,
            level = levelRules.info(totalXp),
            streak = StreakCalculator.compute(parsed, LocalDate.now(clock)),
            completedLessonIds = completed
        )
    }
}
