package com.migratrack.app.ui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.migratrack.app.generated.resources.Res
import com.migratrack.app.generated.resources.common_back
import com.migratrack.app.generated.resources.common_settings
import com.migratrack.app.generated.resources.tab_export
import com.migratrack.app.generated.resources.tab_insights
import com.migratrack.app.generated.resources.tab_log
import com.migratrack.app.generated.resources.tab_summary
import com.migratrack.app.ui.icons.AppIcons
import com.migratrack.app.ui.icons.Icon
import com.migratrack.app.ui.icons.Symbols
import com.migratrack.app.ui.stringResource
import com.migratrack.app.ui.theme.MtColors
import com.migratrack.app.ui.theme.MtSpacing
import com.migratrack.app.ui.theme.MtTheme
import com.migratrack.app.ui.theme.WithoutFontScaling
import org.jetbrains.compose.resources.StringResource

val HeaderHeight = 64.dp
val TabBarHeight = 60.dp

/** Narrowest [TabRail]. On iPhone Duo the rail fills the 84 pt column under the status bar. */
val TabRailWidth = 84.dp

/** Space the floating tab bar takes at the bottom of the current screen; zero where it is hidden. */
val LocalBottomBarInset = compositionLocalOf { 0.dp }

/** Widest a single column grows, side margins included. Only the inner display of iPhone Duo is wider. */
val MaxContentWidth = 520.dp

/**
 * Centers a column no wider than [maxWidth] and keeps it out of the side safe areas.
 * On iPhone Duo the status bar is on the right edge, and only `safeDrawing` reports it.
 */
@Composable
fun Modifier.contentColumn(maxWidth: Dp = MaxContentWidth): Modifier = this
    .fillMaxWidth()
    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
    .wrapContentWidth()
    .widthIn(max = maxWidth)
    .fillMaxWidth()

/** "MIGRATRACK / Summary" header with the brain mark, or a back button on pushed screens. */
@Composable
fun BrandHeader(
    title: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    onSettings: (() -> Unit)? = null,
) {
    val backLabel = stringResource(Res.string.common_back)
    val settingsLabel = stringResource(Res.string.common_settings)
    Row(
        modifier = modifier.fillMaxWidth().height(HeaderHeight).padding(horizontal = MtSpacing.screen),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MtColors.Card)
                .border(1.dp, Color.White.copy(alpha = 0.1f), CircleShape)
                .then(
                    if (onBack != null) Modifier.semantics { contentDescription = backLabel }.pressable(onClick = onBack)
                    else Modifier,
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (onBack != null) {
                Icon(Symbols.ArrowBackIosNew, tint = MtColors.BrandLabel, size = 20.dp)
            } else {
                BrandMark()
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f).semantics(mergeDescendants = true) { heading() }) {
            WithoutFontScaling {
                MtText("MIGRATRACK", MtTheme.type.caption.copy(letterSpacing = 0.16.em, fontWeight = FontWeight.SemiBold), color = MtColors.BrandLabel)
                // Shrinks only a title that does not fit, for example "Политика конфиденциальности". With an ellipsis
                // the text never shrank on iOS (Compose 1.12.1), so the overflow is Clip.
                MtText(
                    title,
                    MtTheme.type.title1,
                    color = MtColors.TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    autoSize = TextAutoSize.StepBased(minFontSize = 18.sp, maxFontSize = MtTheme.type.title1.fontSize),
                )
            }
        }
        if (onSettings != null) {
            Box(
                Modifier.size(MtSpacing.minTouch).semantics { contentDescription = settingsLabel }.pressable(onClick = onSettings),
                contentAlignment = Alignment.Center,
            ) {
                Icon(AppIcons.Settings, tint = Color(0xFFA1A1AA), size = 24.dp)
            }
        }
    }
}

/**
 * A scrolling page under a fixed header, padded for the status bar and the tab bar.
 * Content scrolls beneath the header as it does under a UINavigationBar.
 * With [panes], a wide page of regular width grows to [MaxTwoPaneWidth] and shows the content's [Panes] side by side.
 */
@Composable
fun Page(
    title: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    onSettings: (() -> Unit)? = null,
    scrollState: ScrollState = rememberScrollState(),
    spacing: Dp = MtSpacing.lg,
    panes: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    val top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val bottom = maxOf(LocalBottomBarInset.current, WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
    val density = LocalDensity.current
    val window = LocalHostWindow.current
    val fold = window.fold
    var pageLeft by remember { mutableStateOf(0.dp) }
    BoxWithConstraints(
        modifier.fillMaxSize().background(MtColors.Canvas).onPlaced { pageLeft = with(density) { it.positionInRoot().x.toDp() } },
    ) {
        val twoPanes = panes && window.regularWidth && maxWidth >= TwoPaneMinWidth
        val columnWidth = if (twoPanes) MaxTwoPaneWidth else MaxContentWidth
        val side = if (twoPanes) Modifier else Modifier.besideFold(fold, pageLeft, maxWidth)
        CompositionLocalProvider(LocalTwoPanes provides twoPanes) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(top = top + HeaderHeight + MtSpacing.xs, bottom = bottom + MtSpacing.lg)
                    .then(side)
                    .contentColumn(columnWidth)
                    .padding(horizontal = MtSpacing.screen),
                verticalArrangement = Arrangement.spacedBy(spacing),
                content = content,
            )
        }
        BrandHeader(
            title = title,
            onBack = onBack,
            onSettings = onSettings,
            modifier = Modifier.background(MtColors.Canvas).padding(top = top).then(side).contentColumn(columnWidth),
        )
    }
}

