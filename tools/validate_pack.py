#!/usr/bin/env python3
"""Validate a content pack JSON. Mirrors ContentValidator.kt (the app) and adds an authoring rule:
examples and exercises may only use characters of words introduced in this or earlier lessons.
Usage: python3 tools/validate_pack.py PACK.json [--require-approved]"""
import csv, json, pathlib, re, sys

ROOT = pathlib.Path(__file__).resolve().parent.parent
TONE = set("āáǎàēéěèīíǐìōóǒòūúǔùǖǘǚǜ")
CJK_ALL = re.compile(r"[\u4e00-\u9fff]")
CJK_WORD = re.compile(r"^[\u4e00-\u9fff]+$")
STATUSES = {"extracted_unreviewed", "draft_unreviewed", "reviewed", "approved"}
CHOICE = {"multiple_choice", "listening_choice"}
TYPES = {"multiple_choice", "matching", "listening_choice", "sentence_ordering", "fill_blank",
         "grammar_choice", "reading_question", "stroke_practice"}


def allowed_chars_up_to(lesson_ids):
    """Characters of every word introduced up to the latest of these lessons (course order)."""
    sp = ROOT / "content_work" / "hsk3_l1_structure_working.json"
    vp = ROOT / "content_work" / "hsk3_l1_vocab_working.csv"
    if not (sp.exists() and vp.exists()):
        return None
    st = json.load(open(sp, encoding="utf-8"))
    order = [l["id"] for l in st["lessons"]]
    known = [order.index(i) for i in lesson_ids if i in order]
    if not known:
        return None
    last = max(known)
    hanzi = {r["id"]: r["hanzi"] for r in csv.DictReader(open(vp, encoding="utf-8"))}
    chars = set()
    for l in st["lessons"][: last + 1]:
        for w in l["vocabulary_ids"]:
            chars |= set(hanzi[w])
    return chars


