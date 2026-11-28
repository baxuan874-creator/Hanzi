package com.hanzijourney.app.domain.lesson

import com.hanzijourney.app.data.content.ExerciseDto
import com.hanzijourney.app.data.content.GrammarPointDto
import com.hanzijourney.app.data.content.VocabularyDto

sealed interface LessonStep {
    data object Intro : LessonStep
    data class Vocab(val item: VocabularyDto) : LessonStep
    data class Grammar(val item: GrammarPointDto) : LessonStep
    data class Practice(val exercise: ExerciseDto, val isRetry: Boolean) : LessonStep
}

data class Feedback(val correct: Boolean, val correctAnswer: String, val explanationVi: String)

data class LessonResult(val totalExercises: Int, val correctFirstTry: Int, val wordsMet: Int, val retried: Int)

/**
 * One run through a lesson: intro, new words, grammar, exercises, result.
 * A wrong exercise is asked once more at the end (a second chance, PRD 5.2); only the first try counts for the score.
 */
class LessonSession(private val content: LessonContent) {
    private val queue = ArrayDeque<LessonStep>()
    private val retried = mutableSetOf<String>()
    private val firstTry = mutableMapOf<String, Boolean>()
    private var doneCount = 0

    var current: LessonStep? = null
        private set
    var feedback: Feedback? = null
        private set

    val isFinished: Boolean get() = current == null

    init {
        queue.addLast(LessonStep.Intro)
        content.vocabulary.forEach { queue.addLast(LessonStep.Vocab(it)) }
        content.grammar.forEach { queue.addLast(LessonStep.Grammar(it)) }
        content.exercises.forEach { queue.addLast(LessonStep.Practice(it, false)) }
        current = queue.removeFirstOrNull()
    }

    /** 0..1 share of steps already completed (grows when a retry is added). */
    val progress: Float
        get() {
            val total = doneCount + queue.size + (if (current != null) 1 else 0)
            return if (total == 0) 1f else doneCount.toFloat() / total
        }

    /** Checks an answer for the current exercise. Returns null if the current step is not an unanswered exercise. */
    fun submit(answer: String): Feedback? {
        val step = current as? LessonStep.Practice ?: return null
        if (feedback != null) return null
        val exercise = step.exercise
        val correct = ExerciseChecker.isCorrect(exercise, answer)
        if (!step.isRetry) firstTry[exercise.id] = correct
        if (!correct && !step.isRetry && retried.add(exercise.id)) queue.addLast(LessonStep.Practice(exercise, true))
        val result = Feedback(correct, exercise.answer, exercise.explanationVi)
        feedback = result
        return result
    }

    /** Moves on. An exercise must be answered first. */
    fun next() {
        val step = current ?: return
        if (step is LessonStep.Practice && feedback == null) return
        feedback = null
        doneCount++
        current = queue.removeFirstOrNull()
    }

    fun result(): LessonResult =
        LessonResult(content.exercises.size, firstTry.count { it.value }, content.vocabulary.size, retried.size)
}
