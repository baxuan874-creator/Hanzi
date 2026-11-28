# CHANGELOG

## 0.0.1-phase0 (chưa build)
- Phase 0 / Task 1: khung dự án Android (Kotlin, Compose, Material 3), theme skeleton, màn hình shell, icon gốc, GitHub Actions build debug APK.

## 0.0.2-phase0 (chưa build)
- Phase 0 / Task 2: thêm Room (HanziDatabase, bảng curriculum_version, schema export), DataStore (SettingsRepository: âm thanh, giảm chuyển động, chế độ tối), AppContainer (DI thủ công), lớp HanziApp, KSP.

## 0.0.3-phase1 (chưa build)
- Phase 1 / Task 1: design tokens (màu sáng/tối, spacing, shape, typography), component EmptyState, Navigation Compose với 5 tab (Trang chủ, Học, Từ vựng, Ôn tập, Hồ sơ), 5 màn hình trạng thái trống. Không dùng dữ liệu giả.

## 0.0.4-phase1 (chưa build)
- Phase 1 / Task 2: component dùng chung (HanziButton, StatusBadge, HanziProgressBar, LessonCard, LearnMap); màn Hồ sơ có lựa chọn giao diện Theo hệ thống/Sáng/Tối lưu bằng DataStore và áp dụng cho toàn app (ProfileViewModel).

## (docs) Phase 2 / Task 1
- Thêm docs/SYLLABUS_LOCK.md (khóa tạm đề cương HSK 3.0 bản 2025-11) và tools/fetch_syllabus.sh (tải PDF + sinh checksum). Không đổi mã app.

## (content) Phase 2 / Task 2
- content_work/hsk3_l1_vocab_working.csv (300 từ Level 1), tools/validate_level1.py (chạy trong CI), docs/EXTRACTION_REPORT_L1.md; chép đầy đủ tài liệu dự án vào docs/ (các bản zip trước thiếu); cập nhật THIRD_PARTY_NOTICES và SYLLABUS_LOCK. Không đổi mã app.

## (content) Phase 2 / Task 3 (một phần)
- tools/extract_l1_from_pdf.py (trích 246 chữ nhận mặt, dump trang ngữ pháp), tools/selftest_extract.py (chạy trong CI), validator kiểm tra thêm file chữ nếu có.

## (content) Phase 2 / Task 3b
- content_work/hsk3_l1_grammar_working.csv (70 điểm ngữ pháp Level 1), validator kiểm tra file ngữ pháp và đối chiếu từ với 300 từ, cập nhật báo cáo/notices/lock.

## (content) Phase 2 / Task 3c
- content_work/hsk3_l1_chars_working.csv (246 chữ nhận mặt Level 1), cập nhật script trích và báo cáo. Giải thích chênh 248/246: 店, 饺.

## (content) Phase 2 / Task 4
- content_work/hsk3_l1_structure_working.json (16 chương, 47 bài, 300 từ + 70 ngữ pháp, mỗi mục đúng một lần), tools/build_l1_structure.py (kế hoạch gốc), docs/CURRICULUM_MAP_L1.md, validator kiểm tra cấu trúc (tiên quyết, giới hạn bài, phủ đủ), CI chạy cả build_l1_structure.py. Không đổi mã app.

## 0.0.5-phase2 (chưa build)
- Phase 2 / Task 5: bộ nạp nội dung bằng Kotlin (kotlinx.serialization): ContentModels, ContentPackParser, ContentValidator, ContentImporter, AssetContentSource; content.db (Room) tách khỏi hanzi.db; test đơn vị với dữ liệu giả (ContentPackTest); workflow chạy test đơn vị trước khi build; bước đăng Release không còn làm đỏ cả build. Không đóng gói dữ liệu HSK.

