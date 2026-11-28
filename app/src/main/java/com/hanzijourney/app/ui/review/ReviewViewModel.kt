package com.hanzijourney.app.ui.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hanzijourney.app.HanziApp
import com.hanzijourney.app.data.content.ContentRepository
import com.hanzijourney.app.data.progress.ProgressRepository
import com.hanzijourney.app.domain.progress.Achievement
import com.hanzijourney.app.domain.review.ReviewCard
import com.hanzijourney.app.domain.review.ReviewResult
import com.hanzijourney.app.domain.review.ReviewSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ReviewUiState(
    val loading: Boolean = true,
    val nothingDue: Boolean = false,
    val card: ReviewCard? = null,
    val revealed: Boolean = false,
    val isRetry: Boolean = false,
    val progress: Float = 0f,
    val answered: Int = 0,
    val total: Int = 0,
    val result: ReviewResult? = null,
    val xpEarned: Int = 0,
    val newAchievements: List<Achievement> = emptyList(),
    val saveFailed: Boolean = false
)

class ReviewViewModel(
    private val progress: ProgressRepository,
    private val content: ContentRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ReviewUiState())
    val state: StateFlow<ReviewUiState> = _state.asStateFlow()

    private var session: ReviewSession? = null
    private var sessionId = ""
    private var xpEarned = 0
    private val achievements = mutableListOf<Achievement>()
    private var saveFailed = false

    init { load() }

    /** Called when the tab is shown again: reloads unless a session is in progress. */
    fun refreshIfIdle() {
        val s = session
        if (s == null || s.isFinished) load()
    }

    /** Starts a new session from the words that are due now. */
    fun load() {
        viewModelScope.launch {
            _state.value = ReviewUiState(loading = true)
            xpEarned = 0; achievements.clear(); saveFailed = false
            val items = try { progress.dueReviewItems() } catch (e: Exception) { emptyList() }
            val vocabById = content.vocabulary(items.map { it.vocabularyId }).associateBy { it.id }
            val cards = items.mapNotNull { item -> vocabById[item.vocabularyId]?.let { ReviewCard(item, it) } }
            if (cards.isEmpty()) {
                session = null
                _state.value = ReviewUiState(loading = false, nothingDue = true)
            } else {
                session = ReviewSession(cards)
                sessionId = System.currentTimeMillis().toString()
                publish()
            }
        }
    }

    fun reveal() { session?.reveal(); publish() }

    fun answer(remembered: Boolean) {
        val s = session ?: return
        val a = s.answer(remembered) ?: return
        publish()
        // A second try of a forgotten card does not change the schedule.
        if (!a.countsForSchedule) return
        viewModelScope.launch {
            try {
                val outcome = progress.recordReview(sessionId, a.card.vocab.id, remembered)
                xpEarned += outcome.xpAwarded
                achievements += outcome.newAchievements
            } catch (e: Exception) {
                saveFailed = true
            }
            publish()
        }
    }

    private fun publish() {
        val s = session ?: return
        _state.value = ReviewUiState(
            loading = false,
            card = s.current,
            revealed = s.revealed,
            isRetry = s.isRetry,
            progress = s.progress,
            answered = s.answeredCount,
            total = s.totalCount,
            result = if (s.isFinished) s.result() else null,
            xpEarned = xpEarned,
            newAchievements = achievements.toList(),
            saveFailed = saveFailed
        )
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HanziApp
                ReviewViewModel(app.container.progressRepository, app.container.contentRepository)
            }
        }
    }
}
