package com.hanzijourney.app.data.content

import java.security.MessageDigest

/** Where imported content lives. Implemented by Room in the app and by a fake in unit tests. */
interface ContentStore {
    suspend fun currentPackHash(): String?
    suspend fun replaceAll(pack: ContentPack, contentHash: String)
}

data class ImportCounts(val chapters: Int, val lessons: Int, val vocabulary: Int, val grammarPoints: Int, val exercises: Int)

sealed interface ImportResult {
    data class Imported(val counts: ImportCounts, val warnings: List<Issue>) : ImportResult
    data class AlreadyUpToDate(val contentHash: String) : ImportResult
    data class Rejected(val issues: List<Issue>) : ImportResult
    data class ParseError(val message: String) : ImportResult
}

class ContentImporter(private val store: ContentStore) {
    /** Parses, validates and stores a pack. The store is untouched unless the pack is valid. */
    suspend fun import(rawJson: String, requireApproved: Boolean = false): ImportResult {
        val hash = sha256(rawJson)
        if (store.currentPackHash() == hash) return ImportResult.AlreadyUpToDate(hash)
        val pack = try {
            ContentPackParser.parse(rawJson)
        } catch (e: IllegalArgumentException) {
            return ImportResult.ParseError(e.message ?: "invalid content pack")
        }
        val report = ContentValidator.validate(pack, requireApproved)
        if (!report.isValid) return ImportResult.Rejected(report.errors)
        store.replaceAll(pack, hash)
        return ImportResult.Imported(
            ImportCounts(pack.chapters.size, pack.lessons.size, pack.vocabulary.size, pack.grammarPoints.size, pack.exercises.size),
            report.warnings
        )
    }

    companion object {
        fun sha256(text: String): String =
            MessageDigest.getInstance("SHA-256").digest(text.toByteArray(Charsets.UTF_8)).joinToString("") { "%02x".format(it) }
    }
}
