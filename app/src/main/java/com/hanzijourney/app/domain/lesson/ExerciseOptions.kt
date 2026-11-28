package com.hanzijourney.app.domain.lesson

import com.hanzijourney.app.data.content.ExerciseDto
import kotlin.random.Random

/**
 * Content packs list the correct option first. The player must never show them in that order,
 * so options (or sentence pieces) are shuffled each time an exercise is shown.
 */
object ExerciseOptions {
    fun shuffled(exercise: ExerciseDto, random: Random = Random.Default): List<String> {
        val options = exercise.options.orEmpty()
        if (options.size < 2) return options
        if (exercise.type != "sentence_ordering") return options.shuffled(random)
        // Sentence pieces: avoid showing them already in the right order (when a different order exists).
        val rightOrder = exercise.answer.split(" ").filter { it.isNotEmpty() }
        if (options.toSet().size < 2) return options
        var result = options.shuffled(random)
        var attempts = 0
        while (result == rightOrder && attempts < 20) {
            result = options.shuffled(random)
            attempts++
        }
        return result
    }
}
