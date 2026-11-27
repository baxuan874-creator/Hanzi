package com.hanzijourney.app.data.content.room

import androidx.room.Entity
import androidx.room.PrimaryKey

// Lists are stored as JSON text so the schema stays simple; content is re-importable from the pack.

@Entity(tableName = "content_pack")
data class ContentPackEntity(
    @PrimaryKey val id: Int = 1,
    val curriculumId: String,
    val version: String,
    val source: String,
    val retrievedAt: String,
    val checksum: String,
    val contentHash: String,
    val importedAtEpochMs: Long
)

@Entity(tableName = "chapter")
data class ChapterEntity(
    @PrimaryKey val id: String,
    val titleVi: String,
    val lessonIdsJson: String,
    val sortOrder: Int
)

@Entity(tableName = "lesson")
data class LessonEntity(
    @PrimaryKey val id: String,
    val curriculumId: String,
    val levelId: String,
    val chapterId: String,
    val titleVi: String,
    val objectivesJson: String,
    val vocabularyIdsJson: String,
    val grammarIdsJson: String,
    val exerciseIdsJson: String,
    val estimatedMinutes: Int,
    val prerequisiteIdsJson: String,
    val contentVersion: String,
    val sortOrder: Int
)

@Entity(tableName = "vocabulary")
data class VocabularyEntity(
    @PrimaryKey val id: String,
    val curriculumId: String,
    val levelId: String,
    val hanzi: String,
    val pinyin: String,
    val meaningVi: String,
    val wordType: String?,
    val topicId: String?,
    val examplesJson: String,
    val audioAssetId: String?,
    val sourceRef: String,
    val licenseRef: String,
    val reviewStatus: String
)

@Entity(tableName = "grammar_point")
data class GrammarPointEntity(
    @PrimaryKey val id: String,
    val curriculumId: String,
    val levelId: String,
    val pattern: String,
    val explanationVi: String,
    val usageNoteVi: String?,
    val commonErrorVi: String?,
    val examplesJson: String,
    val sourceRef: String,
    val reviewStatus: String
)

@Entity(tableName = "exercise")
data class ExerciseEntity(
    @PrimaryKey val id: String,
    val lessonId: String,
    val type: String,
    val prompt: String,
    val optionsJson: String?,
    val answer: String,
    val explanationVi: String,
    val difficulty: Int,
    val skillTagsJson: String,
    val sourceRef: String,
    val sortOrder: Int
)
