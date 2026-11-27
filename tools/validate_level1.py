#!/usr/bin/env python3
"""Validate content_work/hsk3_l1_vocab_working.csv (Phase 2 Task 2).
Errors -> exit code 1. Warnings -> printed, exit 0. Never auto-corrects data."""
import csv, re, sys, pathlib

PATH = pathlib.Path(sys.argv[1] if len(sys.argv) > 1 else
                    pathlib.Path(__file__).resolve().parent.parent / "content_work" / "hsk3_l1_vocab_working.csv")
EXPECTED_VOCAB = 300          # HSK 3.0 (2025-11) Level 1 words, see docs/SYLLABUS_LOCK.md
POS_ALLOWED = {"名", "动", "形", "副", "数", "量", "代", "助", "介", "连", "叹", "前缀", "后缀", "数量"}
NEUTRAL_OK = {"吧", "的", "了", "们", "吗", "呢"}          # particles/suffix: no tone mark expected
TONE = set("āáǎàēéěèīíǐìōóǒòūúǔùǖǘǚǜ")
PINYIN_OK = re.compile(r"^[A-Za-zāáǎàēéěèīíǐìōóǒòūúǔùǖǘǚǜü’' /]+$")
REQUIRED = ["id", "syllabus_seq", "level_cell", "hanzi", "pinyin", "curriculum_id", "level_id", "source_ref", "review_status"]

errors, warnings = [], []
def err(rid, msg): errors.append(f"ERROR   {rid}: {msg}")
def warn(rid, msg): warnings.append(f"WARNING {rid}: {msg}")

if not PATH.exists():
    print(f"ERROR file not found: {PATH}"); sys.exit(1)
with open(PATH, newline="", encoding="utf-8") as f:
    rows = list(csv.DictReader(f))

if len(rows) != EXPECTED_VOCAB:
    err("file", f"expected {EXPECTED_VOCAB} rows, found {len(rows)}")
for col in REQUIRED:
    if rows and col not in rows[0]: err("file", f"missing column {col}")

seen_ids, seen_keys, seqs = set(), set(), []
for r in rows:
    rid = r.get("id", "?")
    if rid in seen_ids: err(rid, "duplicate id")
    seen_ids.add(rid)
    key = (r.get("hanzi"), r.get("homograph_index", ""))
    if key in seen_keys: err(rid, f"duplicate hanzi/homograph {key}")
    seen_keys.add(key)
    for col in ("hanzi", "pinyin", "source_ref"):
        if not (r.get(col) or "").strip(): err(rid, f"empty {col}")
    if not re.match(r"^1(（[^）]*）)*$", r.get("level_cell", "")): err(rid, f"primary level is not 1: {r.get('level_cell')}")
    if r.get("curriculum_id") != "hsk_3_0": err(rid, "curriculum_id must be hsk_3_0")
    if r.get("level_id") != "hsk3_l1": err(rid, "level_id must be hsk3_l1")
    hanzi, pin = r.get("hanzi", ""), r.get("pinyin", "")
    if not re.fullmatch(r"[\u4e00-\u9fff]+", hanzi): err(rid, f"non-CJK characters in hanzi: {hanzi!r}")
    if not PINYIN_OK.match(pin): err(rid, f"unexpected characters in pinyin: {pin!r}")
    marks = sum(ch in TONE for ch in pin)
    if marks == 0 and not (set(hanzi) <= NEUTRAL_OK):
        warn(rid, f"no tone mark in pinyin {pin!r} for {hanzi}")
    if marks > len(hanzi) + 1: warn(rid, f"more tone marks than characters: {hanzi} {pin}")
    pos_raw = r.get("pos_raw", "")
    if pos_raw:
        for t in re.split(r"[、（）()]", pos_raw):
            if t and t not in POS_ALLOWED: err(rid, f"unknown part of speech {t!r}")
    else:
        warn(rid, "no part of speech in source (phrase entry?)")
    if r.get("flags"): warn(rid, f"review flag: {r['flags']}")
    try: seqs.append(int(r["syllabus_seq"]))
    except Exception: err(rid, "syllabus_seq not an integer")
    if r.get("review_status") not in ("extracted_unreviewed", "reviewed", "approved"):
        err(rid, f"bad review_status {r.get('review_status')!r}")

if seqs != list(range(1, len(seqs) + 1)): err("file", "syllabus_seq is not contiguous from 1")


# ---- optional: recognition characters file (created by tools/extract_l1_from_pdf.py) ----
CHARS = PATH.with_name("hsk3_l1_chars_working.csv")
if CHARS.exists():
    with open(CHARS, newline="", encoding="utf-8") as f:
        crows = list(csv.DictReader(f))
    if len(crows) != 246: err("chars", f"expected 246 recognition characters, found {len(crows)}")
    hs = [c.get("hanzi", "") for c in crows]
    if len(set(hs)) != len(hs): err("chars", "duplicate characters")
    for c in crows:
        if not re.fullmatch(r"[\u4e00-\u9fff]", c.get("hanzi", "")): err(c.get("id", "?"), f"not a single CJK character: {c.get('hanzi')!r}")
    in_words = {ch for r in rows for ch in r.get("hanzi", "")}
    missing = sorted(in_words - set(hs)); extra = sorted(set(hs) - in_words)
    if missing: warn("chars", f"in the 300 words but not in the 246 list: {''.join(missing)}")
    if extra: warn("chars", f"in the 246 list but not in any of the 300 words: {''.join(extra)}")
    print(f"Characters checked: {len(crows)} (expected 246)")
else:
    print("PENDING: content_work/hsk3_l1_chars_working.csv not present yet")

# ---- optional: grammar file (70 points) ----
GRAM = PATH.with_name("hsk3_l1_grammar_working.csv")
if GRAM.exists():
    with open(GRAM, newline="", encoding="utf-8") as f:
        grows = list(csv.DictReader(f))
    if len(grows) != 70: err("grammar", f"expected 70 grammar points, found {len(grows)}")
    gids = [g.get("id") for g in grows]
    if len(set(gids)) != len(gids): err("grammar", "duplicate ids")
    if [int(g["syllabus_seq"]) for g in grows] != list(range(1, len(grows) + 1)): err("grammar", "syllabus_seq not contiguous")
    GCATS = {"语素", "词类", "短语", "句子成分", "句子的类型", "动作的态", "特殊表达法"}
    vocab_words = {r.get("hanzi", "") for r in rows}
    for g in grows:
        gid = g.get("id", "?")
        if g.get("category") not in GCATS: err(gid, f"unknown category {g.get('category')!r}")
        if not (g.get("detail") or g.get("content") or "").strip(): err(gid, "empty detail and content")
        if g.get("flags"): warn(gid, f"review flag: {g['flags']}")
        if g.get("word_list") == "Y":
            for tok in re.split(r"[、/]", g["content"].split("：")[-1]):
                tok = re.sub(r"\d", "", tok).replace("—", "")
                opts = {re.sub(r"[（）]", "", tok), re.sub(r"（[^）]*）", "", tok)}
                if not (opts & vocab_words): warn(gid, f"word {tok!r} is in the grammar list but not in the 300 words")
    print(f"Grammar points checked: {len(grows)} (expected 70)")
else:
    print("PENDING: content_work/hsk3_l1_grammar_working.csv not present yet")

print(f"Rows checked: {len(rows)} (expected {EXPECTED_VOCAB})")
for line in warnings: print(line)
for line in errors: print(line)
print(f"{len(errors)} error(s), {len(warnings)} warning(s)")
sys.exit(1 if errors else 0)
