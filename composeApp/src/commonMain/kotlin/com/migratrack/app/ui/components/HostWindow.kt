package com.migratrack.app.ui.components

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp

/** A band from [left] to [right] across the whole height of the window, in root coordinates. */
@Immutable
data class VerticalBand(val left: Dp, val right: Dp) {
    val width: Dp get() = right - left
}

/**
 * What UIKit reports about the window that Compose cannot see.
 * [regularWidth] is the horizontal size class, which decides between one and two columns.
 * [statusBarColumn] is set when the status bar is a column on the right edge, as on both displays of iPhone Duo.
 * [fold] is set while iPhone Duo is partly folded and the fold crosses the window from top to bottom.
 */
@Immutable
data class HostWindow(
    val regularWidth: Boolean = false,
    val statusBarColumn: VerticalBand? = null,
    val fold: VerticalBand? = null,
)

val LocalHostWindow = staticCompositionLocalOf { HostWindow() }
