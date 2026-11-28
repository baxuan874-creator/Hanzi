package com.hanzijourney.app.domain.review

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ReviewSchedulerTest {
    private val minute = 60_000L
    private val day = 24 * 60 * minute
    private val t0 = 1_000_000L

    @Test fun newWordIsDueTenMinutesAfterLearning() {
        val item = ReviewScheduler.newItem("w1", t0)
        assertEquals(t0 + 10 * minute, item.dueAt)
        assertFalse(ReviewScheduler.isDue(item, t0 + 9 * minute))
        assertTrue(ReviewScheduler.isDue(item, t0 + 10 * minute))
    }

    @Test fun rememberingWalksUpTheIntervals() {
        var item = ReviewScheduler.newItem("w1", t0)
        val expected = listOf(1 * day, 3 * day, 7 * day, 14 * day, 14 * day, 14 * day)
        var now = t0
        expected.forEachIndexed { i, gap ->
            now += 1
            item = ReviewScheduler.answer(item, remembered = true, now = now)
            assertEquals("step $i", now + gap, item.dueAt)
            assertEquals(minOf(i + 1, 4), item.stage)
        }
        assertEquals(6, item.reviewCount)
        assertEquals(0, item.lapses)
    }

    @Test fun forgettingGoesBackToTheFirstStep() {
        var item = ReviewScheduler.newItem("w1", t0)
        repeat(3) { item = ReviewScheduler.answer(item, true, t0 + 1) }
        assertEquals(3, item.stage)
        val after = ReviewScheduler.answer(item, remembered = false, now = t0 + 5)
        assertEquals(0, after.stage)
        assertEquals(t0 + 5 + 10 * minute, after.dueAt)
        assertEquals(1, after.lapses)
        assertEquals(4, after.reviewCount)
        assertEquals(t0 + 5, after.lastReviewedAt)
    }

    @Test fun directionAlternatesByStage() {
        assertEquals(ReviewDirection.HANZI_TO_MEANING, ReviewScheduler.direction(0))
        assertEquals(ReviewDirection.MEANING_TO_HANZI, ReviewScheduler.direction(1))
        assertEquals(ReviewDirection.HANZI_TO_MEANING, ReviewScheduler.direction(2))
    }
}
