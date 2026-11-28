# HANDOFF — HANZI JOURNEY

Date: 2026-11-28
Project version: 0.0.9-phase4

## Current phase and task
- Phase 4, Task 1 xong về mã (chưa xác nhận build): lưu tiến độ, XP chống cộng trùng, cấp độ, streak. Phase 3 (Task 1–3) đã xong, Actions xanh.

## Completed
- Phase 0–2: xong, Actions xanh. Phase 3 Task 1 (trình học v1): Actions xanh, người dùng đã thử bài 004 trên máy (Redmi 15C).
- Phase 3 Task 2: trộn đáp án (ExerciseOptions + ExerciseOptionsTest), giao diện lớn hơn/đỡ trống (PracticeStep, LessonScreen, LessonSteps, LessonLayout.CenteredScroll), dấu ✓/✗, thanh tiến độ tự vẽ, màu container trong Theme, nền trang trí ScatteredIcons (emoji rải mờ, theo yêu cầu người dùng; bỏ ý tưởng ảnh linh vật).

## Files created / modified (Phase 4 Task 1)
- Mới: domain/progress/{ProgressModels,XpRules,LevelRules,StreakCalculator}.kt, data/progress/{ProgressStore,ProgressRepository,RoomProgressStore}.kt, data/local/{ProgressEntities,ProgressDao}.kt, ui/progress/ProgressViewModel.kt, ui/components/ProgressSummaryCard.kt, 3 file test, docs/PROGRESS.md.
- Sửa: HanziDatabase.kt (v2 + migration), AppContainer.kt, LearnViewModel/LearnScreen, LessonViewModel/LessonScreen (lưu khi xong, hiện XP), HomeScreen, ProfileScreen, strings.xml, version 0.0.9.

## Files created / modified (Task 3)
- Mới: domain/lesson/Matching.kt. Sửa: ExerciseChecker.kt, ExerciseOptions.kt, ContentValidator.kt, ui/lesson/PracticeStep.kt (MatchingInput), ScatteredIcons.kt (thưa/mờ), strings.xml, tools/validate_pack.py, content_authoring/lesson_004.json + gói mẫu (12 bài tập), 4 file test, docs (CONTENT_PACK_FORMAT, LESSON_PLAYER), version 0.0.8.

## Files created / modified (Task 2)
- Mới: domain/lesson/ExerciseOptions.kt, ui/components/ScatteredIcons.kt, ui/lesson/LessonLayout.kt, test ExerciseOptionsTest.kt.
- Sửa: ui/lesson/PracticeStep.kt, LessonScreen.kt, LessonSteps.kt, ui/components/HanziButton.kt, ui/theme/Theme.kt, app/build.gradle.kts (version 0.0.7), docs/LESSON_PLAYER.md, CHANGELOG.md.

## Build and tests actually run
- Python validator/pack: đạt ở các lần chạy trước.
- Phase 4 Task 1: Python không liên quan; Kotlin/Gradle CHƯA biên dịch/chạy ở môi trường tạo file. Actions sẽ chạy test mới.
- Python Task 3: build_pack.py và validate_pack.py chạy trên gói mẫu mới: 12 bài tập, 0 lỗi, 0 cảnh báo.
- Kotlin/Gradle Task 3: CHƯA biên dịch/chạy ở môi trường tạo file (không có Kotlin/Android SDK); chỉ kiểm tra cân bằng ngoặc. Actions sẽ chạy unit test + build.

## Known issues
- Migration Room 1→2 chưa có test nâng cấp trên máy (cần instrumented test, dự kiến Phase 7); nếu app crash khi mở sau khi cài đè bản cũ, đó là chỗ đầu tiên cần xem (thử xóa dữ liệu app để xác nhận).
- Chưa có nhiệm vụ ngày, thành tích, XP ôn thẻ, khóa bài theo prerequisite.
- Emoji nền phụ thuộc font emoji của máy (máy rất cũ có thể hiện ô vuông); chưa xem thực tế trên máy.
- Chưa có listening; chưa lưu tiến độ; chưa âm thanh.
- Bài mẫu 004 là bản nháp chưa rà soát; bài sắp xếp câu chỉ 2 mảnh nên quá dễ (cần nội dung dài hơn khi soạn bài thật); 店/饺 ngoài 246 chữ; khóa đề cương PROVISIONAL; repo có thể còn Public; giấy phép danh sách từ chưa rõ.

## Decisions that must remain unchanged
- Android Native / Kotlin / Compose; Vietnamese-first; gamified UI; HSK 3.0 track; không AI/backend/API trả phí/login/ads/Premium; offline-first.

## Exact next task
- Xác nhận Actions xanh cho 0.0.9; cài đè APK cũ (kiểm tra không crash), hoàn thành một bài, đóng hẳn app, mở lại: XP/cấp/streak/nhãn Hoàn thành phải còn; học lại bài: không cộng XP. Sau đó Phase 4, Task 2: nhiệm vụ hằng ngày + thành tích.

## Context for next Claude session
Read CLAUDE.md, docs/PRD.md, docs/PHASES.md and this file before changing code.
