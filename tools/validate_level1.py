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

print(f"Rows checked: {len(rows)} (expected {EXPECTED_VOCAB})")
print("PENDING (not extracted yet): 246 recognition characters, 70 grammar points")
for line in warnings: print(line)
for line in errors: print(line)
print(f"{len(errors)} error(s), {len(warnings)} warning(s)")
sys.exit(1 if errors else 0)
