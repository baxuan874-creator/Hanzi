package com.hanzijourney.app.domain.lesson

import com.hanzijourney.app.data.content.ExerciseDto

data class MatchPair(val left: String, val right: String)

/**
 * "matching" exercises: every option is one pair written "left|right" (e.g. "你好|xin chào");
 * the answer is the same pairs joined by ";". Order does not matter when checking.
 */
object Matching {
    const val MIN_PAIRS = 2
    const val MAX_PAIRS = 5

    fun parse(text: String): MatchPair? {
        val parts = text.split("|")
        if (parts.size != 2) return null
        val left = parts[0].trim()
        val right = parts[1].trim()
        return if (left.isEmpty() || right.isEmpty()) null else MatchPair(left, right)
    }

    fun pairs(exercise: ExerciseDto): List<MatchPair> = exercise.options.orEmpty().mapNotNull { parse(it) }

    fun encode(pairs: List<MatchPair>): String = pairs.joinToString(";") { "${it.left}|${it.right}" }

    /** Pairs of an answer string, trimmed and sorted, so two answers compare equal whatever their order. */
    fun normalize(answer: String): List<String> =
        answer.split(";").mapNotNull { parse(it.trim()) }.map { "${it.left}|${it.right}" }.sorted()

    /** Human-readable correct answer: one "left – right" line per pair. */
    fun display(answer: String): String =
        answer.split(";").mapNotNull { parse(it.trim()) }.joinToString("\n") { "${it.left} – ${it.right}" }
}
