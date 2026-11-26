package com.hanzijourney.app.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.hanzijourney.app.domain.model.DarkModeSetting
import com.hanzijourney.app.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

class SettingsRepository(private val dataStore: DataStore<Preferences>) {
    private object Keys {
        val SOUND = booleanPreferencesKey("sound_enabled")
        val REDUCE_MOTION = booleanPreferencesKey("reduce_motion")
        val DARK_MODE = stringPreferencesKey("dark_mode")
    }

    val settings: Flow<UserSettings> = dataStore.data.map { p ->
        val defaults = UserSettings()
        UserSettings(
            soundEnabled = p[Keys.SOUND] ?: defaults.soundEnabled,
            reduceMotion = p[Keys.REDUCE_MOTION] ?: defaults.reduceMotion,
            darkMode = p[Keys.DARK_MODE]
                ?.let { name -> DarkModeSetting.entries.firstOrNull { it.name == name } }
                ?: defaults.darkMode
        )
    }

    suspend fun setSoundEnabled(value: Boolean) = dataStore.edit { it[Keys.SOUND] = value }
    suspend fun setReduceMotion(value: Boolean) = dataStore.edit { it[Keys.REDUCE_MOTION] = value }
    suspend fun setDarkMode(value: DarkModeSetting) = dataStore.edit { it[Keys.DARK_MODE] = value.name }
}
