# HANDOFF — HANZI JOURNEY

Date: 2026-11-26
Project version: 0.0.1-phase0

## Current phase and task
- Phase 0, Task 1: khung dự án + quy trình build trên GitHub Actions.

## Completed
- Gradle (Kotlin DSL), AGP 8.5.2, Kotlin 2.0.20, Compose BOM 2024.09.00, minSdk 26, compileSdk 34.
- App shell: MainActivity + theme skeleton (Light/Dark) + chuỗi tiếng Việt + icon gốc.
- Workflow .github/workflows/build-apk.yml: build debug APK, upload artifact, đăng lên release "debug-latest".
- Docs đã chép vào docs/.

## Files created
- settings.gradle.kts, build.gradle.kts, gradle.properties, .gitignore
- app/build.gradle.kts, app/proguard-rules.pro, app/src/main/AndroidManifest.xml
- app/src/main/java/com/hanzijourney/app/{MainActivity.kt, ui/theme/Color.kt, ui/theme/Theme.kt}
- app/src/main/res/{values/*, drawable/ic_launcher_foreground.xml, mipmap-anydpi-v26/ic_launcher.xml}
- .github/workflows/build-apk.yml, CHANGELOG.md, HANDOFF.md

## Build and tests actually run
- Build: KHÔNG chạy (môi trường tạo file không có Internet/Android SDK). Build đầu tiên sẽ chạy trên GitHub Actions.
- Tests: chưa có.

## Known issues
- Chưa xác nhận build thành công; có thể cần sửa phiên bản plugin/Gradle sau khi xem log Actions.
- Chưa có gradle wrapper (workflow dùng Gradle 8.9 cài sẵn qua setup-gradle).

## Decisions that must remain unchanged
- Android Native / Kotlin / Compose; Vietnamese-first; gamified UI; HSK 3.0 track; không AI/backend/API trả phí/login/ads/Premium; offline-first.

## Exact next task
- Xác nhận build Actions xanh và cài APK lên máy. Sau đó: Phase 0, Task 2 — thêm Room + DataStore (database rỗng + UserSettings) và package structure.

## Context for next Claude session
Read CLAUDE.md, docs/PRD.md, docs/PHASES.md and this file before changing code.
