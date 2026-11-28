package com.hanzijourney.app.ui.learn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hanzijourney.app.data.content.ContentLoader
import com.hanzijourney.app.data.content.ContentRepository
import com.hanzijourney.app.data.content.ImportResult
import com.hanzijourney.app.data.content.LessonSummary
import com.hanzijourney.app.data.content.LoadStatus
import com.hanzijourney.app.data.progress.ProgressRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface LearnUiState {
    data object Loading : LearnUiState
    /** No lessons to show. [error] is set when a bundled pack was refused. */
    data class Empty(val error: String?) : LearnUiState
    data class Lessons(val lessons: List<LessonSummary>, val draft: Boolean, val completedIds: Set<String>) : LearnUiState
}

class LearnViewModel(
    private val repository: ContentRepository,
    private val loader: ContentLoader,
    private val progress: ProgressRepository
) : ViewModel() {
    private val _state = MutableStateFlow<LearnUiState>(LearnUiState.Loading)
    val state: StateFlow<LearnUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val error = when (val status = loader.ensureLoaded()) {
                is LoadStatus.NoPackBundled -> null
                is LoadStatus.Done -> when (val r = status.result) {
                    is ImportResult.Rejected -> r.issues.take(3).joinToString("; ") { "${it.recordId}: ${it.message}" }
                    is ImportResult.ParseError -> r.message
                    else -> null
                }
            }
            combine(repository.observeLessonSummaries(), progress.observeCompletedLessonIds()) { lessons, done ->
                if (lessons.isEmpty()) LearnUiState.Empty(error)
                else LearnUiState.Lessons(lessons, draft = repository.hasUnapprovedContent(), completedIds = done)
            }.collect { _state.value = it }
        }
    }

    companion object {
        fun factory(repository: ContentRepository, loader: ContentLoader, progress: ProgressRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T = LearnViewModel(repository, loader, progress) as T
            }
    }
}
