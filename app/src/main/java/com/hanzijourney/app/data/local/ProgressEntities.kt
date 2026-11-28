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
