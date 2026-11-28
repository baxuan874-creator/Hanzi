package com.hanzijourney.app.data.progress

import androidx.room.withTransaction
import com.hanzijourney.app.data.local.HanziDatabase
import com.hanzijourney.app.data.local.LessonProgressEntity
import com.hanzijourney.app.data.local.PracticeDayEntity
import com.hanzijourney.app.data.local.XpEventEntity
import com.hanzijourney.app.domain.progress.LessonProgress
import com.hanzijourney.app.domain.progress.XpEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomProgressStore(private val database: HanziDatabase) : ProgressStore {
    private val dao get() = database.progressDao()

    override suspend fun getLesson(lessonId: String): LessonProgress? = dao.getLesson(lessonId)?.let {
        LessonProgress(it.lessonId, it.firstCompletedAt, it.lastCompletedAt, it.bestCorrect, it.totalExercises, it.timesCompleted)
    }

    override suspend fun saveLesson(progress: LessonProgress) {
        dao.upsertLesson(
            LessonProgressEntity(
                progress.lessonId, progress.firstCompletedAt, progress.lastCompletedAt,
                progress.bestCorrect, progress.totalExercises, progress.timesCompleted
            )
        )
    }

    override suspend fun insertXpEvent(event: XpEvent): Boolean =
        dao.insertXpEvent(XpEventEntity(event.eventId, event.kind, event.amount, event.createdAt, event.localDate)) != -1L

    override suspend fun markPracticeDay(localDate: String) {
        dao.insertPracticeDay(PracticeDayEntity(localDate))
    }

    override suspend fun <T> atomically(block: suspend ProgressStore.() -> T): T =
        database.withTransaction { block(this@RoomProgressStore) }

    override fun observeCompletedLessonIds(): Flow<Set<String>> = dao.observeCompletedLessonIds().map { it.toSet() }
    override fun observeTotalXp(): Flow<Int> = dao.observeTotalXp()
    override fun observePracticeDays(): Flow<Set<String>> = dao.observePracticeDays().map { it.toSet() }
}
