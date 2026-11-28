package com.hanzijourney.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Local relational database for user data (content lives in content.db).
 * v1: CurriculumVersion. v2 (Phase 4): lesson progress, XP ledger, practice days.
 */
@Database(
    entities = [
        CurriculumVersionEntity::class,
        LessonProgressEntity::class,
        XpEventEntity::class,
        PracticeDayEntity::class
    ],
    version = 2,
    exportSchema = true
)
abstract class HanziDatabase : RoomDatabase() {
    abstract fun curriculumVersionDao(): CurriculumVersionDao
    abstract fun progressDao(): ProgressDao

    companion object {
        const val NAME = "hanzi.db"

        /** Adds the progress tables without touching existing data. */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `lesson_progress` (`lessonId` TEXT NOT NULL, `firstCompletedAt` INTEGER NOT NULL, " +
                        "`lastCompletedAt` INTEGER NOT NULL, `bestCorrect` INTEGER NOT NULL, `totalExercises` INTEGER NOT NULL, " +
                        "`timesCompleted` INTEGER NOT NULL, PRIMARY KEY(`lessonId`))"
                )
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `xp_event` (`eventId` TEXT NOT NULL, `kind` TEXT NOT NULL, `amount` INTEGER NOT NULL, " +
                        "`createdAt` INTEGER NOT NULL, `localDate` TEXT NOT NULL, PRIMARY KEY(`eventId`))"
                )
                db.execSQL("CREATE TABLE IF NOT EXISTS `practice_day` (`localDate` TEXT NOT NULL, PRIMARY KEY(`localDate`))")
            }
        }

        fun create(context: Context): HanziDatabase =
            Room.databaseBuilder(context.applicationContext, HanziDatabase::class.java, NAME)
                .addMigrations(MIGRATION_1_2)
                .build()
    }
}
