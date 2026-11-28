package com.hanzijourney.app.domain.progress

/** What the imported content offers; missions and chapter achievements are based on it. */
data class ContentCatalog(
    val lessonIds: Set<String>,
    val chapterLessons: Map<String, Set<String>>,
    val wordIds: Set<String>
)

data class DailyMission(
    val id: String,
    val localDate: String,
    val type: String,
    val target: Int,
    val progress: Int,
    val rewardXp: Int,
    /** Epoch millis when it was completed; null while open. */
    val completedAt: Long?
) {
    val done: Boolean get() = completedAt != null
}

object MissionTypes {
    const val COMPLETE_LESSON = "complete_lesson"
    const val NEW_WORDS = "new_words"
}

/** Starting rewards (PRD section 7: 10–15 XP per daily mission); to be balanced after playtests. */
object DailyMissionPlanner {
    data class Plan(val type: String, val target: Int, val rewardXp: Int)

    const val NEW_WORDS_TARGET = 10

    /**
     * Only missions that can actually be finished with the content on hand are planned:
     * "learn 10 new words" needs at least 10 words the user has not learned yet.
     */
    fun plan(totalLessons: Int, unlearnedWords: Int): List<Plan> = buildList {
        if (totalLessons >= 1) add(Plan(MissionTypes.COMPLETE_LESSON, target = 1, rewardXp = 10))
        if (unlearnedWords >= NEW_WORDS_TARGET) add(Plan(MissionTypes.NEW_WORDS, target = NEW_WORDS_TARGET, rewardXp = 15))
    }

    fun missionId(localDate: String, type: String) = "$localDate:$type"
    fun rewardEventId(missionId: String) = "mission:$missionId"
}

enum class Achievement(val id: String) {
    FIRST_LESSON("first_lesson"),
    WORDS_25("words_25"),
    CHAPTER_COMPLETE("chapter_complete"),
    SEVEN_DAY_PRACTICE("seven_day_practice"),
    REVIEWS_100("reviews_100");

    companion object {
        fun fromId(id: String): Achievement? = entries.firstOrNull { it.id == id }
    }
}

data class AchievementContext(
    val completedLessons: Int,
    val learnedWords: Int,
    val chaptersCompleted: Int,
    val bestStreak: Int,
    val totalReviews: Int = 0
)

object AchievementRules {
    const val WORDS_TARGET = 25
    const val STREAK_TARGET = 7
    const val REVIEWS_TARGET = 100

    fun unlocked(c: AchievementContext): Set<Achievement> = buildSet {
        if (c.completedLessons >= 1) add(Achievement.FIRST_LESSON)
        if (c.learnedWords >= WORDS_TARGET) add(Achievement.WORDS_25)
        if (c.chaptersCompleted >= 1) add(Achievement.CHAPTER_COMPLETE)
        if (c.bestStreak >= STREAK_TARGET) add(Achievement.SEVEN_DAY_PRACTICE)
        if (c.totalReviews >= REVIEWS_TARGET) add(Achievement.REVIEWS_100)
    }

    fun chaptersCompleted(catalog: ContentCatalog, completedLessonIds: Set<String>): Int =
        catalog.chapterLessons.count { (_, ids) -> ids.isNotEmpty() && completedLessonIds.containsAll(ids) }
}
