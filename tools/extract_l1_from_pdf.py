#!/usr/bin/env python3
"""Phase 2 Task 3: extract HSK 3.0 Level 1 recognition characters (expected 246) and
dump the Level 1 grammar pages (expected 70 points) from the official syllabus PDF.

Run in Codespace after tools/fetch_syllabus.sh:
    python3 tools/extract_l1_from_pdf.py
Pages are located by PRINTED page numbers from the PDF's table of contents:
    vocabulary L1 starts p.77 (used to find the offset), characters L1 p.353-354,
    grammar L1 p.383-385 (L2 starts p.386). Nothing is auto-corrected."""
import csv, pathlib, re, subprocess, sys

ROOT = pathlib.Path(__file__).resolve().parent.parent
PDF = ROOT / "data_source" / "HSK_syllabus_2025-11_1219.pdf"
VOCAB_CSV = ROOT / "content_work" / "hsk3_l1_vocab_working.csv"
CHARS_CSV = ROOT / "content_work" / "hsk3_l1_chars_working.csv"
RAW_GRAMMAR = ROOT / "data_source" / "l1_grammar_raw.txt"
RAW_CHARS = ROOT / "data_source" / "l1_chars_raw.txt"
NOISE = {"汉考国际", "际"}            # watermark tokens that appear on every page
EXPECTED_CHARS = 246
CJK1 = re.compile(r"^[\u4e00-\u9fff]$")


def clean_text(text):
    """Drop watermark tokens; keep line structure."""
    lines = []
    for line in text.splitlines():
        toks = [t for t in line.split() if t not in NOISE]
        if toks:
            lines.append(" ".join(toks))
    return "\n".join(lines)


def find_offset(pages, printed=77):
    """PDF index minus printed page number, found via the first vocabulary page."""
    for i, t in enumerate(pages):
        if "包子" in t and "bāozi" in t and "杯子" in t:
            return i - printed
    return None


def pages_for(pages, offset, first, last):
    return [pages[i + offset] for i in range(first, last + 1)]


def numbered_chars(texts):
    """Real layout (seen in l1_chars_raw.txt): one entry per line, '12. 边'. Returns [(n, char)]."""
    out = []
    for t in texts:
        for line in t.splitlines():
            m = re.match(r"^(\d+)\.\s*([\u4e00-\u9fff])\s*$", line.strip())
            if m: out.append((int(m.group(1)), m.group(2)))
    return out


def single_char_tokens(texts):
    out = []
    for t in texts:
        for tok in t.split():
            if CJK1.match(tok) and tok not in out:
                out.append(tok)
    return out


def load_pages():
    try:
        from pypdf import PdfReader
    except ImportError:
        subprocess.check_call([sys.executable, "-m", "pip", "install", "--quiet", "pypdf"])
        from pypdf import PdfReader
    reader = PdfReader(str(PDF))
    return [clean_text(p.extract_text() or "") for p in reader.pages]


def main():
    if not PDF.exists():
        print(f"PDF not found: {PDF}\nRun: bash tools/fetch_syllabus.sh"); return 1
    pages = load_pages()
    print(f"PDF pages read: {len(pages)}")
    off = find_offset(pages)
    if off is None:
        print("Could not find the Level 1 vocabulary page; cannot compute page offset."); return 1
    print(f"Page offset (pdf index - printed page): {off}")
    (ROOT / "data_source").mkdir(exist_ok=True)

    # --- characters ---
    ctexts = pages_for(pages, off, 353, 354)
    RAW_CHARS.write_text("\n\n=====PAGE=====\n".join(ctexts), encoding="utf-8")
    numbered = numbered_chars(ctexts)
    chars = [c for _, c in numbered] if numbered else single_char_tokens(ctexts)
    if numbered and [n for n, _ in numbered] != list(range(1, len(numbered) + 1)):
        print("WARNING: numbering is not contiguous from 1"); chars = []
    print(f"Single-character tokens on printed pages 353-354: {len(chars)} (expected {EXPECTED_CHARS})")
    cand = []
    if VOCAB_CSV.exists():
        for r in csv.DictReader(open(VOCAB_CSV, encoding="utf-8")):
            for ch in r["hanzi"]:
                if ch not in cand: cand.append(ch)
        print(f"Characters appearing in the 300 words: {len(cand)}")
        print("In PDF list but not in words :", "".join(c for c in chars if c not in cand) or "-")
        print("In words but not in PDF list :", "".join(c for c in cand if c not in chars) or "-")
    if len(chars) == EXPECTED_CHARS:
        with open(CHARS_CSV, "w", newline="", encoding="utf-8") as f:
            w = csv.writer(f); w.writerow(["id", "syllabus_seq", "hanzi", "curriculum_id", "level_id", "source_ref", "review_status", "flags"])
            for i, ch in enumerate(chars, 1):
                w.writerow([f"char_l1_{i:04d}", i, ch, "hsk_3_0", "hsk3_l1",
                            f"hsk_3_0_2025_11/pdf1219/recognition_chars#{i}", "extracted_unreviewed", ""])
        print(f"WROTE {CHARS_CSV.relative_to(ROOT)}")
        rc = 0
    else:
        print(f"Count mismatch: NOT writing the CSV. Raw text saved to {RAW_CHARS.relative_to(ROOT)}")
        rc = 2

    # --- grammar (raw dump only; parsing is done after a human/Claude looks at the layout) ---
    gtexts = pages_for(pages, off, 383, 385)
    RAW_GRAMMAR.write_text("\n\n=====PAGE=====\n".join(gtexts), encoding="utf-8")
    n_lines = sum(len(t.splitlines()) for t in gtexts)
    print(f"Grammar pages 383-385 dumped: {n_lines} lines -> {RAW_GRAMMAR.relative_to(ROOT)}")
    print("Next: download data_source/l1_grammar_raw.txt (and l1_chars_raw.txt if count mismatched) and send it to Claude.")
    return rc


if __name__ == "__main__":
    sys.exit(main())
