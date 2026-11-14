package com.migratrack.app.ui.components

import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class FoldLayoutTest {
    // The open iPhone Duo: the fold band of the simulator, 455 to 496 pt, in a 951 pt window.
    private val fold = VerticalBand(455.dp, 496.dp)

    @Test
    fun leftToRightPutsTheStartPaneLeftOfTheFold() {
        assertEquals(FoldSplit(startWidth = 435.dp, gap = 41.dp), foldSplit(fold, rowLeft = 20.dp, rowWidth = 827.dp, ltr = true))
    }

    @Test
    fun rightToLeftPutsTheStartPaneRightOfTheFold() {
        assertEquals(FoldSplit(startWidth = 351.dp, gap = 41.dp), foldSplit(fold, rowLeft = 20.dp, rowWidth = 827.dp, ltr = false))
    }

    @Test
    fun aFoldNearTheEdgeSplitsEvenly() {
        assertNull(foldSplit(fold, rowLeft = 300.dp, rowWidth = 600.dp, ltr = true))
        assertNull(foldSplit(null, rowLeft = 20.dp, rowWidth = 827.dp, ltr = true))
    }

    @Test
    fun aSingleColumnMovesToTheWiderSide() {
        assertEquals(SidePadding(0.dp, 412.dp), foldPadding(fold, pageLeft = 0.dp, pageWidth = 867.dp))
        assertEquals(SidePadding(196.dp, 0.dp), foldPadding(fold, pageLeft = 300.dp, pageWidth = 651.dp))
    }

    @Test
    fun aFoldOutsideThePageChangesNothing() {
        assertNull(foldPadding(fold, pageLeft = 500.dp, pageWidth = 451.dp))
        assertNull(foldPadding(null, pageLeft = 0.dp, pageWidth = 867.dp))
    }
}
