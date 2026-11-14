package com.migratrack.app.platform

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.changedToDown
import androidx.compose.ui.input.pointer.pointerInput

/** Where the user last touched the Compose UI, in points. Popovers point at it, because the dialog service gets no anchor. */
internal object LastTouch {
    var point: Offset? = null
}

internal fun Modifier.recordTouches(): Modifier = pointerInput(Unit) {
    awaitPointerEventScope {
        while (true) {
            val event = awaitPointerEvent(PointerEventPass.Initial)
            event.changes.firstOrNull { it.changedToDown() }?.let { LastTouch.point = it.position / density }
        }
    }
}