def validate(pack, require_approved=False, allowed=None):
    errors, warnings = [], []
    err = lambda i, m: errors.append(f"ERROR   {i}: {m}")
    warn = lambda i, m: warnings.append(f"WARNING {i}: {m}")
    blank = lambda i, f, v: err(i, f"empty {f}") if not (v or "").strip() else None

    def dups(kind, ids):
        for k in sorted({x for x in ids if ids.count(x) > 1}):
            err(k, f"duplicate {kind} id")

    m = pack.get("manifest", {})
    for f in ("curriculum_id", "version", "source", "retrieved_at", "checksum"):
        blank("manifest", f, m.get(f))
    ch, ls, vs = pack.get("chapters", []), pack.get("lessons", []), pack.get("vocabulary", [])
    gs, es = pack.get("grammar_points", []), pack.get("exercises", [])
    dups("chapter", [c["id"] for c in ch]); dups("lesson", [l["id"] for l in ls])
    dups("vocabulary", [v["id"] for v in vs]); dups("grammar", [g["id"] for g in gs]); dups("exercise", [e["id"] for e in es])
    cids, lids = {c["id"] for c in ch}, {l["id"] for l in ls}
    vids, gids, ex_by = {v["id"] for v in vs}, {g["id"] for g in gs}, {e["id"]: e for e in es}
    cur = m.get("curriculum_id")

    for c in ch:
        blank(c["id"], "title_vi", c.get("title_vi"))
        for l in c.get("lesson_ids", []):
            if l not in lids: err(c["id"], f"chapter lists unknown lesson {l}")
    seen_v, seen_g = {}, {}
    for l in ls:
        i = l["id"]
        blank(i, "title_vi", l.get("title_vi"))
        if l.get("curriculum_id") != cur: err(i, f"curriculum_id {l.get('curriculum_id')} differs from manifest {cur} (tracks must not be mixed)")
        if l.get("chapter_id") not in cids: err(i, f"unknown chapter {l.get('chapter_id')}")
        elif i not in next(c for c in ch if c["id"] == l["chapter_id"]).get("lesson_ids", []): warn(i, f"chapter {l['chapter_id']} does not list this lesson")
        if not l.get("objectives"): err(i, "no objectives")
        if not 5 <= l.get("estimated_minutes", 0) <= 10: warn(i, f"estimated_minutes {l.get('estimated_minutes')} is outside 5-10")
        for v in l.get("vocabulary_ids", []):
            if v not in vids: err(i, f"unknown vocabulary id {v}")
            elif v in seen_v: err(i, f"vocabulary {v} already introduced in {seen_v[v]}")
            else: seen_v[v] = i
        for g in l.get("grammar_ids", []):
            if g not in gids: err(i, f"unknown grammar id {g}")
            elif g in seen_g: err(i, f"grammar {g} already introduced in {seen_g[g]}")
            else: seen_g[g] = i
        for e in l.get("exercise_ids", []):
            if e not in ex_by: err(i, f"unknown exercise id {e}")
            elif ex_by[e]["lesson_id"] != i: err(i, f"exercise {e} belongs to lesson {ex_by[e]['lesson_id']}")
        for p in l.get("prerequisite_ids", []):
            if p == i: err(i, "lesson is its own prerequisite")
            elif p not in lids: err(i, f"unknown prerequisite {p}")

    def hanzi_rule(i, label, text):
        if allowed is None: return
        for ch_ in sorted(set(CJK_ALL.findall(text or "")) - allowed):
            err(i, f"{label} uses character {ch_} that is not introduced yet")

    def check_review(i, status):
        if status not in STATUSES: err(i, f"bad review_status {status}")
        elif require_approved and status != "approved": err(i, f"review_status is {status}, approved required")

    for v in vs:
        i = v["id"]
        for f in ("hanzi", "pinyin", "meaning_vi", "source_ref", "license_ref"): blank(i, f, v.get(f))
        if v.get("curriculum_id") != cur: err(i, f"curriculum_id {v.get('curriculum_id')} differs from manifest {cur} (tracks must not be mixed)")
        if v.get("hanzi") and not CJK_WORD.match(v["hanzi"]): err(i, f"hanzi contains non-CJK characters: {v['hanzi']}")
        if v.get("pinyin") and not any(c in TONE for c in v["pinyin"]): warn(i, f"pinyin has no tone mark: {v['pinyin']}")
        check_review(i, v.get("review_status"))
        for x in v.get("examples", []):
            if not (x.get("hanzi") and x.get("pinyin") and x.get("meaning_vi")): err(i, "example with an empty field")
            hanzi_rule(i, "example", x.get("hanzi"))
    for g in gs:
        i = g["id"]
        for f in ("pattern", "explanation_vi", "source_ref"): blank(i, f, g.get(f))
        if g.get("curriculum_id") != cur: err(i, f"curriculum_id {g.get('curriculum_id')} differs from manifest {cur} (tracks must not be mixed)")
        check_review(i, g.get("review_status"))
        if not g.get("examples"): warn(i, "no examples")
        for x in g.get("examples", []):
            if not (x.get("hanzi") and x.get("pinyin") and x.get("meaning_vi")): err(i, "example with an empty field")
            hanzi_rule(i, "example", x.get("hanzi"))
    for e in es:
        i = e["id"]
        for f in ("prompt", "answer", "explanation_vi", "source_ref"): blank(i, f, e.get(f))
        if e.get("lesson_id") not in lids: err(i, f"unknown lesson {e.get('lesson_id')}")
        if e.get("type") not in TYPES: err(i, f"unknown exercise type {e.get('type')}")
        if not 1 <= e.get("difficulty", 0) <= 5: err(i, f"difficulty {e.get('difficulty')} outside 1-5")
        opts = e.get("options")
        if opts:
            if len(opts) < 2: err(i, "fewer than 2 options")
            if len(set(opts)) != len(opts): err(i, "duplicate options")
            if e["type"] in CHOICE and e["answer"] not in opts: err(i, "answer is not one of the options")
        elif e.get("type") in CHOICE:
            err(i, f"{e['type']} needs options")
        if e.get("type") == "sentence_ordering":
            toks = [t for t in e.get("answer", "").split(" ") if t]
            if not opts or sorted(opts) != sorted(toks): err(i, "sentence_ordering answer tokens must match the options")
        if e.get("type") == "matching":
            ps = [o.split("|") for o in opts or []]
            if not opts or len(opts) < 2 or len(opts) > 5: err(i, "matching needs 2-5 pairs")
            elif any(len(x) != 2 or not x[0].strip() or not x[1].strip() for x in ps): err(i, "matching option must be 'left|right'")
            else:
                if len({x[0].strip() for x in ps}) != len(ps) or len({x[1].strip() for x in ps}) != len(ps): err(i, "matching sides must be unique")
                ans = sorted(t.strip() for t in e.get("answer", "").split(";") if t.strip())
                if ans != sorted(o.strip() for o in opts): err(i, "matching answer pairs must equal the options")
        for label, text in (("prompt", e.get("prompt")), ("answer", e.get("answer")), ("explanation", e.get("explanation_vi"))):
            hanzi_rule(i, label, text)
        for o in opts or []: hanzi_rule(i, "option", o)
    return errors, warnings


if __name__ == "__main__":
    args = [a for a in sys.argv[1:] if not a.startswith("--")]
    if not args:
        print("usage: validate_pack.py PACK.json [--require-approved]"); sys.exit(2)
    pack = json.load(open(args[0], encoding="utf-8"))
    allowed = allowed_chars_up_to([l["id"] for l in pack.get("lessons", [])])
    if allowed is None: print("NOTE: course structure not found; skipping the introduced-characters rule")
    errs, warns = validate(pack, "--require-approved" in sys.argv, allowed)
    for w in warns: print(w)
    for e in errs: print(e)
    print(f"{args[0]}: {len(errs)} error(s), {len(warns)} warning(s)")
    sys.exit(1 if errs else 0)
