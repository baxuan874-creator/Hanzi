package com.hanzijourney.app.ui.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hanzijourney.app.HanziApp
import com.hanzijourney.app.data.content.ContentLoader
import com.hanzijourney.app.data.content.ContentRepository
import com.hanzijourney.app.data.progress.ProgressRepository
import com.hanzijourney.app.domain.progress.ProgressSnapshot
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProgressViewModel(
    private val repository: ProgressRepository,
    private val content: ContentRepository,
    private val loader: ContentLoader
) : ViewModel() {
    init {
        // Plan today's missions from the content that is actually available (needs the pack imported first).
        viewModelScope.launch {
            try {
                loader.ensureLoaded()
                repository.ensureTodayMissions(content.catalog())
            } catch (e: Exception) {
                // Missions simply stay empty; learning is not affected.
            }
        }
    }

    /** null until the first value has been read from the database. */
    val snapshot: StateFlow<ProgressSnapshot?> = repository.observeSnapshot()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    /** Words due for review now (re-read every 30 s while subscribed). */
    val dueCount: StateFlow<Int> = repository.observeDueCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HanziApp
                ProgressViewModel(app.container.progressRepository, app.container.contentRepository, app.container.contentLoader)
            }
        }
    }
}
