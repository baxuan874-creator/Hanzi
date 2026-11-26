package com.hanzijourney.app.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.hanzijourney.app.R
import com.hanzijourney.app.ui.components.EmptyState

@Composable
fun ReviewScreen() {
    EmptyState(
        glyph = "复",
        title = stringResource(R.string.review_title),
        message = stringResource(R.string.review_empty)
    )
}
