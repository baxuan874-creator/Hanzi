package com.hanzijourney.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CurriculumVersionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: CurriculumVersionEntity)

    @Query("SELECT * FROM curriculum_version ORDER BY retrievedAt DESC")
    fun observeAll(): Flow<List<CurriculumVersionEntity>>

    @Query("SELECT * FROM curriculum_version WHERE id = :id")
    suspend fun getById(id: String): CurriculumVersionEntity?
}
