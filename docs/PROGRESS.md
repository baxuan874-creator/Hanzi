# TIẾN ĐỘ, XP, NHIỆM VỤ, THÀNH TÍCH (Phase 4, Task 1–2)

## Lưu trữ (hanzi.db, Room v2)
| Bảng | Nội dung |
|---|---|
| lesson_progress | mỗi bài một dòng: lần đầu hoàn thành, lần gần nhất, điểm tốt nhất (đúng ngay lần đầu), số câu, số lần học |
| xp_event | sổ cái XP; khóa chính `eventId` là khóa chống cộng trùng |
| practice_day | mỗi ngày (giờ địa phương, yyyy-MM-dd) có học một dòng; dùng để tính streak |
| daily_mission | nhiệm vụ từng ngày: loại, mục tiêu, tiến độ, thưởng XP, thời điểm hoàn thành (mục tiêu và thưởng lưu theo dòng nên đổi luật sau này không làm đổi quá khứ) |
| user_achievement | thành tích đã đạt và thời điểm đạt |
| learned_word | từ đã gặp trong bài hoàn thành (nền cho thẻ ôn ở Phase 5) |

Migration 1→2 và 2→3 chỉ thêm bảng, không đụng dữ liệu cũ. Nội dung bài học vẫn ở content.db, tách khỏi dữ liệu người dùng.

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

## Nhiệm vụ hằng ngày (Task 2)
- Chỉ lập từ nội dung đang có trong app, và mỗi ngày chỉ lập một lần (không lập lại trong ngày):
  - "Hoàn thành 1 bài học" (+10 XP): cần có ít nhất 1 bài. Học lại cũng tính.
  - "Học 10 từ mới" (+15 XP): chỉ lập khi còn ít nhất 10 từ bạn chưa học; chỉ từ mới gặp lần đầu mới tính, từ lặp lại không tính.
- Thưởng XP dùng khóa sự kiện `mission:<ngày>:<loại>`, mỗi nhiệm vụ chỉ trả một lần. Hoàn thành nhiệm vụ không bị giới hạn bởi streak và không phạt khi bỏ lỡ.
- Hiển thị ở Trang chủ ("Nhiệm vụ hôm nay"), màn kết quả nêu nhiệm vụ vừa xong.

## Thành tích (Task 2)
| Thành tích | Điều kiện |
|---|---|
| Bài học đầu tiên | hoàn thành 1 bài |
| 25 từ | gặp 25 từ khác nhau |
| Hoàn thành một chương | xong mọi bài của một chương |
| 7 ngày luyện tập | chuỗi dài nhất đạt 7 ngày liên tiếp |
- "100 lượt ôn" chưa có vì chưa có tính năng ôn; sẽ thêm ở Phase 5.
- Thành tích không thưởng XP. Hiển thị ở Hồ sơ (kèm tiến độ cho "25 từ" và "7 ngày"), màn kết quả báo thành tích mới.
- Bài đã học ở bản 0.0.9 (trước khi có bảng từ đã học) chưa được tính từ; học lại bài đó sẽ bù.

## Chưa làm
XP ôn thẻ, XP checkpoint, khóa bài theo bài tiên quyết, thành tích "100 lượt ôn".
