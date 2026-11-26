# HANZI JOURNEY — MASTER PRD

Version 1.0 | 26 November 2026 | Vietnamese-first | Offline-first

## 0. Tóm tắt điều hành

Hanzi Journey là ứng dụng Android học tiếng Trung cho người Việt, có trải nghiệm gamification kiểu hành trình khám phá. MVP tập trung vào một lộ trình HSK được kiểm soát chất lượng, thay vì cố bao phủ nhiều cấp độ với nội dung sơ sài.

Định hướng đã chốt:
- Quy mô: MVP.
- Giao diện: game/gamification.
- AI: không sử dụng.
- Chương trình: HSK.
- Nền tảng: Android Native.
- Công nghệ: Kotlin, Jetpack Compose, Room, DataStore, JSON assets.
- Ngôn ngữ giao diện: tiếng Việt mặc định.
- Chi phí vận hành mục tiêu: 0 đồng; không server, không API trả phí, không quảng cáo, không Premium.
- Offline-first: toàn bộ chức năng học cốt lõi chạy ngoại tuyến.

Định nghĩa thành công: người mới có thể mở app, học bài, làm bài tập, nghe phát âm (nếu thiết bị có giọng tiếng Trung phù hợp hoặc app có audio được cấp phép), ôn từ đến hạn, nhận XP và đóng/mở app mà không mất tiến độ.

## 1. Bối cảnh HSK và quyết định chương trình

Tại ngày lập tài liệu 26/11/2026, Chinese Testing International (CTI) thông báo HSK 3.0 sẽ chính thức triển khai toàn cầu ngày 13/12/2026. Vì vậy, dự án chọn HSK 3.0 làm định hướng nội dung mới, nhưng phải khóa phiên bản đề cương và kiểm tra nguồn chính thức trước khi nhập dữ liệu.

HSK 3.0 được mô tả trên trang CTI với năm thành phần của đề cương: nhiệm vụ, chủ đề, từ vựng, ngữ pháp và chữ Hán. Chuẩn GF0025-2021 của Bộ Giáo dục Trung Quốc mô tả khung ba bậc, chín cấp, bốn yếu tố ngôn ngữ (âm tiết, chữ Hán, từ vựng, ngữ pháp) và năm kỹ năng (nghe, nói, đọc, viết, dịch).

Quy tắc bắt buộc:
1. HSK 3.0 là curriculum mặc định của sản phẩm.
2. Không trộn danh sách HSK 2.0 và HSK 3.0 trong cùng một track.
3. Mỗi content pack phải ghi curriculum_id, version, source, retrieved_at và checksum.
4. Nếu người học cần chuẩn bị kỳ thi theo phiên bản cũ, tạo track HSK 2.0 riêng trong tương lai.
5. Trước khi phát hành nội dung Level 1, đối chiếu trực tiếp bản đề cương HSK 3.0 hiện hành trên CTI; không lấy blog tổng hợp làm nguồn chuẩn.
6. Không khẳng định app được CTI chứng nhận hoặc bảo trợ.

## 2. Product vision, mục tiêu và ngoài phạm vi

Vision: giúp người Việt hình thành nền tảng tiếng Trung có thể sử dụng, bằng những phiên học ngắn, có mục tiêu, phản hồi tức thì và ôn tập có kế hoạch.

Mục tiêu MVP:
- Có một lộ trình Foundation + HSK 3.0 Level 1 được biên soạn và rà soát.
- Tổ chức bài học thành các nhiệm vụ nhỏ, có giải thích tiếng Việt.
- Luyện nhận diện chữ Hán, Pinyin, nghĩa, nghe hiểu cơ bản, cấu trúc câu và viết nét ở mức hướng dẫn.
- Có ôn tập chủ động và lịch lặp lại cách quãng.
- Có tiến độ, XP, streak, thành tích và nhiệm vụ hằng ngày.
- Lưu dữ liệu hoàn toàn trên thiết bị.
- Không bắt buộc đăng nhập hoặc kết nối Internet.

Ngoài phạm vi MVP:
- AI tutor, chatbot, chấm nói bằng AI.
- Đồng bộ cloud, tài khoản, bảng xếp hạng trực tuyến.
- HSK Level 2–9, HSKK, luyện thi đầy đủ.
- Nhận diện chữ viết tay bằng ML.
- Tự động đánh giá phát âm chuyên sâu.
- Mạng xã hội, lớp học trực tuyến, cửa hàng vật phẩm, thanh toán.

