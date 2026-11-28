package com.hanzijourney.app.data.progress

import com.hanzijourney.app.domain.progress.Achievement
import com.hanzijourney.app.domain.progress.ContentCatalog
import com.hanzijourney.app.domain.progress.DailyMission
import com.hanzijourney.app.domain.progress.LessonProgress
import com.hanzijourney.app.domain.progress.MissionTypes
import com.hanzijourney.app.domain.progress.XpEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

/** In-memory store that behaves like the Room one: ids can only be stored once. */
private class FakeProgressStore : ProgressStore {
    val lessons = MutableStateFlow<Map<String, LessonProgress>>(emptyMap())
    val events = MutableStateFlow<Map<String, XpEvent>>(emptyMap())
    val days = MutableStateFlow<Set<String>>(emptySet())
    val missions = MutableStateFlow<Map<String, DailyMission>>(emptyMap())
    val words = MutableStateFlow<Set<String>>(emptySet())
    val achievements = MutableStateFlow<Set<String>>(emptySet())

    override suspend fun getLesson(lessonId: String) = lessons.value[lessonId]
    override suspend fun saveLesson(progress: LessonProgress) { lessons.value = lessons.value + (progress.lessonId to progress) }
    override suspend fun insertXpEvent(event: XpEvent): Boolean {
        if (event.eventId in events.value) return false
        events.value = events.value + (event.eventId to event)
        return true
    }
    override suspend fun markPracticeDay(localDate: String) { days.value = days.value + localDate }
    override suspend fun getCompletedLessonIds(): Set<String> = lessons.value.keys
    override suspend fun getPracticeDays(): Set<String> = days.value
    override suspend fun getMissions(localDate: String) = missions.value.values.filter { it.localDate == localDate }.sortedBy { it.id }
    override suspend fun saveMission(mission: DailyMission) { missions.value = missions.value + (mission.id to mission) }
    override suspend fun markWordLearned(vocabularyId: String, at: Long): Boolean {
        if (vocabularyId in words.value) return false
        words.value = words.value + vocabularyId
        return true
    }
    override suspend fun getLearnedWordIds(): Set<String> = words.value
    override suspend fun unlockAchievement(achievementId: String, at: Long): Boolean {
        if (achievementId in achievements.value) return false
        achievements.value = achievements.value + achievementId
        return true
    }
    override suspend fun <T> atomically(block: suspend ProgressStore.() -> T): T = block(this)
    override fun observeCompletedLessonIds(): Flow<Set<String>> = lessons.map { it.keys }
    override fun observeTotalXp(): Flow<Int> = events.map { e -> e.values.sumOf { it.amount } }
    override fun observePracticeDays(): Flow<Set<String>> = days
    override fun observeMissions(localDate: String): Flow<List<DailyMission>> =
        missions.map { m -> m.values.filter { it.localDate == localDate }.sortedBy { it.id } }
    override fun observeLearnedWordCount(): Flow<Int> = words.map { it.size }
    override fun observeUnlockedAchievementIds(): Flow<Set<String>> = achievements
}

class ProgressRepositoryTest {
    private val clock = Clock.fixed(Instant.parse("2026-11-28T05:00:00Z"), ZoneId.of("UTC"))
    private val store = FakeProgressStore()
    private val repo = ProgressRepository(store, clock = clock)

    /** No lessons or words known: no missions are planned, so only lesson XP is in play. */
    private val noMissions = ContentCatalog(emptySet(), emptyMap(), emptySet())

    /** Two lessons in one chapter and 30 words. */
    private val catalog = ContentCatalog(
        lessonIds = setOf("l1", "l2"),
        chapterLessons = mapOf("c1" to setOf("l1", "l2")),
        wordIds = (1..30).map { "w$it" }.toSet()
    )
    private fun words(range: IntRange) = range.map { "w$it" }

    // ---- lesson XP (idempotency)

    @Test fun firstCompletionAwardsXpOnce() = runBlocking {
        val c = repo.completeLesson("l1", 10, 12, emptyList(), noMissions)
        assertTrue(c.firstTime)
        assertEquals(20, c.xpAwarded)
        assertEquals(setOf("lesson_first:l1"), store.events.value.keys)
        assertEquals(20, store.observeTotalXp().first())
    }

    @Test fun replayDoesNotAwardXpAgain() = runBlocking {
        repo.completeLesson("l1", 8, 12, emptyList(), noMissions)
        val again = repo.completeLesson("l1", 12, 12, emptyList(), noMissions)
        assertFalse(again.firstTime)
        assertEquals(0, again.xpAwarded)
        assertEquals(20, store.observeTotalXp().first())
        val saved = store.lessons.value.getValue("l1")
        assertEquals(2, saved.timesCompleted)
        assertEquals(12, saved.bestCorrect)
    }

    @Test fun lowerScoreOnReplayKeepsTheBestScore() = runBlocking {
        repo.completeLesson("l1", 12, 12, emptyList(), noMissions)
        repo.completeLesson("l1", 5, 12, emptyList(), noMissions)
        assertEquals(12, store.lessons.value.getValue("l1").bestCorrect)
    }

    @Test fun savingTheSameCompletionManyTimesNeverDoublesXp() = runBlocking {
        repeat(3) { repo.completeLesson("l1", 12, 12, words(1..12), catalog) }
        assertEquals(45, store.observeTotalXp().first()) // 20 lesson + 10 + 15 missions, each exactly once
        assertEquals(3, store.events.value.size)
    }

