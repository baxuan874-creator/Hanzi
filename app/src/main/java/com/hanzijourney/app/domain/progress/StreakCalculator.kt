package com.hanzijourney.app.domain.progress

import java.time.LocalDate

/**
 * Streak = consecutive local days with some practice. It stays alive until the end of the day after the last
 * practice day. Losing a streak only resets the counter: no learning progress is ever removed (PRD section 7).
 */
object StreakCalculator {
    fun compute(practiceDays: Set<LocalDate>, today: LocalDate): Streak {
        val days = practiceDays.filter { !it.isAfter(today) }.toSet()
        if (days.isEmpty()) return Streak(current = 0, best = 0, practicedToday = false)

        val sorted = days.sorted()
        var best = 1
        var run = 1
        for (i in 1 until sorted.size) {
            run = if (sorted[i - 1].plusDays(1) == sorted[i]) run + 1 else 1
            if (run > best) best = run
        }

        val practicedToday = today in days
        var cursor = if (practicedToday) today else today.minusDays(1)
        var current = 0
        while (cursor in days) {
            current++
            cursor = cursor.minusDays(1)
        }
        return Streak(current = current, best = best, practicedToday = practicedToday)
    }
}
