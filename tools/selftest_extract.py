#!/usr/bin/env python3
"""Offline tests for the pure functions in extract_l1_from_pdf.py (synthetic pages, no PDF)."""
import importlib.util, pathlib
spec = importlib.util.spec_from_file_location("ex", pathlib.Path(__file__).with_name("extract_l1_from_pdf.py"))
ex = importlib.util.module_from_spec(spec); spec.loader.exec_module(ex)

noisy = "汉考国际 汉考国际 汉考国际 \n际 汉考国际 汉考国际 \n爱 八\n汉考国际 汉考国际 353"
assert ex.clean_text(noisy) == "爱 八\n353", repr(ex.clean_text(noisy))

pages = ["cover", "toc 国际", "x"] + [f"filler{i}" for i in range(3, 81)]
pages[80] = "序号 等级 词语 拼音 词性 8 1 包子 bāozi 名 9 1 杯子 bēizi 名"      # printed 77 at index 80
assert ex.find_offset(pages) == 3
assert ex.find_offset(["nothing"]) is None

pages += [f"p{i}" for i in range(len(pages), 360 + 3)]
pages[353 + 3] = "1 爱 2 八 3 爸 认读字"
pages[354 + 3] = "4 吧 5 白 6 天"
got = ex.single_char_tokens(ex.pages_for(pages, 3, 353, 354))
assert got == ["爱", "八", "爸", "吧", "白", "天"], got      # '认读字' (3 chars) is not a single-char token
assert ex.single_char_tokens(["爱 爱 八"]) == ["爱", "八"]    # de-duplicates
assert ex.numbered_chars(["HSK（一级）认读字\n1. 爱\n2. 八\n353", "354\n3. 爸"]) == [(1, "爱"), (2, "八"), (3, "爸")]
print("selftest OK")
