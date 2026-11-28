package com.hanzijourney.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {
    @Query("SELECT * FROM lesson_progress WHERE lessonId = :lessonId")
    suspend fun getLesson(lessonId: String): LessonProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLesson(entity: LessonProgressEntity)

    /** Returns the new row id, or -1 when an event with the same id already exists (nothing is changed). */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertXpEvent(entity: XpEventEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPracticeDay(entity: PracticeDayEntity)

    @Query("SELECT lessonId FROM lesson_progress")
    fun observeCompletedLessonIds(): Flow<List<String>>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM xp_event")
    fun observeTotalXp(): Flow<Int>

    @Query("SELECT localDate FROM practice_day")
    fun observePracticeDays(): Flow<List<String>>
}