    @Test fun eachLessonAwardsItsOwnXp() = runBlocking {
        repo.completeLesson("l1", 12, 12, emptyList(), noMissions)
        repo.completeLesson("l2", 12, 12, emptyList(), noMissions)
        assertEquals(40, store.observeTotalXp().first())
    }

    @Test fun completingMarksTodayAsPracticeDay() = runBlocking {
        repo.completeLesson("l1", 12, 12, emptyList(), noMissions)
        assertEquals(setOf("2026-11-28"), store.days.value)
    }

    // ---- daily missions

    @Test fun missionsAreOnlyPlannedFromAvailableContent() = runBlocking {
        repo.ensureTodayMissions(catalog)
        assertEquals(setOf(MissionTypes.COMPLETE_LESSON, MissionTypes.NEW_WORDS), store.missions.value.values.map { it.type }.toSet())

        val small = FakeProgressStore()
        ProgressRepository(small, clock = clock).ensureTodayMissions(ContentCatalog(setOf("l1"), emptyMap(), (1..5).map { "w$it" }.toSet()))
        assertEquals(listOf(MissionTypes.COMPLETE_LESSON), small.missions.value.values.map { it.type })

        val none = FakeProgressStore()
        ProgressRepository(none, clock = clock).ensureTodayMissions(noMissions)
        assertTrue(none.missions.value.isEmpty())
    }

    @Test fun todaysMissionsAreNeverReplanned() = runBlocking {
        repo.ensureTodayMissions(catalog)
        val first = store.missions.value
        repo.ensureTodayMissions(ContentCatalog(setOf("l1"), emptyMap(), emptySet()))
        assertEquals(first, store.missions.value)
    }

    @Test fun lessonCompletesBothMissionsAndPaysThemOnce() = runBlocking {
        val c = repo.completeLesson("l1", 12, 12, words(1..12), catalog)
        assertEquals(2, c.completedMissions.size)
        assertEquals(45, store.observeTotalXp().first())
        val replay = repo.completeLesson("l1", 12, 12, words(1..12), catalog)
        assertTrue(replay.completedMissions.isEmpty())
        assertEquals(45, store.observeTotalXp().first())
    }

    @Test fun newWordsMissionAccumulatesAndRepeatedWordsDoNotCount() = runBlocking {
        repo.completeLesson("l1", 12, 12, words(1..6), catalog)
        val mid = store.missions.value.values.first { it.type == MissionTypes.NEW_WORDS }
        assertEquals(6, mid.progress); assertFalse(mid.done)

        repo.completeLesson("l1", 12, 12, words(1..6), catalog) // same words again: no progress
        assertEquals(6, store.missions.value.values.first { it.type == MissionTypes.NEW_WORDS }.progress)

        val c = repo.completeLesson("l2", 12, 12, words(7..12), catalog)
        val done = store.missions.value.values.first { it.type == MissionTypes.NEW_WORDS }
        assertTrue(done.done); assertEquals(10, done.progress)
        assertEquals(listOf(MissionTypes.NEW_WORDS), c.completedMissions.map { it.type })
        assertEquals(65, store.observeTotalXp().first()) // 20 + 10 (lesson mission) + 20 + 15 (words mission)
    }

    // ---- achievements

    @Test fun firstLessonAchievementIsReportedOnce() = runBlocking {
        assertEquals(listOf(Achievement.FIRST_LESSON), repo.completeLesson("l1", 12, 12, emptyList(), noMissions).newAchievements)
        assertTrue(repo.completeLesson("l1", 12, 12, emptyList(), noMissions).newAchievements.isEmpty())
    }

    @Test fun wordsAndChapterAchievementsUnlockWhenReached() = runBlocking {
        repo.completeLesson("l1", 12, 12, words(1..12), catalog)
        val c = repo.completeLesson("l2", 12, 12, words(13..25), catalog)
        assertEquals(setOf(Achievement.WORDS_25, Achievement.CHAPTER_COMPLETE), c.newAchievements.toSet())
    }

    @Test fun chapterAchievementNeedsEveryLessonOfTheChapter() = runBlocking {
        val c = repo.completeLesson("l1", 12, 12, words(1..12), catalog)
        assertFalse(Achievement.CHAPTER_COMPLETE in c.newAchievements)
    }

    @Test fun sevenConsecutiveDaysUnlockThePracticeAchievement() = runBlocking {
        var last = emptyList<Achievement>()
        for (day in 0..6) {
            val dayClock = Clock.fixed(Instant.parse("2026-11-22T05:00:00Z").plusSeconds(day * 86_400L), ZoneId.of("UTC"))
            last = ProgressRepository(store, clock = dayClock).completeLesson("l1", 12, 12, emptyList(), noMissions).newAchievements
            if (day < 6) assertFalse(Achievement.SEVEN_DAY_PRACTICE in last)
        }
        assertTrue(Achievement.SEVEN_DAY_PRACTICE in last)
    }

    // ---- snapshot

    @Test fun snapshotReflectsSavedProgress() = runBlocking {
        repo.completeLesson("l1", 12, 12, words(1..12), catalog)
        repo.completeLesson("l2", 12, 12, words(13..14), catalog)
        val s = repo.observeSnapshot().first()
        assertEquals(setOf("l1", "l2"), s.completedLessonIds)
        assertEquals(14, s.learnedWordCount)
        assertEquals(1, s.streak.current)
        assertTrue(s.streak.practicedToday)
        assertEquals(2, s.missions.size)
        assertTrue(Achievement.FIRST_LESSON in s.unlockedAchievements)
        assertEquals(store.observeTotalXp().first(), s.totalXp)
    }
}
