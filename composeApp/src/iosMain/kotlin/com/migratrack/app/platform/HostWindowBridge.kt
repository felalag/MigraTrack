package com.migratrack.app.platform

import androidx.compose.ui.unit.dp
import com.migratrack.app.ui.components.HostWindow
import com.migratrack.app.ui.components.VerticalBand
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private val window = MutableStateFlow(HostWindow())

internal val hostWindow: StateFlow<HostWindow> = window

/**
 * Called by ComposeHostController in Swift after each layout pass and each hinge change.
 * The bands are points in window coordinates; a band whose right edge is not after its left edge is absent.
 */
fun updateHostWindow(regularWidth: Boolean, statusBarLeft: Double, statusBarRight: Double, foldLeft: Double, foldRight: Double) {
    window.value = HostWindow(regularWidth, statusBarColumn = band(statusBarLeft, statusBarRight), fold = band(foldLeft, foldRight))
}

private fun band(left: Double, right: Double): VerticalBand? = if (right > left) VerticalBand(left.dp, right.dp) else null
