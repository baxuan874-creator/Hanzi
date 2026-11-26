package com.hanzijourney.app.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.hanzijourney.app.R
import com.hanzijourney.app.ui.components.EmptyState

@Composable
fun HomeScreen() {
    EmptyState(
        glyph = "家",
        title = stringResource(R.string.home_title),
        message = stringResource(R.string.home_empty)
    )
}
