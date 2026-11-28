package com.hanzijourney.app.domain.review

/** A word waiting to be reviewed. Times are epoch milliseconds; lastReviewedAt is 0 until the first review. */
data class ReviewItem(
    val vocabularyId: String,
    val stage: Int,
    val dueAt: Long,
    val lastReviewedAt: Long,
    val reviewCount: Int,
    val lapses: Int,
    val createdAt: Long
)

enum class ReviewDirection { HANZI_TO_MEANING, MEANING_TO_HANZI }

/**
 * Spaced-practice schedule (PRD 5.3). The gaps 10 minutes, 1 day, 3 days, 7 days, 14 days are only a starting
 * heuristic, not a claim of scientific optimum; tune them with real usage data.
 *
 * A new word is first due [INTERVALS] 0 after it is learned. Remembering moves it one stage up (longer gap,
 * the last gap repeats); forgetting sends it back to stage 0. Nothing is ever removed from the schedule.
 */
object ReviewScheduler {
    private const val MINUTE = 60_000L
    private const val DAY = 24 * 60 * MINUTE

    val INTERVALS: List<Long> = listOf(10 * MINUTE, 1 * DAY, 3 * DAY, 7 * DAY, 14 * DAY)

    /** Cards per review session; also bounds the review XP a session can give. */
    const val SESSION_SIZE = 10

    fun newItem(vocabularyId: String, now: Long) =
        ReviewItem(vocabularyId, stage = 0, dueAt = now + INTERVALS.first(), lastReviewedAt = 0, reviewCount = 0, lapses = 0, createdAt = now)

    fun isDue(item: ReviewItem, now: Long) = item.dueAt <= now

    fun answer(item: ReviewItem, remembered: Boolean, now: Long): ReviewItem {
        val stage = if (remembered) minOf(item.stage + 1, INTERVALS.lastIndex) else 0
        return item.copy(
            stage = stage,
            dueAt = now + INTERVALS[stage],
            lastReviewedAt = now,
            reviewCount = item.reviewCount + 1,
            lapses = item.lapses + if (remembered) 0 else 1
        )
    }

    /** Alternates the question direction by stage so a word is recalled both ways over time. */
    fun direction(stage: Int) = if (stage % 2 == 0) ReviewDirection.HANZI_TO_MEANING else ReviewDirection.MEANING_TO_HANZI
}
