# CONTENT SCHEMA — HANZI JOURNEY

## Vocabulary
```json
{
  "id": "vocab_l1_0001",
  "curriculum_id": "hsk_3_0",
  "level_id": "hsk3_l1",
  "hanzi": "你好",
  "pinyin": "nǐ hǎo",
  "meaning_vi": "xin chào",
  "word_type": "phrase",
  "topic_id": "greetings",
  "examples": [
    {
      "hanzi": "你好！",
      "pinyin": "nǐ hǎo!",
      "meaning_vi": "Xin chào!"
    }
  ],
  "audio_asset_id": null,
  "source_ref": "cti_syllabus_2025_xxx",
  "license_ref": "original_editorial_content",
  "review_status": "approved"
}
```

## Lesson
```json
{
  "id": "hsk3_l1_lesson_001",
  "curriculum_id": "hsk_3_0",
  "level_id": "hsk3_l1",
  "chapter_id": "chapter_greetings",
  "title_vi": "Chào hỏi",
  "objectives": ["Nhận diện lời chào cơ bản"],
  "vocabulary_ids": ["vocab_l1_0001"],
  "grammar_ids": [],
  "exercise_ids": ["ex_l1_0001"],
  "estimated_minutes": 8,
  "prerequisite_ids": [],
  "content_version": "1.0.0"
}
```

## Exercise
```json
{
  "id": "ex_l1_0001",
  "lesson_id": "hsk3_l1_lesson_001",
  "type": "multiple_choice",
  "prompt": "你好 có nghĩa là gì?",
  "options": ["Xin chào", "Tạm biệt", "Cảm ơn"],
  "answer": "Xin chào",
  "explanation_vi": "你好 là lời chào thông dụng.",
  "difficulty": 1,
  "skill_tags": ["vocabulary", "reading"],
  "source_ref": "editorial_lesson_001"
}
```

## Validation rules
- IDs are unique and stable.
- Hanzi, Pinyin and Vietnamese meaning are non-empty.
- Pinyin tone marks are checked.
- HSK level assignment references the locked syllabus version.
- Every example has translation and a source/editorial record.
- Audio and stroke assets have explicit license records.
- Every exercise has a valid answer and explanation.
- No copyrighted textbook/audio/image is bundled without permission.
