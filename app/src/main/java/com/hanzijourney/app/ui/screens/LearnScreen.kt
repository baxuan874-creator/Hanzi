package com.hanzijourney.app.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hanzijourney.app.HanziApp
import com.hanzijourney.app.R
import com.hanzijourney.app.ui.components.EmptyState
import com.hanzijourney.app.ui.components.LearnMap
import com.hanzijourney.app.ui.components.LessonNodeState
import com.hanzijourney.app.ui.components.LessonNodeUi
import com.hanzijourney.app.ui.learn.LearnUiState
import com.hanzijourney.app.ui.learn.LearnViewModel
import com.hanzijourney.app.ui.theme.Spacing

@Composable
fun LearnScreen(onOpenLesson: (String) -> Unit) {
    val app = LocalContext.current.applicationContext as HanziApp
    val viewModel: LearnViewModel = viewModel(
        factory = LearnViewModel.factory(app.container.contentRepository, app.container.contentLoader)
    )
    val state by viewModel.state.collectAsState()

    when (val s = state) {
        is LearnUiState.Loading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.learn_loading), style = MaterialTheme.typography.bodyLarge)
        }
        is LearnUiState.Empty -> EmptyState(
            glyph = "学",
            title = stringResource(R.string.learn_title),
            message = if (s.error != null) stringResource(R.string.learn_load_error, s.error) else stringResource(R.string.learn_empty)
        )
        is LearnUiState.Lessons -> {
            val nodes = s.lessons.map {
                LessonNodeUi(
                    id = it.id,
                    title = it.title,
                    subtitle = stringResource(R.string.learn_lesson_subtitle, it.chapterTitle, it.wordCount, it.minutes),
                    // Progress is not saved yet (Phase 4), so every lesson is simply available.
                    state = LessonNodeState.AVAILABLE
                )
            }
            Column(modifier = Modifier.fillMaxSize()) {
                if (s.draft) {
                    Text(
                        stringResource(R.string.learn_draft_banner),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth().padding(Spacing.md)
                    )
                }
                LearnMap(nodes = nodes, onNodeClick = onOpenLesson, modifier = Modifier.weight(1f))
            }
        }
    }
}
