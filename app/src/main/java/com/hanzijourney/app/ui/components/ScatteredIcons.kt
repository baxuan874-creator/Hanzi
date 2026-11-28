package com.hanzijourney.app.ui.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.sp
import kotlin.random.Random

private val Glyphs = listOf("🏮", "🐉", "🎋", "🍵", "🥟", "🌸", "🐼", "⭐", "🍜", "📖", "🎐", "🌙", "🐲", "🍡", "🏯", "📚")

private data class Scatter(val glyph: String, val x: Float, val y: Float, val sizeSp: Float, val rotation: Float)

private fun buildScatter(seed: Int): List<Scatter> {
    val random = Random(seed)
    val columns = 4
    val rows = 8
    val items = mutableListOf<Scatter>()
    for (row in 0 until rows) {
        for (column in 0 until columns) {
            if (random.nextFloat() > 0.2f) continue
            items += Scatter(
                glyph = Glyphs[random.nextInt(Glyphs.size)],
                x = (column + 0.1f + random.nextFloat() * 0.5f) / columns,
                y = (row + 0.1f + random.nextFloat() * 0.5f) / rows,
                sizeSp = 22f + random.nextFloat() * 10f,
                rotation = -25f + random.nextFloat() * 50f
            )
        }
    }
    return items
}

/**
 * Faint decorative emoji scattered over the background so screens do not look empty.
 * Purely decorative: hidden from screen readers, never carries information, static (no motion).
 * Uses the system emoji font, so no image files or licences are involved.
 */
@Composable
fun ScatteredIcons(seed: Int, modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier = modifier.fillMaxSize().clearAndSetSemantics { }) {
        val items = remember(seed) { buildScatter(seed) }
        val width = maxWidth
        val height = maxHeight
        items.forEach { item ->
            Text(
                text = item.glyph,
                fontSize = item.sizeSp.sp,
                modifier = Modifier
                    .offset(x = width * item.x, y = height * item.y)
                    .rotate(item.rotation)
                    .alpha(0.12f)
            )
        }
    }
}
