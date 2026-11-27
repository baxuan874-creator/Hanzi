package com.hanzijourney.app.data.content

import kotlinx.serialization.json.Json

object ContentPackParser {
    private val json = Json { ignoreUnknownKeys = true }

    /** Throws IllegalArgumentException (SerializationException) when the JSON is malformed or a required field is missing. */
    fun parse(raw: String): ContentPack = json.decodeFromString(ContentPack.serializer(), raw)
}
