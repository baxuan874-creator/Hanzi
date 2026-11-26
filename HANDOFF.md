# HANDOFF — HANZI JOURNEY

Date: 2026-11-26
Project version: 0.0.1-phase0

## Current phase and task
- Phase 1, Task 1: design tokens + navigation 5 tab (xong, chờ xác nhận build Actions).

## Completed
- Phase 0 Task 1-2: khung dự án, Room, DataStore, DI (build xanh, chạy OK trên máy người dùng).
- Phase 1 Task 1: ui/theme (Color, Type, Shape, Spacing, Theme), ui/components/EmptyState, ui/navigation (TopLevelDestination, HanziAppRoot), ui/screens (Home, Learn, Vocabulary, Review, Profile — chỉ trạng thái trống thật, không mock data).

## Build and tests actually run
- Phase 1 Task 1: KHÔNG chạy build (không có Internet/SDK). Chưa có tests.

## Known issues
- Chưa xác nhận build; điểm rủi ro: import icon (Icons.AutoMirrored.Filled.List, LocationOn) và navigation-compose 2.8.2.
- Theme chưa đọc DarkModeSetting từ DataStore (chưa có màn Cài đặt); đang theo hệ thống.
- Chưa có Learn Map thật, component thẻ/nút game; chưa có Settings/About & Licenses.

## Exact next task
- Xác nhận Actions xanh và 5 tab chạy. Sau đó Phase 1, Task 2: component dùng chung (nút chính, thẻ bài học, thanh tiến độ, huy hiệu) + Learn Map khung node theo dữ liệu rỗng, và nối theme với DarkModeSetting.

## Decisions that must remain unchanged
- Android Native / Kotlin / Compose; Vietnamese-first; gamified UI; HSK 3.0 track; không AI/backend/API trả phí/login/ads/Premium; offline-first.

## Exact next task
- Xác nhận build Actions xanh và cài APK lên máy. Sau đó: Phase 0, Task 2 — thêm Room + DataStore (database rỗng + UserSettings) và package structure.

## Context for next Claude session
Read CLAUDE.md, docs/PRD.md, docs/PHASES.md and this file before changing code.
