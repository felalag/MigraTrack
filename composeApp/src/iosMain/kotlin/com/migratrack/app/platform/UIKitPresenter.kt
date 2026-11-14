package com.migratrack.app.platform

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRectMake
import platform.UIKit.UIApplication
import platform.UIKit.UIUserInterfaceSizeClassRegular
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene
import platform.UIKit.popoverPresentationController

/** Finds the view controller that can present right now: the key window's root, then its presented chain. */
internal fun topViewController(): UIViewController? {
    val window = UIApplication.sharedApplication.connectedScenes
        .filterIsInstance<UIWindowScene>()
        .flatMap { scene -> scene.windows.filterIsInstance<UIWindow>() }
        .firstOrNull { it.isKeyWindow() }
    var top = window?.rootViewController
    while (top?.presentedViewController != null) top = top.presentedViewController
    return top
}

/**
 * A regular-width window, such as the inner display of iPhone Duo, shows action sheets and the share sheet as popovers.
 * They point at the last touch, which is on the control that opened them; with a sheet on top, they open centered.
 * A compact window gets no anchor: since iOS 26 an anchored action sheet becomes a popover there too.
 */
@OptIn(ExperimentalForeignApi::class)
internal fun UIViewController.anchorPopover(host: UIViewController) {
    if (host.traitCollection.horizontalSizeClass != UIUserInterfaceSizeClassRegular) return
    val popover = popoverPresentationController ?: return
    val window = host.view.window ?: return
    val touch = LastTouch.point?.takeIf { host.presentingViewController == null }
    popover.sourceView = window
    if (touch != null) {
        popover.sourceRect = CGRectMake(touch.x.toDouble(), touch.y.toDouble(), 1.0, 1.0)
    } else {
        popover.sourceRect = window.bounds.useContents { CGRectMake(size.width / 2, size.height / 2, 0.0, 0.0) }
        popover.permittedArrowDirections = 0uL
    }
}
