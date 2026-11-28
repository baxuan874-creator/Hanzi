package com.hanzijourney.app.data.progress

import com.hanzijourney.app.domain.progress.LessonProgress
import com.hanzijourney.app.domain.progress.XpEvent
import kotlinx.coroutines.flow.Flow

/** Storage of user progress. Separate from the content database; implemented with Room and with a fake in tests. */
interface ProgressStore {
    suspend fun getLesson(lessonId: String): LessonProgress?
    suspend fun saveLesson(progress: LessonProgress)

    /** Stores the event unless one with the same id already exists. Returns true only if it was stored. */
    suspend fun insertXpEvent(event: XpEvent): Boolean

    /** Records that the user practised on this local day (yyyy-MM-dd). Safe to call repeatedly. */
    suspend fun markPracticeDay(localDate: String)

    /** Runs several writes as one unit: all of them are saved, or none. */
    suspend fun <T> atomically(block: suspend ProgressStore.() -> T): T

    fun observeCompletedLessonIds(): Flow<Set<String>>
    fun observeTotalXp(): Flow<Int>
    fun observePracticeDays(): Flow<Set<String>>
}
