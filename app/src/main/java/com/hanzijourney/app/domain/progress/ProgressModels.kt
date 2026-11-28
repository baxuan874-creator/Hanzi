package com.hanzijourney.app.domain.progress

/** Saved result of a lesson. One row per lesson; replays update it. Times are epoch milliseconds. */
data class LessonProgress(
    val lessonId: String,
    val firstCompletedAt: Long,
    val lastCompletedAt: Long,
    val bestCorrect: Int,
    val totalExercises: Int,
    val timesCompleted: Int
)

/** One entry of the XP ledger. [eventId] is the idempotency key: the same id can never be stored twice. */
data class XpEvent(
    val eventId: String,
    val kind: String,
    val amount: Int,
    val createdAt: Long,
    /** Local calendar day (yyyy-MM-dd) the event happened on. */
    val localDate: String
)

data class LessonCompletion(
    val firstTime: Boolean,
    val xpAwarded: Int,
    /** Daily missions this lesson just completed (their XP is already in the ledger). */
    val completedMissions: List<DailyMission> = emptyList(),
    /** Achievements unlocked by this lesson. */
    val newAchievements: List<Achievement> = emptyList()
)

data class LevelInfo(
    val level: Int,
    val xpIntoLevel: Int,
    /** XP needed to go from this level to the next, or null at the highest level. */
    val xpForNextLevel: Int?
)

data class Streak(val current: Int, val best: Int, val practicedToday: Boolean)

data class ProgressSnapshot(
    val totalXp: Int,
    val level: LevelInfo,
    val streak: Streak,
    val completedLessonIds: Set<String>,
    val learnedWordCount: Int,
    val missions: List<DailyMission>,
    val unlockedAchievements: Set<Achievement>,
    val totalReviews: Int
)
