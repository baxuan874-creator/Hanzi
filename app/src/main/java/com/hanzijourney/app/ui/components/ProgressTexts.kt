package com.hanzijourney.app.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.hanzijourney.app.R
import com.hanzijourney.app.domain.progress.Achievement
import com.hanzijourney.app.domain.progress.DailyMission
import com.hanzijourney.app.domain.progress.MissionTypes

@Composable
fun missionTitle(mission: DailyMission): String = when (mission.type) {
    MissionTypes.COMPLETE_LESSON -> stringResource(R.string.mission_complete_lesson)
    MissionTypes.NEW_WORDS -> stringResource(R.string.mission_new_words, mission.target)
    else -> mission.type
}

@Composable
fun achievementName(achievement: Achievement): String = stringResource(
    when (achievement) {
        Achievement.FIRST_LESSON -> R.string.achievement_first_lesson
        Achievement.WORDS_25 -> R.string.achievement_words_25
        Achievement.CHAPTER_COMPLETE -> R.string.achievement_chapter_complete
        Achievement.SEVEN_DAY_PRACTICE -> R.string.achievement_seven_day
    }
)

@Composable
fun achievementHint(achievement: Achievement): String = stringResource(
    when (achievement) {
        Achievement.FIRST_LESSON -> R.string.achievement_first_lesson_hint
        Achievement.WORDS_25 -> R.string.achievement_words_25_hint
        Achievement.CHAPTER_COMPLETE -> R.string.achievement_chapter_complete_hint
        Achievement.SEVEN_DAY_PRACTICE -> R.string.achievement_seven_day_hint
    }
)
