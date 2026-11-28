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
import com.hanzijourney.app.domain.progress.ProgressSnapshot
import com.hanzijourney.app.ui.theme.Spacing

/** Level, XP, streak and finished lessons, read from saved progress. */
@Composable
fun ProgressSummaryCard(snapshot: ProgressSnapshot, modifier: Modifier = Modifier) {
    val level = snapshot.level
    val next = level.xpForNextLevel
    Card(modifier = modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
        Column(modifier = Modifier.padding(Spacing.md), verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            Text(stringResource(R.string.progress_level, level.level), style = MaterialTheme.typography.headlineMedium)
            HanziProgressBar(
                progress = if (next == null) 1f else level.xpIntoLevel.toFloat() / next,
                label = stringResource(R.string.progress_xp, snapshot.totalXp)
            )
            Text(
                text = if (next == null) stringResource(R.string.progress_max_level)
                else stringResource(R.string.progress_to_next, next - level.xpIntoLevel, level.level + 1),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(stringResource(R.string.progress_streak, snapshot.streak.current), style = MaterialTheme.typography.titleMedium)
            Text(stringResource(R.string.progress_best_streak, snapshot.streak.best), style = MaterialTheme.typography.bodyMedium)
            Text(stringResource(R.string.progress_lessons_done, snapshot.completedLessonIds.size), style = MaterialTheme.typography.bodyMedium)
        }
    }
}
