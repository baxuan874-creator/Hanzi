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
 * v3 (Phase 4): daily missions, achievements, learned words.
 * v4 (Phase 5): review schedule (review_item).
 */
@Database(
    entities = [
        CurriculumVersionEntity::class,
        LessonProgressEntity::class,
        XpEventEntity::class,
        PracticeDayEntity::class,
        DailyMissionEntity::class,
        UserAchievementEntity::class,
        LearnedWordEntity::class,
        ReviewItemEntity::class
    ],
    version = 4,
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

        /** Adds missions, achievements and learned words without touching existing data. */
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `daily_mission` (`missionId` TEXT NOT NULL, `localDate` TEXT NOT NULL, `type` TEXT NOT NULL, " +
                        "`target` INTEGER NOT NULL, `progress` INTEGER NOT NULL, `rewardXp` INTEGER NOT NULL, `completedAt` INTEGER, " +
                        "PRIMARY KEY(`missionId`))"
                )
                db.execSQL("CREATE TABLE IF NOT EXISTS `user_achievement` (`achievementId` TEXT NOT NULL, `unlockedAt` INTEGER NOT NULL, PRIMARY KEY(`achievementId`))")
                db.execSQL("CREATE TABLE IF NOT EXISTS `learned_word` (`vocabularyId` TEXT NOT NULL, `firstSeenAt` INTEGER NOT NULL, PRIMARY KEY(`vocabularyId`))")
            }
        }

        /**
         * Adds the review schedule. Words already learned get a review item, first due 10 minutes after
         * they were learned (so they are due right away for words learned earlier).
         */
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `review_item` (`vocabularyId` TEXT NOT NULL, `stage` INTEGER NOT NULL, `dueAt` INTEGER NOT NULL, " +
                        "`lastReviewedAt` INTEGER NOT NULL, `reviewCount` INTEGER NOT NULL, `lapses` INTEGER NOT NULL, " +
                        "`createdAt` INTEGER NOT NULL, PRIMARY KEY(`vocabularyId`))"
                )
                db.execSQL(
                    "INSERT OR IGNORE INTO `review_item` (`vocabularyId`, `stage`, `dueAt`, `lastReviewedAt`, `reviewCount`, `lapses`, `createdAt`) " +
                        "SELECT `vocabularyId`, 0, `firstSeenAt` + 600000, 0, 0, 0, `firstSeenAt` FROM `learned_word`"
                )
            }
        }

        fun create(context: Context): HanziDatabase =
            Room.databaseBuilder(context.applicationContext, HanziDatabase::class.java, NAME)
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                .build()
    }
}
