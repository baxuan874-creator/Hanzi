package com.hanzijourney.app.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.hanzijourney.app.R
import com.hanzijourney.app.ui.components.EmptyState

@Composable
fun ProfileScreen() {
    EmptyState(
        glyph = "我",
        title = stringResource(R.string.profile_title),
        message = stringResource(R.string.profile_empty)
    )
}
