package com.hanzijourney.app.di

import android.content.Context
import com.hanzijourney.app.data.content.AssetContentSource
import com.hanzijourney.app.BuildConfig
import com.hanzijourney.app.data.content.ContentImporter
import com.hanzijourney.app.data.content.ContentLoader
import com.hanzijourney.app.data.content.ContentRepository
import com.hanzijourney.app.data.content.ContentStore
import com.hanzijourney.app.data.content.room.ContentDatabase
import com.hanzijourney.app.data.content.room.RoomContentStore
import com.hanzijourney.app.data.local.HanziDatabase
import com.hanzijourney.app.data.prefs.SettingsRepository
import com.hanzijourney.app.data.prefs.settingsDataStore

class AppContainer(private val context: Context) {
    val database: HanziDatabase by lazy { HanziDatabase.create(context) }
    val contentDatabase: ContentDatabase by lazy { ContentDatabase.create(context) }
    val contentStore: ContentStore by lazy { RoomContentStore(contentDatabase) }
    val contentImporter: ContentImporter by lazy { ContentImporter(contentStore) }
    val contentAssets: AssetContentSource by lazy { AssetContentSource(context) }
    val contentRepository: ContentRepository by lazy { ContentRepository(contentDatabase.contentDao()) }
    val contentLoader: ContentLoader by lazy { ContentLoader(contentAssets, contentImporter, BuildConfig.DEBUG) }
    val settingsRepository: SettingsRepository by lazy { SettingsRepository(context.settingsDataStore) }
}
