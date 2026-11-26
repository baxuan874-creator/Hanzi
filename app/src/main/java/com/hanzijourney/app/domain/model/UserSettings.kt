package com.hanzijourney.app.domain.model

enum class DarkModeSetting { SYSTEM, LIGHT, DARK }

data class UserSettings(
    val soundEnabled: Boolean = true,
    val reduceMotion: Boolean = false,
    val darkMode: DarkModeSetting = DarkModeSetting.SYSTEM
)
