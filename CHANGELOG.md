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
