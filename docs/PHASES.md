# HANZI JOURNEY — PHASE PLAN

## Phase 0 — Foundation
Project setup, Gradle, Kotlin, Compose, package structure, Room, DataStore, documentation, first debug build.
Exit: app builds and launches.

## Phase 1 — Design system & navigation
Design tokens, typography, reusable components, navigation, Home, Learn Map, Profile.
Exit: screens navigate without crashes; visual system consistent.

## Phase 2 — Curriculum pipeline
Verify official CTI HSK 3.0 syllabus version, define schema, importer, validator, Level 1 content pack and provenance.
Exit: content passes schema, duplicate-ID, level mapping and completeness checks.

## Phase 3 — Learning engine
Lesson flow, vocabulary, grammar, exercise renderer, feedback, scoring and result.
Exit: one complete lesson works end-to-end.

## Phase 4 — Progress & gamification
Room progress, XP event ledger, level, streak, daily missions and achievements.
Exit: no duplicate XP; state persists after restart.

## Phase 5 — Dictionary & review
Search, vocabulary details, flashcards, scheduler and review queue.
Exit: review schedule persists and is testable.

## Phase 6 — Pinyin, Hanzi & audio
Pinyin drills, audio service, writing canvas, stroke display only after license review.
Exit: clear device fallback; no false pronunciation/stroke claims.

## Phase 7 — QA
Unit/UI tests, offline tests, migration tests, content QA, accessibility and performance.
Exit: no blocker/critical defects in main flows.

## Phase 8 — Release
Icon, splash, versioning, privacy disclosures, third-party notices, APK/AAB.
Exit: release checklist passes.

## Claude Free operating protocol
- One task per chat/turn; do not ask Claude to implement an entire phase in one response.
- Provide CLAUDE.md and relevant docs.
- Use Android Studio to build and test.
- Keep backups or Git commits before large changes.
- Update HANDOFF.md after each work session.
