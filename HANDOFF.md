# HANDOFF — HANZI JOURNEY

Date: 2026-11-28
Project version: 0.0.8-phase3

## Current phase and task
- Phase 3, Task 3 xong về mã (chưa xác nhận build): dạng bài ghép đôi + giảm biểu tượng nền. Task 2 (trộn đáp án, giao diện to, nền biểu tượng): Actions xanh.

## Completed
- Phase 0–2: xong, Actions xanh. Phase 3 Task 1 (trình học v1): Actions xanh, người dùng đã thử bài 004 trên máy (Redmi 15C).
- Phase 3 Task 2: trộn đáp án (ExerciseOptions + ExerciseOptionsTest), giao diện lớn hơn/đỡ trống (PracticeStep, LessonScreen, LessonSteps, LessonLayout.CenteredScroll), dấu ✓/✗, thanh tiến độ tự vẽ, màu container trong Theme, nền trang trí ScatteredIcons (emoji rải mờ, theo yêu cầu người dùng; bỏ ý tưởng ảnh linh vật).

## Files created / modified (Task 3)
- Mới: domain/lesson/Matching.kt. Sửa: ExerciseChecker.kt, ExerciseOptions.kt, ContentValidator.kt, ui/lesson/PracticeStep.kt (MatchingInput), ScatteredIcons.kt (thưa/mờ), strings.xml, tools/validate_pack.py, content_authoring/lesson_004.json + gói mẫu (12 bài tập), 4 file test, docs (CONTENT_PACK_FORMAT, LESSON_PLAYER), version 0.0.8.

## Files created / modified (Task 2)
- Mới: domain/lesson/ExerciseOptions.kt, ui/components/ScatteredIcons.kt, ui/lesson/LessonLayout.kt, test ExerciseOptionsTest.kt.
- Sửa: ui/lesson/PracticeStep.kt, LessonScreen.kt, LessonSteps.kt, ui/components/HanziButton.kt, ui/theme/Theme.kt, app/build.gradle.kts (version 0.0.7), docs/LESSON_PLAYER.md, CHANGELOG.md.

## Build and tests actually run
- Python validator/pack: đạt ở các lần chạy trước.
- Python Task 3: build_pack.py và validate_pack.py chạy trên gói mẫu mới: 12 bài tập, 0 lỗi, 0 cảnh báo.
- Kotlin/Gradle Task 3: CHƯA biên dịch/chạy ở môi trường tạo file (không có Kotlin/Android SDK); chỉ kiểm tra cân bằng ngoặc. Actions sẽ chạy unit test + build.

## Known issues
- Emoji nền phụ thuộc font emoji của máy (máy rất cũ có thể hiện ô vuông); chưa xem thực tế trên máy.
- Chưa có listening; chưa lưu tiến độ; chưa âm thanh.
- Bài mẫu 004 là bản nháp chưa rà soát; bài sắp xếp câu chỉ 2 mảnh nên quá dễ (cần nội dung dài hơn khi soạn bài thật); 店/饺 ngoài 246 chữ; khóa đề cương PROVISIONAL; repo có thể còn Public; giấy phép danh sách từ chưa rõ.

## Decisions that must remain unchanged
- Android Native / Kotlin / Compose; Vietnamese-first; gamified UI; HSK 3.0 track; không AI/backend/API trả phí/login/ads/Premium; offline-first.

## Exact next task
- Xác nhận Actions xanh cho 0.0.8, cài APK, thử ghép đôi và xem lại mật độ biểu tượng nền. Sau đó Phase 4: lưu tiến độ (Room), XP chống cộng trùng, streak.

## Context for next Claude session
Read CLAUDE.md, docs/PRD.md, docs/PHASES.md and this file before changing code.
