package com.hanzijourney.app.di

import android.content.Context
import com.hanzijourney.app.data.local.HanziDatabase
import com.hanzijourney.app.data.prefs.SettingsRepository
import com.hanzijourney.app.data.prefs.settingsDataStore

class AppContainer(private val context: Context) {
    val database: HanziDatabase by lazy { HanziDatabase.create(context) }
    val settingsRepository: SettingsRepository by lazy { SettingsRepository(context.settingsDataStore) }
}
