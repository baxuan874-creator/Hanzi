# PROMPT TEMPLATES — HANZI JOURNEY / CLAUDE FREE

Use one focused task at a time. Do not paste every document if the current task only needs a subset; always include CLAUDE.md and the relevant specification.

## 1. Start a new task

You are continuing the Hanzi Journey Android project.

Read:
- CLAUDE.md
- PRD.md
- PHASES.md
- HANDOFF.md
- The relevant technical specification

Current phase: [PHASE]
Current task: [TASK NAME]

Requirements:
[PASTE TASK REQUIREMENTS]

Acceptance criteria:
[PASTE TESTABLE ACCEPTANCE CRITERIA]

Before editing:
1. Inspect the current project and relevant files.
2. Identify the files likely to change.
3. Explain the implementation plan and any risks.
4. Ask for clarification only if a requirement is genuinely ambiguous or conflicts with a fixed product decision.

Implementation rules:
- Implement only this task.
- Preserve existing working functionality.
- Follow the existing architecture and design system.
- Do not add AI, server, login, paid API, ads or Premium.
- Do not use placeholder behavior for a required feature.
- Do not invent HSK data or copy proprietary learning content.
- Add tests for business logic where practical.

At the end:
1. List files created, modified and deleted.
2. Summarize behavior implemented.
3. List tests actually run and their results.
4. Explicitly list tests not run.
5. List known issues and remaining work.
6. Update HANDOFF.md and CHANGELOG.md.
7. State the exact next task. Do not start it.

## 2. Content authoring task

You are acting as a Chinese-language curriculum editor and educational content engineer.

Curriculum track: [EXACT HSK VERSION]
Level: [LEVEL]
Unit/topic: [TOPIC]
Source syllabus version/date: [SOURCE VERSION]
Source mapping: [PAGE/SECTION/ENTRY REFERENCES]

Create original Vietnamese-language learning content using the source syllabus as the scope authority.

For every vocabulary item include:
- stable ID
- Simplified Chinese
- Pinyin with tone marks
- Vietnamese meaning
- part of speech / item type
- topic and level mapping
- original example sentence
- Pinyin for the example
- Vietnamese translation
- source reference
- review status

For every grammar point include:
- stable ID
- form/pattern
- Vietnamese explanation
- usage constraints
- original examples
- common learner error
- source reference
- review status

For every exercise include:
- stable ID
- exercise type
- prompt
- answer/options schema
- correct answer
- short Vietnamese explanation
- difficulty
- skill tags
- source reference

Quality rules:
- Do not invent syllabus membership.
- Do not mix HSK 2.0 and HSK 3.0.
- Do not copy textbook passages, proprietary audio, or app exercises.
- Mark uncertain entries for human review; never silently guess.
- Check that examples are natural, grammatical and appropriate to the learner level.
- Output machine-valid JSON and a concise editorial QA report.

## 3. Content validation task

Inspect the content pack and validate:
- JSON syntax and schema.
- Duplicate IDs.
- Missing Hanzi/Pinyin/meaning.
- Pinyin tone-mark presence and suspicious readings.
- Broken references between lessons, vocabulary, grammar and exercises.
- Invalid answer keys.
- Missing Vietnamese explanations.
- Missing curriculum version/source mapping.
- Missing asset license records.
- Content assigned to the wrong HSK track or level.

Do not silently auto-correct uncertain linguistic data. Produce a report with file, record ID, issue, severity and recommended human action.

## 4. Bug-fix task

Bug:
[DESCRIBE BUG]

Reproduction steps:
[STEPS]

Expected:
[EXPECTED RESULT]

Actual:
[ACTUAL RESULT]

Inspect before changing code. Find the smallest safe fix. Add a regression test if possible. Do not refactor unrelated modules. Report the root cause, changed files, tests run and remaining risks.

## 5. Session handoff

Do not implement new features. Inspect current files and update HANDOFF.md with:
- current phase/task
- completed work
- files changed
- build/test commands actually run
- test results
- known bugs
- incomplete work
- architecture decisions
- exact next task
- files the next Claude session must read

Do not claim a build or test passed unless it was actually executed.
