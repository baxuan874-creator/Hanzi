package com.hanzijourney.app.data.content

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** Synthetic fixture used only by tests. It is NOT bundled in the app and is not HSK content. */
private val FIXTURE = """
{
  "manifest": {"curriculum_id": "test_track", "version": "0.0.1", "source": "test_fixture", "retrieved_at": "2026-01-01", "checksum": "none"},
  "chapters": [{"id": "ch1", "title_vi": "Chương thử", "lesson_ids": ["l1", "l2"]}],
  "lessons": [
    {"id": "l1", "curriculum_id": "test_track", "level_id": "lv1", "chapter_id": "ch1", "title_vi": "Bài 1",
     "objectives": ["Mục tiêu 1"], "vocabulary_ids": ["v1", "v2"], "grammar_ids": [], "exercise_ids": ["e1"],
     "estimated_minutes": 6, "prerequisite_ids": [], "content_version": "0.0.1"},
    {"id": "l2", "curriculum_id": "test_track", "level_id": "lv1", "chapter_id": "ch1", "title_vi": "Bài 2",
     "objectives": ["Mục tiêu 2"], "vocabulary_ids": ["v3"], "grammar_ids": ["g1"], "exercise_ids": ["e2"],
     "estimated_minutes": 7, "prerequisite_ids": ["l1"], "content_version": "0.0.1"}
  ],
  "vocabulary": [
    {"id": "v1", "curriculum_id": "test_track", "level_id": "lv1", "hanzi": "你", "pinyin": "nǐ", "meaning_vi": "bạn",
     "examples": [{"hanzi": "你好", "pinyin": "nǐ hǎo", "meaning_vi": "xin chào"}],
     "source_ref": "test_fixture", "license_ref": "test_fixture", "review_status": "reviewed"},
    {"id": "v2", "curriculum_id": "test_track", "level_id": "lv1", "hanzi": "好", "pinyin": "hǎo", "meaning_vi": "tốt",
     "source_ref": "test_fixture", "license_ref": "test_fixture", "review_status": "reviewed"},
    {"id": "v3", "curriculum_id": "test_track", "level_id": "lv1", "hanzi": "谢谢", "pinyin": "xièxie", "meaning_vi": "cảm ơn",
     "word_type": "verb", "source_ref": "test_fixture", "license_ref": "test_fixture", "review_status": "reviewed"}
  ],
  "grammar_points": [
    {"id": "g1", "curriculum_id": "test_track", "level_id": "lv1", "pattern": "A + 好", "explanation_vi": "Giải thích thử",
     "examples": [{"hanzi": "你好", "pinyin": "nǐ hǎo", "meaning_vi": "xin chào"}],
     "source_ref": "test_fixture", "review_status": "reviewed"}
  ],
  "exercises": [
    {"id": "e1", "lesson_id": "l1", "type": "multiple_choice", "prompt": "你 nghĩa là gì?", "options": ["bạn", "tốt"],
     "answer": "bạn", "explanation_vi": "你 là bạn.", "difficulty": 1, "skill_tags": ["vocabulary"], "source_ref": "test_fixture"},
    {"id": "e2", "lesson_id": "l2", "type": "fill_blank", "prompt": "你___", "answer": "好",
     "explanation_vi": "Điền 好.", "difficulty": 1, "source_ref": "test_fixture"}
  ]
}
""".trimIndent()

private fun pack(): ContentPack = ContentPackParser.parse(FIXTURE)

private class FakeContentStore : ContentStore {
    var hash: String? = null
    var stored: ContentPack? = null
    var writes = 0
    override suspend fun currentPackHash(): String? = hash
    override suspend fun replaceAll(pack: ContentPack, contentHash: String) {
        stored = pack; hash = contentHash; writes++
    }
}

class ContentParserTest {
    @Test fun parsesFixtureWithExpectedCounts() {
        val p = pack()
        assertEquals(1, p.chapters.size)
        assertEquals(2, p.lessons.size)
        assertEquals(3, p.vocabulary.size)
        assertEquals(1, p.grammarPoints.size)
        assertEquals(2, p.exercises.size)
        assertEquals("test_track", p.manifest.curriculumId)
        assertNull(p.vocabulary[1].wordType)
        assertEquals("verb", p.vocabulary[2].wordType)
    }

    @Test(expected = IllegalArgumentException::class)
    fun malformedJsonThrows() {
        ContentPackParser.parse("{ not json")
    }

    @Test(expected = IllegalArgumentException::class)
    fun missingRequiredFieldThrows() {
        ContentPackParser.parse("""{"manifest": {"curriculum_id": "x"}}""")
    }
}

class ContentValidatorTest {
    private fun errors(p: ContentPack, requireApproved: Boolean = false) =
        ContentValidator.validate(p, requireApproved).errors.map { it.message }

