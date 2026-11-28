package com.hanzijourney.app.domain.lesson

import com.hanzijourney.app.data.content.ExerciseDto
import com.hanzijourney.app.data.content.GrammarPointDto
import com.hanzijourney.app.data.content.LessonDto
import com.hanzijourney.app.data.content.VocabularyDto

/** Everything the lesson player needs for one lesson, in teaching order. */
data class LessonContent(
    val lesson: LessonDto,
    val vocabulary: List<VocabularyDto>,
    val grammar: List<GrammarPointDto>,
    val exercises: List<ExerciseDto>
)
