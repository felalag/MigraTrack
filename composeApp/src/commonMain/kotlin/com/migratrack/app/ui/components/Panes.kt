package com.migratrack.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentWithReceiverOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.migratrack.app.ui.theme.MtSpacing

/** A [Page] of regular width at least this wide shows its [Panes] side by side: the inner display of iPhone Duo in landscape. */
val TwoPaneMinWidth = 700.dp

/** Widest a two-pane column grows, side margins included. */
val MaxTwoPaneWidth = 1000.dp

/** Narrowest pane that the fold may leave; a fold nearer to the edge splits the panes evenly. */
private val MinPaneWidth = 240.dp

val LocalTwoPanes = compositionLocalOf { false }

/**
 * Two groups of blocks: one after the other in a narrow [Page], side by side in a wide one.
 * The blocks move between the layouts without losing their state, so folding iPhone Duo keeps what the user entered.
 * While the device is partly folded, the gap between the panes lies on the fold.
 */
@Composable
fun ColumnScope.Panes(
    start: @Composable ColumnScope.() -> Unit,
    end: @Composable ColumnScope.() -> Unit,
    spacing: Dp = MtSpacing.lg,
) {
    val currentStart by rememberUpdatedState(start)
    val currentEnd by rememberUpdatedState(end)
    val startPane = remember { movableContentWithReceiverOf<ColumnScope> { currentStart() } }
    val endPane = remember { movableContentWithReceiverOf<ColumnScope> { currentEnd() } }

    if (!LocalTwoPanes.current) {
        startPane()
        endPane()
        return
    }

    val density = LocalDensity.current
    val fold = LocalHostWindow.current.fold
    val ltr = LocalLayoutDirection.current == LayoutDirection.Ltr
    var rowLeft by remember { mutableStateOf<Dp?>(null) }
    var rowWidth by remember { mutableStateOf(0.dp) }
    val split = rowLeft?.let { foldSplit(fold, it, rowWidth, ltr) }
    val startWidth = split?.startWidth
    val gapWidth = split?.gap ?: MtSpacing.lg

    Row(
        modifier = Modifier.fillMaxWidth().onPlaced { coordinates ->
            with(density) {
                rowLeft = coordinates.positionInRoot().x.toDp()
                rowWidth = coordinates.size.width.toDp()
            }
        },
        verticalAlignment = Alignment.Top,
    ) {
        val startModifier = if (startWidth != null) Modifier.width(startWidth) else Modifier.weight(1f)
        Column(startModifier, verticalArrangement = Arrangement.spacedBy(spacing)) { startPane() }
        Spacer(Modifier.width(gapWidth))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(spacing)) { endPane() }
    }
}

internal data class FoldSplit(val startWidth: Dp, val gap: Dp)

/**
 * The start pane width and the gap that put the gap on [fold], for a row at [rowLeft] in root coordinates.
 * The fold is physical, so in a right-to-left row the start pane is the one right of it.
 * Null when there is no fold, or when it would leave a pane narrower than [MinPaneWidth].
 */
internal fun foldSplit(fold: VerticalBand?, rowLeft: Dp, rowWidth: Dp, ltr: Boolean): FoldSplit? {
    if (fold == null) return null
    val leftWidth = fold.left - rowLeft
    val rightWidth = rowWidth - leftWidth - fold.width
    if (leftWidth < MinPaneWidth || rightWidth < MinPaneWidth) return null
    return FoldSplit(startWidth = if (ltr) leftWidth else rightWidth, gap = fold.width)
}
