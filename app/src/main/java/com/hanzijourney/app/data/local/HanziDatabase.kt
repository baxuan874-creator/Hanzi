package com.hanzijourney.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Local relational database. Phase 0 only holds CurriculumVersion;
 * content and user-progress tables are added in later phases with migrations.
 */
@Database(entities = [CurriculumVersionEntity::class], version = 1, exportSchema = true)
abstract class HanziDatabase : RoomDatabase() {
    abstract fun curriculumVersionDao(): CurriculumVersionDao

    companion object {
        const val NAME = "hanzi.db"

        fun create(context: Context): HanziDatabase =
            Room.databaseBuilder(context.applicationContext, HanziDatabase::class.java, NAME).build()
    }
}
