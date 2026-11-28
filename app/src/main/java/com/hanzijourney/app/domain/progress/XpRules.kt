package com.hanzijourney.app.domain.progress

/** Starting XP values (PRD section 7); to be balanced after playtests. */
object XpRules {
    const val LESSON_FIRST_COMPLETION = 20
    const val KIND_LESSON_FIRST = "lesson_first"

    /** One card remembered in review. Per session at most ReviewScheduler.SESSION_SIZE cards, so at most that many XP. */
    const val REVIEW_REMEMBERED = 1
    const val KIND_REVIEW = "review"

    /** Same lesson, same id: XP for a lesson's first completion can only ever be recorded once. */
    fun lessonFirstEventId(lessonId: String): String = "$KIND_LESSON_FIRST:$lessonId"

    /** Same session and word: the XP of one remembered card can only be recorded once. */
    fun reviewEventId(sessionId: String, vocabularyId: String): String = "review:$sessionId:$vocabularyId"
}
