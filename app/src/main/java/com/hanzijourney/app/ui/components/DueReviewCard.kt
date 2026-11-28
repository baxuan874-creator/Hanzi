package com.hanzijourney.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hanzijourney.app.R
import com.hanzijourney.app.ui.theme.Spacing

/** How many learned words are due for review now, with a shortcut to the Review tab. */
@Composable
fun DueReviewCard(dueCount: Int, onOpenReview: () -> Unit, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
        Column(modifier = Modifier.padding(Spacing.md), verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            Text(stringResource(R.string.review_title), style = MaterialTheme.typography.titleLarge)
            if (dueCount > 0) {
                Text(stringResource(R.string.home_due_count, dueCount), style = MaterialTheme.typography.bodyLarge)
                HanziButton(text = stringResource(R.string.home_review_now), onClick = onOpenReview, modifier = Modifier.fillMaxWidth().height(56.dp))
            } else {
                Text(stringResource(R.string.home_due_none), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
