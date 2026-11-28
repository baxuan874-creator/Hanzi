package com.hanzijourney.app.data.content

import com.hanzijourney.app.domain.lesson.Matching

enum class Severity { ERROR, WARNING }

data class Issue(val severity: Severity, val recordId: String, val message: String)

data class ValidationReport(val issues: List<Issue>) {
    val errors: List<Issue> get() = issues.filter { it.severity == Severity.ERROR }
    val warnings: List<Issue> get() = issues.filter { it.severity == Severity.WARNING }
    val isValid: Boolean get() = errors.isEmpty()
}

/** Mirrors the rules of tools/validate_level1.py for content packs that the app loads. Never auto-corrects data. */
object ContentValidator {
    private val toneMarks = "āáǎàēéěèīíǐìōóǒòūúǔùǖǘǚǜ".toSet()
    private val cjk = Regex("^[\u4e00-\u9fff]+$")
    private val reviewStatuses = setOf("extracted_unreviewed", "draft_unreviewed", "reviewed", "approved")
    private val choiceTypes = setOf("multiple_choice", "listening_choice")
    val exerciseTypes = setOf(
        "multiple_choice", "matching", "listening_choice", "sentence_ordering",
        "fill_blank", "grammar_choice", "reading_question", "stroke_practice"
    )

