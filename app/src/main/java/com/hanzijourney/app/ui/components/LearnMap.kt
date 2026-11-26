package com.hanzijourney.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hanzijourney.app.ui.theme.Spacing

/** Zig-zag journey map. Reusable component; it is wired to real lessons once content exists. */
@Composable
fun LearnMap(nodes: List<LessonNodeUi>, onNodeClick: (String) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        itemsIndexed(nodes, key = { _, node -> node.id }) { index, node ->
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = if (index % 2 == 0) Alignment.CenterStart else Alignment.CenterEnd
            ) {
                LessonCard(node = node, onClick = { onNodeClick(node.id) }, modifier = Modifier.fillMaxWidth(0.85f))
            }
        }
    }
}
