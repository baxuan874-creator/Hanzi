# TIẾN ĐỘ & XP (Phase 4, Task 1)

## Lưu trữ (hanzi.db, Room v2)
| Bảng | Nội dung |
|---|---|
| lesson_progress | mỗi bài một dòng: lần đầu hoàn thành, lần gần nhất, điểm tốt nhất (đúng ngay lần đầu), số câu, số lần học |
| xp_event | sổ cái XP; khóa chính `eventId` là khóa chống cộng trùng |
| practice_day | mỗi ngày (giờ địa phương, yyyy-MM-dd) có học một dòng; dùng để tính streak |

Migration 1→2 chỉ thêm 3 bảng, không đụng dữ liệu cũ. Nội dung bài học vẫn ở content.db, tách khỏi dữ liệu người dùng.

## Luật
- Hoàn thành bài lần đầu: +20 XP, event id `lesson_first:<lessonId>`. Học lại: 0 XP (id đã tồn tại nên `INSERT OR IGNORE` bỏ qua). Gọi lưu hai lần cũng không cộng đôi.
- Lưu bài + đánh dấu ngày học + ghi XP nằm trong một transaction: hoặc có cả ba, hoặc không có gì.
- Chỉ bài hoàn thành mới được lưu (thoát giữa chừng không ghi gì).
- Cấp độ: bảng ngưỡng ở `LevelRules.Default` (0, 40, 100, 180, 280, 400, 540, 700, 880, 1080 XP cho cấp 1–10), không nằm trong UI. Có thể cân bằng lại sau playtest.
- Streak: số ngày liên tiếp có học theo ngày địa phương. Còn sống đến hết ngày hôm sau ngày học cuối. Đứt chuỗi chỉ đặt lại bộ đếm, không xóa tiến độ hay XP. Lưu cả chuỗi dài nhất.

## Hiển thị
- Trang chủ và Hồ sơ: cấp, XP, thanh tiến độ tới cấp sau, streak, chuỗi dài nhất, số bài đã xong.
- Bản đồ Học: bài đã hoàn thành có nhãn "Hoàn thành".
- Màn kết quả: "+20 XP", hoặc "không cộng thêm XP" khi học lại, hoặc báo lỗi nếu không lưu được.

## Chưa làm (Task 2 của Phase 4)
Nhiệm vụ hằng ngày, thành tích (First Lesson, 25 Words, 100 Reviews, Chapter Complete, 7-Day Practice), XP ôn thẻ, XP checkpoint, khóa bài theo prerequisite.
