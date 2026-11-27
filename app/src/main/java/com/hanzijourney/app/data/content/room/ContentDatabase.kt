package com.hanzijourney.app.data.content.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Content database, kept separate from user data (PRD section 10). It holds only data that can be
 * re-imported from the content pack, so a schema change may rebuild it (destructive migration).
 * User progress lives in HanziDatabase and needs real migrations.
 */
@Database(
    entities = [
        ContentPackEntity::class, ChapterEntity::class, LessonEntity::class,
        VocabularyEntity::class, GrammarPointEntity::class, ExerciseEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class ContentDatabase : RoomDatabase() {
    abstract fun contentDao(): ContentDao

    companion object {
        const val NAME = "content.db"

        fun create(context: Context): ContentDatabase =
            Room.databaseBuilder(context.applicationContext, ContentDatabase::class.java, NAME)
                .fallbackToDestructiveMigration()
                .build()
    }
}
