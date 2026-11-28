package com.hanzijourney.app.ui.lesson

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.hanzijourney.app.R
import com.hanzijourney.app.domain.lesson.Feedback
import com.hanzijourney.app.domain.lesson.LessonStep
import com.hanzijourney.app.ui.components.HanziButton
import com.hanzijourney.app.ui.theme.Spacing

@Composable
internal fun PracticeStep(
    step: LessonStep.Practice,
    feedback: Feedback?,
    onSubmit: (String) -> Unit,
    onNext: () -> Unit
) {
    val exercise = step.exercise
    val options = exercise.options.orEmpty()
    // key() resets the remembered selection when the next exercise (or a retry) appears.
    key(exercise.id, step.isRetry) {
        var answer by remember { mutableStateOf("") }
        val answered = feedback != null
        Column(modifier = Modifier.fillMaxSize().padding(Spacing.lg), verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
            Column(
                modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(Spacing.md)
            ) {
                if (step.isRetry) Text(stringResource(R.string.lesson_retry_label), style = MaterialTheme.typography.labelMedium)
                Text(exercise.prompt, style = MaterialTheme.typography.titleLarge)
                when {
                    exercise.type == "sentence_ordering" && options.isNotEmpty() ->
                        OrderingInput(options, answered) { answer = it }
                    options.isEmpty() ->
                        OutlinedTextField(
                            value = answer, onValueChange = { answer = it }, enabled = !answered, singleLine = true,
                            label = { Text(stringResource(R.string.lesson_type_hint)) }, modifier = Modifier.fillMaxWidth()
                        )
                    else -> options.forEach { option ->
                        val selected = answer == option
                        val mod = Modifier.fillMaxWidth().heightIn(min = Spacing.touchTarget)
                        if (selected) Button(onClick = { answer = option }, enabled = !answered, modifier = mod) { Text(option) }
                        else OutlinedButton(onClick = { answer = option }, enabled = !answered, modifier = mod) { Text(option) }
                    }
                }
                if (feedback != null) FeedbackPanel(feedback)
            }
            if (!answered) {
                HanziButton(
                    text = stringResource(R.string.lesson_check),
                    onClick = { onSubmit(answer) },
                    enabled = answer.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                HanziButton(text = stringResource(R.string.lesson_next), onClick = onNext, modifier = Modifier.fillMaxWidth())
            }
        }
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
    Text(stringResource(R.string.lesson_ordering_hint), style = MaterialTheme.typography.bodyMedium)
    FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.sm), verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
        chosen.forEach { index ->
            Button(onClick = { update(chosen - index) }, enabled = !locked, modifier = Modifier.heightIn(min = Spacing.touchTarget)) {
                Text(tokens[index])
            }
        }
    }
    FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.sm), verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
        tokens.indices.filter { it !in chosen }.forEach { index ->
            OutlinedButton(onClick = { update(chosen + index) }, enabled = !locked, modifier = Modifier.heightIn(min = Spacing.touchTarget)) {
                Text(tokens[index])
            }
        }
    }
}

@Composable
private fun FeedbackPanel(feedback: Feedback) {
    val container = if (feedback.correct) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.errorContainer
    val content = if (feedback.correct) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onErrorContainer
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = container, contentColor = content)
    ) {
        Column(modifier = Modifier.padding(Spacing.md), verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
            Text(
                stringResource(if (feedback.correct) R.string.lesson_correct else R.string.lesson_incorrect),
                style = MaterialTheme.typography.titleMedium
            )
            if (!feedback.correct) Text(stringResource(R.string.lesson_correct_answer, feedback.correctAnswer), style = MaterialTheme.typography.bodyLarge)
            Text(feedback.explanationVi, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
