package com.hanzijourney.app.domain.progress

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LevelRulesTest {
    private val rules = LevelRules(listOf(0, 40, 100))

    @Test fun startsAtLevelOne() {
        assertEquals(LevelInfo(1, 0, 40), rules.info(0))
        assertEquals(LevelInfo(1, 39, 40), rules.info(39))
        assertEquals(LevelInfo(1, 0, 40), rules.info(-5))
    }

    @Test fun levelsUpExactlyAtTheThreshold() {
        assertEquals(LevelInfo(2, 0, 60), rules.info(40))
        assertEquals(LevelInfo(2, 59, 60), rules.info(99))
        assertEquals(3, rules.info(100).level)
    }

    @Test fun highestLevelHasNoNext() {
        val info = rules.info(5_000)
        assertEquals(3, info.level)
        assertNull(info.xpForNextLevel)
    }

    @Test fun defaultTableIsValid() {
        assertEquals(1, LevelRules.Default.info(0).level)
        assertEquals(2, LevelRules.Default.info(40).level)
    }

    @Test(expected = IllegalArgumentException::class) fun rejectsBadTables() {
        LevelRules(listOf(0, 50, 50))
    }
}
