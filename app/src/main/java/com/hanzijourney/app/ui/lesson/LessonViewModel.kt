package com.hanzijourney.app.ui.lesson

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hanzijourney.app.data.content.ContentRepository
import com.hanzijourney.app.data.progress.ProgressRepository
import com.hanzijourney.app.domain.progress.LessonCompletion
import com.hanzijourney.app.domain.lesson.Feedback
import com.hanzijourney.app.domain.lesson.LessonContent
import com.hanzijourney.app.domain.lesson.LessonResult
import com.hanzijourney.app.domain.lesson.LessonSession
import com.hanzijourney.app.domain.lesson.LessonStep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LessonUiState(
    val loading: Boolean = true,
    val notFound: Boolean = false,
    val title: String = "",
    val objectives: List<String> = emptyList(),
    val draftNotice: Boolean = false,
    val step: LessonStep? = null,
    val feedback: Feedback? = null,
    val progress: Float = 0f,
    val result: LessonResult? = null,
    val completion: LessonCompletion? = null,
    val saveFailed: Boolean = false
)

class LessonViewModel(
    private val repository: ContentRepository,
    private val progress: ProgressRepository,
    private val lessonId: String
) : ViewModel() {
    private val _state = MutableStateFlow(LessonUiState())
    val state: StateFlow<LessonUiState> = _state.asStateFlow()
    private var content: LessonContent? = null
    private var session: LessonSession? = null
    // The finished lesson is saved once per run; XP idempotency is also enforced in the store.
    private var recorded = false
    private var completion: LessonCompletion? = null
    private var saveFailed = false

    init {
        viewModelScope.launch {
            val loaded = repository.loadLesson(lessonId)
            if (loaded == null) {
                _state.value = LessonUiState(loading = false, notFound = true)
            } else {
                content = loaded
                session = LessonSession(loaded)
                publish()
            }
        }
    }

    fun submit(answer: String) { session?.submit(answer); publish() }
    fun next() { session?.next(); publish(); recordIfFinished() }
    fun restart() {
        content?.let {
            session = LessonSession(it)
            recorded = false; completion = null; saveFailed = false
            publish()
        }
    }

    private fun recordIfFinished() {
        val s = session ?: return
        if (!s.isFinished || recorded) return
        recorded = true
        val r = s.result()
        viewModelScope.launch {
            try {
                completion = progress.completeLesson(
                    lessonId = lessonId,
                    correctFirstTry = r.correctFirstTry,
                    totalExercises = r.totalExercises,
                    vocabularyIds = content?.vocabulary?.map { it.id }.orEmpty(),
                    catalog = repository.catalog()
                )
            } catch (e: Exception) {
                saveFailed = true
            }
            publish()
        }
    }

    private fun publish() {
        val s = session ?: return
        val c = content ?: return
        _state.value = LessonUiState(
            loading = false,
            title = c.lesson.titleVi,
            objectives = c.lesson.objectives,
            draftNotice = c.vocabulary.any { it.reviewStatus != "approved" },
            step = s.current,
            feedback = s.feedback,
            progress = s.progress,
            result = if (s.isFinished) s.result() else null,
            completion = completion,
            saveFailed = saveFailed
        )
    }

    companion object {
        fun factory(repository: ContentRepository, progress: ProgressRepository, lessonId: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T = LessonViewModel(repository, progress, lessonId) as T
            }
    }
}
