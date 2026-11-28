package com.hanzijourney.app.ui.lesson

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hanzijourney.app.R
import com.hanzijourney.app.domain.lesson.ExerciseOptions
import com.hanzijourney.app.domain.lesson.Feedback
import com.hanzijourney.app.domain.lesson.LessonStep
import com.hanzijourney.app.ui.components.HanziButton
import com.hanzijourney.app.ui.theme.Spacing

private val OptionShape = RoundedCornerShape(20.dp)

@Composable
internal fun PracticeStep(
    step: LessonStep.Practice,
    feedback: Feedback?,
    onSubmit: (String) -> Unit,
    onNext: () -> Unit
) {
    val exercise = step.exercise
    // key() resets the remembered selection and the shuffled order when the next exercise (or a retry) appears.
    key(exercise.id, step.isRetry) {
        val options = remember { ExerciseOptions.shuffled(exercise) }
        var answer by remember { mutableStateOf("") }
        val answered = feedback != null
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = Spacing.lg, vertical = Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            CenteredScroll {
                if (step.isRetry) Text(stringResource(R.string.lesson_retry_label), style = MaterialTheme.typography.labelMedium)
                Text(exercise.prompt, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
                when {
                    exercise.type == "sentence_ordering" && options.isNotEmpty() ->
                        OrderingInput(options, answered) { answer = it }
                    options.isEmpty() ->
                        OutlinedTextField(
                            value = answer, onValueChange = { answer = it }, enabled = !answered, singleLine = true,
                            label = { Text(stringResource(R.string.lesson_type_hint)) }, modifier = Modifier.fillMaxWidth()
                        )
                    else -> options.forEach { option ->
                        ChoiceOption(
                            text = option,
                            selected = answer == option,
                            enabled = !answered,
                            mark = when {
                                feedback == null -> null
                                option.trim() == feedback.correctAnswer.trim() -> Mark.Correct
                                option == answer -> Mark.Wrong
                                else -> null
                            },
                            onClick = { answer = option }
                        )
                    }
                }
            }
            if (feedback != null) FeedbackPanel(feedback)
            if (!answered) {
                HanziButton(
                    text = stringResource(R.string.lesson_check),
                    onClick = { onSubmit(answer) },
                    enabled = answer.isNotBlank(),
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                )
            } else {
                HanziButton(text = stringResource(R.string.lesson_next), onClick = onNext, modifier = Modifier.fillMaxWidth().height(56.dp))
            }
        }
    }
}

private enum class Mark { Correct, Wrong }

/** Large tappable answer card. After answering, the mark is also written ("✓"/"✗"), never colour alone. */
@Composable
private fun ChoiceOption(text: String, selected: Boolean, enabled: Boolean, mark: Mark?, onClick: () -> Unit) {
    val scheme = MaterialTheme.colorScheme
    val background = when (mark) {
        Mark.Correct -> scheme.secondaryContainer
        Mark.Wrong -> scheme.errorContainer
        null -> if (selected) scheme.primaryContainer else scheme.surface
    }
    val borderColor = when (mark) {
        Mark.Correct -> scheme.secondary
        Mark.Wrong -> scheme.error
        null -> if (selected) scheme.primary else scheme.outline
    }
    val prefix = when (mark) {
        Mark.Correct -> "✓  "
        Mark.Wrong -> "✗  "
        null -> ""
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 72.dp)
            .clip(OptionShape)
            .background(background)
            .border(BorderStroke(if (selected || mark != null) 3.dp else 1.5.dp, borderColor), OptionShape)
            .clickable(enabled = enabled, role = Role.RadioButton, onClick = onClick)
            .padding(horizontal = Spacing.md, vertical = Spacing.sm),
        contentAlignment = Alignment.Center
    ) {
        Text(prefix + text, style = MaterialTheme.typography.titleLarge, color = scheme.onSurface, textAlign = TextAlign.Center)
    }
}

@Composable
private fun TokenChip(text: String, filled: Boolean, enabled: Boolean, onClick: () -> Unit) {
    val scheme = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .heightIn(min = 56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (filled) scheme.primaryContainer else scheme.surface)
            .border(BorderStroke(if (filled) 3.dp else 1.5.dp, if (filled) scheme.primary else scheme.outline), RoundedCornerShape(16.dp))
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .padding(horizontal = Spacing.lg, vertical = Spacing.sm),
        contentAlignment = Alignment.Center
    ) {
        Text(text, style = MaterialTheme.typography.titleLarge, color = scheme.onSurface)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun OrderingInput(tokens: List<String>, locked: Boolean, onAnswer: (String) -> Unit) {
    var chosen by remember { mutableStateOf(listOf<Int>()) }
    fun update(next: List<Int>) {
        chosen = next
        // Only a complete sentence counts as an answer; otherwise the answer stays empty and Check is disabled.
        onAnswer(if (next.size == tokens.size) next.joinToString(" ") { tokens[it] } else "")
    }
    Text(stringResource(R.string.lesson_ordering_hint), style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
    // Answer area keeps a fixed minimum height so the layout does not jump when pieces move.
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 88.dp)
            .clip(OptionShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(Spacing.sm),
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm)
    ) {
        chosen.forEach { index -> TokenChip(tokens[index], filled = true, enabled = !locked) { update(chosen - index) } }
    }
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm)
    ) {
        tokens.indices.filter { it !in chosen }.forEach { index ->
            TokenChip(tokens[index], filled = false, enabled = !locked) { update(chosen + index) }
        }
    }
}

@Composable
private fun FeedbackPanel(feedback: Feedback) {
    val container = if (feedback.correct) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.errorContainer
    val content = if (feedback.correct) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onErrorContainer
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(container)
            .padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.xs)
    ) {
        Text(
            stringResource(if (feedback.correct) R.string.lesson_correct else R.string.lesson_incorrect),
            style = MaterialTheme.typography.titleLarge,
            color = content
        )
        if (!feedback.correct) Text(stringResource(R.string.lesson_correct_answer, feedback.correctAnswer), style = MaterialTheme.typography.bodyLarge, color = content)
        Text(feedback.explanationVi, style = MaterialTheme.typography.bodyLarge, color = content)
    }
}
