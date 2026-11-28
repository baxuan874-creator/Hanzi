package com.hanzijourney.app.data.content.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ContentDao {
    @Query("DELETE FROM content_pack") suspend fun clearPack()
    @Query("DELETE FROM chapter") suspend fun clearChapters()
    @Query("DELETE FROM lesson") suspend fun clearLessons()
    @Query("DELETE FROM vocabulary") suspend fun clearVocabulary()
    @Query("DELETE FROM grammar_point") suspend fun clearGrammarPoints()
    @Query("DELETE FROM exercise") suspend fun clearExercises()

    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertPack(entity: ContentPackEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertChapters(items: List<ChapterEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertLessons(items: List<LessonEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertVocabulary(items: List<VocabularyEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertGrammarPoints(items: List<GrammarPointEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertExercises(items: List<ExerciseEntity>)

    @Query("SELECT * FROM content_pack WHERE id = 1") suspend fun getPack(): ContentPackEntity?
    @Query("SELECT * FROM chapter ORDER BY sortOrder") fun observeChapters(): Flow<List<ChapterEntity>>
    @Query("SELECT * FROM lesson WHERE chapterId = :chapterId ORDER BY sortOrder")
    fun observeLessons(chapterId: String): Flow<List<LessonEntity>>
    @Query("SELECT * FROM lesson ORDER BY sortOrder") fun observeAllLessons(): Flow<List<LessonEntity>>
    @Query("SELECT COUNT(*) FROM vocabulary WHERE reviewStatus != 'approved'") suspend fun countNotApproved(): Int
    @Query("SELECT * FROM lesson WHERE id = :id") suspend fun getLesson(id: String): LessonEntity?
    @Query("SELECT * FROM vocabulary WHERE id IN (:ids)") suspend fun getVocabulary(ids: List<String>): List<VocabularyEntity>
    @Query("SELECT * FROM grammar_point WHERE id IN (:ids)") suspend fun getGrammarPoints(ids: List<String>): List<GrammarPointEntity>
    @Query("SELECT * FROM exercise WHERE lessonId = :lessonId ORDER BY sortOrder")
    suspend fun getExercisesForLesson(lessonId: String): List<ExerciseEntity>
}
