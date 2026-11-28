package com.hanzijourney.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import com.hanzijourney.app.R
import com.hanzijourney.app.domain.model.DarkModeSetting
import com.hanzijourney.app.ui.components.AchievementsCard
import com.hanzijourney.app.ui.components.ProgressSummaryCard
import com.hanzijourney.app.ui.profile.ProfileViewModel
import com.hanzijourney.app.ui.progress.ProgressViewModel
import com.hanzijourney.app.ui.theme.Spacing

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.Factory),
    progressViewModel: ProgressViewModel = viewModel(factory = ProgressViewModel.Factory)
) {
    val settings by viewModel.settings.collectAsState()
    val progress by progressViewModel.snapshot.collectAsState()
    Column(
        modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        Text(stringResource(R.string.profile_title), style = MaterialTheme.typography.headlineMedium)
        progress?.let {
            ProgressSummaryCard(it)
            AchievementsCard(it)
        }
        Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
            Column(modifier = Modifier.padding(Spacing.md)) {
                Text(stringResource(R.string.profile_appearance), style = MaterialTheme.typography.titleMedium)
                val options = listOf(
                    DarkModeSetting.SYSTEM to R.string.dark_mode_system,
                    DarkModeSetting.LIGHT to R.string.dark_mode_light,
                    DarkModeSetting.DARK to R.string.dark_mode_dark
                )
                options.forEach { (mode, labelRes) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = Spacing.touchTarget)
                            .selectable(
                                selected = settings.darkMode == mode,
                                onClick = { viewModel.setDarkMode(mode) },
                                role = Role.RadioButton
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = settings.darkMode == mode, onClick = null)
                        Spacer(Modifier.width(Spacing.sm))
                        Text(stringResource(labelRes), style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}
