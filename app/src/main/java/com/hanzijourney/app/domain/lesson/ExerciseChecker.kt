package com.hanzijourney.app.domain.lesson

import com.hanzijourney.app.data.content.ExerciseDto

object ExerciseChecker {
    /**
     * Choice, fill-blank and text answers must equal the stored answer.
     * Matching compares the pairs in any order.
     * Sentence ordering compares the tokens (joined by single spaces), ignoring extra spaces.
     */
    fun isCorrect(exercise: ExerciseDto, userAnswer: String): Boolean = when (exercise.type) {
        "sentence_ordering" -> normalizeTokens(userAnswer) == normalizeTokens(exercise.answer)
        "matching" -> Matching.normalize(userAnswer).let { it.isNotEmpty() && it == Matching.normalize(exercise.answer) }
        else -> userAnswer.trim() == exercise.answer.trim()
    }

    private fun normalizeTokens(text: String): String = text.split(" ").filter { it.isNotEmpty() }.joinToString(" ")
}
