package com.hanzijourney.app.domain.review

import com.hanzijourney.app.data.content.VocabularyDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

private fun card(id: String) = ReviewCard(
    ReviewScheduler.newItem(id, 0),
    VocabularyDto(
        id = id, curriculumId = "c", levelId = "l", hanzi = id, pinyin = id, meaningVi = id,
        sourceRef = "s", licenseRef = "r", reviewStatus = "approved"
    )
)

class ReviewSessionTest {
    @Test fun answerIsIgnoredUntilRevealed() {
        val s = ReviewSession(listOf(card("a")))
        assertNull(s.answer(true))
        assertFalse(s.isFinished)
        s.reveal()
        assertEquals("a", s.answer(true)?.card?.vocab?.id)
        assertTrue(s.isFinished)
    }

    @Test fun rememberedCardsFinishTheSession() {
        val s = ReviewSession(listOf(card("a"), card("b")))
        s.reveal(); s.answer(true)
        assertEquals(0.5f, s.progress, 0.001f)
        s.reveal(); s.answer(true)
        assertTrue(s.isFinished)
        assertEquals(ReviewResult(2, 0), s.result())
        assertEquals(1f, s.progress, 0.001f)
    }

    @Test fun forgottenCardComesBackOnceAndTheRetryDoesNotCount() {
        val s = ReviewSession(listOf(card("a"), card("b")))
        s.reveal(); val first = s.answer(false)!!
        assertTrue(first.countsForSchedule)
        s.reveal(); s.answer(true)               // b
        assertEquals("a", s.current?.vocab?.id)  // a is back at the end
        assertTrue(s.isRetry)
        s.reveal(); val retry = s.answer(false)!! // forgetting it again does not re-queue it
        assertFalse(retry.countsForSchedule)
        assertTrue(s.isFinished)
        assertEquals(ReviewResult(1, 1), s.result())
        assertEquals(2, s.answeredCount)
    }

    @Test fun emptySessionIsFinishedImmediately() {
        val s = ReviewSession(emptyList())
        assertTrue(s.isFinished)
        assertNull(s.current)
        assertEquals(1f, s.progress, 0.001f)
    }
}
