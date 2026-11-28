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
    error = ErrorRed, onError = Color.White,
    primaryContainer = Color(0xFFDCE7FF), onPrimaryContainer = Color(0xFF00174A),
    secondaryContainer = Color(0xFFD3F3EE), onSecondaryContainer = Color(0xFF00201D),
    errorContainer = Color(0xFFFFDAD6), onErrorContainer = Color(0xFF410002),
    surfaceVariant = Color(0xFFEFE8D8), onSurfaceVariant = Color(0xFF4A463C),
    outline = Color(0xFF8A8478)
)

private val DarkColors = darkColorScheme(
    primary = BluePrimaryDark, onPrimary = OnBluePrimaryDark,
    secondary = TealSecondaryDark, onSecondary = Color(0xFF00201D),
    tertiary = GoldAccent, onTertiary = OnGold,
    background = DarkBackground, onBackground = DarkInkText,
    surface = DarkSurface, onSurface = DarkInkText,
    error = ErrorRedDark, onError = Color(0xFF601410),
    primaryContainer = Color(0xFF1F3F85), onPrimaryContainer = Color(0xFFDCE7FF),
    secondaryContainer = Color(0xFF0C4A45), onSecondaryContainer = Color(0xFFBDF2EB),
    errorContainer = Color(0xFF8C1D18), onErrorContainer = Color(0xFFFFDAD6),
    surfaceVariant = Color(0xFF2B3550), onSurfaceVariant = Color(0xFFC3C8D8),
    outline = Color(0xFF8F97AD)
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
