package com.hanzijourney.app.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.hanzijourney.app.R
import com.hanzijourney.app.ui.components.EmptyState

@Composable
fun VocabularyScreen() {
    EmptyState(
        glyph = "词",
        title = stringResource(R.string.vocabulary_title),
        message = stringResource(R.string.vocabulary_empty)
    )
}
