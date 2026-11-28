package com.hanzijourney.app.ui.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hanzijourney.app.HanziApp
import com.hanzijourney.app.data.progress.ProgressRepository
import com.hanzijourney.app.domain.progress.ProgressSnapshot
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ProgressViewModel(repository: ProgressRepository) : ViewModel() {
    /** null until the first value has been read from the database. */
    val snapshot: StateFlow<ProgressSnapshot?> = repository.observeSnapshot()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HanziApp
                ProgressViewModel(app.container.progressRepository)
            }
        }
    }
}
