package com.hanzijourney.app.ui.lesson

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hanzijourney.app.ui.theme.Spacing

/**
 * Fills the space above the bottom button and centres its content vertically when it is short
 * (so screens are not empty at the bottom); scrolls when it is long.
 */
@Composable
internal fun ColumnScope.CenteredScroll(content: @Composable ColumnScope.() -> Unit) {
    BoxWithConstraints(modifier = Modifier.weight(1f).fillMaxWidth()) {
        val minHeight = maxHeight
        Column(
            modifier = Modifier.fillMaxWidth().heightIn(min = minHeight).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Spacing.md, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = content
        )
    }
}
