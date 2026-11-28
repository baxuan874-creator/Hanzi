package com.hanzijourney.app.data.content

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/** Checks that the real sample pack in the repo parses and passes the SAME validator the app uses. */
class SamplePackTest {
    private fun load(): String {
        // Gradle runs unit tests with the module directory (app/) as working directory.
        val f = File("../content_work/packs/hsk3_l1_sample_lesson004.pack.json")
        check(f.exists()) { "sample pack not found at ${f.absolutePath}" }
        return f.readText(Charsets.UTF_8)
    }

    @Test fun samplePackParsesAndValidates() {
        val pack = ContentPackParser.parse(load())
        val report = ContentValidator.validate(pack)
        assertTrue(report.errors.toString(), report.isValid)
        assertEquals(12, pack.vocabulary.size)
        assertEquals(1, pack.grammarPoints.size)
        assertEquals(11, pack.exercises.size)
        assertEquals(1, pack.lessons.size)
    }

    @Test fun samplePackIsNotAcceptedAsReleaseContent() {
        // It is a draft: a release build (requireApproved = true) must refuse it.
        val report = ContentValidator.validate(ContentPackParser.parse(load()), requireApproved = true)
        assertTrue(report.errors.any { it.message.contains("approved required") })
    }

    @Test fun samplePackImportsIntoAStore() = runBlocking {
        val store = object : ContentStore {
            var hash: String? = null
            var count = 0
            override suspend fun currentPackHash(): String? = hash
            override suspend fun replaceAll(pack: ContentPack, contentHash: String) { hash = contentHash; count = pack.vocabulary.size }
        }
        val result = ContentImporter(store).import(load())
        assertTrue(result.toString(), result is ImportResult.Imported)
        assertEquals(12, store.count)
    }
}
