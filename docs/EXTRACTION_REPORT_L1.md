# EXTRACTION REPORT — HSK 3.0 Level 1 vocabulary (Phase 2, Task 2)

Ngày: 2026-11-26
Nguồn: 《HSK 考试大纲》2025-11 (PDF bản "1219", mục 词汇大纲, HSK 一级, 序号 1–300), đọc trực tiếp nội dung văn bản PDF.
Kết quả: `content_work/hsk3_l1_vocab_working.csv` — 300 dòng.

## Đã làm
- Trích đủ 300 mục: hanzi, pinyin, từ loại gốc, ô cấp độ gốc (ví dụ `1（4）` = từ trở lại ở cấp 4 với nghĩa khác).
- ID `vocab_l1_NNNN` gán theo thứ tự trong bản PDF này rồi **đóng băng**. `syllabus_seq` chỉ để tham chiếu, không dùng làm khóa vì số thứ tự khác nhau giữa các bản PDF.
- `review_status = extracted_unreviewed` cho cả 300 dòng. Chưa có nghĩa tiếng Việt, ví dụ hay bài tập.

## Kiểm tra đã chạy thật (tools/validate_level1.py)
- Dữ liệu thật: 0 lỗi, 14 cảnh báo.
- Kiểm thử âm: sửa một bản sao (trùng ID, thiếu pinyin, từ loại sai, thiếu một dòng) → báo 5 lỗi, thoát mã 1. Đạt.
- Script cũng chạy trong GitHub Actions trước bước build; có lỗi thì build đỏ (chưa chạy trên Actions).
- Chưa kiểm tra được: từng dòng có khớp PDF hay không (cần người rà soát), checksum PDF.

## Cảnh báo cần người có năng lực tiếng Trung xem
- 6 mục không có từ loại trong nguồn (cụm): 不客气, 打电话, 没关系, 你好, 下雨, 做饭.
- 谁 có hai cách đọc: shéi/shuí.
- **小朋友: nguồn in `xiǎopéngyǒu`, trong khi 朋友 và 男朋友 in `péngyou` (thanh nhẹ).** Có thể là lỗi in của PDF hoặc lỗi trích xuất. Giữ nguyên như nguồn, không tự sửa.

## Chưa làm (Task 3)
- 246 chữ nhận mặt và 70 điểm ngữ pháp Level 1 (trang 353 và 383–385 của PDF).
- Chủ đề/nhiệm vụ Level 1 (đề cương ghi 5 chủ đề cấp một, 15 cấp hai, 30 cấp ba; 15 nhóm nhiệm vụ) chưa nhập vào bảng.
- Checksum PDF (chạy tools/fetch_syllabus.sh).

## Lưu ý bản quyền
Danh sách từ thuộc đề cương do 中外语言交流合作中心 phát hành; giấy phép phân phối lại chưa được nêu trong nội dung đã đọc. File CSV chỉ là dữ liệu làm việc, ghi trong THIRD_PARTY_NOTICES với trạng thái NOT APPROVED, chưa đưa vào assets của app. Kho GitHub đang Public, nên cân nhắc chuyển Private cho tới khi có kết luận về giấy phép.
