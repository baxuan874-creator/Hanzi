# LESSON PLAYER v1.1 (Phase 3, Task 1–2)

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

## Thay đổi ở Task 2
- Thứ tự đáp án luôn được **trộn** mỗi lần hiện câu (gói nội dung ghi đáp án đúng ở vị trí đầu); mảnh ghép câu không bao giờ hiện sẵn đúng thứ tự (`domain/lesson/ExerciseOptions.kt`, có test).
- Giao diện to hơn: đáp án là thẻ lớn (cao tối thiểu 72dp, chữ 22sp), nút chính cao 56dp, câu hỏi cỡ 28sp, thẻ từ chữ Hán 96sp, nội dung căn giữa theo chiều dọc để màn hình không trống nửa dưới.
- Sau khi trả lời: thẻ đúng có dấu ✓, thẻ chọn sai có dấu ✗ (ngoài màu còn có ký hiệu); khung giải thích nằm cố định phía trên nút Tiếp tục.
- Thanh tiến độ tự vẽ (bỏ chấm tròn thừa cuối thanh); thêm màu container vào theme sáng/tối.
- Nền trang trí: `ui/components/ScatteredIcons.kt` rải mờ các biểu tượng emoji (đèn lồng, rồng, trà, sủi cảo, hoa đào…) ở nền mọi màn học; vị trí ngẫu nhiên nhưng cố định theo từng bước. Chỉ để trang trí: ẩn với trình đọc màn hình, không chuyển động, dùng font emoji của hệ thống (không cần file ảnh/giấy phép). Không dùng ảnh linh vật.
