package com.hanzijourney.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hanzijourney.app.R
import com.hanzijourney.app.domain.review.ReviewCard
import com.hanzijourney.app.domain.review.ReviewDirection
import com.hanzijourney.app.ui.components.EmptyState
import com.hanzijourney.app.ui.components.HanziButton
import com.hanzijourney.app.ui.components.HanziProgressBar
import com.hanzijourney.app.ui.components.ScatteredIcons
import com.hanzijourney.app.ui.components.achievementName
import com.hanzijourney.app.ui.lesson.CenteredScroll
import com.hanzijourney.app.ui.review.ReviewUiState
import com.hanzijourney.app.ui.review.ReviewViewModel
import com.hanzijourney.app.ui.theme.HanziGlyphStyle
import com.hanzijourney.app.ui.theme.Spacing

@Composable
fun ReviewScreen(viewModel: ReviewViewModel = viewModel(factory = ReviewViewModel.Factory)) {
    val state by viewModel.state.collectAsState()
    // Coming back to this tab picks up words that became due meanwhile (not while a session is running).
    LaunchedEffect(Unit) { viewModel.refreshIfIdle() }

    Box(modifier = Modifier.fillMaxSize()) {
        ScatteredIcons(seed = state.card?.vocab?.id.hashCode() + state.answered)
        when {
            state.loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.learn_loading), style = MaterialTheme.typography.bodyLarge)
            }
            state.nothingDue -> EmptyState(
                glyph = "复",
                title = stringResource(R.string.review_title),
                message = stringResource(R.string.review_empty)
            )
            state.result != null -> ReviewResultContent(state, onMore = viewModel::load)
            state.card != null -> ReviewCardContent(state, onReveal = viewModel::reveal, onAnswer = viewModel::answer)
        }
    }
}

@Composable
private fun ReviewCardContent(state: ReviewUiState, onReveal: () -> Unit, onAnswer: (Boolean) -> Unit) {
    val card = state.card ?: return
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = Spacing.lg, vertical = Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        HanziProgressBar(progress = state.progress, label = stringResource(R.string.review_progress, state.answered, state.total))
        CenteredScroll {
            if (state.isRetry) Text(stringResource(R.string.review_retry_label), style = MaterialTheme.typography.labelMedium)
            Text(
                text = stringResource(
                    if (card.direction == ReviewDirection.HANZI_TO_MEANING) R.string.review_hint_meaning else R.string.review_hint_hanzi
                ),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            FlashCard(card, state.revealed)
        }
        if (state.saveFailed) {
            Text(stringResource(R.string.review_save_failed), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error)
        }
        if (!state.revealed) {
            HanziButton(text = stringResource(R.string.review_reveal), onClick = onReveal, modifier = Modifier.fillMaxWidth().height(56.dp))
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = { onAnswer(false) }, modifier = Modifier.weight(1f).height(56.dp)) {
                    Text(stringResource(R.string.review_forgot), style = MaterialTheme.typography.titleMedium)
                }
                HanziButton(text = stringResource(R.string.review_remembered), onClick = { onAnswer(true) }, modifier = Modifier.weight(1f).height(56.dp))
            }
        }
    }
}

/** The question is always visible; the rest of the word appears only after "Hiện đáp án" (recall before looking). */
@Composable
private fun FlashCard(card: ReviewCard, revealed: Boolean) {
    val v = card.vocab
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = Spacing.lg, horizontal = Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (card.direction == ReviewDirection.HANZI_TO_MEANING) {
                Text(v.hanzi, style = HanziGlyphStyle.copy(fontSize = 80.sp, lineHeight = 96.sp), color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center)
                if (revealed) {
                    HorizontalDivider()
                    Text(v.pinyin, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
                    Text(v.meaningVi, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
                }
            } else {
                Text(v.meaningVi, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
                if (revealed) {
                    HorizontalDivider()
                    Text(v.hanzi, style = HanziGlyphStyle.copy(fontSize = 80.sp, lineHeight = 96.sp), color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center)
                    Text(v.pinyin, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
                }
            }
            if (revealed) v.examples.firstOrNull()?.let { ex ->
                HorizontalDivider()
                Text(ex.hanzi, style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
                Text(ex.pinyin, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
                Text(ex.meaningVi, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
private fun ReviewResultContent(state: ReviewUiState, onMore: () -> Unit) {
    val result = state.result ?: return
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = Spacing.lg, vertical = Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        CenteredScroll {
            Text(stringResource(R.string.review_result_title), style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
            Text(stringResource(R.string.review_result_counts, result.remembered, result.forgotten), style = MaterialTheme.typography.titleLarge)
            if (state.xpEarned > 0) {
                Text(stringResource(R.string.lesson_result_xp, state.xpEarned), style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
            }
            if (result.forgotten > 0) {
                Text(stringResource(R.string.review_result_forgot_note), style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
            }
            state.newAchievements.forEach { a ->
                Text(stringResource(R.string.lesson_result_achievement, achievementName(a)), style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
            }
            if (state.saveFailed) {
                Text(stringResource(R.string.review_save_failed), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error)
            }
        }
        HanziButton(text = stringResource(R.string.review_more), onClick = onMore, modifier = Modifier.fillMaxWidth().height(56.dp))
    }
}
