# HANDOFF — HANZI JOURNEY

Date: 2026-11-26
Project version: 0.0.1-phase0

## Current phase and task
- Phase 2, Task 3b: ngữ pháp Level 1 xong; còn 246 chữ nhận mặt.

## Completed
- Phase 0, Phase 1: build xanh, chạy OK. Phase 2 Task 1-2 (khóa đề cương PROVISIONAL, 300 từ, validator).
- Task 3: script trích PDF chạy được trong Codespace (đã đọc đúng trang ngữ pháp 383-385). Task 3b: hsk3_l1_grammar_working.csv (70 dòng), validator mở rộng.

## Build and tests actually run
- validate_level1.py trên dữ liệu thật (300 từ + 70 ngữ pháp): 0 lỗi; selftest_extract.py đạt.
- Build Actions cho Task 3b chưa xác nhận.

## Known issues
- 246 chữ nhận mặt chưa có (300 từ chứa 248 ký tự khác nhau; chênh 2 chữ chưa rõ).
- Cách đếm 70 điểm ngữ pháp là của dự án; cần người biết tiếng Trung xác nhận. Cảnh báo nguồn: 存现句1 (1) in 所处.
- Dữ liệu chưa được rà soát; repo nên chuyển Private; khóa đề cương còn PROVISIONAL (checksum đã tạo nếu người dùng chạy fetch_syllabus.sh).

## Exact next task
- Nhận l1_chars_raw.txt (hoặc ảnh kết quả script), hoàn tất bảng 246 chữ, đưa khóa đề cương sang LOCKED nếu đủ điều kiện. Sau đó Phase 2, Task 4: thiết kế cấu trúc chương/bài (chủ đề Level 1) từ đề cương, chưa soạn nghĩa tiếng Việt.

## Decisions that must remain unchanged
- Android Native / Kotlin / Compose; Vietnamese-first; gamified UI; HSK 3.0 track; không AI/backend/API trả phí/login/ads/Premium; offline-first.

## Exact next task
- Xác nhận build Actions xanh và cài APK lên máy. Sau đó: Phase 0, Task 2 — thêm Room + DataStore (database rỗng + UserSettings) và package structure.

## Context for next Claude session
Read CLAUDE.md, docs/PRD.md, docs/PHASES.md and this file before changing code.
