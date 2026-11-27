package com.hanzijourney.app.data.content

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Content pack format. Field names follow CONTENT_SCHEMA.md. Holds no user progress. */
@Serializable
data class ContentManifest(
    @SerialName("curriculum_id") val curriculumId: String,
    val version: String,
    val source: String,
    @SerialName("retrieved_at") val retrievedAt: String,
    val checksum: String
)

@Serializable
data class ExampleDto(
    val hanzi: String,
    val pinyin: String,
    @SerialName("meaning_vi") val meaningVi: String
)

@Serializable
data class ChapterDto(
    val id: String,
    @SerialName("title_vi") val titleVi: String,
    @SerialName("lesson_ids") val lessonIds: List<String> = emptyList()
)

@Serializable
data class LessonDto(
    val id: String,
    @SerialName("curriculum_id") val curriculumId: String,
    @SerialName("level_id") val levelId: String,
    @SerialName("chapter_id") val chapterId: String,
    @SerialName("title_vi") val titleVi: String,
    val objectives: List<String> = emptyList(),
    @SerialName("vocabulary_ids") val vocabularyIds: List<String> = emptyList(),
    @SerialName("grammar_ids") val grammarIds: List<String> = emptyList(),
    @SerialName("exercise_ids") val exerciseIds: List<String> = emptyList(),
    @SerialName("estimated_minutes") val estimatedMinutes: Int,
    @SerialName("prerequisite_ids") val prerequisiteIds: List<String> = emptyList(),
    @SerialName("content_version") val contentVersion: String = ""
)

@Serializable
data class VocabularyDto(
    val id: String,
    @SerialName("curriculum_id") val curriculumId: String,
    @SerialName("level_id") val levelId: String,
    val hanzi: String,
    val pinyin: String,
    @SerialName("meaning_vi") val meaningVi: String,
    @SerialName("word_type") val wordType: String? = null,
    @SerialName("topic_id") val topicId: String? = null,
    val examples: List<ExampleDto> = emptyList(),
    @SerialName("audio_asset_id") val audioAssetId: String? = null,
    @SerialName("source_ref") val sourceRef: String,
    @SerialName("license_ref") val licenseRef: String,
    @SerialName("review_status") val reviewStatus: String
)

@Serializable
data class GrammarPointDto(
    val id: String,
    @SerialName("curriculum_id") val curriculumId: String,
    @SerialName("level_id") val levelId: String,
    val pattern: String,
    @SerialName("explanation_vi") val explanationVi: String,
    @SerialName("usage_note_vi") val usageNoteVi: String? = null,
    @SerialName("common_error_vi") val commonErrorVi: String? = null,
    val examples: List<ExampleDto> = emptyList(),
    @SerialName("source_ref") val sourceRef: String,
    @SerialName("review_status") val reviewStatus: String
)

@Serializable
data class ExerciseDto(
    val id: String,
    @SerialName("lesson_id") val lessonId: String,
    val type: String,
    val prompt: String,
    val options: List<String>? = null,
    val answer: String,
    @SerialName("explanation_vi") val explanationVi: String,
    val difficulty: Int,
    @SerialName("skill_tags") val skillTags: List<String> = emptyList(),
    @SerialName("source_ref") val sourceRef: String
)

@Serializable
data class ContentPack(
    val manifest: ContentManifest,
    val chapters: List<ChapterDto> = emptyList(),
    val lessons: List<LessonDto> = emptyList(),
    val vocabulary: List<VocabularyDto> = emptyList(),
    @SerialName("grammar_points") val grammarPoints: List<GrammarPointDto> = emptyList(),
    val exercises: List<ExerciseDto> = emptyList()
)
