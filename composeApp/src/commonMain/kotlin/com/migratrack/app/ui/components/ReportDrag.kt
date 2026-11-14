package com.migratrack.app.ui.components

import androidx.compose.ui.Modifier
import com.migratrack.export.ExportFile

/**
 * Lets the user drag the report into another app, for example into Mail beside MigraTrack in Split View.
 * [makeFile] runs only when the other app asks for the file.
 */
expect fun Modifier.reportDragSource(makeFile: suspend () -> ExportFile): Modifier
