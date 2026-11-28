package com.hanzijourney.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.hanzijourney.app.R
import com.hanzijourney.app.domain.progress.Achievement
import com.hanzijourney.app.domain.progress.AchievementRules
import com.hanzijourney.app.domain.progress.DailyMission
import com.hanzijourney.app.domain.progress.ProgressSnapshot
import com.hanzijourney.app.ui.theme.Spacing

@Composable
fun MissionsCard(missions: List<DailyMission>, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
        Column(modifier = Modifier.padding(Spacing.md), verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            Text(stringResource(R.string.missions_title), style = MaterialTheme.typography.titleLarge)
            if (missions.isEmpty()) {
                Text(stringResource(R.string.missions_none), style = MaterialTheme.typography.bodyMedium)
            }
            missions.forEach { m ->
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                    Text(missionTitle(m), style = MaterialTheme.typography.titleMedium)
                    // The state is written out; colour is never the only signal.
                    Text(
                        text = if (m.done) stringResource(R.string.mission_done, m.rewardXp)
                        else stringResource(R.string.mission_open, m.progress, m.target, m.rewardXp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun AchievementsCard(snapshot: ProgressSnapshot, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
        Column(modifier = Modifier.padding(Spacing.md), verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            Text(stringResource(R.string.achievements_title), style = MaterialTheme.typography.titleLarge)
            Achievement.entries.forEach { a ->
                val unlocked = a in snapshot.unlockedAchievements
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                    Text(achievementName(a), style = MaterialTheme.typography.titleMedium)
                    Text(achievementHint(a), style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = when {
                            unlocked -> stringResource(R.string.achievement_unlocked)
                            a == Achievement.WORDS_25 -> stringResource(R.string.achievement_progress, snapshot.learnedWordCount.coerceAtMost(AchievementRules.WORDS_TARGET), AchievementRules.WORDS_TARGET)
                            a == Achievement.SEVEN_DAY_PRACTICE -> stringResource(R.string.achievement_progress, snapshot.streak.best.coerceAtMost(AchievementRules.STREAK_TARGET), AchievementRules.STREAK_TARGET)
                            a == Achievement.REVIEWS_100 -> stringResource(R.string.achievement_progress, snapshot.totalReviews.coerceAtMost(AchievementRules.REVIEWS_TARGET), AchievementRules.REVIEWS_TARGET)
                            else -> stringResource(R.string.achievement_locked)
                        },
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}
