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

## Cập nhật Task 6
- `review_status` có thêm `draft_unreviewed` = nội dung do Claude tự biên soạn, đang chờ người biết tiếng Trung rà soát. Bản phát hành (`requireApproved = true`) chỉ nhận `approved`.
- Quy ước `sentence_ordering`: `options` là các mảnh câu, `answer` là các mảnh đó nối bằng dấu cách theo thứ tự đúng (ví dụ `"好 再见"`). Cả Kotlin và Python kiểm tra danh sách mảnh trong đáp án trùng với `options`.
- Quy tắc soạn bài (chỉ Python kiểm tra): câu ví dụ, đề bài, lựa chọn và đáp án chỉ được dùng chữ Hán của các từ đã học ở bài này hoặc các bài trước.
- Công cụ: `tools/build_pack.py lesson_004` ghép dữ liệu đề cương (ID, chữ, pinyin, nguồn) + cấu trúc khóa học + nội dung tự soạn trong `content_authoring/lesson_004.json` thành gói; `tools/validate_pack.py` kiểm tra gói (giống `ContentValidator.kt` cộng quy tắc chữ Hán ở trên).
- Gói mẫu: `content_work/packs/hsk3_l1_sample_lesson004.pack.json` (bài 004 "Xin chào": 12 từ, 1 điểm ngữ pháp, 11 bài tập). Là bản nháp, chưa rà soát, **chưa đóng gói trong app**. Gói mẫu chỉ có bài 004 nên không chứa bài tiên quyết (003 Khởi động).

## Cập nhật Phase 3 / Task 3: dạng bài `matching` (ghép đôi)
- `options`: 2–5 cặp, mỗi cặp viết `trái|phải` (ví dụ `"你好|xin chào"`). Hai phía mỗi bên không được trùng nhau.
- `answer`: chính các cặp đó nối bằng `;` (ví dụ `"你好|xin chào;谢谢|cảm ơn"`). Thứ tự không quan trọng khi chấm.
- Kotlin (`ContentValidator`) và Python (`validate_pack.py`) cùng kiểm tra: số cặp, định dạng `trái|phải`, phía trái/phải không trùng, đáp án khớp đúng với `options`.
- App tự trộn cả hai cột mỗi lần hiện câu, và đảm bảo hai cột không xếp sẵn thẳng hàng.
- Bài mẫu 004 có thêm `ex_l1_0012` (ghép 4 từ), tổng 12 bài tập.

