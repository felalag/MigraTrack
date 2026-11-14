package com.migratrack.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.uikit.LocalUIViewController
import androidx.compose.ui.window.ComposeUIViewController
import com.migratrack.app.di.AppGraph
import com.migratrack.app.platform.AppInfo
import com.migratrack.app.platform.HealthKitSleepDataSource
import com.migratrack.app.platform.PlatformServices
import com.migratrack.app.platform.UIKitDialogs
import com.migratrack.app.platform.UIKitFileSharing
import com.migratrack.app.platform.UIKitLargeContentViewer
import com.migratrack.app.platform.hostWindow
import com.migratrack.app.platform.recordTouches
import com.migratrack.app.ui.components.LocalHostWindow
import com.migratrack.app.ui.components.LocalLargeContentViewer
import com.migratrack.data.AppleDatabase
import com.migratrack.data.LocalStore
import com.migratrack.export.pdf.UIKitPdfRenderer
import platform.Foundation.NSBundle
import platform.Foundation.NSUserDefaults
import platform.UIKit.UIViewController
import kotlin.experimental.ExperimentalNativeApi

/** One graph per process: the UI and the App Intents share it. */
internal val appGraph: AppGraph by lazy {
    AppGraph(
        PlatformServices(
            store = LocalStore(AppleDatabase.driver()),
            dialogs = UIKitDialogs(),
            files = UIKitFileSharing(),
            sleep = HealthKitSleepDataSource(),
            pdfRenderer = UIKitPdfRenderer(),
            appInfo = bundleAppInfo(),
        ),
    )
}

/** Entry point for ComposeHostController in Swift. */
fun MainViewController(): UIViewController = ComposeUIViewController {
    val window by hostWindow.collectAsState()
    val largeContent = remember { UIKitLargeContentViewer() }
    val view = LocalUIViewController.current.view
    DisposableEffect(view) {
        largeContent.attach(view)
        onDispose { largeContent.detach(view) }
    }
    CompositionLocalProvider(LocalHostWindow provides window, LocalLargeContentViewer provides largeContent) {
        Box(Modifier.fillMaxSize().recordTouches()) { App(appGraph) }
    }
}

@OptIn(ExperimentalNativeApi::class)
private fun bundleAppInfo(): AppInfo {
    val info = NSBundle.mainBundle.infoDictionary.orEmpty()
    return AppInfo(
        version = info["CFBundleShortVersionString"] as? String ?: "1.0",
        build = info["CFBundleVersion"] as? String ?: "1",
        isDebug = Platform.isDebugBinary,
        // A launch argument "-MigraTrackScreen Insights" lands in the user defaults (scripts/l10n-screenshots.sh).
        startScreen = if (Platform.isDebugBinary) NSUserDefaults.standardUserDefaults.stringForKey("MigraTrackScreen") else null,
    )
}
