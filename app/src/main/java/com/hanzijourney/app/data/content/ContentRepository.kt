package com.hanzijourney.app.data.content

import com.hanzijourney.app.data.content.room.ContentDao
import com.hanzijourney.app.data.content.room.ExerciseEntity
import com.hanzijourney.app.data.content.room.GrammarPointEntity
import com.hanzijourney.app.data.content.room.LessonEntity
import com.hanzijourney.app.data.content.room.VocabularyEntity
import com.hanzijourney.app.domain.lesson.LessonContent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import com.hanzijourney.app.domain.progress.ContentCatalog
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

data class LessonSummary(val id: String, val chapterTitle: String, val title: String, val wordCount: Int, val minutes: Int)

/** Reads imported content from Room and turns it back into the pack models. */
class ContentRepository(private val dao: ContentDao) {
    private val json = Json { ignoreUnknownKeys = true }
    private val strings = ListSerializer(String.serializer())
    private val examples = ListSerializer(ExampleDto.serializer())

    fun observeLessonSummaries(): Flow<List<LessonSummary>> =
        combine(dao.observeChapters(), dao.observeAllLessons()) { chapters, lessons ->
            val titles = chapters.associate { it.id to it.titleVi }
            lessons.map { l ->
                LessonSummary(
                    id = l.id,
                    chapterTitle = titles[l.chapterId] ?: "",
                    title = l.titleVi,
                    wordCount = json.decodeFromString(strings, l.vocabularyIdsJson).size,
                    minutes = l.estimatedMinutes
                )
            }
        }

    /** What exists in the imported content, used to plan daily missions and check chapter achievements. */
    suspend fun catalog(): ContentCatalog {
        val lessons = dao.observeAllLessons().first()
        return ContentCatalog(
            lessonIds = lessons.map { it.id }.toSet(),
            chapterLessons = lessons.groupBy { it.chapterId }.mapValues { (_, ls) -> ls.map { it.id }.toSet() },
            wordIds = lessons.flatMap { json.decodeFromString(strings, it.vocabularyIdsJson) }.toSet()
        )
    }

    /** Vocabulary entries for the given ids, in the same order (unknown ids are skipped). */
    suspend fun vocabulary(ids: List<String>): List<VocabularyDto> {
        if (ids.isEmpty()) return emptyList()
        val byId = dao.getVocabulary(ids).associateBy { it.id }
        return ids.mapNotNull { byId[it]?.toDto() }
    }

    suspend fun hasUnapprovedContent(): Boolean = dao.countNotApproved() > 0

    suspend fun loadLesson(lessonId: String): LessonContent? {
        val entity = dao.getLesson(lessonId) ?: return null
        val lesson = entity.toDto()
        val vocabById = dao.getVocabulary(lesson.vocabularyIds).associateBy { it.id }
        val grammarById = dao.getGrammarPoints(lesson.grammarIds).associateBy { it.id }
        val exercisesById = dao.getExercisesForLesson(lessonId).associateBy { it.id }
        return LessonContent(
            lesson = lesson,
            vocabulary = lesson.vocabularyIds.mapNotNull { vocabById[it]?.toDto() },
            grammar = lesson.grammarIds.mapNotNull { grammarById[it]?.toDto() },
            exercises = lesson.exerciseIds.mapNotNull { exercisesById[it]?.toDto() }
        )
    }

    private fun LessonEntity.toDto() = LessonDto(
        id = id, curriculumId = curriculumId, levelId = levelId, chapterId = chapterId, titleVi = titleVi,
        objectives = json.decodeFromString(strings, objectivesJson),
        vocabularyIds = json.decodeFromString(strings, vocabularyIdsJson),
        grammarIds = json.decodeFromString(strings, grammarIdsJson),
        exerciseIds = json.decodeFromString(strings, exerciseIdsJson),
        estimatedMinutes = estimatedMinutes,
        prerequisiteIds = json.decodeFromString(strings, prerequisiteIdsJson),
        contentVersion = contentVersion
    )

    private fun VocabularyEntity.toDto() = VocabularyDto(
        id = id, curriculumId = curriculumId, levelId = levelId, hanzi = hanzi, pinyin = pinyin, meaningVi = meaningVi,
        wordType = wordType, topicId = topicId, examples = json.decodeFromString(examples, examplesJson),
        audioAssetId = audioAssetId, sourceRef = sourceRef, licenseRef = licenseRef, reviewStatus = reviewStatus
    )

    private fun GrammarPointEntity.toDto() = GrammarPointDto(
        id = id, curriculumId = curriculumId, levelId = levelId, pattern = pattern, explanationVi = explanationVi,
        usageNoteVi = usageNoteVi, commonErrorVi = commonErrorVi, examples = json.decodeFromString(examples, examplesJson),
        sourceRef = sourceRef, reviewStatus = reviewStatus
    )

    private fun ExerciseEntity.toDto() = ExerciseDto(
        id = id, lessonId = lessonId, type = type, prompt = prompt,
        options = optionsJson?.let { json.decodeFromString(strings, it) },
        answer = answer, explanationVi = explanationVi, difficulty = difficulty,
        skillTags = json.decodeFromString(strings, skillTagsJson), sourceRef = sourceRef
    )
}
