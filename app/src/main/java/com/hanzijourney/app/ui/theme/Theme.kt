package com.hanzijourney.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = BluePrimary, onPrimary = Color.White,
    secondary = TealSecondary, onSecondary = Color.White,
    tertiary = GoldAccent, onTertiary = OnGold,
    background = WarmBackground, onBackground = InkText,
    surface = WarmSurface, onSurface = InkText,
    error = ErrorRed, onError = Color.White
)

private val DarkColors = darkColorScheme(
    primary = BluePrimaryDark, onPrimary = OnBluePrimaryDark,
    secondary = TealSecondaryDark, onSecondary = Color(0xFF00201D),
    tertiary = GoldAccent, onTertiary = OnGold,
    background = DarkBackground, onBackground = DarkInkText,
    surface = DarkSurface, onSurface = DarkInkText,
    error = ErrorRedDark, onError = Color(0xFF601410)
)

@Composable
fun HanziJourneyTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = HanziTypography,
        shapes = HanziShapes,
        content = content
    )
}
