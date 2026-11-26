package com.hanzijourney.app.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.hanzijourney.app.R
import com.hanzijourney.app.ui.components.EmptyState

@Composable
fun LearnScreen() {
    EmptyState(
        glyph = "学",
        title = stringResource(R.string.learn_title),
        message = stringResource(R.string.learn_empty)
    )
}