## 3. Người dùng mục tiêu và nguyên tắc trải nghiệm

Nhóm chính: người Việt mới bắt đầu hoặc ở trình độ sơ cấp, muốn tự học theo lộ trình có cấu trúc và có thể học offline.

Nguyên tắc:
- Tiếng Việt giải thích rõ ràng; chữ Hán và Pinyin luôn hiển thị đúng vai trò.
- Mỗi màn hình có một mục tiêu học rõ.
- Không biến XP/streak thành áp lực hoặc hình phạt.
- Không dùng hearts/energy để chặn học.
- Người học được xem lại giải thích sau khi trả lời sai.
- Có thể tắt âm thanh, giảm chuyển động và dùng Dark Mode.
- Bài học ngắn nhưng có truy hồi kiến thức cũ.
- Hình ảnh, nhân vật và bản đồ là thiết kế nguyên bản, không sao chép mascot, UI hoặc tài sản của app khác.

## 4. Cấu trúc thông tin và điều hướng

Điều hướng chính gồm 5 tab:
1. Home: mục tiêu hôm nay, tiếp tục học, số từ đến hạn, streak và tiến độ.
2. Learn: bản đồ hành trình, Foundation, chương, bài học, checkpoint và bài kiểm tra.
3. Vocabulary: từ đã học, tìm kiếm Hanzi/Pinyin/nghĩa, yêu thích và chi tiết từ.
4. Review: hàng đợi ôn tập, flashcards, bài kiểm tra truy hồi và thống kê ghi nhớ.
5. Profile: XP, level, thành tích, lịch sử, cài đặt, sao lưu và giấy phép dữ liệu.

Màn hình phụ: Lesson Intro, Vocabulary Card, Grammar Note, Exercise, Lesson Result, Chapter Checkpoint, Pinyin Practice, Character Writing, Dictionary Entry, Settings, About & Licenses.

## 5. Learning design — phương pháp sư phạm

Không thiết kế khóa học chỉ bằng trắc nghiệm. Mỗi đơn vị học phải tạo cơ hội tiếp xúc, hiểu, truy hồi và sử dụng kiến thức.

