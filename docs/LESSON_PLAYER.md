# LESSON PLAYER v1 (Phase 3, Task 1)

## Luồng một bài học
Giới thiệu mục tiêu → từng từ mới (thẻ: chữ Hán, pinyin, nghĩa, ví dụ) → ngữ pháp (nếu có) → bài tập → kết quả.
- Bài tập hỗ trợ: trắc nghiệm, điền chỗ trống (chọn đáp án), sắp xếp câu (chạm mảnh để ghép), và ô nhập chữ nếu bài tập không có lựa chọn.
- Sau mỗi câu: báo "✓ Chính xác!" hoặc "✗ Chưa đúng" bằng chữ (không chỉ bằng màu), kèm đáp án đúng và giải thích tiếng Việt.
- Câu sai được hỏi lại **một lần** ở cuối bài; chỉ lần đầu được tính điểm. Không có trái tim hay giới hạn lượt.
- Màn kết quả: số câu đúng ngay lần đầu, số từ đã gặp, số câu làm lại; nút "Học lại" và "Về bản đồ".

## Nội dung ở đâu ra
- Bản debug (APK tải từ Actions) có kèm gói mẫu `content_work/packs/hsk3_l1_sample_lesson004.pack.json`; lần đầu mở tab **Học** app nạp gói này vào `content.db`.
- Bản release hiện KHÔNG chứa gói nào (tab Học sẽ trống) và nếu sau này có gói, chỉ nhận nội dung `approved`.
- Mọi chỗ hiển thị nội dung bản nháp đều có dòng cảnh báo: bản nháp, chưa được người biết tiếng Trung rà soát.

## Chưa có (cố ý)
- Chưa lưu tiến độ, XP, streak, lịch ôn (Phase 4–5): mọi bài đều "sẵn sàng", app không nhớ bạn đã học bài nào.
- Chưa có âm thanh, chưa có luyện viết nét (Phase 6).
- Chưa có ghép đôi (matching) và nghe chọn (listening_choice).

## Mã nguồn
- Logic (kiểm thử được trên JVM): `domain/lesson/LessonSession.kt`, `ExerciseChecker.kt`.
- Dữ liệu: `data/content/ContentRepository.kt`, `ContentLoader.kt`.
- Giao diện: `ui/lesson/*`, `ui/learn/*`, `ui/screens/LearnScreen.kt`; điều hướng `lesson/{lessonId}` ẩn thanh tab.
