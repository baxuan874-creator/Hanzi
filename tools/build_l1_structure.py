#!/usr/bin/env python3
"""Phase 2 Task 4: editorial plan that assigns the 300 Level 1 words and 70 grammar points to
chapters and lessons. Source of truth for the plan; writes content_work/hsk3_l1_structure_working.json.
Teaching ORDER is our editorial decision. Syllabus MEMBERSHIP is never changed here:
every word/grammar id must exist in the working CSVs and be used exactly once."""
import csv, json, pathlib, sys
ROOT = pathlib.Path(__file__).resolve().parent.parent
CW = ROOT / "content_work"

# (chapter_id, title_vi, official_task, official_topic_L1, [lessons])
# lesson = (title_vi, objective_vi, "words separated by spaces", [grammar seq numbers])
PLAN = [
 ("ch_foundation", "Khởi động: âm và chữ", "—", "—", [
   ("Thanh điệu và pinyin đầu tiên", "Nhận ra 4 thanh điệu và thanh nhẹ", "", []),
   ("Thanh mẫu và vận mẫu thường gặp", "Đọc được các âm tiết đơn giản", "", []),
   ("Nét chữ và chữ Hán đầu tiên", "Nhận biết các nét cơ bản và thứ tự nét đơn giản", "", []),
 ]),
 ("ch01_greetings", "Chào hỏi và lịch sự", "六 日常言语交往", "日常生活 / 交往", [
   ("Xin chào", "Chào hỏi và nói lời lịch sự cơ bản", "你好 您 你 我 好 再见 谢谢 对不起 不客气 没关系 请 请问", [51]),
   ("Chúng tôi, họ, mọi người", "Dùng đại từ nhân xưng số nhiều", "他 她 它 他们 她们 它们 我们 你们 大家 们", [9, 2]),
   ("Là và không phải", "Nói câu có 是 và phủ định bằng 不", "是 不", [43, 47, 48, 53]),
   ("的, 吗, 呢, 吧", "Dùng 的 và các trợ từ cuối câu", "的 吗 呢 吧", [24, 26]),
 ]),
 ("ch02_about_me", "Giới thiệu bản thân", "一 问答个人基本信息", "基本信息 / 个人信息", [
   ("Tên và quốc tịch", "Hỏi và nói tên, quốc tịch", "叫 什么 名字 人 国 中国 汉语 中文 汉字 谁", [8, 49]),
   ("Tuổi và giới tính", "Hỏi và nói tuổi, giới tính", "岁 年 几 多少 男 女 多", []),
   ("Gia đình", "Nói về các thành viên trong gia đình", "爸爸 妈妈 哥哥 姐姐 弟弟 妹妹 儿子 女儿 孩子 家 家人 有", [54]),
   ("Bạn bè và thầy cô", "Giới thiệu bạn bè, thầy cô", "朋友 男朋友 女朋友 认识 同学 老师 学生 先生 女士 小朋友", []),
   ("Biết làm gì", "Nói điều mình biết, có thể làm", "会 能 可以 说 说话 写", [4, 6]),
 ]),
 ("ch03_time", "Số, ngày, giờ", "二 问答事件信息", "基本信息 / 事件信息", [
   ("Số từ 0 đến 10", "Đếm và đọc số đơn giản", "零 一 二 两 三 四 五 六 七 八 九 十", [11]),
   ("Số lớn, thứ tự", "Đọc số lớn và số thứ tự", "百 千 半 一半 第", [1, 68]),
   ("Hôm qua, hôm nay, ngày mai", "Nói các ngày trong tuần", "今天 明天 昨天 天 星期 星期日 星期天", [45]),
   ("Ngày, tháng, năm", "Nói ngày tháng năm", "月 日 号 今年 明年 去年", [14, 69]),
   ("Mấy giờ rồi", "Hỏi và nói giờ, gọi điện", "点 分 分钟 小时 时候 时间 现在 电话 打电话 喂", [70, 27]),
   ("Các buổi trong ngày", "Nói về buổi trong ngày và sinh hoạt", "早 早上 上午 中午 下午 晚 晚上 白天 起床 睡 睡觉", []),
 ]),
 ("ch04_things", "Đồ vật quanh ta", "三 问答物品信息", "基本信息 / 物品信息", [
   ("Đồ dùng", "Gọi tên đồ vật thường gặp", "东西 书 杯子 桌子 椅子 手机 电脑 电视 衣服 穿", []),
   ("Lượng từ", "Dùng lượng từ khi đếm đồ vật", "个 本 件 只 口 块 元", [12, 13]),
   ("Đặc điểm đồ vật", "Mô tả đồ vật bằng tính từ", "大 小 新 少 贵 便宜 漂亮", []),
   ("Rất, hơi, quá", "Dùng phó từ chỉ mức độ", "很 非常 太 真 有点儿 好看 好听 好吃 好玩儿", [15, 44]),
   ("Cái này, cái kia", "Chỉ định đồ vật", "一点儿 一些 些 一下 有的 有些 这 那 这个 那个 这些 那些", [10]),
   ("Không có, cho, tìm", "Nói có, không có, cho và tìm đồ", "没有 不要 没事 给 找", [20, 59]),
 ]),
 ("ch05_weather", "Thời tiết và cảm xúc", "四 问答天气", "基本信息 / 环境信息(天气)", [
   ("Thời tiết", "Hỏi và nói thời tiết", "天气 冷 热 雨 下雨 雪", []),
   ("Thích và muốn", "Nói điều thích, muốn, nghĩ", "喜欢 想 要 觉得 知道 高兴 忙", [5]),
   ("Con vật yêu thích", "Nói về con vật mình thích", "爱 狗 猫", []),
 ]),
 ("ch06_places", "Nơi chốn", "五 问答环境信息", "基本信息 / 环境信息", [
   ("Địa điểm quanh ta", "Gọi tên nơi chốn và nói ở đâu", "学校 商店 超市 电影院 饭店 医院 书店 店 房间 住 在", [21, 55, 56]),
   ("Phương hướng", "Nói vị trí trên dưới trước sau", "上 下 里 外 前 后 外边 边 那边 这边", [3, 34]),
   ("Ở đâu, ở đây", "Hỏi và chỉ nơi chốn", "这里 那里 哪里 这儿 那儿 哪儿 哪 哪个 哪些 怎么 怎么样", []),
 ]),
 ("ch07_food", "Ăn uống", "七 问答饮食喜好 / 十五 问答中国传统饮食", "日常生活 / 饮食; 文化与传统 / 饮食文化", [
   ("Đồ ăn", "Gọi tên món ăn thường gặp", "饭 菜 米饭 面包 面条儿 包子 饺子 鸡蛋", []),
   ("Đồ uống và trái cây", "Nói về đồ uống, trái cây", "水 水果 苹果 茶 牛奶 喝 吃", []),
   ("Bữa ăn", "Nói về ba bữa và nấu ăn", "早饭 午饭 晚饭 做饭 做", []),
 ]),
 ("ch08_travel", "Đi lại", "八 问答出行信息", "日常生活 / 交通出行", [
   ("Phương tiện", "Nói về phương tiện đi lại", "车 出租车 火车 飞机 开车 开 坐", [57]),
   ("Đi và đến", "Nói đi đâu, về đâu", "去 来 到 回 上学", [58]),
 ]),
 ("ch09_shopping", "Mua sắm", "九 问答商品信息", "日常生活 / 购物", [
   ("Mua và bán", "Hỏi giá, mua bán", "买 卖 钱 和 对", [22, 23, 67]),
 ]),
 ("ch10_health", "Sức khỏe", "十 告知生病、探病", "日常生活 / 医疗健康", [
   ("Bị ốm", "Nói khi bị ốm, đi khám", "病 生病 看病 医生 休息", []),
 ]),
 ("ch11_leisure", "Giải trí", "十一 交流休闲活动", "日常生活 / 休闲", [
   ("Xem, nghe, hát, chơi", "Nói về hoạt động giải trí", "看 听 唱 歌 玩 电影 看见 听见 见", []),
 ]),
 ("ch12_study", "Học tập và trường học", "十二 介绍学习情况 / 十三 交流学校情况、课后活动", "教育情况", [
   ("Trong lớp học", "Nói về giờ học và bài học", "学 学习 课 上课 下课 读 读书 字 问 问题", [7]),
   ("Các cấp học", "Nói về các cấp học", "大学 大学生 中学 中学生 小学 小学生", []),
 ]),
 ("ch13_work", "Công việc", "十四 交流工作信息", "职场生活", [
   ("Đi làm", "Nói về công việc, nơi làm việc", "工作 公司 事 上班 下班", []),
 ]),
 ("ch14_grammar_words", "Từ chỉ hành động và trạng thái", "(tổng hợp ngữ pháp)", "(nhiều chủ đề)", [
   ("Việc đã xảy ra", "Dùng 了 cho việc đã xảy ra và thay đổi", "了", [25, 62, 63]),
   ("Đang làm gì", "Nói hành động đang diễn ra", "正在", [17, 64, 65, 66]),
   ("Cũng, đều, còn, lại", "Dùng 都, 也, 还, 再", "都 也 还 再", [16, 18, 19, 60, 61]),
 ]),
 ("ch15_sentences", "Tổng hợp cấu trúc câu", "(tổng hợp ngữ pháp)", "(nhiều chủ đề)", [
   ("Cụm từ", "Nhận biết các loại cụm từ", "", [28, 29, 30, 31, 32, 33]),
   ("Thành phần câu", "Nhận biết chủ ngữ, vị ngữ, tân ngữ, định ngữ, trạng ngữ, bổ ngữ", "", [35, 36, 37, 38, 39, 40, 41, 42]),
   ("Các loại câu", "Nhận biết câu vô chủ, câu hỏi chính phản, câu cảm thán", "", [46, 50, 52]),
 ]),
]

