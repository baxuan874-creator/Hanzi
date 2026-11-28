# HANDOFF — HANZI JOURNEY

Date: 2026-11-26
Project version: 0.0.1-phase0

## Current phase and task
- Phase 3, Task 1 xong (chưa xác nhận build): trình học bài v1 chạy bài mẫu 004 trong bản debug.

## Completed
- Phase 0, Phase 1: build xanh, chạy OK. Phase 2 (Task 1-6): khóa đề cương (PROVISIONAL), dữ liệu 300/246/70, cấu trúc 47 bài, bộ nạp nội dung Kotlin, bài mẫu 004 + gói JSON. Task 5-6 chờ xác nhận build/test trên Actions.
- Phase 3 Task 1: domain/lesson (LessonSession, ExerciseChecker, LessonContent), data/content (ContentRepository, ContentLoader), ui/lesson (LessonViewModel, LessonScreen, LessonSteps, PracticeStep), ui/learn/LearnViewModel, LearnScreen dùng LearnMap + LessonCard thật, route lesson/{lessonId} ẩn thanh tab, gradle: buildConfig=true và assets debug trỏ tới content_work/packs, docs/LESSON_PLAYER.md, LessonSessionTest (ExerciseChecker, session, chạy hết bài mẫu).

## Build and tests actually run
- Python (validate_level1, build_pack, validate_pack, selftest_extract, build_l1_structure): đạt ở lần chạy trước. Mô phỏng: 11/11 bài tập của bài mẫu có đáp án hợp lệ.
- Kotlin/Gradle (Task 5, 6, Phase 3 Task 1): CHƯA biên dịch hay chạy ở môi trường tạo file (không có Kotlin/Android SDK); kiểm tra duy nhất là cân bằng ngoặc. Actions sẽ chạy unit test + build.

## Known issues
- Rủi ro biên dịch lớn hơn do lượng code Kotlin/Compose mới chưa từng compile (FlowRow + OptIn, sourceSets debug assets, Room, serialization, test đọc file ../content_work). Nếu Actions đỏ: gửi log.
- Trình học: chưa lưu tiến độ; chưa âm thanh; thiếu matching/listening; chưa thử trên thiết bị (giao diện chưa ai xem).
- Bài mẫu 004 là bản nháp chưa rà soát; dữ liệu: 店/饺 ngoài 246 chữ, cảnh báo nguồn (小朋友, 存现句1); repo có thể còn Public, giấy phép danh sách từ chưa rõ; khóa đề cương PROVISIONAL. Bản release không chứa nội dung (tab Học trống) — chủ ý.

## Exact next task
- Xác nhận Actions xanh, cài APK debug và thử bài 004 trên máy; gửi ảnh/ghi chú lỗi giao diện. Sau đó Phase 3, Task 2: sửa lỗi/chỉnh giao diện theo phản hồi, thêm dạng bài ghép đôi (matching), rồi Phase 4 (lưu tiến độ, XP chống cộng trùng, streak).

## Decisions that must remain unchanged
- Android Native / Kotlin / Compose; Vietnamese-first; gamified UI; HSK 3.0 track; không AI/backend/API trả phí/login/ads/Premium; offline-first.

## Exact next task
- Xác nhận build Actions xanh và cài APK lên máy. Sau đó: Phase 0, Task 2 — thêm Room + DataStore (database rỗng + UserSettings) và package structure.

## Context for next Claude session
Read CLAUDE.md, docs/PRD.md, docs/PHASES.md and this file before changing code.