/** Moves a single column to the wider side of the fold, so that no control lies across it. */
private fun Modifier.besideFold(fold: VerticalBand?, pageLeft: Dp, pageWidth: Dp): Modifier {
    val padding = foldPadding(fold, pageLeft, pageWidth) ?: return this
    return absolutePadding(left = padding.left, right = padding.right)
}

internal data class SidePadding(val left: Dp, val right: Dp)

/** The fold is physical, so the padding is absolute in right-to-left languages too. Null when the fold misses the page. */
internal fun foldPadding(fold: VerticalBand?, pageLeft: Dp, pageWidth: Dp): SidePadding? {
    if (fold == null) return null
    val left = fold.left - pageLeft
    val right = fold.right - pageLeft
    if (right <= 0.dp || left >= pageWidth) return null
    return if (left >= pageWidth - right) SidePadding(0.dp, pageWidth - left) else SidePadding(right, 0.dp)
}

/** [symbol] is the SF Symbol closest to [icon], for the Large Content Viewer. */
enum class Tab(val title: StringResource, val icon: ImageVector, val symbol: String) {
    Summary(Res.string.tab_summary, Symbols.CalendarMonth, "calendar"),
    Log(Res.string.tab_log, AppIcons.TabLog, "plus.circle"),
    Insights(Res.string.tab_insights, AppIcons.TabInsights, "waveform.path.ecg"),
    Export(Res.string.tab_export, AppIcons.TabExport, "square.and.arrow.up"),
}

@Composable
fun TabBar(selected: Tab, onSelect: (Tab) -> Unit, modifier: Modifier = Modifier) {
    val bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    Column(modifier.fillMaxWidth().background(MtColors.Canvas)) {
        Box(Modifier.fillMaxWidth().height(0.5.dp).background(Color.White.copy(alpha = 0.1f)))
        Row(
            modifier = Modifier.contentColumn().height(TabBarHeight).padding(horizontal = MtSpacing.xs),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Tab.entries.forEach { tab ->
                TabItem(tab, active = tab == selected, onSelect = onSelect, modifier = Modifier.weight(1f).height(TabBarHeight))
            }
        }
        Spacer(Modifier.height(bottom))
    }
}

/**
 * The tab bar as a column under the status bar, where iPhone Duo shows the status bar on the right edge.
 * iOS 27.1 shows system tab bars there vertically below the status bar, so the items are centered to stay clear of it.
 * The status bar stays on the right in right-to-left languages, so the rail and its separator do too.
 */
@Composable
fun TabRail(selected: Tab, onSelect: (Tab) -> Unit, modifier: Modifier = Modifier) {
    Box(modifier.fillMaxHeight().background(MtColors.Canvas)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(MtSpacing.sm, Alignment.CenterVertically),
        ) {
            Tab.entries.forEach { tab ->
                TabItem(tab, active = tab == selected, onSelect = onSelect, modifier = Modifier.fillMaxWidth().height(TabBarHeight))
            }
        }
        Box(Modifier.align(AbsoluteAlignment.CenterLeft).fillMaxHeight().width(0.5.dp).background(Color.White.copy(alpha = 0.1f)))
    }
}

@Composable
private fun TabItem(tab: Tab, active: Boolean, onSelect: (Tab) -> Unit, modifier: Modifier) {
    val color = if (active) MtColors.Pink else MtColors.TextSecondary
    Column(
        modifier = modifier
            .largeContentItem(stringResource(tab.title), tab.symbol) { onSelect(tab) }
            .semantics { this.selected = active }
            .pressable(role = Role.Tab, pressedScale = 0.92f) { onSelect(tab) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            tab.icon,
            tint = color,
            size = 24.dp,
            modifier = if (active) Modifier.glow(MtColors.Pink, radius = 10.dp, alpha = 0.35f) else Modifier,
        )
        Spacer(Modifier.height(4.dp))
        WithoutFontScaling { MtText(stringResource(tab.title), MtTheme.type.tabLabel, color = color) }
    }
}

/** Short confirmation that slides in under the status bar, like the design's "Logger ready" toast. */
@Composable
fun Banner(text: String, icon: ImageVector, tint: Color, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .contentColumn()
            .padding(horizontal = MtSpacing.screen)
            .clip(RoundedCornerShape(14.dp))
            .background(MtColors.ControlHigh)
            .border(1.dp, MtColors.BorderSubtle, RoundedCornerShape(14.dp))
            .padding(horizontal = MtSpacing.md, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, tint = tint, size = 20.dp)
        Spacer(Modifier.width(MtSpacing.sm))
        MtText(text, MtTheme.type.subhead, color = MtColors.TextBody)
    }
}
