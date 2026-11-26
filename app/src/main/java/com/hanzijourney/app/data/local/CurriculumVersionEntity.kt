package com.hanzijourney.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Provenance record of a content pack (PRD section 1, rule 3). Holds no user progress. */
@Entity(tableName = "curriculum_version")
data class CurriculumVersionEntity(
    @PrimaryKey val id: String,
    val curriculumId: String,
    val version: String,
    val source: String,
    val retrievedAt: String,
    val checksum: String
)
