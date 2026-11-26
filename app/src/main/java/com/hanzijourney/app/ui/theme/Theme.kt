package com.hanzijourney.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = BluePrimary,
    secondary = TealSecondary,
    tertiary = GoldAccent,
    background = WarmBackground
)

private val DarkColors = darkColorScheme(
    primary = BluePrimary,
    secondary = TealSecondary,
    tertiary = GoldAccent,
    background = DarkBackground
)

// Theme skeleton for Phase 0. Full design tokens come in Phase 1.
@Composable
fun HanziJourneyTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}