    fun validate(pack: ContentPack, requireApproved: Boolean = false): ValidationReport {
        val issues = mutableListOf<Issue>()
        fun error(id: String, msg: String) { issues += Issue(Severity.ERROR, id, msg) }
        fun warn(id: String, msg: String) { issues += Issue(Severity.WARNING, id, msg) }
        fun blank(id: String, field: String, value: String?) { if (value.isNullOrBlank()) error(id, "empty $field") }
        fun duplicates(kind: String, ids: List<String>) {
            ids.groupBy { it }.filter { it.value.size > 1 }.keys.forEach { error(it, "duplicate $kind id") }
        }

        val m = pack.manifest
        blank("manifest", "curriculum_id", m.curriculumId)
        blank("manifest", "version", m.version)
        blank("manifest", "source", m.source)
        blank("manifest", "retrieved_at", m.retrievedAt)
        blank("manifest", "checksum", m.checksum)

        duplicates("chapter", pack.chapters.map { it.id })
        duplicates("lesson", pack.lessons.map { it.id })
        duplicates("vocabulary", pack.vocabulary.map { it.id })
        duplicates("grammar", pack.grammarPoints.map { it.id })
        duplicates("exercise", pack.exercises.map { it.id })

        val chapterIds = pack.chapters.map { it.id }.toSet()
        val lessonIds = pack.lessons.map { it.id }.toSet()
        val vocabIds = pack.vocabulary.map { it.id }.toSet()
        val grammarIds = pack.grammarPoints.map { it.id }.toSet()
        val exerciseById = pack.exercises.associateBy { it.id }

        for (c in pack.chapters) {
            blank(c.id, "title_vi", c.titleVi)
            for (l in c.lessonIds) if (l !in lessonIds) error(c.id, "chapter lists unknown lesson $l")
        }

        val seenVocab = mutableMapOf<String, String>()
        val seenGrammar = mutableMapOf<String, String>()
        for (l in pack.lessons) {
            blank(l.id, "title_vi", l.titleVi)
            if (l.curriculumId != m.curriculumId) error(l.id, "curriculum_id ${l.curriculumId} differs from manifest ${m.curriculumId} (tracks must not be mixed)")
            if (l.chapterId !in chapterIds) error(l.id, "unknown chapter ${l.chapterId}")
            else if (pack.chapters.first { it.id == l.chapterId }.lessonIds.none { it == l.id }) warn(l.id, "chapter ${l.chapterId} does not list this lesson")
            if (l.objectives.isEmpty()) error(l.id, "no objectives")
            if (l.estimatedMinutes !in 5..10) warn(l.id, "estimated_minutes ${l.estimatedMinutes} is outside 5-10")
            for (v in l.vocabularyIds) {
                if (v !in vocabIds) error(l.id, "unknown vocabulary id $v")
                else seenVocab.put(v, l.id)?.let { error(l.id, "vocabulary $v already introduced in $it") }
            }
            for (g in l.grammarIds) {
                if (g !in grammarIds) error(l.id, "unknown grammar id $g")
                else seenGrammar.put(g, l.id)?.let { error(l.id, "grammar $g already introduced in $it") }
            }
            for (e in l.exerciseIds) {
                val ex = exerciseById[e]
                if (ex == null) error(l.id, "unknown exercise id $e")
                else if (ex.lessonId != l.id) error(l.id, "exercise $e belongs to lesson ${ex.lessonId}")
            }
            for (p in l.prerequisiteIds) {
                if (p == l.id) error(l.id, "lesson is its own prerequisite")
                else if (p !in lessonIds) error(l.id, "unknown prerequisite $p")
            }
        }

        for (v in pack.vocabulary) {
            blank(v.id, "hanzi", v.hanzi); blank(v.id, "pinyin", v.pinyin); blank(v.id, "meaning_vi", v.meaningVi)
            blank(v.id, "source_ref", v.sourceRef); blank(v.id, "license_ref", v.licenseRef)
            if (v.curriculumId != m.curriculumId) error(v.id, "curriculum_id ${v.curriculumId} differs from manifest ${m.curriculumId} (tracks must not be mixed)")
            if (v.hanzi.isNotBlank() && !cjk.matches(v.hanzi)) error(v.id, "hanzi contains non-CJK characters: ${v.hanzi}")
            if (v.pinyin.isNotBlank() && v.pinyin.none { it in toneMarks }) warn(v.id, "pinyin has no tone mark: ${v.pinyin}")
            if (v.reviewStatus !in reviewStatuses) error(v.id, "bad review_status ${v.reviewStatus}")
            else if (requireApproved && v.reviewStatus != "approved") error(v.id, "review_status is ${v.reviewStatus}, approved required")
            for (ex in v.examples) {
                if (ex.hanzi.isBlank() || ex.pinyin.isBlank() || ex.meaningVi.isBlank()) error(v.id, "example with an empty field")
            }
        }

        for (g in pack.grammarPoints) {
            blank(g.id, "pattern", g.pattern); blank(g.id, "explanation_vi", g.explanationVi); blank(g.id, "source_ref", g.sourceRef)
            if (g.curriculumId != m.curriculumId) error(g.id, "curriculum_id ${g.curriculumId} differs from manifest ${m.curriculumId} (tracks must not be mixed)")
            if (g.reviewStatus !in reviewStatuses) error(g.id, "bad review_status ${g.reviewStatus}")
            else if (requireApproved && g.reviewStatus != "approved") error(g.id, "review_status is ${g.reviewStatus}, approved required")
            if (g.examples.isEmpty()) warn(g.id, "no examples")
            for (ex in g.examples) {
                if (ex.hanzi.isBlank() || ex.pinyin.isBlank() || ex.meaningVi.isBlank()) error(g.id, "example with an empty field")
            }
        }

        for (e in pack.exercises) {
            blank(e.id, "prompt", e.prompt); blank(e.id, "answer", e.answer)
            blank(e.id, "explanation_vi", e.explanationVi); blank(e.id, "source_ref", e.sourceRef)
            if (e.lessonId !in lessonIds) error(e.id, "unknown lesson ${e.lessonId}")
            if (e.type !in exerciseTypes) error(e.id, "unknown exercise type ${e.type}")
            if (e.difficulty !in 1..5) error(e.id, "difficulty ${e.difficulty} outside 1-5")
            val options = e.options
            if (options != null && options.isNotEmpty()) {
                if (options.size < 2) error(e.id, "fewer than 2 options")
                if (options.toSet().size != options.size) error(e.id, "duplicate options")
                if (e.type in choiceTypes && e.answer !in options) error(e.id, "answer is not one of the options")
            } else if (e.type in choiceTypes) {
                error(e.id, "${e.type} needs options")
            }
            if (e.type == "sentence_ordering") {
                val tokens = e.answer.split(" ").filter { it.isNotEmpty() }
                if (options == null || options.sorted() != tokens.sorted()) error(e.id, "sentence_ordering answer tokens must match the options")
            }
            if (e.type == "matching") {
                val parsed = options.orEmpty().map { Matching.parse(it) }
                if (options == null || options.size < Matching.MIN_PAIRS || options.size > Matching.MAX_PAIRS) {
                    error(e.id, "matching needs ${Matching.MIN_PAIRS}-${Matching.MAX_PAIRS} pairs")
                } else if (parsed.any { it == null }) {
                    error(e.id, "matching option must be 'left|right'")
                } else {
                    val pairs = parsed.filterNotNull()
                    if (pairs.map { it.left }.toSet().size != pairs.size || pairs.map { it.right }.toSet().size != pairs.size) {
                        error(e.id, "matching sides must be unique")
                    }
                    if (Matching.normalize(e.answer) != Matching.normalize(options.joinToString(";"))) {
                        error(e.id, "matching answer pairs must equal the options")
                    }
                }
            }
        }

        return ValidationReport(issues)
    }
}
