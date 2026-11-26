package com.hanzijourney.app.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hanzijourney.app.HanziApp
import com.hanzijourney.app.data.prefs.SettingsRepository
import com.hanzijourney.app.domain.model.DarkModeSetting
import com.hanzijourney.app.domain.model.UserSettings
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: SettingsRepository) : ViewModel() {
    val settings: StateFlow<UserSettings> = repository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UserSettings())

    fun setDarkMode(mode: DarkModeSetting) {
        viewModelScope.launch { repository.setDarkMode(mode) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HanziApp
                ProfileViewModel(app.container.settingsRepository)
            }
        }
    }
}
