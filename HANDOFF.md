# HANDOFF — HANZI JOURNEY

Date: 2026-11-26
Project version: 0.0.1-phase0

## Current phase and task
- Phase 1, Task 2: component dùng chung + chế độ sáng/tối (xong, chờ xác nhận build Actions).

## Completed
- Phase 0 (Task 1-2) và Phase 1 Task 1: build xanh, chạy OK trên máy người dùng.
- Phase 1 Task 2: ui/components (HanziButton, StatusBadge, HanziProgressBar, LessonCard, LearnMap), ProfileViewModel + màn Hồ sơ có chọn giao diện Sáng/Tối/Theo hệ thống (DataStore, áp dụng toàn app qua MainActivity).

## Build and tests actually run
- Phase 1 Task 2: KHÔNG chạy build (không có Internet/SDK). Chưa có tests.

## Known issues
- Chưa xác nhận build. Rủi ro: viewModel factory/CreationExtras, Card(onClick, enabled), LinearProgressIndicator(progress lambda).
- Các component (HanziButton, LessonCard, HanziProgressBar, LearnMap) CHƯA được màn hình nào dùng; sẽ dùng ở Phase 3. Không có mock data theo quyết định ở Task 1.
- Chưa có Settings đầy đủ (âm thanh, giảm chuyển động chưa có điều khiển vì chưa có tác dụng), About & Licenses.
- Chưa có unit/UI test.

## Exact next task
- Xác nhận Actions xanh; thử đổi Sáng/Tối trong tab Hồ sơ, tắt app mở lại phải giữ lựa chọn. Sau đó Phase 2, Task 1: xác minh và khóa phiên bản đề cương HSK 3.0 từ nguồn CTI (ghi version, ngày truy cập, nguồn vào docs/SYLLABUS_LOCK.md), chưa nhập từ vựng.

## Decisions that must remain unchanged
- Android Native / Kotlin / Compose; Vietnamese-first; gamified UI; HSK 3.0 track; không AI/backend/API trả phí/login/ads/Premium; offline-first.

## Exact next task
- Xác nhận build Actions xanh và cài APK lên máy. Sau đó: Phase 0, Task 2 — thêm Room + DataStore (database rỗng + UserSettings) và package structure.

## Context for next Claude session
Read CLAUDE.md, docs/PRD.md, docs/PHASES.md and this file before changing code.
