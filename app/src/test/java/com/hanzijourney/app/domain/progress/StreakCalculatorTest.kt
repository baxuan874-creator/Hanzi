package com.hanzijourney.app.domain.progress

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class StreakCalculatorTest {
    private val today = LocalDate.of(2026, 11, 28)
    private fun d(back: Long) = today.minusDays(back)

    @Test fun noPracticeMeansNoStreak() {
        assertEquals(Streak(0, 0, false), StreakCalculator.compute(emptySet(), today))
    }

    @Test fun practiceTodayCountsOne() {
        val s = StreakCalculator.compute(setOf(d(0)), today)
        assertEquals(1, s.current); assertEquals(1, s.best); assertTrue(s.practicedToday)
    }

    @Test fun streakStaysAliveUntilEndOfTheNextDay() {
        val s = StreakCalculator.compute(setOf(d(1), d(2)), today)
        assertEquals(2, s.current); assertFalse(s.practicedToday)
    }

    @Test fun missingAWholeDayResetsCurrentButKeepsBest() {
        val s = StreakCalculator.compute(setOf(d(2), d(3), d(4)), today)
        assertEquals(0, s.current); assertEquals(3, s.best)
    }

    @Test fun gapSplitsStreaks() {
        val s = StreakCalculator.compute(setOf(d(0), d(1), d(3), d(4), d(5), d(6)), today)
        assertEquals(2, s.current); assertEquals(4, s.best)
    }

    @Test fun datesInTheFutureAreIgnored() {
        val s = StreakCalculator.compute(setOf(d(0), today.plusDays(1), today.plusDays(2)), today)
        assertEquals(1, s.current); assertEquals(1, s.best)
    }
}
