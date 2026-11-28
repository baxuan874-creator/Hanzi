package com.hanzijourney.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lesson_progress")
data class LessonProgressEntity(
    @PrimaryKey val lessonId: String,
    val firstCompletedAt: Long,
    val lastCompletedAt: Long,
    val bestCorrect: Int,
    val totalExercises: Int,
    val timesCompleted: Int
)

/** XP ledger. The primary key is the idempotency key. */
@Entity(tableName = "xp_event")
data class XpEventEntity(
    @PrimaryKey val eventId: String,
    val kind: String,
    val amount: Int,
    val createdAt: Long,
    val localDate: String
)

/** One row per local day (yyyy-MM-dd) with any practice; streaks are computed from these rows. */
@Entity(tableName = "practice_day")
data class PracticeDayEntity(@PrimaryKey val localDate: String)

/** A daily mission. Target and reward are stored with the row, so later rule changes never alter past days. */
@Entity(tableName = "daily_mission")
data class DailyMissionEntity(
    @PrimaryKey val missionId: String,
    val localDate: String,
    val type: String,
    val target: Int,
    val progress: Int,
    val rewardXp: Int,
    val completedAt: Long?
)

@Entity(tableName = "user_achievement")
data class UserAchievementEntity(@PrimaryKey val achievementId: String, val unlockedAt: Long)

/** A word the user has met in a completed lesson (also the base for review items later). */
@Entity(tableName = "learned_word")
data class LearnedWordEntity(@PrimaryKey val vocabularyId: String, val firstSeenAt: Long)

/** A learned word on the review schedule (see ReviewScheduler). lastReviewedAt is 0 until the first review. */
@Entity(tableName = "review_item")
data class ReviewItemEntity(
    @PrimaryKey val vocabularyId: String,
    val stage: Int,
    val dueAt: Long,
    val lastReviewedAt: Long,
    val reviewCount: Int,
    val lapses: Int,
    val createdAt: Long
)

