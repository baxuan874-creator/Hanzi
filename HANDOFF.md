# HANDOFF — HANZI JOURNEY

Date: 2026-11-26
Project version: 0.0.1-phase0

## Current phase and task
- Phase 0, Task 2: Room + DataStore + package structure (xong, chờ xác nhận build Actions).

## Completed
- Task 1: khung dự án, app shell, workflow build APK (đã build xanh và cài chạy OK trên máy người dùng).
- Task 2: Room 2.6.1 (KSP 2.0.20-1.0.25), HanziDatabase v1 + CurriculumVersionEntity/Dao, DataStore 1.1.1 + SettingsRepository, AppContainer, HanziApp.

## Package structure (com.hanzijourney.app)
- data/local (Room), data/prefs (DataStore), domain/model, di (AppContainer), ui/theme.

## Build and tests actually run
- Task 1: build Actions xanh, APK cài chạy OK (người dùng xác nhận).
- Task 2: KHÔNG chạy build (không có Internet/SDK). Chưa có unit test.

## Known issues
- Chưa xác nhận Task 2 build; KSP/Room là điểm dễ lỗi nhất nếu Actions đỏ.
- Chưa có UI dùng DB/Settings; chưa có tests. Thư mục app/schemas sẽ do KSP sinh khi build.

## Exact next task
- Xác nhận Actions xanh cho Task 2 và app vẫn mở được. Sau đó Phase 1, Task 1: design tokens + typography + Navigation Compose với 5 tab (Home, Learn, Vocabulary, Review, Profile) dùng màn hình trạng thái trống thật, không nút giả.

## Decisions that must remain unchanged
- Android Native / Kotlin / Compose; Vietnamese-first; gamified UI; HSK 3.0 track; không AI/backend/API trả phí/login/ads/Premium; offline-first.

## Exact next task
- Xác nhận build Actions xanh và cài APK lên máy. Sau đó: Phase 0, Task 2 — thêm Room + DataStore (database rỗng + UserSettings) và package structure.

## Context for next Claude session
Read CLAUDE.md, docs/PRD.md, docs/PHASES.md and this file before changing code.
