package com.hanzijourney.app.data.content

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed interface LoadStatus {
    /** No content pack is bundled in this build (the normal state of a release build for now). */
    data object NoPackBundled : LoadStatus
    data class Done(val result: ImportResult) : LoadStatus
}

/**
 * Imports the bundled content pack once per content change. Debug builds bundle the draft sample pack;
 * release builds bundle nothing yet and, if they ever do, accept only approved content.
 */
class ContentLoader(
    private val assets: AssetContentSource,
    private val importer: ContentImporter,
    private val debugBuild: Boolean
) {
    suspend fun ensureLoaded(): LoadStatus {
        val raw = withContext(Dispatchers.IO) { assets.readPack(SAMPLE_PACK_ASSET) } ?: return LoadStatus.NoPackBundled
        return LoadStatus.Done(importer.import(raw, requireApproved = !debugBuild))
    }

    companion object {
        const val SAMPLE_PACK_ASSET = "hsk3_l1_sample_lesson004.pack.json"
    }
}