def load():
    v = list(csv.DictReader(open(CW / "hsk3_l1_vocab_working.csv", encoding="utf-8")))
    g = list(csv.DictReader(open(CW / "hsk3_l1_grammar_working.csv", encoding="utf-8")))
    return v, g

def build():
    v, g = load()
    vid = {r["hanzi"]: r["id"] for r in v}
    gid = {int(r["syllabus_seq"]): r["id"] for r in g}
    problems, used_w, used_g = [], {}, {}
    chapters, lessons, prev = [], [], None
    n = 0
    for cid, ctitle, task, topic, items in PLAN:
        ch_lessons = []
        for title, obj, words, gseq in items:
            n += 1
            lid = f"hsk3_l1_lesson_{n:03d}"
            wids = []
            for w in words.split():
                if w not in vid: problems.append(f"unknown word {w} in {lid}"); continue
                if w in used_w: problems.append(f"word {w} used twice ({used_w[w]}, {lid})"); continue
                used_w[w] = lid; wids.append(vid[w])
            gids = []
            for q in gseq:
                if q not in gid: problems.append(f"unknown grammar {q} in {lid}"); continue
                if q in used_g: problems.append(f"grammar {q} used twice ({used_g[q]}, {lid})"); continue
                used_g[q] = lid; gids.append(gid[q])
            minutes = min(10, 5 + (len(wids) + len(gids) + 1) // 3)
            lessons.append({"id": lid, "curriculum_id": "hsk_3_0", "level_id": "hsk3_l1", "chapter_id": cid,
                "title_vi": title, "objectives": [obj], "vocabulary_ids": wids, "grammar_ids": gids,
                "exercise_ids": [], "estimated_minutes": minutes,
                "prerequisite_ids": [prev] if prev else [], "content_version": "0.1.0-draft"})
            prev = lid; ch_lessons.append(lid)
        chapters.append({"id": cid, "title_vi": ctitle, "official_task_refs": task, "official_topic_refs": topic,
                         "lesson_ids": ch_lessons,
                         "checkpoint": {"id": cid + "_checkpoint", "title_vi": "Kiểm tra chương: " + ctitle,
                                        "covers_lesson_ids": ch_lessons, "exercise_ids": []}})
    for r in v:
        if r["hanzi"] not in used_w: problems.append(f"word not assigned: {r['hanzi']}")
    for q in gid:
        if q not in used_g: problems.append(f"grammar not assigned: {q}")
    return {"curriculum_id": "hsk_3_0", "level_id": "hsk3_l1", "syllabus_id": "hsk_3_0_2025_11",
            "status": "draft_unreviewed", "chapters": chapters, "lessons": lessons}, problems

if __name__ == "__main__":
    data, problems = build()
    for p in problems: print("PROBLEM:", p)
    if problems: sys.exit(1)
    out = CW / "hsk3_l1_structure_working.json"
    out.write_text(json.dumps(data, ensure_ascii=False, indent=1), encoding="utf-8")
    nl = len(data["lessons"]); nw = sum(len(l["vocabulary_ids"]) for l in data["lessons"]); ng = sum(len(l["grammar_ids"]) for l in data["lessons"])
    print(f"OK: {len(data['chapters'])} chapters, {nl} lessons, {nw} words, {ng} grammar points -> {out.name}")
    print("max words in a lesson:", max(len(l["vocabulary_ids"]) for l in data["lessons"]),
          "| max grammar in a lesson:", max(len(l["grammar_ids"]) for l in data["lessons"]))
