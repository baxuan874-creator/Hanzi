# CONTENT PACK FORMAT (Phase 2, Task 5)

Định dạng file JSON mà app đọc (`ContentPack` trong `data/content/ContentModels.kt`). Tên trường theo `CONTENT_SCHEMA.md`.
**Hiện chưa có gói nội dung nào được đóng gói trong app.** Dữ liệu HSK chỉ vào app sau khi chủ dự án chốt giấy phép và nội dung được rà soát.

## Cấu trúc
```
{ "manifest":      { curriculum_id, version, source, retrieved_at, checksum },
  "chapters":       [ { id, title_vi, lesson_ids[] } ],
  "lessons":        [ { id, curriculum_id, level_id, chapter_id, title_vi, objectives[], vocabulary_ids[],
                        grammar_ids[], exercise_ids[], estimated_minutes, prerequisite_ids[], content_version } ],
  "vocabulary":     [ { id, curriculum_id, level_id, hanzi, pinyin, meaning_vi, word_type?, topic_id?, examples[],
                        audio_asset_id?, source_ref, license_ref, review_status } ],
  "grammar_points": [ { id, curriculum_id, level_id, pattern, explanation_vi, usage_note_vi?, common_error_vi?,
                        examples[], source_ref, review_status } ],
  "exercises":      [ { id, lesson_id, type, prompt, options?, answer, explanation_vi, difficulty(1-5),
                        skill_tags[], source_ref } ] }
```
Ví dụ (`examples`): `{ hanzi, pinyin, meaning_vi }`. `review_status`: `extracted_unreviewed`, `reviewed`, `approved`.

## Quy trình nạp (`ContentImporter`)
1. Tính SHA-256 của file; nếu trùng gói đã nạp → `AlreadyUpToDate`.
2. Đọc JSON; sai cú pháp hoặc thiếu trường bắt buộc → `ParseError` (không động vào dữ liệu đang có).
3. Kiểm tra (`ContentValidator`): ID trùng, tham chiếu sai, từ/ngữ pháp giới thiệu hai lần, trộn `curriculum_id`, đáp án không nằm trong lựa chọn, thiếu pinyin/nghĩa/giải thích, loại bài tập lạ, tiên quyết sai. Có lỗi → `Rejected` (không ghi gì).
4. Hợp lệ → ghi toàn bộ trong một giao dịch vào `content.db` (xóa cũ, ghi mới) → `Imported`.
- Bản phát hành sẽ nạp với `requireApproved = true` (mọi mục phải `approved`).
- Cảnh báo (không chặn): pinyin không có dấu thanh, số phút ngoài 5–10, bài không có trong danh sách của chương, ngữ pháp không có ví dụ.

## Cơ sở dữ liệu
- `content.db` (mới): chỉ chứa dữ liệu có thể nạp lại từ gói nội dung nên cho phép dựng lại khi đổi cấu trúc (destructive migration). Tách khỏi dữ liệu người dùng theo PRD mục 10.
- `hanzi.db` (đã có): dành cho tiến độ người dùng; khi thêm bảng sẽ có migration thật.
- Danh sách trong bảng lưu dưới dạng chuỗi JSON.

## Chưa làm
- Chưa có chỗ nào trong app gọi bộ nạp (chưa có gói nội dung).
- Chưa kiểm thử Room trên thiết bị (cần test thiết bị); test đơn vị chỉ chạy phần phân tích, kiểm tra và logic nạp với kho giả.
- Chưa có công cụ chuyển các file CSV/JSON làm việc trong `content_work/` thành gói theo định dạng này (cần thêm nghĩa tiếng Việt, ví dụ, bài tập).
