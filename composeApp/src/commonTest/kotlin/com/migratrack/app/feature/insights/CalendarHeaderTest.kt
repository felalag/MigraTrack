package com.migratrack.app.feature.insights

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.migratrack.app.ui.theme.MigraTrackTheme
import com.migratrack.domain.analytics.CalendarBuilder
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class CalendarHeaderTest {
    @Test
    fun theMonthFitsTheOuterDisplayOfIPhoneDuo() = runComposeUiTest {
        val empty = CalendarBuilder.month(
            episodes = emptyList(),
            month = YearMonth(2026, Month.NOVEMBER),
            today = LocalDate(2026, 11, 14),
            timeZone = TimeZone.UTC,
            trackedSince = LocalDate(2026, 1, 1),
            firstDayOfWeek = DayOfWeek.SUNDAY,
        )
        val attacks = setOf(11, 12, 13)
        val calendar = empty.copy(days = empty.days.map { if (it.inMonth && it.date.day in attacks) it.copy(attacks = 1) else it })

        setContent {
            // The cut showed at the largest text scale that the theme allows.
            CompositionLocalProvider(LocalDensity provides Density(LocalDensity.current.density, fontScale = 1.35f)) {
                MigraTrackTheme {
                    // On the outer display the content column is 382 pt and the screen margins are 20 pt.
                    Box(Modifier.requiredWidth(342.dp)) {
                        CalendarCard(
                            calendar = calendar,
                            selected = null,
                            day = null,
                            timeZone = TimeZone.UTC,
                            canShowNextMonth = false,
                            onShowMonth = {},
                            onPickMonth = {},
                            onSelect = {},
                            onEdit = {},
                            onDelete = {},
                            onLog = {},
                        )
                    }
                }
            }
        }

        val node = onNodeWithText("November 2026", useUnmergedTree = true).fetchSemanticsNode()
        val layouts = mutableListOf<TextLayoutResult>()
        node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(layouts)
        val layout = layouts.single()
        // Compose on iOS does not report the ellipsis, so the test compares the width the text needs with the width it got.
        val needed = layout.multiParagraph.intrinsics.maxIntrinsicWidth
        assertTrue(needed <= layout.size.width + 1, "The month title needs $needed px and got ${layout.size.width} px")
    }
}
