# HANDOFF — HANZI JOURNEY

Date: 2026-11-26
Project version: 0.0.1-phase0

## Current phase and task
- Phase 2, Task 6 xong (chưa xác nhận build): bài mẫu 004 + công cụ tạo/kiểm tra gói.

## Completed
- Phase 0, Phase 1: build xanh, chạy OK. Phase 2 Task 1-4: khóa đề cương (PROVISIONAL), bảng 300 từ/246 chữ/70 ngữ pháp, cấu trúc 16 chương/47 bài (nháp). Task 5: bộ nạp nội dung Kotlin + 18 test (chờ xác nhận build/test trên Actions).
- Task 6: content_authoring/lesson_004.json (nội dung tự soạn, draft_unreviewed), tools/build_pack.py, tools/validate_pack.py, content_work/packs/hsk3_l1_sample_lesson004.pack.json (12 từ, 1 ngữ pháp, 11 bài tập), Kotlin SamplePackTest + 2 test mới (ordering, draft status), cập nhật docs/CONTENT_PACK_FORMAT.md.

## Build and tests actually run
- Python: build_pack + validate_pack trên gói mẫu: 0 lỗi, 0 cảnh báo; kiểm thử âm (chữ chưa học, đáp án sai, ordering sai, pinyin rỗng, tiên quyết sai) bị bắt đủ 9 lỗi. validate_level1, selftest_extract, build_l1_structure: đạt.
- Kotlin (ContentPackTest, SamplePackTest) và Gradle: CHƯA biên dịch/chạy ở môi trường tạo file; chờ Actions.

## Known issues
- Nội dung bài 004 do Claude tự soạn, chưa có người biết tiếng Trung rà soát (một số câu ví dụ rất ngắn vì chỉ được dùng 12 từ của bài: 我请你, 您请, 请问！).
- Rủi ro biên dịch Kotlin (Task 5 và 6) như đã ghi; SamplePackTest giả định thư mục chạy test là app/ (đường dẫn ../content_work/...).
- Chưa có gói nào trong app, chưa có UI; Room chưa test trên thiết bị. Dữ liệu: 店/饺 không có trong 246 chữ; cảnh báo nguồn (小朋友, 存现句1); repo có thể còn Public, giấy phép danh sách từ chưa rõ; khóa đề cương PROVISIONAL.

## Exact next task
- Xác nhận Actions xanh (đặc biệt bước Run unit tests). Quyết định giấy phép/Private. Rồi Phase 3, Task 1: màn hình học bài (lesson player) đọc bài 004 từ gói mẫu — để phát triển UI, gói mẫu được đặt vào thư mục test/debug-only hoặc nạp qua cơ chế chỉ có ở bản debug, KHÔNG đóng gói trong bản release cho tới khi giấy phép rõ.

## Decisions that must remain unchanged
- Android Native / Kotlin / Compose; Vietnamese-first; gamified UI; HSK 3.0 track; không AI/backend/API trả phí/login/ads/Premium; offline-first.

## Exact next task
- Xác nhận build Actions xanh và cài APK lên máy. Sau đó: Phase 0, Task 2 — thêm Room + DataStore (database rỗng + UserSettings) và package structure.

## Context for next Claude session
Read CLAUDE.md, docs/PRD.md, docs/PHASES.md and this file before changing code.
