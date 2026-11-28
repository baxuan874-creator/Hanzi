package com.hanzijourney.app.domain.progress

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MissionsAndAchievementsTest {
    @Test fun plannerNeedsLessonsAndEnoughUnlearnedWords() {
        assertTrue(DailyMissionPlanner.plan(0, 100).none { it.type == MissionTypes.COMPLETE_LESSON })
        assertEquals(listOf(MissionTypes.COMPLETE_LESSON), DailyMissionPlanner.plan(1, 9).map { it.type })
        assertEquals(listOf(MissionTypes.COMPLETE_LESSON, MissionTypes.NEW_WORDS), DailyMissionPlanner.plan(1, 10).map { it.type })
    }

    @Test fun missionRewardsStayInThePrdRange() {
        DailyMissionPlanner.plan(5, 50).forEach { assertTrue(it.rewardXp in 10..15) }
    }

    @Test fun achievementThresholds() {
        val none = AchievementContext(0, 0, 0, 0)
        assertTrue(AchievementRules.unlocked(none).isEmpty())
        assertEquals(setOf(Achievement.FIRST_LESSON), AchievementRules.unlocked(none.copy(completedLessons = 1)))
        assertTrue(Achievement.WORDS_25 !in AchievementRules.unlocked(none.copy(learnedWords = 24)))
        assertTrue(Achievement.WORDS_25 in AchievementRules.unlocked(none.copy(learnedWords = 25)))
        assertTrue(Achievement.SEVEN_DAY_PRACTICE !in AchievementRules.unlocked(none.copy(bestStreak = 6)))
        assertTrue(Achievement.SEVEN_DAY_PRACTICE in AchievementRules.unlocked(none.copy(bestStreak = 7)))
        assertTrue(Achievement.CHAPTER_COMPLETE in AchievementRules.unlocked(none.copy(chaptersCompleted = 1)))
    }

    @Test fun chapterCountsOnlyFullyFinishedChapters() {
        val catalog = ContentCatalog(setOf("a", "b", "c"), mapOf("x" to setOf("a", "b"), "y" to setOf("c"), "z" to emptySet()), emptySet())
        assertEquals(0, AchievementRules.chaptersCompleted(catalog, setOf("a")))
        assertEquals(1, AchievementRules.chaptersCompleted(catalog, setOf("a", "b")))
        assertEquals(2, AchievementRules.chaptersCompleted(catalog, setOf("a", "b", "c")))
    }

    @Test fun achievementIdsRoundTrip() {
        Achievement.entries.forEach { assertEquals(it, Achievement.fromId(it.id)) }
        assertNull(Achievement.fromId("nope"))
    }
}
