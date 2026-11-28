# ÔN TẬP (Phase 5, Task 1)

## Lịch ôn (spaced practice)
- Từ nào được gặp lần đầu trong một bài hoàn thành sẽ vào lịch ôn, đến hạn **10 phút** sau đó. Từ đã học trước đó được bù khi nâng cấp cơ sở dữ liệu (migration 3→4: đến hạn ngay).
- Nhớ: lên một bậc. Các khoảng cách: 10 phút → 1 ngày → 3 ngày → 7 ngày → 14 ngày (bậc cuối lặp lại 14 ngày).
- Chưa nhớ: về bậc đầu (10 phút), tính thêm một lần "quên".
- Đây chỉ là heuristic khởi điểm (PRD 5.3), không tuyên bố tối ưu khoa học; hiệu chỉnh bằng dữ liệu sử dụng sau này (`ReviewScheduler.INTERVALS`).
- Không bao giờ xóa từ khỏi lịch; học lại bài không đặt lại lịch của từ.

## Phiên ôn (flashcard, nhớ lại chủ động)
- Mỗi phiên tối đa 10 từ đến hạn, từ quá hạn lâu nhất trước.
- Thẻ hiện câu hỏi trước, người học tự nhớ, bấm "Hiện đáp án" rồi tự đánh giá "Nhớ rồi" / "Chưa nhớ" (chỉ bấm được sau khi hiện đáp án).
- Hướng hỏi đổi theo bậc: bậc chẵn hỏi chữ Hán → nghĩa, bậc lẻ hỏi nghĩa → chữ Hán + pinyin (xen kẽ hai chiều).
- Từ chưa nhớ quay lại một lần ở cuối phiên; lần thử lại đó không đổi lịch và không cộng XP.
- Mỗi câu trả lời được lưu ngay (thoát giữa phiên không mất kết quả đã làm); gồm: cập nhật lịch, đánh dấu ngày học (tính streak), XP, thành tích, trong một transaction.

## XP ôn
- 1 XP cho mỗi từ nhớ trong lượt đầu, khóa sự kiện `review:<phiên>:<từ>` nên không cộng trùng; tối đa 10 XP mỗi phiên (10 thẻ).
- Đánh giá là do người học tự báo nên chỉ là thưởng nhẹ, không dùng để mở khóa gì.

## Hiển thị
- Tab Ôn tập: phiên ôn, màn kết quả (số nhớ/chưa nhớ, XP, thành tích mới), nút "Ôn thêm"; khi chưa có từ đến hạn thì giải thích.
- Trang chủ: thẻ "Có N từ đến hạn ôn" kèm nút "Ôn ngay" (số đếm cập nhật mỗi 30 giây).
- Thành tích mới "100 lượt ôn" (đếm các lượt ôn tính lịch, cả nhớ lẫn chưa nhớ), xem ở Hồ sơ.

## Chưa làm
- Tab Từ vựng: tìm kiếm Hanzi/Pinyin/nghĩa, chi tiết từ, yêu thích (Phase 5, Task 2).
- Thống kê ghi nhớ, bài kiểm tra truy hồi dạng trắc nghiệm trong Ôn tập, nhắc ôn, bảng ReviewSession riêng (hiện chỉ cần mã phiên trong khóa XP).
