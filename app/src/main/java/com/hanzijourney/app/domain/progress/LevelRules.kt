package com.hanzijourney.app.domain.progress

/**
 * Level table: thresholds[i] is the total XP needed to reach level i + 1.
 * Kept in the domain (not in the UI) so it can be changed in one place.
 */
class LevelRules(private val thresholds: List<Int>) {
    init {
        require(thresholds.isNotEmpty() && thresholds.first() == 0) { "first threshold must be 0" }
        require(thresholds.zipWithNext().all { (a, b) -> a < b }) { "thresholds must increase" }
    }

    fun info(totalXp: Int): LevelInfo {
        val xp = totalXp.coerceAtLeast(0)
        val index = thresholds.indexOfLast { it <= xp }
        val next = thresholds.getOrNull(index + 1)
        return LevelInfo(
            level = index + 1,
            xpIntoLevel = xp - thresholds[index],
            xpForNextLevel = next?.let { it - thresholds[index] }
        )
    }

    companion object {
        val Default = LevelRules(listOf(0, 40, 100, 180, 280, 400, 540, 700, 880, 1080))
    }
}
