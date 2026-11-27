# HANDOFF — HANZI JOURNEY

Date: 2026-11-26
Project version: 0.0.1-phase0

## Current phase and task
- Phase 2, Task 5 xong (chưa xác nhận build): bộ nạp nội dung Kotlin + test đơn vị.

## Completed
- Phase 0, Phase 1: build xanh, chạy OK. Phase 2 Task 1-4: khóa đề cương (PROVISIONAL), bảng 300 từ/246 chữ/70 ngữ pháp, cấu trúc 16 chương/47 bài (bản nháp), validator Python.
- Task 5: data/content/{ContentModels, ContentPackParser, ContentValidator, ContentImporter, AssetContentSource}.kt, data/content/room/{ContentEntities, ContentDao, ContentDatabase, RoomContentStore}.kt, AppContainer nối dây, app/src/test/.../ContentPackTest.kt (17 test), docs/CONTENT_PACK_FORMAT.md. Plugin kotlinx.serialization 2.0.20 + thư viện 1.7.3; junit 4.13.2.

## Build and tests actually run
- Kiểm tra được ở môi trường tạo file: fixture JSON của test hợp lệ, dấu ngoặc cân bằng. KHÔNG có trình biên dịch Kotlin/Android SDK nên code Kotlin và test CHƯA được biên dịch hay chạy.
- Actions sẽ chạy `gradle :app:testDebugUnitTest` rồi build; kết quả chờ người dùng.

## Known issues
- Rủi ro biên dịch (code Kotlin chưa compile): plugin serialization, Room entity/DAO mới, test. Nếu Actions đỏ, tải log gửi lại; test report được upload thành artifact "unit-test-report" khi lỗi test.
- Chưa có chỗ nào trong app gọi ContentImporter; chưa có gói nội dung; Room chưa được test trên thiết bị.
- Phần dữ liệu: thứ tự dạy do Claude đề xuất, chưa rà soát; 店/饺 không có trong 246 chữ; cảnh báo nguồn (小朋友, 存现句1); repo Public, giấy phép danh sách từ chưa rõ; khóa đề cương PROVISIONAL.

## Exact next task
- Xác nhận Actions xanh (cả bước unit test). Sau đó chủ dự án quyết định giấy phép/Private. Rồi Phase 2, Task 6: công cụ Python chuyển dữ liệu làm việc + nội dung tự biên soạn thành gói JSON đúng định dạng (bắt đầu với 1 bài mẫu đầy đủ do Claude tự viết: nghĩa tiếng Việt, ví dụ, bài tập), kiểm tra bằng validator, chưa đóng gói vào app.

## Decisions that must remain unchanged
- Android Native / Kotlin / Compose; Vietnamese-first; gamified UI; HSK 3.0 track; không AI/backend/API trả phí/login/ads/Premium; offline-first.

## Exact next task
- Xác nhận build Actions xanh và cài APK lên máy. Sau đó: Phase 0, Task 2 — thêm Room + DataStore (database rỗng + UserSettings) và package structure.

## Context for next Claude session
Read CLAUDE.md, docs/PRD.md, docs/PHASES.md and this file before changing code.
