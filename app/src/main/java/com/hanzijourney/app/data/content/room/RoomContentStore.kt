package com.hanzijourney.app.data.content.room

import androidx.room.withTransaction
import com.hanzijourney.app.data.content.ContentPack
import com.hanzijourney.app.data.content.ContentStore
import com.hanzijourney.app.data.content.ExampleDto
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

class RoomContentStore(
    private val db: ContentDatabase,
    private val clock: () -> Long = { System.currentTimeMillis() }
) : ContentStore {
    private val dao = db.contentDao()
    private val json = Json
    private val strings = ListSerializer(String.serializer())
    private val examples = ListSerializer(ExampleDto.serializer())

    override suspend fun currentPackHash(): String? = dao.getPack()?.contentHash

    override suspend fun replaceAll(pack: ContentPack, contentHash: String) {
        db.withTransaction {
            dao.clearExercises(); dao.clearGrammarPoints(); dao.clearVocabulary()
            dao.clearLessons(); dao.clearChapters(); dao.clearPack()
            val m = pack.manifest
            dao.insertPack(ContentPackEntity(1, m.curriculumId, m.version, m.source, m.retrievedAt, m.checksum, contentHash, clock()))
            dao.insertChapters(pack.chapters.mapIndexed { i, c ->
                ChapterEntity(c.id, c.titleVi, json.encodeToString(strings, c.lessonIds), i)
            })
            dao.insertLessons(pack.lessons.mapIndexed { i, l ->
                LessonEntity(
                    l.id, l.curriculumId, l.levelId, l.chapterId, l.titleVi,
                    json.encodeToString(strings, l.objectives), json.encodeToString(strings, l.vocabularyIds),
                    json.encodeToString(strings, l.grammarIds), json.encodeToString(strings, l.exerciseIds),
                    l.estimatedMinutes, json.encodeToString(strings, l.prerequisiteIds), l.contentVersion, i
                )
            })
            dao.insertVocabulary(pack.vocabulary.map { v ->
                VocabularyEntity(
                    v.id, v.curriculumId, v.levelId, v.hanzi, v.pinyin, v.meaningVi, v.wordType, v.topicId,
                    json.encodeToString(examples, v.examples), v.audioAssetId, v.sourceRef, v.licenseRef, v.reviewStatus
                )
            })
            dao.insertGrammarPoints(pack.grammarPoints.map { g ->
                GrammarPointEntity(
                    g.id, g.curriculumId, g.levelId, g.pattern, g.explanationVi, g.usageNoteVi, g.commonErrorVi,
                    json.encodeToString(examples, g.examples), g.sourceRef, g.reviewStatus
                )
            })
            dao.insertExercises(pack.exercises.mapIndexed { i, e ->
                ExerciseEntity(
                    e.id, e.lessonId, e.type, e.prompt, e.options?.let { json.encodeToString(strings, it) },
                    e.answer, e.explanationVi, e.difficulty, json.encodeToString(strings, e.skillTags), e.sourceRef, i
                )
            })
        }
    }
}
