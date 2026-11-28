package com.hanzijourney.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hanzijourney.app.R
import com.hanzijourney.app.ui.components.MissionsCard
import com.hanzijourney.app.ui.components.ProgressSummaryCard
import com.hanzijourney.app.ui.progress.ProgressViewModel
import com.hanzijourney.app.ui.theme.Spacing

@Composable
fun HomeScreen(viewModel: ProgressViewModel = viewModel(factory = ProgressViewModel.Factory)) {
    val snapshot by viewModel.snapshot.collectAsState()
    Column(
        modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        Text(stringResource(R.string.home_title), style = MaterialTheme.typography.headlineMedium)
        snapshot?.let { s ->
            ProgressSummaryCard(s)
            MissionsCard(s.missions)
            if (s.completedLessonIds.isEmpty()) Text(stringResource(R.string.home_empty), style = MaterialTheme.typography.bodyLarge)
        }
    }
}
