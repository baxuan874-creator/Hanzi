# HANDOFF — HANZI JOURNEY

Date: 2026-11-26
Project version: 0.0.1-phase0

## Current phase and task
- Phase 2, Task 4 xong: cấu trúc chương/bài Level 1 (bản nháp).

## Completed
- Phase 0, Phase 1: build xanh, chạy OK. Phase 2: khóa đề cương (PROVISIONAL), bảng 300 từ / 246 chữ / 70 ngữ pháp, script trích PDF, validator.
- Task 4: 16 chương (Khởi động + 15 chương theo 15 nhiệm vụ của đề cương), 47 bài; mỗi từ và mỗi điểm ngữ pháp đúng một lần; tối đa 12 từ/bài, 8 ngữ pháp/bài. Tệp: content_work/hsk3_l1_structure_working.json, tools/build_l1_structure.py (kế hoạch gốc, sửa ở đây rồi chạy lại), docs/CURRICULUM_MAP_L1.md.

## Build and tests actually run
- build_l1_structure.py và validate_level1.py (300 từ, 246 chữ, 70 ngữ pháp, cấu trúc): 0 lỗi, 17 cảnh báo; kiểm thử âm (từ trùng, tiên quyết sai thứ tự) bị bắt đúng. selftest_extract.py đạt.
- Build Gradle/Actions cho các task Phase 2 sau Task 2: chờ người dùng xác nhận.

## Known issues
- Thứ tự dạy và cách nhóm do biên tập viên (Claude) đề xuất, chưa có người biết tiếng Trung/sư phạm rà soát.
- Tiên quyết tuyến tính; bài tập và checkpoint chưa có nội dung; nghĩa tiếng Việt/ví dụ chưa soạn.
- 店 và 饺 không có trong 246 chữ nhận mặt; cảnh báo nguồn khác (小朋友, 存现句1); repo Public, giấy phép danh sách từ chưa rõ; khóa đề cương PROVISIONAL.

## Exact next task
- Phase 2, Task 5: bộ nạp nội dung bằng Kotlin (parser JSON theo CONTENT_SCHEMA.md, bảng Room cho nội dung, test đơn vị dùng dữ liệu giả chỉ để test, không đóng gói dữ liệu HSK vào app). Sau đó quyết định cùng chủ dự án về giấy phép trước khi soạn bài đầu tiên (Phase 3).

## Decisions that must remain unchanged
- Android Native / Kotlin / Compose; Vietnamese-first; gamified UI; HSK 3.0 track; không AI/backend/API trả phí/login/ads/Premium; offline-first.

## Exact next task
- Xác nhận build Actions xanh và cài APK lên máy. Sau đó: Phase 0, Task 2 — thêm Room + DataStore (database rỗng + UserSettings) và package structure.

## Context for next Claude session
Read CLAUDE.md, docs/PRD.md, docs/PHASES.md and this file before changing code.