5.1. Bốn mạch học tập (Nation's Four Strands)
- Meaning-focused input: nghe và đọc câu/hội thoại dễ hiểu, có ngữ cảnh.
- Meaning-focused output: chọn, sắp xếp, tạo câu hoặc viết câu ngắn để truyền đạt ý.
- Language-focused learning: học có chủ đích về âm, Pinyin, chữ, từ, ngữ pháp.
- Fluency development: luyện nhanh với nội dung đã học, giảm tải suy nghĩ về kiến thức mới.
Không bắt buộc chia đúng 25% trong từng bài; cần cân bằng trên toàn khóa học.

5.2. Retrieval practice / Active recall
Thay vì chỉ xem lại flashcard, app yêu cầu người học nhớ nghĩa, nhớ chữ, nghe nhận diện hoặc tự tạo câu trước khi xem đáp án. Mỗi câu sai phải có giải thích và cơ hội thử lại sau.

5.3. Spaced practice
Lặp lại kiến thức qua nhiều phiên, không nhồi toàn bộ vào một lần. Lịch ban đầu có thể dùng các khoảng 10 phút, 1 ngày, 3 ngày, 7 ngày, 14 ngày nhưng đây chỉ là heuristic khởi điểm; cần hiệu chỉnh bằng dữ liệu sử dụng và không tuyên bố tối ưu khoa học tuyệt đối.

5.4. Interleaving
Trộn có chủ đích từ cũ và mới, các dạng câu hỏi khác nhau, nghe–đọc–nhận diện. Tránh trộn quá nhiều nội dung chưa học khiến người mới quá tải.

5.5. Contextual learning
Từ mới phải xuất hiện trong cụm từ, câu ví dụ và hội thoại. Tách nghĩa đơn lẻ khỏi cách dùng sẽ không đủ.

5.6. Dual coding và chunking
Kết hợp chữ, âm thanh, nghĩa, ví dụ và hình ảnh có ích; chia nội dung thành nhóm nhỏ theo mục tiêu giao tiếp. Không thêm hình trang trí gây nhiễu.

5.7. Feedback
Phản hồi tức thì, ngắn, giải thích vì sao đúng/sai. Tránh chỉ hiện màu xanh/đỏ mà không dạy lại.

5.8. Character learning
Dạy bộ/thành phần chữ khi hữu ích, quy tắc nét cơ bản và nhận diện hình thể. Không hứa rằng mọi chữ đều có thể đoán nghĩa từ bộ thủ; giải thích nguồn gốc chữ chỉ đưa vào khi có nguồn đáng tin cậy.

5.9. Pronunciation
Bắt đầu với Pinyin, thanh mẫu/vận mẫu và thanh điệu; dùng cặp âm dễ nhầm, nghe–phân biệt–nhắc lại. TTS chỉ là phương án dự phòng, không thay thế bản thu âm chuẩn cho bài nghe đánh giá.

5.10. Spiral review
Một khái niệm quay lại ở các bài sau với độ khó và ngữ cảnh tăng dần.

## 6. Khung một bài học và loại bài tập

Một lesson 5–10 phút, tùy mục tiêu:
A. Mục tiêu học tập.
B. Warm-up: truy hồi 1–3 kiến thức cũ.
C. Input: từ/câu mới, âm thanh và ngữ cảnh.
D. Guided practice: nhận diện, ghép, chọn, hoàn thành.
E. Output: sắp xếp câu hoặc tạo phản hồi ngắn.
F. Quick review: câu hỏi tổng hợp.
G. Result: điểm, XP, lỗi cần xem lại và từ được đưa vào lịch ôn.

Các loại exercise:
- Hanzi → nghĩa.
- Nghĩa → Hanzi (nhận diện, không mặc định phải viết).
- Pinyin → âm/chữ.
- Nghe → chọn từ/câu.
- Ghép từ với nghĩa hoặc ví dụ.
- Sắp xếp từ thành câu.
- Điền chỗ trống.
- Chọn cấu trúc ngữ pháp.
- Đọc câu ngắn và trả lời.
- Nhận diện/thực hành nét chữ.
Mỗi exercise có ID ổn định, prompt, answer schema, explanation_vi, difficulty, skill_tags và source_ref.

## 7. Gamification — luật chơi

Phong cách: playful educational adventure, không sao chép Duolingo. Dùng bản đồ hành trình, khu vực/chapter, node bài học, checkpoint và huy hiệu.

XP khởi điểm (có thể cân bằng sau playtest):
- Hoàn thành lesson lần đầu: 20 XP.
- Hoàn thành checkpoint: 30 XP.
- Ôn đúng một thẻ: 1–2 XP, có giới hạn theo phiên.
- Hoàn thành nhiệm vụ ngày: 10–15 XP.
Không cộng XP vô hạn khi lặp cùng bài; ghi event_id/idempotency key để chống cộng trùng.

Level: dùng bảng ngưỡng cấu hình, không hardcode ở UI.
Streak: tính theo ngày địa phương; có grace/không có grace phải là quyết định sản phẩm rõ ràng. MVP khuyến nghị không phạt mất tiến độ học khi đứt streak.
Daily missions: chỉ sinh nhiệm vụ dựa trên nội dung đã mở khóa và lượng bài phù hợp.
Achievements: First Lesson, 25 Words, 100 Reviews, Chapter Complete, 7-Day Practice.
Không có energy/hearts ngăn người học tiếp tục học. Không có loot box, cơ chế ngẫu nhiên trả phí hoặc vật phẩm mua bán.

## 8. Curriculum production — quy trình biên soạn

Nguồn chuẩn thứ nhất: đề cương HSK 3.0 hiện hành do CTI công bố; đối chiếu các phần nhiệm vụ, chủ đề, từ vựng, ngữ pháp và chữ Hán.

Nguồn chuẩn nền tảng: GF0025-2021, Chinese Proficiency Grading Standards for International Chinese Language Education, do Bộ Giáo dục Trung Quốc công bố.

Quy trình:
1. Tải/đọc đề cương từ CTI và lưu bản, ngày truy cập, checksum.
2. Trích xuất danh mục Level 1 thành bảng dữ liệu làm việc.
3. Gắn từng từ/chữ/ngữ pháp vào chủ đề và mục tiêu giao tiếp.
4. Soạn nghĩa tiếng Việt, ví dụ tự viết và giải thích.
5. Kiểm tra bởi người có năng lực tiếng Trung; ít nhất kiểm tra chéo các mục trọng yếu.
6. Tạo exercise và đáp án từ dữ liệu đã duyệt.
7. Kiểm tra nhất quán Hanzi–Pinyin–nghĩa–audio.
8. Lưu nguồn và quyền sử dụng cho từng asset.
9. Đóng gói content pack có version.
10. Chạy kiểm tra tự động: thiếu trường, trùng ID, từ ngoài cấp độ, Pinyin thiếu dấu, đáp án không hợp lệ.

Không dùng danh sách do blog/ứng dụng bên thứ ba tổng hợp làm chuẩn cuối cùng. Không chép nguyên văn bài đọc, hội thoại, hình, audio hoặc bài tập từ sách thương mại. Có thể dùng giáo trình thương mại làm tài liệu tham khảo khi người biên soạn có quyền truy cập, nhưng nội dung app phải tự biên soạn hoặc có giấy phép sử dụng.

## 9. Nguồn giáo trình, tài liệu và dữ liệu

A. Curriculum / chuẩn chính thức
1. Chinese Test Service / CTI – HSK: https://www.chinesetest.cn/HSK
   Dùng để kiểm tra đề cương, thông báo, mẫu đề và phiên bản áp dụng. Đây là nguồn ưu tiên cao nhất cho phạm vi thi HSK.
2. Bộ Giáo dục Trung Quốc – GF0025-2021: https://hudong.moe.gov.cn/jyb_xwfb/gzdt_gzdt/s5987/202103/t20210329_523304.html
   Dùng làm khung tham chiếu năng lực, kỹ năng và thành tố ngôn ngữ.
3. CTI announcement HSK 3.0: https://old.chinesetest.cn/gonewcontent.do?flag=kdnew&id=51336883
   Thông báo ngày triển khai toàn cầu 13/12/2026; phải kiểm tra lại trước release vì lịch/đề cương có thể được cập nhật.

B. Từ điển / chữ Hán / stroke data
4. Make Me a Hanzi: https://github.com/skishore/makemeahanzi
   Có dữ liệu từ điển và đồ họa nét cho nhiều chữ. Các file có nguồn và giấy phép khác nhau; không coi toàn bộ repository có một license chung. Đọc LICENSE/NOTICE của từng file trước khi phân phối.
5. Hanzi Writer: https://hanziwriter.org/
   Thư viện hiển thị/animation và kiểm tra thứ tự nét; xác minh license của phiên bản và dữ liệu đi kèm trước khi tích hợp.
6. CC-CEDICT: https://www.mdbg.net/chinese/dictionary?page=cc-cedict
   Từ điển cộng đồng; kiểm tra license CC BY-SA và nghĩa vụ attribution/share-alike trước khi nhúng hoặc sửa dữ liệu.
7. Unicode Unihan: https://www.unicode.org/charts/unihan.html
   Tham chiếu thông tin ký tự; kiểm tra điều khoản Unicode License.

C. Ngữ pháp / giải thích
8. Chinese Grammar Wiki: https://resources.allsetlearning.com/chinese/grammar/
   Tài liệu tham khảo giải thích cấu trúc; không sao chép nguyên văn nội dung nếu chưa có quyền.
9. Cambridge – Paul Nation, Four Strands / vocabulary teaching:
   https://www.cambridge.org/core/books/abs/methodology-in-language-teaching/best-practice-in-vocabulary-teaching-and-learning/DB2CDA1901F8974EA424E745D05AA505
   Tham khảo thiết kế cân bằng input, output, học có chủ đích và fluency.

D. Nghiên cứu phương pháp
10. The Four Strands (Nation, 2007): https://doi.org/10.2167/illt039.0
11. The Effects of Spaced Practice on Second Language Learning: A Meta-Analysis (2022): https://onlinelibrary.wiley.com/doi/10.1111/lang.12479
12. Retrieval practice & spacing in L2 vocabulary learning: https://doi.org/10.1177/13621688211053525

E. Android kỹ thuật
13. Jetpack Compose: https://developer.android.com/develop/ui/compose
14. Room: https://developer.android.com/training/data-storage/room
15. DataStore: https://developer.android.com/topic/libraries/architecture/datastore
16. TextToSpeech: https://developer.android.com/reference/android/speech/tts/TextToSpeech

License gate:
- Tạo THIRD_PARTY_NOTICES.md.
- Ghi tên asset, nguồn, tác giả, version, license, thay đổi đã thực hiện và file được sử dụng.
- Không phân phối asset nếu license chưa rõ.
- TTS engine có thể khác nhau giữa thiết bị; không cam kết mọi máy có cùng giọng.
- Audio do người khác thu phải có quyền phân phối trong app offline.

## 10. Data model và content schema

Các bảng cốt lõi:
- CurriculumVersion
- Chapter
- Lesson
- Vocabulary
- GrammarPoint
- Exercise
- ExerciseAttempt
- UserProgress
- ReviewItem
- ReviewSession
- XPEvent
- Achievement
- UserAchievement
- DailyMission
- UserSettings
- BackupMetadata

Nguyên tắc: content data tách khỏi user data; ID ổn định; có version/migration; không nhúng tiến độ người dùng vào JSON giáo trình.

Vocabulary tối thiểu:
id, hanzi, pinyin, meaning_vi, word_type, level_id, topic_id, examples[], audio_asset_id?, source_ref, license_ref.

Exercise tối thiểu:
id, lesson_id, type, prompt, options?, answer, explanation_vi, difficulty, skill_tags[], source_ref.

Lesson:
id, curriculum_id, level_id, chapter_id, title_vi, objectives[], vocabulary_ids[], grammar_ids[], exercise_ids[], estimated_minutes, prerequisite_ids[].

## 11. Kiến trúc kỹ thuật

Android Native:
- Kotlin.
- Jetpack Compose + Material 3.
- MVVM.
- Navigation Compose.
- Room cho dữ liệu quan hệ và tiến độ.
- DataStore cho preferences.
- Kotlin Coroutines + Flow.
- JSON assets cho curriculum pack.
- Manual dependency injection trong MVP để giảm phụ thuộc.
- Android TextToSpeech làm fallback, không dùng làm nguồn duy nhất cho bài nghe chấm điểm.
- Canvas/Compose cho vùng viết chữ.
- JUnit, AndroidX Test và Compose UI Test.

Phân tầng:
Presentation: Screens, Components, ViewModels, Navigation.
Domain: Use cases, learning rules, review scheduler, XP rules.
Data: Repositories, Room DAOs, DataStore, JSON content loader.

Không cần Firebase, Supabase, backend, analytics bên thứ ba, AI API hoặc tài khoản.

## 12. UI/UX design system

Phong cách: playful educational adventure, sáng sủa, thân thiện, có cảm giác game nhưng không trẻ con quá mức.

Visual:
- Light theme mặc định, Dark theme tùy chọn.
- Nền sáng ấm, xanh dương primary, xanh ngọc secondary, vàng accent; màu success/error có tương phản rõ.
- Card bo góc, phân cấp typography rõ; chữ Hán hiển thị bằng font hỗ trợ CJK đầy đủ.
- Nhân vật linh vật nguyên bản, bản đồ thế giới học tập, node bài học, checkpoint, huy hiệu.
- Không tái tạo mascot, layout hoặc nhận diện thương hiệu của app tham khảo.
- Tất cả icon/illustration phải có nguồn hoặc được tự tạo.

Motion:
- Transition màn hình ngắn.
- Feedback đúng/sai.
- XP counter.
- Badge reveal.
- Chapter unlock.
- Lesson completion.
- Respect system reduce-motion / setting.
- Tránh animation kéo dài làm chậm thao tác học.

Accessibility:
- Font scale, độ tương phản, vùng chạm đủ lớn.
- Không dùng màu làm tín hiệu duy nhất.
- Có caption/visual support cho âm thanh.
- Tùy chọn tắt âm và giảm motion.

## 13. Functional requirements & acceptance criteria

Must:
FR-01 First launch và chọn mục tiêu học cơ bản.
FR-02 Home dashboard.
FR-03 Learning map và khóa/mở bài theo prerequisite.
FR-04 Lesson player.
FR-05 Vocabulary and grammar content.
FR-06 Exercise engine với ít nhất các dạng multiple choice, matching, listening-choice (nếu audio có), sentence ordering và fill blank.
FR-07 Feedback có giải thích.
FR-08 Progress persistence.
FR-09 Review queue và scheduler.
FR-10 XP, level, streak, achievement.
FR-11 Dictionary search.
FR-12 Audio playback có fallback rõ ràng.
FR-13 Offline core flow.
FR-14 Settings và licenses.

Should:
FR-15 Character writing canvas.
FR-16 Backup/restore local file.
FR-17 Dark Mode.
FR-18 Chapter checkpoint.

Acceptance:
- Cài đặt và mở app được trên thiết bị mục tiêu.
- Hoàn thành lesson, đóng app, mở lại vẫn giữ kết quả.
- Câu trả lời sai không làm app crash; giải thích hiện đúng.
- XP không cộng trùng khi replay.
- Review item được lên lịch và tồn tại sau restart.
- Không có nội dung HSK sai level theo source mapping đã duyệt.
- App dùng được offline cho các chức năng cốt lõi.
- Tất cả asset bên thứ ba có license/attribution record.
- Không có nút UI chỉ là placeholder giả.

## 14. Roadmap phát triển bằng Claude Free

Phase 0 — Foundation & project setup
- Khởi tạo project, Gradle, package, theme skeleton, Room/DataStore, docs.
- Build app shell đầu tiên.
- Exit criteria: build thành công, app mở được.

Phase 1 — Design system & navigation
- Tokens, typography, components, navigation, Home, Learn Map, Profile mock data.
- Exit: luồng điều hướng không lỗi; giao diện nhất quán.

Phase 2 — Curriculum content pipeline
- Chốt HSK syllabus version; schema; content importer; validation scripts.
- Soạn và rà soát Foundation + HSK 3.0 Level 1 content pack.
- Exit: JSON hợp lệ, không ID trùng, mapping nguồn rõ.

Phase 3 — Learning engine
- Lesson flow, vocabulary, grammar, exercise renderer, scoring, result.
- Exit: người dùng hoàn thành được lesson đầu cuối.

Phase 4 — Progress & gamification
- Room progress, XP event, level, streak, missions, achievements.
- Exit: không mất dữ liệu và không cộng XP trùng.

Phase 5 — Dictionary & review
- Search, detail, flashcards, scheduler, review queue.
- Exit: review state đúng sau restart.

Phase 6 — Pinyin, Hanzi & audio
- Pinyin drills, audio service, writing canvas, stroke display nếu license/data sẵn sàng.
- Exit: fallback thiết bị rõ, không tuyên bố chức năng chưa có.

Phase 7 — QA, accessibility & performance
- Unit/UI tests, offline tests, migration, content QA, performance, accessibility.
- Exit: không còn bug blocker/critical.

Phase 8 — Release
- App icon, splash, versioning, privacy disclosures, third-party notices, APK/AAB.
- Exit: release checklist pass.

Quy trình Claude Free:
- Mỗi cuộc trò chuyện chỉ xử lý một Task có phạm vi cụ thể.
- Đưa CLAUDE.md và tài liệu liên quan vào context.
- Claude phải inspect code hiện tại trước khi sửa.
- Không yêu cầu Claude viết cả app trong một prompt.
- Build/test trên Android Studio; chỉ ghi nhận test đã chạy thật.
- Cuối phiên cập nhật HANDOFF.md, CHANGELOG.md.
- Nếu hết hạn mức, dùng handoff file trong phiên mới.
- Giữ bản backup/commit trước các thay đổi lớn.

## 15. Hướng mở rộng sau MVP

P0 — Củng cố chất lượng nền tảng (ngay sau MVP)
- Mở rộng HSK 3.0 Level 2–3.
- Ôn tập thích ứng dựa trên lịch sử trả lời.
- Từ điển nâng cao: bộ thủ, thành phần, âm Hán Việt (nếu được biên soạn/kiểm chứng).
- Thêm audio người bản ngữ có giấy phép.
- Bài đọc phân cấp và câu ví dụ theo ngữ cảnh.
- Backup/restore và xuất dữ liệu tiến độ.

P1 — Kỹ năng ngôn ngữ toàn diện
- Listening Lab: tốc độ, nghe ý chính, nghe chi tiết, dictation.
- Speaking Lab không AI: ghi âm, nghe lại, shadowing và tự đánh dấu; không tự động chấm phát âm nếu chưa có mô hình đáng tin.
- Writing Lab: thứ tự nét, viết từ/câu, luyện chép.
- Reading Lab: truyện ngắn phân cấp, gloss từ, câu hỏi hiểu bài.
- Grammar Lab: bản đồ cấu trúc, lỗi thường gặp, bài tập đối chiếu.

P2 — Luyện thi
- Mock test theo cấu trúc HSK đúng phiên bản.
- Bộ đếm thời gian, phân tích điểm theo kỹ năng.
- Question bank do đội ngũ tự biên soạn hoặc có giấy phép.
- Chế độ ôn lỗi sai.
- Lộ trình ôn theo ngày thi.

P3 — Cá nhân hóa không cần AI
- Diagnostic placement test.
- Lịch học theo mục tiêu thời gian.
- Tự điều chỉnh lượng ôn theo độ chính xác và độ trễ.
- Gợi ý bài học dựa trên prerequisite và điểm yếu.
- Tùy chọn cường độ học và nhắc học cục bộ.

P4 — Nội dung mở rộng
- HSK 3.0 Level 4–6 và advanced 7–9 theo chiến lược sản phẩm.
- HSK 2.0 track riêng để tương thích người học theo giáo trình cũ.
- Phồn thể và biến thể từ vựng vùng miền.
- Chủ đề đời sống: trường học, du lịch, mua sắm, công việc.
- Văn hóa, thành ngữ, chữ phồn thể, thành tố chữ.

P5 — Tính năng nền tảng
- Đồng bộ đa thiết bị tự host (chỉ khi có phương án chi phí và bảo mật phù hợp).
- Desktop/web companion.
- Import/export Anki/CSV.
- Widgets và reminder.
- Offline content packs theo cấp độ.
- Accessibility nâng cao.

Không nên ưu tiên sớm:
- AI chatbot, social feed, leaderboard online, marketplace, cosmetic shop, cloud sync bắt buộc. Các tính năng này tăng chi phí, độ phức tạp và rủi ro dữ liệu nhưng không phải điều kiện để MVP dạy tốt.

## 16. Rủi ro và biện pháp giảm thiểu

1. HSK syllabus thay đổi: version hóa curriculum; khóa đề cương theo ngày; không hardcode.
2. Sai dữ liệu ngôn ngữ: nguồn gốc từng mục, rà soát bởi người có năng lực tiếng Trung, test chéo.
3. Vi phạm bản quyền: content tự biên soạn; license registry; không dùng audio/sách/ảnh không rõ quyền.
4. TTS không đồng nhất: fallback thông báo; audio chuẩn cho bài kiểm tra nghe.
5. Claude Free giới hạn: task nhỏ, handoff, docs, commit/backup.
6. Scope creep: mọi tính năng mới phải vào backlog, không chen vào MVP.
7. Offline DB lỗi: migrations, backup/restore, test restart và test upgrade.
8. Gamification gây áp lực: không khóa bài bằng hearts; streak không xóa kiến thức.
9. Chữ Hán hiển thị lỗi: font CJK fallback, test trên nhiều thiết bị.
10. Stroke order data license phức tạp: chỉ tích hợp sau khi license của từng dataset/file được rà soát.

## 17. Định nghĩa hoàn thành MVP

MVP chỉ được gắn nhãn 1.0 khi:
- Build Release APK/AAB thành công.
- Người dùng mới học được trọn một lesson.
- Học được từ và ngữ pháp với nội dung có nguồn.
- Làm được các dạng bài đã công bố.
- Tiến độ, XP và lịch ôn được lưu bền vững.
- Core learning hoạt động offline.
- Audio có fallback và mô tả giới hạn chính xác.
- Không có crash blocker/critical trong luồng chính.
- Không còn placeholder được trình bày như tính năng đã hoàn thiện.
- License/attribution file đầy đủ.
- README, CHANGELOG, HANDOFF và hướng dẫn build cập nhật.

