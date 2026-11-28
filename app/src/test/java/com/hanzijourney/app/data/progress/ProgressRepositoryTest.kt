package com.hanzijourney.app.data.progress

import com.hanzijourney.app.domain.progress.LessonProgress
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

/** In-memory store that behaves like the Room one: an XP event id can only be stored once. */
private class FakeProgressStore : ProgressStore {
    val lessons = MutableStateFlow<Map<String, LessonProgress>>(emptyMap())
    val events = MutableStateFlow<Map<String, XpEvent>>(emptyMap())
    val days = MutableStateFlow<Set<String>>(emptySet())

    override suspend fun getLesson(lessonId: String) = lessons.value[lessonId]
    override suspend fun saveLesson(progress: LessonProgress) { lessons.value = lessons.value + (progress.lessonId to progress) }
    override suspend fun insertXpEvent(event: XpEvent): Boolean {
        if (event.eventId in events.value) return false
        events.value = events.value + (event.eventId to event)
        return true
    }
    override suspend fun markPracticeDay(localDate: String) { days.value = days.value + localDate }
    override suspend fun <T> atomically(block: suspend ProgressStore.() -> T): T = block(this)
    override fun observeCompletedLessonIds(): Flow<Set<String>> = lessons.map { it.keys }
    override fun observeTotalXp(): Flow<Int> = events.map { e -> e.values.sumOf { it.amount } }
    override fun observePracticeDays(): Flow<Set<String>> = days
}

class ProgressRepositoryTest {
    private val clock = Clock.fixed(Instant.parse("2026-11-28T05:00:00Z"), ZoneId.of("UTC"))
    private val store = FakeProgressStore()
    private val repo = ProgressRepository(store, clock = clock)

    @Test fun firstCompletionAwardsXpOnce() = runBlocking {
        val c = repo.completeLesson("l1", correctFirstTry = 10, totalExercises = 12)
        assertTrue(c.firstTime)
        assertEquals(20, c.xpAwarded)
        assertEquals(setOf("lesson_first:l1"), store.events.value.keys)
        assertEquals(20, store.observeTotalXp().first())
    }

    @Test fun replayDoesNotAwardXpAgain() = runBlocking {
        repo.completeLesson("l1", 8, 12)
        val again = repo.completeLesson("l1", 12, 12)
        assertFalse(again.firstTime)
        assertEquals(0, again.xpAwarded)
        assertEquals(20, store.observeTotalXp().first())
        val saved = store.lessons.value.getValue("l1")
        assertEquals(2, saved.timesCompleted)
        assertEquals(12, saved.bestCorrect)
    }

    @Test fun lowerScoreOnReplayKeepsTheBestScore() = runBlocking {
        repo.completeLesson("l1", 12, 12)
        repo.completeLesson("l1", 5, 12)
        assertEquals(12, store.lessons.value.getValue("l1").bestCorrect)
    }

    @Test fun savingTheSameCompletionTwiceNeverDoublesXp() = runBlocking {
        repo.completeLesson("l1", 12, 12)
        repo.completeLesson("l1", 12, 12)
        repo.completeLesson("l1", 12, 12)
        assertEquals(1, store.events.value.size)
        assertEquals(20, store.observeTotalXp().first())
    }

    @Test fun eachLessonAwardsItsOwnXp() = runBlocking {
        repo.completeLesson("l1", 12, 12)
        repo.completeLesson("l2", 12, 12)
        assertEquals(40, store.observeTotalXp().first())
    }

    @Test fun completingMarksTodayAsPracticeDay() = runBlocking {
        repo.completeLesson("l1", 12, 12)
        assertEquals(setOf("2026-11-28"), store.days.value)
    }

    @Test fun snapshotReflectsSavedProgress() = runBlocking {
        repo.completeLesson("l1", 12, 12)
        repo.completeLesson("l2", 12, 12)
        val s = repo.observeSnapshot().first()
        assertEquals(40, s.totalXp)
        assertEquals(2, s.level.level)
        assertEquals(setOf("l1", "l2"), s.completedLessonIds)
        assertEquals(1, s.streak.current)
        assertTrue(s.streak.practicedToday)
    }
}
