package com.hanzijourney.app.domain.lesson

import com.hanzijourney.app.data.content.ExerciseDto

object ExerciseChecker {
    /**
     * Choice, fill-blank and text answers must equal the stored answer.
     * Sentence ordering compares the tokens (joined by single spaces), ignoring extra spaces.
     */
    fun isCorrect(exercise: ExerciseDto, userAnswer: String): Boolean = when (exercise.type) {
        "sentence_ordering" -> normalizeTokens(userAnswer) == normalizeTokens(exercise.answer)
        else -> userAnswer.trim() == exercise.answer.trim()
    }

    private fun normalizeTokens(text: String): String = text.split(" ").filter { it.isNotEmpty() }.joinToString(" ")
}
