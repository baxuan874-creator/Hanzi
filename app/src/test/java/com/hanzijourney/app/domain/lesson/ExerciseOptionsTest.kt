package com.hanzijourney.app.domain.lesson

import com.hanzijourney.app.data.content.ExerciseDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

private fun ex(type: String, options: List<String>?, answer: String) = ExerciseDto(
    id = "e", lessonId = "l", type = type, prompt = "p", options = options, answer = answer,
    explanationVi = "x", difficulty = 1, sourceRef = "t"
)

class ExerciseOptionsTest {
    @Test fun shuffleKeepsTheSameOptions() {
        val e = ex("multiple_choice", listOf("a", "b", "c"), "a")
        repeat(50) { seed ->
            assertEquals(setOf("a", "b", "c"), ExerciseOptions.shuffled(e, Random(seed)).toSet())
            assertEquals(3, ExerciseOptions.shuffled(e, Random(seed)).size)
        }
    }

    @Test fun correctAnswerIsNotAlwaysFirst() {
        val e = ex("multiple_choice", listOf("a", "b", "c"), "a")
        val positions = (0 until 200).map { ExerciseOptions.shuffled(e, Random(it)).indexOf("a") }.toSet()
        assertEquals(setOf(0, 1, 2), positions)
    }

    @Test fun orderingPiecesAreNeverShownInTheRightOrder() {
        val e = ex("sentence_ordering", listOf("谢谢", "你"), "谢谢 你")
        repeat(100) { seed ->
            assertFalse(ExerciseOptions.shuffled(e, Random(seed)) == listOf("谢谢", "你"))
        }
        val e3 = ex("sentence_ordering", listOf("a", "b", "c"), "a b c")
        repeat(100) { seed -> assertFalse(ExerciseOptions.shuffled(e3, Random(seed)) == listOf("a", "b", "c")) }
    }

    @Test fun noOptionsOrOneOptionIsReturnedAsIs() {
        assertTrue(ExerciseOptions.shuffled(ex("text", null, "a")).isEmpty())
        assertEquals(listOf("a"), ExerciseOptions.shuffled(ex("multiple_choice", listOf("a"), "a")))
    }

    @Test fun matchingColumnsKeepAllPairsAndDoNotLineUp() {
        val e = ex("matching", listOf("a|1", "b|2", "c|3"), "a|1;b|2;c|3")
        repeat(100) { seed ->
            val (left, right) = ExerciseOptions.matchingColumns(e, Random(seed))
            assertEquals(3, left.size)
            assertEquals(left.toSet(), right.toSet())
            assertFalse(left == right)
        }
    }
}

class MatchingTest {
    @Test fun parsesPairsAndRejectsBadOnes() {
        assertEquals(MatchPair("你好", "xin chào"), Matching.parse("你好|xin chào"))
        assertEquals(null, Matching.parse("你好"))
        assertEquals(null, Matching.parse("|x"))
        assertEquals(null, Matching.parse("a|b|c"))
    }

    @Test fun displayWritesOneLinePerPair() {
        assertEquals("a – 1\nb – 2", Matching.display("a|1;b|2"))
    }

    @Test fun encodeAndNormalizeRoundTrip() {
        val encoded = Matching.encode(listOf(MatchPair("b", "2"), MatchPair("a", "1")))
        assertEquals("b|2;a|1", encoded)
        assertEquals(listOf("a|1", "b|2"), Matching.normalize(encoded))
    }
}
