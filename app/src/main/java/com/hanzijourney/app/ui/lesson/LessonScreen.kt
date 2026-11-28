package com.hanzijourney.app.ui.lesson

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hanzijourney.app.HanziApp
import com.hanzijourney.app.R
import com.hanzijourney.app.domain.lesson.LessonResult
import com.hanzijourney.app.domain.lesson.LessonStep
import com.hanzijourney.app.ui.components.HanziButton
import com.hanzijourney.app.ui.theme.Spacing

@Composable
fun LessonScreen(lessonId: String, onClose: () -> Unit) {
    val app = LocalContext.current.applicationContext as HanziApp
    val viewModel: LessonViewModel = viewModel(
        key = lessonId,
        factory = LessonViewModel.factory(app.container.contentRepository, lessonId)
    )
    val state by viewModel.state.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClose) {
                Icon(Icons.Filled.Close, contentDescription = stringResource(R.string.lesson_close))
            }
            LinearProgressIndicator(progress = { state.progress }, modifier = Modifier.weight(1f).padding(end = Spacing.md))
        }
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            val step = state.step
            when {
                state.loading -> CenteredText(stringResource(R.string.lesson_loading))
                state.notFound -> Column(
                    modifier = Modifier.fillMaxSize().padding(Spacing.lg),
                    verticalArrangement = Arrangement.spacedBy(Spacing.md, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(stringResource(R.string.lesson_not_found), style = MaterialTheme.typography.bodyLarge)
                    HanziButton(text = stringResource(R.string.lesson_result_back), onClick = onClose)
                }
                state.result != null -> ResultStep(state.result!!, onAgain = viewModel::restart, onBack = onClose)
                step is LessonStep.Intro -> IntroStep(state.title, state.objectives, state.draftNotice, viewModel::next)
                step is LessonStep.Vocab -> VocabStep(step, viewModel::next)
                step is LessonStep.Grammar -> GrammarStep(step, viewModel::next)
                step is LessonStep.Practice -> PracticeStep(step, state.feedback, viewModel::submit, viewModel::next)
            }
        }
    }
}

@Composable
private fun CenteredText(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun IntroStep(title: String, objectives: List<String>, draftNotice: Boolean, onStart: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(Spacing.lg), verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            Text(title, style = MaterialTheme.typography.headlineMedium)
            Text(stringResource(R.string.lesson_goals), style = MaterialTheme.typography.titleMedium)
            objectives.forEach { Text("• $it", style = MaterialTheme.typography.bodyLarge) }
            if (draftNotice) {
                Text(
                    stringResource(R.string.lesson_draft_notice),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        HanziButton(text = stringResource(R.string.lesson_start), onClick = onStart, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun ResultStep(result: LessonResult, onAgain: () -> Unit, onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(Spacing.lg),
        verticalArrangement = Arrangement.spacedBy(Spacing.md, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.lesson_result_title), style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Text(stringResource(R.string.lesson_result_score, result.correctFirstTry, result.totalExercises), style = MaterialTheme.typography.titleLarge)
        Text(stringResource(R.string.lesson_result_words, result.wordsMet), style = MaterialTheme.typography.bodyLarge)
        Text(stringResource(R.string.lesson_result_retried, result.retried), style = MaterialTheme.typography.bodyLarge)
        HanziButton(text = stringResource(R.string.lesson_result_back), onClick = onBack, modifier = Modifier.fillMaxWidth())
        OutlinedButton(onClick = onAgain, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.lesson_result_again)) }
    }
}
