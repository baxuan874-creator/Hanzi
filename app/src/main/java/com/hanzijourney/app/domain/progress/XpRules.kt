package com.hanzijourney.app.domain.progress

/** Starting XP values (PRD section 7); to be balanced after playtests. */
object XpRules {
    const val LESSON_FIRST_COMPLETION = 20
    const val KIND_LESSON_FIRST = "lesson_first"

    /** Same lesson, same id: XP for a lesson's first completion can only ever be recorded once. */
    fun lessonFirstEventId(lessonId: String): String = "$KIND_LESSON_FIRST:$lessonId"
}
