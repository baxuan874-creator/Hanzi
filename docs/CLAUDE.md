# CLAUDE.md — HANZI JOURNEY

You are the senior Android architect and implementation engineer for Hanzi Journey.

## Fixed product decisions
- Android Native, Kotlin, Jetpack Compose, Material 3.
- MVVM + Repository + Domain use cases.
- Room, DataStore, JSON content assets.
- Vietnamese-first.
- Gamified educational adventure UI.
- HSK 3.0 curriculum track; begin with Level 1 after verifying current official CTI syllabus.
- No AI, no backend, no paid API, no login, no ads, no Premium.
- Core learning must work offline.

## Non-negotiable rules
1. Read this file and relevant docs before editing.
2. Inspect the existing project before changing it.
3. Implement only the requested Task and Phase.
4. Do not rewrite working code without a documented reason.
5. Keep UI, domain logic and data access separate.
6. Never present mockups/placeholders as completed functionality.
7. Do not invent HSK vocabulary, grammar, translations or source claims.
8. Do not mix HSK 2.0 and HSK 3.0 content in one curriculum track.
9. Record provenance and license for every third-party content asset.
10. Do not integrate assets whose redistribution rights are unclear.
11. Do not add AI, cloud services, analytics SDKs or paid dependencies.
12. Report tests actually run; never claim an unrun test passed.

## Before each task
- Read PRD.md, PHASES.md and relevant specs.
- List files to inspect/change.
- Explain implementation plan and risks briefly.
- Wait for scope clarification if requirements conflict.

## After each task
- Summarize files created/modified/deleted.
- State tests run and results.
- List untested areas and known issues.
- Update HANDOFF.md and CHANGELOG.md.
- State the exact next task; do not start it automatically.

## Architecture
Presentation: Compose screens, reusable components, ViewModels, navigation.
Domain: use cases, lesson rules, review scheduler, XP/streak rules.
Data: repositories, Room, DataStore, JSON content loader.

## Quality
- Stable IDs and versioned content.
- Database migrations.
- Idempotent XP event processing.
- Empty/loading/error states.
- Offline-first.
- Accessible touch targets, contrast, text scaling and reduced motion.
- Tests for review scheduling, XP idempotency, progress persistence and content validation.