    @Test fun fixtureIsValid() {
        val report = ContentValidator.validate(pack())
        assertTrue(report.errors.toString(), report.isValid)
    }

    @Test fun detectsDuplicateIds() {
        val p = pack()
        val msgs = errors(p.copy(vocabulary = p.vocabulary + p.vocabulary[0]))
        assertTrue(msgs.any { it.contains("duplicate") })
    }

    @Test fun detectsUnknownVocabularyReference() {
        val p = pack()
        val bad = p.lessons[0].copy(vocabularyIds = p.lessons[0].vocabularyIds + "nope")
        assertTrue(errors(p.copy(lessons = listOf(bad, p.lessons[1]))).any { it.contains("unknown vocabulary id nope") })
    }

    @Test fun detectsWordIntroducedTwice() {
        val p = pack()
        val bad = p.lessons[1].copy(vocabularyIds = listOf("v3", "v1"))
        assertTrue(errors(p.copy(lessons = listOf(p.lessons[0], bad))).any { it.contains("already introduced") })
    }

    @Test fun detectsMixedCurriculumTracks() {
        val p = pack()
        val bad = p.vocabulary[0].copy(curriculumId = "other_track")
        assertTrue(errors(p.copy(vocabulary = listOf(bad) + p.vocabulary.drop(1))).any { it.contains("must not be mixed") })
    }

    @Test fun detectsAnswerNotInOptions() {
        val p = pack()
        val bad = p.exercises[0].copy(answer = "không có")
        assertTrue(errors(p.copy(exercises = listOf(bad, p.exercises[1]))).any { it.contains("not one of the options") })
    }

    @Test fun detectsBadPrerequisite() {
        val p = pack()
        val bad = p.lessons[1].copy(prerequisiteIds = listOf("l2"))
        assertTrue(errors(p.copy(lessons = listOf(p.lessons[0], bad))).any { it.contains("its own prerequisite") })
    }

    @Test fun detectsEmptyPinyin() {
        val p = pack()
        val bad = p.vocabulary[0].copy(pinyin = "")
        assertTrue(errors(p.copy(vocabulary = listOf(bad) + p.vocabulary.drop(1))).any { it.contains("empty pinyin") })
    }

    @Test fun warnsButDoesNotFailWhenPinyinHasNoToneMark() {
        val p = pack()
        val changed = p.vocabulary[0].copy(pinyin = "ni")
        val report = ContentValidator.validate(p.copy(vocabulary = listOf(changed) + p.vocabulary.drop(1)))
        assertTrue(report.isValid)
        assertTrue(report.warnings.any { it.message.contains("no tone mark") })
    }

    @Test fun requireApprovedRejectsReviewedContent() {
        assertTrue(errors(pack(), requireApproved = true).any { it.contains("approved required") })
    }
}

class ContentImporterTest {
    @Test fun importsValidPackOnce() = runBlocking {
        val store = FakeContentStore()
        val importer = ContentImporter(store)
        val first = importer.import(FIXTURE)
        assertTrue(first is ImportResult.Imported)
        assertEquals(3, (first as ImportResult.Imported).counts.vocabulary)
        assertEquals(1, store.writes)
        val second = importer.import(FIXTURE)
        assertTrue(second is ImportResult.AlreadyUpToDate)
        assertEquals(1, store.writes)
    }

    @Test fun parseErrorLeavesStoreUntouched() = runBlocking {
        val store = FakeContentStore()
        val result = ContentImporter(store).import("{ broken")
        assertTrue(result is ImportResult.ParseError)
        assertNull(store.stored)
        assertEquals(0, store.writes)
    }

    @Test fun invalidPackIsRejectedAndStoreUntouched() = runBlocking {
        val store = FakeContentStore()
        val broken = FIXTURE.replace("\"answer\": \"bạn\"", "\"answer\": \"không có\"")
        val result = ContentImporter(store).import(broken)
        assertTrue(result is ImportResult.Rejected)
        assertNull(store.stored)
        assertEquals(0, store.writes)
    }

    @Test fun changedPackReplacesStoredContent() = runBlocking {
        val store = FakeContentStore()
        val importer = ContentImporter(store)
        importer.import(FIXTURE)
        val changed = FIXTURE.replace("\"version\": \"0.0.1\"", "\"version\": \"0.0.2\"")
        val result = importer.import(changed)
        assertTrue(result is ImportResult.Imported)
        assertEquals(2, store.writes)
        assertNotNull(store.stored)
        assertEquals("0.0.2", store.stored!!.manifest.version)
    }

    @Test fun hashIsStableAndDependsOnContent() {
        assertEquals(ContentImporter.sha256("a"), ContentImporter.sha256("a"))
        assertTrue(ContentImporter.sha256("a") != ContentImporter.sha256("b"))
        assertEquals(64, ContentImporter.sha256("a").length)
    }
}
