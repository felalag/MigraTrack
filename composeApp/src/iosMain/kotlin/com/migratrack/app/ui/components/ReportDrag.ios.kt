package com.migratrack.app.ui.components

import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import com.migratrack.app.platform.writeExport
import com.migratrack.export.ExportFile
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import platform.Foundation.NSError
import platform.Foundation.NSItemProvider
import platform.Foundation.NSItemProviderRepresentationVisibilityAll
import platform.Foundation.NSLocalizedDescriptionKey
import platform.UIKit.UIDragItem
import platform.UniformTypeIdentifiers.UTTypePDF

private val dragScope = MainScope()

@OptIn(ExperimentalComposeUiApi::class)
actual fun Modifier.reportDragSource(makeFile: suspend () -> ExportFile): Modifier = dragAndDropSource { _ ->
    val provider = NSItemProvider()
    provider.registerFileRepresentationForTypeIdentifier(
        typeIdentifier = UTTypePDF.identifier,
        fileOptions = 0L,
        visibility = NSItemProviderRepresentationVisibilityAll,
    ) { completion ->
        dragScope.launch {
            try {
                completion?.invoke(writeExport(makeFile()), false, null)
            } catch (failure: Exception) {
                val info = mapOf<Any?, Any?>(NSLocalizedDescriptionKey to failure.message.orEmpty())
                completion?.invoke(null, false, NSError.errorWithDomain("MigraTrack", 1, info))
            }
        }
        null
    }
    DragAndDropTransferData(items = listOf(UIDragItem(itemProvider = provider)))
}
