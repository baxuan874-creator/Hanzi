package com.hanzijourney.app.ui.lesson

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hanzijourney.app.R
import com.hanzijourney.app.data.content.ExampleDto
import com.hanzijourney.app.domain.lesson.LessonStep
import com.hanzijourney.app.ui.components.HanziButton
import com.hanzijourney.app.ui.theme.HanziGlyphStyle
import com.hanzijourney.app.ui.theme.Spacing

@Composable
internal fun VocabStep(step: LessonStep.Vocab, onNext: () -> Unit) {
    val v = step.item
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = Spacing.lg, vertical = Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        CenteredScroll {
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
                    Text(
                        v.hanzi,
                        style = HanziGlyphStyle.copy(fontSize = 96.sp, lineHeight = 112.sp),
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                    Text(v.pinyin, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
                    Text(v.meaningVi, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
                    v.wordType?.let { Text(it, style = MaterialTheme.typography.labelMedium) }
                }
            }
            if (v.examples.isNotEmpty()) {
                Text(stringResource(R.string.lesson_example), style = MaterialTheme.typography.titleMedium)
                v.examples.forEach { ExampleCard(it) }
            }
        }
        HanziButton(text = stringResource(R.string.lesson_next), onClick = onNext, modifier = Modifier.fillMaxWidth().height(56.dp))
    }
}

@Composable
internal fun GrammarStep(step: LessonStep.Grammar, onNext: () -> Unit) {
    val g = step.item
    Column(modifier = Modifier.fillMaxSize().padding(Spacing.lg), verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            Text(stringResource(R.string.lesson_grammar_title), style = MaterialTheme.typography.titleMedium)
            Text(g.pattern, style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
            Text(g.explanationVi, style = MaterialTheme.typography.bodyLarge)
            g.usageNoteVi?.let { Text(stringResource(R.string.lesson_usage_note) + ": " + it, style = MaterialTheme.typography.bodyMedium) }
            g.commonErrorVi?.let { Text(stringResource(R.string.lesson_common_error) + ": " + it, style = MaterialTheme.typography.bodyMedium) }
            if (g.examples.isNotEmpty()) {
                Text(stringResource(R.string.lesson_example), style = MaterialTheme.typography.titleMedium)
                g.examples.forEach { ExampleCard(it) }
            }
        }
        HanziButton(text = stringResource(R.string.lesson_next), onClick = onNext, modifier = Modifier.fillMaxWidth().height(56.dp))
    }
}

@Composable
private fun ExampleCard(example: ExampleDto) {
    Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium) {
        Column(modifier = Modifier.padding(Spacing.md), verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
            Text(example.hanzi, style = MaterialTheme.typography.headlineMedium)
            Text(example.pinyin, style = MaterialTheme.typography.bodyLarge)
            Text(example.meaningVi, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
