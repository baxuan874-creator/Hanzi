# HANDOFF — HANZI JOURNEY

Date: 2026-11-26
Project version: 0.0.1-phase0

## Current phase and task
- Phase 2, Task 1: xác minh và khóa đề cương HSK 3.0 (trạng thái PROVISIONAL).

## Completed
- Phase 0, Phase 1: build xanh, chạy OK (5 tab, theme, Room, DataStore, chọn Sáng/Tối).
- Phase 2 Task 1: docs/SYLLABUS_LOCK.md (syllabus_id hsk_3_0_2025_11; Level 1 = 300 từ, 246 chữ nhận mặt, 0 chữ viết, 70 ngữ pháp), tools/fetch_syllabus.sh.

## Build and tests actually run
- Không đổi mã app ở task này; chưa chạy build. Script fetch_syllabus.sh chưa chạy (không có Internet ở môi trường tạo file).

## Known issues
- Chưa mở trực tiếp chinesetest.cn; chưa có checksum PDF. Khóa còn PROVISIONAL.
- Số 299 vs 300 từ Level 1 ở nguồn thứ cấp chưa giải quyết.
- PRD chưa cập nhật theo phát hiện (xem SYLLABUS_LOCK.md mục 5), chờ chủ dự án đồng ý.

## Exact next task
- Người dùng chạy tools/fetch_syllabus.sh trong Codespace và push syllabus_checksums.txt. Sau đó Phase 2, Task 2: trích danh mục Level 1 từ PDF chính thức thành bảng làm việc (chưa soạn nghĩa/ví dụ), đối chiếu số lượng 300/246/70, và viết validator cơ bản.

## Decisions that must remain unchanged
- Android Native / Kotlin / Compose; Vietnamese-first; gamified UI; HSK 3.0 track; không AI/backend/API trả phí/login/ads/Premium; offline-first.

## Exact next task
- Xác nhận build Actions xanh và cài APK lên máy. Sau đó: Phase 0, Task 2 — thêm Room + DataStore (database rỗng + UserSettings) và package structure.

## Context for next Claude session
Read CLAUDE.md, docs/PRD.md, docs/PHASES.md and this file before changing code.
