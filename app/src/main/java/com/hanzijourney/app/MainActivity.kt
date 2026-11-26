package com.hanzijourney.app

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.hanzijourney.app.domain.model.DarkModeSetting
import com.hanzijourney.app.domain.model.UserSettings
import com.hanzijourney.app.ui.navigation.HanziAppRoot
import com.hanzijourney.app.ui.theme.HanziJourneyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = (application as HanziApp).container.settingsRepository
        setContent {
            val settings by repository.settings.collectAsState(initial = UserSettings())
            val systemDark = isSystemInDarkTheme()
            val dark = when (settings.darkMode) {
                DarkModeSetting.SYSTEM -> systemDark
                DarkModeSetting.LIGHT -> false
                DarkModeSetting.DARK -> true
            }
            DisposableEffect(dark) {
                enableEdgeToEdge(
                    statusBarStyle = if (dark) SystemBarStyle.dark(Color.TRANSPARENT)
                    else SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
                    navigationBarStyle = if (dark) SystemBarStyle.dark(Color.TRANSPARENT)
                    else SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
                )
                onDispose {}
            }
            HanziJourneyTheme(darkTheme = dark) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    HanziAppRoot()
                }
            }
        }
    }
}
