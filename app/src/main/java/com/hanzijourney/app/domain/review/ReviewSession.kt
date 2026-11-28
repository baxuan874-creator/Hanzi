package com.hanzijourney.app.domain.review

import com.hanzijourney.app.data.content.VocabularyDto

data class ReviewCard(val item: ReviewItem, val vocab: VocabularyDto) {
    val direction: ReviewDirection get() = ReviewScheduler.direction(item.stage)
}

/** What happened on one answer. [countsForSchedule] is false for a second try of a forgotten card. */
data class ReviewAnswer(val card: ReviewCard, val remembered: Boolean, val countsForSchedule: Boolean)

data class ReviewResult(val remembered: Int, val forgotten: Int)

/**
 * One review session over a list of due cards (active recall, PRD 5.2): the learner recalls first, then reveals the
 * answer and says whether they remembered. A forgotten card comes back once more at the end of the session;
 * that second try never changes the schedule or gives XP.
 */
class ReviewSession(cards: List<ReviewCard>) {
    private data class Entry(val card: ReviewCard, val retry: Boolean)

    private val queue = ArrayDeque(cards.map { Entry(it, retry = false) })
    private val total = cards.size
    private var firstPassAnswered = 0
    private var remembered = 0
    private var forgotten = 0

    var revealed: Boolean = false
        private set

    val current: ReviewCard? get() = queue.firstOrNull()?.card
    val isRetry: Boolean get() = queue.firstOrNull()?.retry == true
    val isFinished: Boolean get() = queue.isEmpty()
    val progress: Float get() = if (total == 0) 1f else firstPassAnswered.toFloat() / total
    val answeredCount: Int get() = firstPassAnswered
    val totalCount: Int get() = total

    fun reveal() {
        if (current != null) revealed = true
    }

    /** Ignored (returns null) until the answer has been revealed. */
    fun answer(remembered: Boolean): ReviewAnswer? {
        val entry = queue.firstOrNull() ?: return null
        if (!revealed) return null
        queue.removeFirst()
        revealed = false
        if (!entry.retry) {
            firstPassAnswered++
            if (remembered) this.remembered++ else {
                forgotten++
                queue.addLast(Entry(entry.card, retry = true))
            }
        }
        return ReviewAnswer(entry.card, remembered, countsForSchedule = !entry.retry)
    }

    fun result() = ReviewResult(remembered, forgotten)
}
