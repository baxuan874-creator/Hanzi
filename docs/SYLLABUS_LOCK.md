# SYLLABUS LOCK — HSK 3.0 (Phase 2, Task 1)

Ngày tra cứu: 2026-11-26
Trạng thái: **PROVISIONAL** (tạm khóa) — chưa đối chiếu trực tiếp trang chủ chinesetest.cn và chưa có checksum.

## Khóa đề xuất
| Trường | Giá trị |
|---|---|
| curriculum_id | hsk_3_0 |
| syllabus_id | hsk_3_0_2025_11 |
| Tên văn bản | 《HSK 考试大纲》 / Syllabus for the Chinese Proficiency Test |
| Đơn vị phát hành | 中外语言交流合作中心 (Trung tâm Hợp tác Giao lưu Ngôn ngữ Trung Quốc – Nước ngoài); thi do CTI/汉考国际 tổ chức |
| Phát hành | 2025-11 |
| Có hiệu lực | 2026-07 |
| Cổng tra cứu chính thức | https://www.chinesetest.cn/syllabus |
| Bản PDF (~406 trang) | https://hsk.cn-bj.ufileos.com/3.0/%E6%96%B0%E7%89%88HSK%E8%80%83%E8%AF%95%E5%A4%A7%E7%BA%B21219.pdf |
| retrieved_at | 2026-11-26 (tra cứu qua tìm kiếm; chưa tải file) |
| checksum | CHƯA CÓ — chạy `tools/fetch_syllabus.sh` để sinh `docs/syllabus_checksums.txt` |

## HSK 3.0 Level 1 theo đề cương 2025
| Hạng mục | Số lượng |
|---|---|
| Từ vựng | 300 |
| Chữ nhận mặt (认读) | 246 |
| Chữ viết tay (书写) | 0 (chữ viết bắt đầu từ Level 2) |
| Điểm ngữ pháp | 70 |

Cấu trúc đề cương: 任务 (nhiệm vụ), 话题 (chủ đề), 词汇, 语法, 汉字. Level 1 không có phần thi nói và không có phần viết (theo bài viết thứ cấp, cần kiểm lại với mẫu đề CTI).

## Ngày triển khai thi
- CTI thông báo HSK 3.0 chính thức thi toàn cầu từ **2026-12-13** (các nguồn ghi ngày thông báo là 8 hoặc 9/9/2026).
- Tin chính thức: https://www.chinesetest.cn/news/51336883 (cùng ID với đường dẫn old.chinesetest.cn trong PRD).
- Kỳ HSK 1–6 theo bản cũ cuối cùng trên lịch CTI: 2026-11-07.

## Nguồn dùng để lập bản khóa
- Chính thức: cổng tra cứu và PDF nêu trên (chỉ xác nhận qua các trang trích dẫn, chưa mở trực tiếp).
- Thứ cấp, dùng để đối chiếu: bản sao dữ liệu cộng đồng github.com/ofou/hsk (tự nhận lấy từ API chính thức, có kiểm tra số lượng từng cấp), gushichinese.com, hskace.com, studycli.org. Không dùng làm nguồn chuẩn cuối cùng. Chưa xem giấy phép của bản sao cộng đồng nên chưa được đưa vào app.

## Phát hiện cần xử lý
1. **Có nhiều danh sách HSK 3.0 cũ lưu hành** (chuẩn 2021, ví dụ PDF năm 2024 trên một số trang). Số lượng khác bản 2025, không được dùng. Chỉ dùng bản 2025-11.
2. **Số thứ tự (序号) khác nhau giữa các bản PDF** (ví dụ 昨天 và 做饭 đổi chỗ). Không dùng 序号 làm khóa ID; dùng (từ + cấp + từ loại).
3. **Một trang thứ cấp ghi 299 từ cho Level 1, đề cương ghi 300.** Cần đếm lại từ PDF chính thức.
4. **Level 1 không có chữ viết bắt buộc.** Màn hình viết chữ (FR-15, mức "nên có") chỉ nên là luyện tập tùy chọn, không phải yêu cầu của cấp.
5. **Đề xuất cập nhật PRD** (chưa sửa, chờ bạn đồng ý): ghi rõ nguồn phạm vi là 《HSK 考试大纲》 2025-11 (GF0025-2021 chỉ là khung nền); đổi ngày hiệu lực "13/12/2026" thành "ngày thi chính thức đầu tiên 13/12/2026; đề cương có hiệu lực 7/2026".

## Việc cần làm để chuyển sang LOCKED
- [ ] Chạy `tools/fetch_syllabus.sh` trong Codespace, commit `docs/syllabus_checksums.txt`.
- [ ] Mở PDF/cổng chinesetest.cn xác nhận: 300 từ, 246 chữ nhận mặt, 70 điểm ngữ pháp ở Level 1.
- [ ] Người có năng lực tiếng Trung duyệt danh sách Level 1 trước khi soạn bài.