## (content) Phase 2 / Task 6
- Bài mẫu 004 "Xin chào" tự soạn (content_authoring/lesson_004.json): 12 từ có nghĩa tiếng Việt và ví dụ, 1 điểm ngữ pháp, 11 bài tập; tools/build_pack.py, tools/validate_pack.py (kèm quy tắc chỉ dùng chữ đã học); gói content_work/packs/hsk3_l1_sample_lesson004.pack.json; Kotlin: thêm review_status draft_unreviewed, kiểm tra sentence_ordering, SamplePackTest (kiểm tra gói thật bằng validator của app); CI chạy build/validate gói. Không đổi giao diện app.

## 0.0.6-phase3 (chưa build)
- Phase 3 / Task 1: trình học bài v1. Tab Học liệt kê bài từ content.db (bản debug tự nạp gói mẫu); màn học bài: mục tiêu, thẻ từ, ngữ pháp, bài tập (trắc nghiệm, điền chỗ trống, sắp xếp câu, nhập chữ), phản hồi có giải thích, làm lại câu sai một lần, màn kết quả. Logic LessonSession/ExerciseChecker có test đơn vị (kể cả chạy hết bài mẫu). Chưa lưu tiến độ.

## 0.0.7-phase3 (chưa build)
- Phase 3 / Task 2: trộn thứ tự đáp án (đáp án đúng không còn luôn ở ô đầu), giao diện bài học to và đỡ trống hơn (thẻ đáp án lớn, nút cao, nội dung căn giữa, thẻ từ chữ Hán lớn), dấu ✓/✗ sau khi trả lời, thanh tiến độ mới, màu container cho theme, nền trang trí ScatteredIcons (emoji rải mờ), ExerciseOptionsTest. Chưa làm ghép đôi (matching): chuyển sang Task 3.

## 0.0.8-phase3 (chưa build)
- Phase 3 / Task 3: dạng bài ghép đôi (matching): Matching.kt, ExerciseChecker/ExerciseOptions/ContentValidator (Kotlin) và validate_pack.py (Python) hỗ trợ, màn MatchingInput (hai cột, đánh số cặp), bài mẫu 004 thêm ex_l1_0012 (12 bài tập), cập nhật/thêm test (MatchingTest, matching trong ExerciseChecker/ContentPack/ExerciseOptions). Giảm biểu tượng nền còn khoảng 6 mỗi màn, nhỏ và mờ hơn.

## 0.0.9-phase4 (chưa build)
- Phase 4 / Task 1: lưu tiến độ (Room hanzi.db v2 + MIGRATION_1_2: lesson_progress, xp_event, practice_day), ProgressStore/RoomProgressStore/ProgressRepository, XP bài đầu tiên (+20) chống cộng trùng bằng khóa sự kiện, LevelRules (bảng ngưỡng cấu hình), StreakCalculator, lưu khi hoàn thành bài (một transaction), hiển thị cấp/XP/streak ở Trang chủ + Hồ sơ, nhãn "Hoàn thành" trên bản đồ, XP ở màn kết quả. Test mới: StreakCalculatorTest, LevelRulesTest, ProgressRepositoryTest. Docs: docs/PROGRESS.md.

## 0.0.10-phase4 (chưa build)
- Phase 4 / Task 2: nhiệm vụ hằng ngày (hoàn thành 1 bài +10 XP; học 10 từ mới +15 XP, lập theo nội dung sẵn có, mỗi ngày lập một lần, thưởng chống trả trùng), thành tích (Bài học đầu tiên, 25 từ, Hoàn thành một chương, 7 ngày luyện tập), bảng từ đã học; Room v3 + MIGRATION_2_3 (daily_mission, user_achievement, learned_word); ContentRepository.catalog(); ContentLoader có Mutex; thẻ Nhiệm vụ ở Trang chủ, thẻ Thành tích ở Hồ sơ, thông báo ở màn kết quả. Test: ProgressRepositoryTest (viết lại, 16 ca), MissionsAndAchievementsTest.

