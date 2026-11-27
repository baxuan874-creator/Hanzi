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

## Cập nhật Task 3 (2026-11-26)
- Các ký tự khác nhau trong 300 từ: **248**, trong khi đề cương ghi **246** chữ nhận mặt. Chênh 2 chữ, nên KHÔNG thể suy ra danh sách chữ từ danh sách từ; phải đọc trang chữ trong PDF (trang 353–354).
- Không đọc được các trang này từ môi trường của tôi (công cụ chỉ lấy được phần đầu PDF; bản sao dữ liệu cộng đồng chặn truy cập tự động). Vì vậy đã viết `tools/extract_l1_from_pdf.py` để chạy trong Codespace: tìm trang theo số trang in, trích chữ, so với 300 từ, ghi `content_work/hsk3_l1_chars_working.csv` chỉ khi đủ 246.
- Ngữ pháp (70 điểm, trang 383–385): script chỉ ghi văn bản thô vào `data_source/l1_grammar_raw.txt` (không đưa lên GitHub). Chưa phân tích cấu trúc vì chưa thấy bố cục thật.
- Đã chạy: `tools/selftest_extract.py` (kiểm thử logic trên trang giả) đạt. Chưa chạy: script trên PDF thật.

## Cập nhật Task 3b — ngữ pháp (2026-11-26)
- Đọc được văn bản thô các trang in 383–385 (do người dùng chạy script trong Codespace). Bảng ngữ pháp là bảng phân cấp **không có số thứ tự**, cột: 类别 / 类别名称 / 细目 / 语法内容.
- Đếm theo từng dòng nội dung nhỏ nhất (mỗi mục (1)(2)(3), mỗi nhóm từ trong 词类 là một điểm): **70 điểm, khớp con số 70 của đề cương**. Đây là cách đếm của mình, chưa có tài liệu chính thức mô tả cách đánh số; người rà soát nên xác nhận ranh giới các điểm.
- File: `content_work/hsk3_l1_grammar_working.csv` (70 dòng, `extracted_unreviewed`). ID `gram_l1_NNNN` theo thứ tự trong PDF rồi đóng băng.
- Đối chiếu chéo: mọi từ trong các danh sách từ của ngữ pháp đều có trong 300 từ Level 1, trừ **杯** (mục “借用名量词”; từ 杯子 có trong danh sách từ, riêng 杯 thì không). Không phải lỗi, ghi cảnh báo.
- Cảnh báo nguồn: mục 存现句1 (1) in `所处+是+名词`, mục (2) in `处所+有+…`; có thể là lỗi in hoặc lỗi trích xuất. Giữ nguyên như nguồn.
- Chưa có ví dụ và giải thích tiếng Việt cho điểm ngữ pháp nào; sẽ tự biên soạn ở giai đoạn soạn bài.
- Còn lại của Task 3: 246 chữ nhận mặt (cần `l1_chars_raw.txt` hoặc ảnh kết quả của script).
