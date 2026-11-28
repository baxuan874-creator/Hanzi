package com.hanzijourney.app.domain.lesson

import com.hanzijourney.app.data.content.ContentPackParser
import com.hanzijourney.app.data.content.ExerciseDto
import com.hanzijourney.app.data.content.GrammarPointDto
import com.hanzijourney.app.data.content.LessonDto
import com.hanzijourney.app.data.content.VocabularyDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

private fun exercise(id: String, type: String, options: List<String>?, answer: String) = ExerciseDto(
    id = id, lessonId = "l1", type = type, prompt = "p", options = options, answer = answer,
    explanationVi = "giải thích $id", difficulty = 1, sourceRef = "test"
)

private fun content() = LessonContent(
    lesson = LessonDto(
        id = "l1", curriculumId = "c", levelId = "lv", chapterId = "ch", titleVi = "Bài thử",
        objectives = listOf("m"), estimatedMinutes = 6
    ),
    vocabulary = listOf(
        VocabularyDto(
            id = "v1", curriculumId = "c", levelId = "lv", hanzi = "你", pinyin = "nǐ", meaningVi = "bạn",
            sourceRef = "t", licenseRef = "t", reviewStatus = "reviewed"
        )
    ),
    grammar = listOf(
        GrammarPointDto(
            id = "g1", curriculumId = "c", levelId = "lv", pattern = "p", explanationVi = "e", sourceRef = "t", reviewStatus = "reviewed"
        )
    ),
    exercises = listOf(
        exercise("e1", "multiple_choice", listOf("a", "b"), "a"),
        exercise("e2", "sentence_ordering", listOf("x", "y"), "x y")
    )
)

class ExerciseCheckerTest {
    @Test fun choiceNeedsExactAnswer() {
        val e = exercise("e", "multiple_choice", listOf("a", "b"), "a")
        assertTrue(ExerciseChecker.isCorrect(e, "a"))
        assertTrue(ExerciseChecker.isCorrect(e, " a "))
        assertFalse(ExerciseChecker.isCorrect(e, "b"))
    }

    @Test fun orderingIgnoresExtraSpacesButNotOrder() {
        val e = exercise("e", "sentence_ordering", listOf("x", "y"), "x y")
        assertTrue(ExerciseChecker.isCorrect(e, "x y"))
        assertTrue(ExerciseChecker.isCorrect(e, "x  y"))
        assertFalse(ExerciseChecker.isCorrect(e, "y x"))
    }
}

class LessonSessionTest {
    @Test fun stepsComeInTeachingOrder() {
        val s = LessonSession(content())
        assertTrue(s.current is LessonStep.Intro)
        s.next(); assertTrue(s.current is LessonStep.Vocab)
        s.next(); assertTrue(s.current is LessonStep.Grammar)
        s.next(); assertTrue((s.current as LessonStep.Practice).exercise.id == "e1")
    }

    @Test fun cannotSkipAnExerciseWithoutAnswering() {
        val s = LessonSession(content())
        repeat(3) { s.next() }
        val before = s.current
        s.next()
        assertEquals(before, s.current)
    }

    @Test fun allCorrectGivesFullScoreAndFinishes() {
        val s = LessonSession(content())
        repeat(3) { s.next() }
        assertTrue(s.submit("a")!!.correct); s.next()
        assertTrue(s.submit("x y")!!.correct); s.next()
        assertTrue(s.isFinished)
        assertNull(s.current)
        assertEquals(1f, s.progress, 0.0001f)
        val r = s.result()
        assertEquals(2, r.totalExercises); assertEquals(2, r.correctFirstTry); assertEquals(0, r.retried); assertEquals(1, r.wordsMet)
    }

    @Test fun wrongAnswerIsAskedAgainOnceAndNotCountedAsFirstTry() {
        val s = LessonSession(content())
        repeat(3) { s.next() }
        val fb = s.submit("b")!!
        assertFalse(fb.correct); assertEquals("a", fb.correctAnswer); assertEquals("giải thích e1", fb.explanationVi)
        s.next()
        assertTrue(s.submit("x y")!!.correct); s.next()
        val retry = s.current as LessonStep.Practice
        assertTrue(retry.isRetry); assertEquals("e1", retry.exercise.id)
        assertTrue(s.submit("a")!!.correct); s.next()
        assertTrue(s.isFinished)
        val r = s.result()
        assertEquals(1, r.correctFirstTry); assertEquals(1, r.retried)
    }

    @Test fun wrongRetryIsNotAskedAgain() {
        val s = LessonSession(content())
        repeat(3) { s.next() }
        s.submit("b"); s.next()
        s.submit("x y"); s.next()
        s.submit("b"); s.next()
        assertTrue(s.isFinished)
    }

    @Test fun submitIsIgnoredOutsideExercisesAndAfterAnswering() {
        val s = LessonSession(content())
        assertNull(s.submit("a"))
        repeat(3) { s.next() }
        assertNotNull(s.submit("a"))
        assertNull(s.submit("a"))
    }
}

class SampleLessonRunThroughTest {
    private fun sampleContent(): LessonContent {
        val f = File("../content_work/packs/hsk3_l1_sample_lesson004.pack.json")
        check(f.exists()) { "sample pack not found at ${f.absolutePath}" }
        val pack = ContentPackParser.parse(f.readText(Charsets.UTF_8))
        val lesson = pack.lessons.single()
        return LessonContent(
            lesson = lesson,
            vocabulary = lesson.vocabularyIds.map { id -> pack.vocabulary.first { it.id == id } },
            grammar = lesson.grammarIds.map { id -> pack.grammarPoints.first { it.id == id } },
            exercises = lesson.exerciseIds.map { id -> pack.exercises.first { it.id == id } }
        )
    }

    private fun runThrough(wrongFirst: Boolean): Pair<LessonSession, Int> {
        val c = sampleContent()
        val s = LessonSession(c)
        var steps = 0
        var firstExercise = true
        while (!s.isFinished) {
            val step = s.current
            if (step is LessonStep.Practice) {
                val answer = if (wrongFirst && firstExercise && !step.isRetry) "không đúng" else step.exercise.answer
                firstExercise = false
                assertNotNull(s.submit(answer))
            }
            s.next()
            steps++
            check(steps < 200) { "lesson did not finish" }
        }
        return s to steps
    }

    @Test fun everyExerciseOfTheSampleLessonCanBeAnsweredCorrectly() {
        val (s, steps) = runThrough(wrongFirst = false)
        val r = s.result()
        assertEquals(11, r.totalExercises); assertEquals(11, r.correctFirstTry); assertEquals(12, r.wordsMet)
        assertEquals(1 + 12 + 1 + 11, steps)
    }

    @Test fun oneWrongAnswerAddsOneRetryStep() {
        val (s, steps) = runThrough(wrongFirst = true)
        val r = s.result()
        assertEquals(10, r.correctFirstTry); assertEquals(1, r.retried)
        assertEquals(1 + 12 + 1 + 11 + 1, steps)
    }
}
