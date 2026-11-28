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

    @Query("SELECT lessonId FROM lesson_progress")
    suspend fun getCompletedLessonIds(): List<String>

    @Query("SELECT localDate FROM practice_day")
    suspend fun getPracticeDays(): List<String>

    @Query("SELECT * FROM daily_mission WHERE localDate = :localDate ORDER BY missionId")
    suspend fun getMissions(localDate: String): List<DailyMissionEntity>

    @Query("SELECT * FROM daily_mission WHERE localDate = :localDate ORDER BY missionId")
    fun observeMissions(localDate: String): Flow<List<DailyMissionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMission(entity: DailyMissionEntity)

    /** Returns -1 when the word was already learned. */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLearnedWord(entity: LearnedWordEntity): Long

    @Query("SELECT vocabularyId FROM learned_word")
    suspend fun getLearnedWordIds(): List<String>

    @Query("SELECT COUNT(*) FROM learned_word")
    fun observeLearnedWordCount(): Flow<Int>

    /** Returns -1 when the achievement was already unlocked. */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAchievement(entity: UserAchievementEntity): Long

    @Query("SELECT achievementId FROM user_achievement")
    suspend fun getAchievementIds(): List<String>

    @Query("SELECT achievementId FROM user_achievement")
    fun observeAchievementIds(): Flow<List<String>>

    @Query("SELECT * FROM review_item WHERE vocabularyId = :id")
    suspend fun getReviewItem(id: String): ReviewItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertReviewItem(entity: ReviewItemEntity)

    /** Returns -1 when the word already has a review item. */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertReviewItem(entity: ReviewItemEntity): Long

    @Query("SELECT * FROM review_item WHERE dueAt <= :now ORDER BY dueAt ASC, vocabularyId ASC LIMIT :limit")
    suspend fun getDueReviewItems(now: Long, limit: Int): List<ReviewItemEntity>

    @Query("SELECT COUNT(*) FROM review_item WHERE dueAt <= :now")
    fun observeDueCount(now: Long): Flow<Int>

    @Query("SELECT COALESCE(SUM(reviewCount), 0) FROM review_item")
    suspend fun getTotalReviews(): Int

    @Query("SELECT COALESCE(SUM(reviewCount), 0) FROM review_item")
    fun observeTotalReviews(): Flow<Int>
}
