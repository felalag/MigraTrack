package com.migratrack.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.migratrack.app.ui.components.HostWindow
import com.migratrack.app.ui.components.LocalHostWindow
import com.migratrack.app.ui.components.Tab
import com.migratrack.app.ui.components.TabBarHeight
import com.migratrack.app.ui.components.VerticalBand
import com.migratrack.app.ui.theme.MigraTrackTheme
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class TabFrameTest {
    private val titles = listOf("Summary", "Log", "Insights", "Export")

    @Test
    fun theRailStaysUnderTheStatusBarInRightToLeftLanguages() = runComposeUiTest {
        // The open iPhone Duo in landscape: the status bar is a column from 867 pt to the right edge.
        showFrame(951.dp, 669.dp, VerticalBand(867.dp, 951.dp), LayoutDirection.Rtl)
        titles.forEach { title -> assertTrue(tabBounds(title).left >= 867.dp, "$title is at ${tabBounds(title)}") }
    }

    @Test
    fun theOuterDisplayShowsTheRailUnderTheStatusBar() = runComposeUiTest {
        showFrame(466.dp, 678.dp, VerticalBand(382.dp, 466.dp), LayoutDirection.Ltr)
        titles.forEach { title -> assertTrue(tabBounds(title).left >= 382.dp, "$title is at ${tabBounds(title)}") }
    }

    @Test
    fun anIPhoneInLandscapeKeepsTheBarAtTheBottom() = runComposeUiTest {
        // iPhone 17 in landscape: wider than tall, but the status bar is hidden and the Dynamic Island is at the side.
        showFrame(874.dp, 402.dp, statusBarColumn = null, LayoutDirection.Ltr)
        titles.forEach { title -> assertTrue(tabBounds(title).top >= 402.dp - TabBarHeight, "$title is at ${tabBounds(title)}") }
    }

    private fun ComposeUiTest.showFrame(width: Dp, height: Dp, statusBarColumn: VerticalBand?, direction: LayoutDirection) {
        setContent {
            MigraTrackTheme {
                CompositionLocalProvider(
                    LocalHostWindow provides HostWindow(statusBarColumn = statusBarColumn),
                    LocalLayoutDirection provides direction,
                ) {
                    Box(Modifier.requiredSize(width, height).testTag(FRAME)) {
                        TabFrame(selected = Tab.Summary, onSelect = {}, tabBarVisible = true, banner = null) {}
                    }
                }
            }
        }
        waitForIdle()
    }

    /** Bounds of the tab item, relative to the frame. */
    private fun ComposeUiTest.tabBounds(title: String): DpRect {
        val frame = onNodeWithTag(FRAME).getBoundsInRoot()
        val item = onNodeWithText(title).getBoundsInRoot()
        return DpRect(item.left - frame.left, item.top - frame.top, item.right - frame.left, item.bottom - frame.top)
    }

    private companion object {
        const val FRAME = "frame"
    }
}
