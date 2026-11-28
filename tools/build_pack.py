#!/usr/bin/env python3
"""Build a content pack for ONE lesson from: syllabus working data (ids, hanzi, pinyin, source refs),
the course structure, and original authored content in content_authoring/<lesson>.json.
Usage: python3 tools/build_pack.py lesson_004     (writes content_work/packs/hsk3_l1_sample_lesson004.pack.json)"""
import csv, json, pathlib, sys
sys.path.insert(0, str(pathlib.Path(__file__).resolve().parent))
import validate_pack

ROOT = pathlib.Path(__file__).resolve().parent.parent
CW = ROOT / "content_work"
LICENSE_REF = "syllabus_scope:unverified_license; meaning_vi,examples:original_editorial"


def build(name):
    auth = json.load(open(ROOT / "content_authoring" / f"{name}.json", encoding="utf-8"))
    st = json.load(open(CW / "hsk3_l1_structure_working.json", encoding="utf-8"))
    vocab = {r["id"]: r for r in csv.DictReader(open(CW / "hsk3_l1_vocab_working.csv", encoding="utf-8"))}
    gram = {r["id"]: r for r in csv.DictReader(open(CW / "hsk3_l1_grammar_working.csv", encoding="utf-8"))}
    lesson = next(l for l in st["lessons"] if l["id"] == auth["lesson_id"])
    chapter = next(c for c in st["chapters"] if c["id"] == lesson["chapter_id"])
    checksum_file = ROOT / "docs" / "syllabus_checksums.txt"
    checksum = "PENDING_SYLLABUS_CHECKSUM"
    if checksum_file.exists():
        for line in checksum_file.read_text(encoding="utf-8").splitlines():
            if line.startswith("sha256:"): checksum = line.split(":", 1)[1].strip()

    vocabulary = []
    for vid in lesson["vocabulary_ids"]:
        row = vocab[vid]
        a = auth["words"][row["hanzi"]]
        vocabulary.append({"id": vid, "curriculum_id": "hsk_3_0", "level_id": "hsk3_l1", "hanzi": row["hanzi"],
            "pinyin": row["pinyin"], "meaning_vi": a["meaning_vi"], "word_type": a["word_type"], "topic_id": a["topic_id"],
            "examples": a["examples"], "audio_asset_id": None, "source_ref": row["source_ref"],
            "license_ref": LICENSE_REF, "review_status": "draft_unreviewed"})
    extra = set(auth["words"]) - {v["hanzi"] for v in vocabulary}
    if extra: raise SystemExit(f"authored words not in this lesson: {sorted(extra)}")

    grammar_points = []
    for gid in lesson["grammar_ids"]:
        a = auth["grammar"][gid]
        grammar_points.append({"id": gid, "curriculum_id": "hsk_3_0", "level_id": "hsk3_l1", "pattern": a["pattern"],
            "explanation_vi": a["explanation_vi"], "usage_note_vi": a.get("usage_note_vi"),
            "common_error_vi": a.get("common_error_vi"), "examples": a["examples"],
            "source_ref": gram[gid]["source_ref"], "review_status": "draft_unreviewed"})

    exercises = [dict(e, lesson_id=lesson["id"]) for e in auth["exercises"]]
    pack_lesson = dict(lesson, objectives=auth["objectives"], exercise_ids=[e["id"] for e in exercises],
                       prerequisite_ids=[], content_version="0.1.0-sample")  # sample subset: no earlier lessons in the pack
    pack = {"manifest": {"curriculum_id": "hsk_3_0", "version": "0.1.0-sample",
                         "source": "HSK 考试大纲 2025-11 (scope only); meanings, examples and exercises are original editorial drafts",
                         "retrieved_at": "2026-11-26", "checksum": checksum},
            "chapters": [{"id": chapter["id"], "title_vi": chapter["title_vi"], "lesson_ids": [lesson["id"]]}],
            "lessons": [pack_lesson], "vocabulary": vocabulary, "grammar_points": grammar_points, "exercises": exercises}
    return pack


if __name__ == "__main__":
    name = sys.argv[1] if len(sys.argv) > 1 else "lesson_004"
    pack = build(name)
    allowed = validate_pack.allowed_chars_up_to([l["id"] for l in pack["lessons"]])
    errs, warns = validate_pack.validate(pack, False, allowed)
    for w in warns: print(w)
    for e in errs: print(e)
    if errs: print(f"{len(errs)} error(s): pack NOT written"); sys.exit(1)
    out = CW / "packs" / f"hsk3_l1_sample_{name.replace('_', '')}.pack.json"
    out.write_text(json.dumps(pack, ensure_ascii=False, indent=1), encoding="utf-8")
    print(f"OK: {out.relative_to(ROOT)}: {len(pack['vocabulary'])} words, {len(pack['grammar_points'])} grammar, {len(pack['exercises'])} exercises, {len(warns)} warning(s)")
