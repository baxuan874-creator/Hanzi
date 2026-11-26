# HANDOFF — HANZI JOURNEY

Date: 2026-11-26
Project version: 0.0.1-phase0

## Current phase and task
- Phase 2, Task 2: bảng làm việc 300 từ Level 1 + validator (xong, chờ xác nhận Actions).

## Completed
- Phase 0, Phase 1: build xanh, chạy OK. Phase 2 Task 1: SYLLABUS_LOCK.md (PROVISIONAL), fetch_syllabus.sh.
- Phase 2 Task 2: content_work/hsk3_l1_vocab_working.csv (300 dòng, extracted_unreviewed), tools/validate_level1.py, bước validate trong workflow, docs/EXTRACTION_REPORT_L1.md, docs/ đầy đủ tài liệu dự án.

## Build and tests actually run
- validate_level1.py chạy trên dữ liệu thật (0 lỗi, 14 cảnh báo) và trên bản sao hỏng (5 lỗi, mã thoát 1): đạt.
- Build Gradle và workflow mới: chưa chạy ở môi trường tạo file; sẽ chạy trên Actions.

## Known issues
- Dữ liệu chưa được người biết tiếng Trung rà soát (cảnh báo 小朋友 pinyin).
- Giấy phép phân phối danh sách từ chưa rõ; repo đang Public. Đề xuất chuyển Private.
- Khóa đề cương vẫn PROVISIONAL (chưa checksum, chưa có 246 chữ / 70 ngữ pháp).
- Phase 2 không làm thay đổi giao diện app; thay đổi nhìn thấy được bắt đầu ở Phase 3.

## Exact next task
- Xác nhận Actions xanh (bước "Validate content" phải qua). Sau đó Phase 2, Task 3: trích 246 chữ nhận mặt + 70 điểm ngữ pháp Level 1, thêm kiểm tra tham chiếu chéo và checksum; chưa soạn nghĩa tiếng Việt.

## Decisions that must remain unchanged
- Android Native / Kotlin / Compose; Vietnamese-first; gamified UI; HSK 3.0 track; không AI/backend/API trả phí/login/ads/Premium; offline-first.

## Exact next task
- Xác nhận build Actions xanh và cài APK lên máy. Sau đó: Phase 0, Task 2 — thêm Room + DataStore (database rỗng + UserSettings) và package structure.

## Context for next Claude session
Read CLAUDE.md, docs/PRD.md, docs/PHASES.md and this file before changing code.
