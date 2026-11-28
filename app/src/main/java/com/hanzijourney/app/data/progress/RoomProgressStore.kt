package com.hanzijourney.app.data.progress

import androidx.room.withTransaction
import com.hanzijourney.app.data.local.DailyMissionEntity
import com.hanzijourney.app.data.local.HanziDatabase
import com.hanzijourney.app.data.local.LearnedWordEntity
import com.hanzijourney.app.data.local.ReviewItemEntity
import com.hanzijourney.app.data.local.UserAchievementEntity
import com.hanzijourney.app.data.local.LessonProgressEntity
import com.hanzijourney.app.data.local.PracticeDayEntity
import com.hanzijourney.app.data.local.XpEventEntity
import com.hanzijourney.app.domain.progress.DailyMission
import com.hanzijourney.app.domain.progress.LessonProgress
import com.hanzijourney.app.domain.progress.XpEvent
import com.hanzijourney.app.domain.review.ReviewItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomProgressStore(private val database: HanziDatabase) : ProgressStore {
    private val dao get() = database.progressDao()

    override suspend fun getLesson(lessonId: String): LessonProgress? = dao.getLesson(lessonId)?.let {
        LessonProgress(it.lessonId, it.firstCompletedAt, it.lastCompletedAt, it.bestCorrect, it.totalExercises, it.timesCompleted)
    }

    override suspend fun saveLesson(progress: LessonProgress) {
        dao.upsertLesson(
            LessonProgressEntity(
                progress.lessonId, progress.firstCompletedAt, progress.lastCompletedAt,
                progress.bestCorrect, progress.totalExercises, progress.timesCompleted
            )
        )
    }

    override suspend fun insertXpEvent(event: XpEvent): Boolean =
        dao.insertXpEvent(XpEventEntity(event.eventId, event.kind, event.amount, event.createdAt, event.localDate)) != -1L

    override suspend fun markPracticeDay(localDate: String) {
        dao.insertPracticeDay(PracticeDayEntity(localDate))
    }

    override suspend fun getCompletedLessonIds(): Set<String> = dao.getCompletedLessonIds().toSet()
    override suspend fun getPracticeDays(): Set<String> = dao.getPracticeDays().toSet()

    override suspend fun getMissions(localDate: String): List<DailyMission> = dao.getMissions(localDate).map { it.toDomain() }
    override suspend fun saveMission(mission: DailyMission) {
        dao.upsertMission(
            DailyMissionEntity(mission.id, mission.localDate, mission.type, mission.target, mission.progress, mission.rewardXp, mission.completedAt)
        )
    }

    override suspend fun markWordLearned(vocabularyId: String, at: Long): Boolean =
        dao.insertLearnedWord(LearnedWordEntity(vocabularyId, at)) != -1L
    override suspend fun getLearnedWordIds(): Set<String> = dao.getLearnedWordIds().toSet()

    override suspend fun unlockAchievement(achievementId: String, at: Long): Boolean =
        dao.insertAchievement(UserAchievementEntity(achievementId, at)) != -1L

    override suspend fun getReviewItem(vocabularyId: String): ReviewItem? = dao.getReviewItem(vocabularyId)?.toDomain()
    override suspend fun saveReviewItem(item: ReviewItem) { dao.upsertReviewItem(item.toEntity()) }
    override suspend fun createReviewItem(item: ReviewItem): Boolean = dao.insertReviewItem(item.toEntity()) != -1L
    override suspend fun getDueReviewItems(now: Long, limit: Int): List<ReviewItem> =
        dao.getDueReviewItems(now, limit).map { it.toDomain() }
    override suspend fun getTotalReviews(): Int = dao.getTotalReviews()

    override suspend fun <T> atomically(block: suspend ProgressStore.() -> T): T =
        database.withTransaction { block(this@RoomProgressStore) }

    override fun observeCompletedLessonIds(): Flow<Set<String>> = dao.observeCompletedLessonIds().map { it.toSet() }
    override fun observeTotalXp(): Flow<Int> = dao.observeTotalXp()
    override fun observePracticeDays(): Flow<Set<String>> = dao.observePracticeDays().map { it.toSet() }

    override fun observeMissions(localDate: String): Flow<List<DailyMission>> =
        dao.observeMissions(localDate).map { list -> list.map { it.toDomain() } }
    override fun observeLearnedWordCount(): Flow<Int> = dao.observeLearnedWordCount()
    override fun observeUnlockedAchievementIds(): Flow<Set<String>> = dao.observeAchievementIds().map { it.toSet() }

    private fun DailyMissionEntity.toDomain() = DailyMission(missionId, localDate, type, target, progress, rewardXp, completedAt)
    override fun observeDueCount(now: Long): Flow<Int> = dao.observeDueCount(now)
    override fun observeTotalReviews(): Flow<Int> = dao.observeTotalReviews()

    private fun ReviewItemEntity.toDomain() = ReviewItem(vocabularyId, stage, dueAt, lastReviewedAt, reviewCount, lapses, createdAt)
    private fun ReviewItem.toEntity() = ReviewItemEntity(vocabularyId, stage, dueAt, lastReviewedAt, reviewCount, lapses, createdAt)
}
