# HANDOFF — HANZI JOURNEY

Date: 2026-11-26
Project version: 0.0.1-phase0

## Current phase and task
- Phase 2, Task 3 hoàn tất về dữ liệu: 300 từ, 246 chữ nhận mặt, 70 ngữ pháp Level 1 (đều extracted_unreviewed).

## Completed
- Phase 0, Phase 1: build xanh, chạy OK. Phase 2: khóa đề cương (PROVISIONAL), bảng từ / chữ / ngữ pháp, validator, script trích PDF.

## Build and tests actually run
- validate_level1.py trên cả 3 bảng: 0 lỗi. selftest_extract.py đạt. Build Actions cho Task 3b/3c chưa xác nhận.

## Known issues
- 店 và 饺 có trong từ Level 1 nhưng không có trong 246 chữ nhận mặt (cần rà soát).
- Cách đếm 70 điểm ngữ pháp là của dự án; cảnh báo nguồn: 存现句1 (1) in 所处; 小朋友 pinyin; 6 cụm không có từ loại.
- Chưa có người biết tiếng Trung rà soát; repo Public (nên chuyển Private); khóa đề cương PROVISIONAL (chưa đối chiếu cổng chinesetest.cn, checksum nếu đã chạy fetch_syllabus.sh).

## Exact next task
- Phase 2, Task 4: đọc chủ đề/nhiệm vụ Level 1 trong đề cương (trang chủ đề, nhiệm vụ) và thiết kế cấu trúc chương/bài (gán 300 từ + 70 ngữ pháp vào bài theo chủ đề), chưa soạn nghĩa tiếng Việt/ví dụ.

## Decisions that must remain unchanged
- Android Native / Kotlin / Compose; Vietnamese-first; gamified UI; HSK 3.0 track; không AI/backend/API trả phí/login/ads/Premium; offline-first.

## Exact next task
- Xác nhận build Actions xanh và cài APK lên máy. Sau đó: Phase 0, Task 2 — thêm Room + DataStore (database rỗng + UserSettings) và package structure.

## Context for next Claude session
Read CLAUDE.md, docs/PRD.md, docs/PHASES.md and this file before changing code.
