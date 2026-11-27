package com.hanzijourney.app.data.content

import android.content.Context
import java.io.FileNotFoundException

/** Reads a content pack from app assets. Returns null when the asset is not bundled. */
class AssetContentSource(private val context: Context) {
    fun readPack(assetPath: String): String? = try {
        context.assets.open(assetPath).bufferedReader(Charsets.UTF_8).use { it.readText() }
    } catch (e: FileNotFoundException) {
        null
    }
}
