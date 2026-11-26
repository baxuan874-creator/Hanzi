package com.hanzijourney.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.hanzijourney.app.R
import com.hanzijourney.app.ui.theme.Spacing

enum class LessonNodeState { LOCKED, AVAILABLE, COMPLETED }

data class LessonNodeUi(val id: String, val title: String, val subtitle: String, val state: LessonNodeState)

@Composable
fun LessonCard(node: LessonNodeUi, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        onClick = onClick,
        enabled = node.state != LessonNodeState.LOCKED,
        modifier = modifier.heightIn(min = Spacing.touchTarget),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(Spacing.md),
            horizontalArrangement = Arrangement.spacedBy(Spacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                Text(text = node.title, style = MaterialTheme.typography.titleMedium)
                Text(text = node.subtitle, style = MaterialTheme.typography.bodyMedium)
            }
            StatusBadge(
                text = stringResource(
                    when (node.state) {
                        LessonNodeState.LOCKED -> R.string.badge_locked
                        LessonNodeState.AVAILABLE -> R.string.badge_available
                        LessonNodeState.COMPLETED -> R.string.badge_completed
                    }
                )
            )
        }
    }
}
