package com.migratrack.app.ui.components

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity

/**
 * The Large Content Viewer of iOS: at accessibility text sizes, holding a small control shows its title enlarged.
 * System tab bars have it; the custom tab bar registers its items here instead.
 */
interface LargeContentViewer {
    /** [bounds] are in points, in root coordinates. [symbol] names an SF Symbol for the enlarged view. */
    fun update(owner: Any, bounds: Rect, title: String, symbol: String?, onSelect: () -> Unit)

    fun remove(owner: Any)
}

val LocalLargeContentViewer = staticCompositionLocalOf<LargeContentViewer?> { null }

fun Modifier.largeContentItem(title: String, symbol: String?, onSelect: () -> Unit): Modifier = composed {
    val viewer = LocalLargeContentViewer.current ?: return@composed Modifier
    val density = LocalDensity.current
    val owner = remember { Any() }
    val currentSelect by rememberUpdatedState(onSelect)
    DisposableEffect(viewer) { onDispose { viewer.remove(owner) } }
    Modifier.onGloballyPositioned { coordinates ->
        val bounds = coordinates.boundsInRoot()
        val points = Rect(bounds.left / density.density, bounds.top / density.density, bounds.right / density.density, bounds.bottom / density.density)
        viewer.update(owner, points, title, symbol) { currentSelect() }
    }
}
