package com.migratrack.app.platform

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.migratrack.app.ui.components.LargeContentViewer
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGPoint
import platform.UIKit.UIEdgeInsets
import platform.UIKit.UIEdgeInsetsMake
import platform.UIKit.UIImage
import platform.UIKit.UILargeContentViewerInteraction
import platform.UIKit.UILargeContentViewerInteractionDelegateProtocol
import platform.UIKit.UILargeContentViewerItemProtocol
import platform.UIKit.UIView
import platform.UIKit.addInteraction
import platform.UIKit.removeInteraction
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
internal class UIKitLargeContentViewer : LargeContentViewer {
    private class Entry(val bounds: Rect, val title: String, val symbol: String?, val onSelect: () -> Unit)

    private val entries = mutableMapOf<Any, Entry>()

    // The interaction holds its delegate weakly.
    private val delegate = Delegate()
    private val interaction = UILargeContentViewerInteraction(delegate = delegate)

    fun attach(view: UIView) = view.addInteraction(interaction)

    fun detach(view: UIView) = view.removeInteraction(interaction)

    override fun update(owner: Any, bounds: Rect, title: String, symbol: String?, onSelect: () -> Unit) {
        entries[owner] = Entry(bounds, title, symbol, onSelect)
    }

    override fun remove(owner: Any) {
        entries.remove(owner)
    }

    private inner class Delegate : NSObject(), UILargeContentViewerInteractionDelegateProtocol {
        override fun largeContentViewerInteraction(
            interaction: UILargeContentViewerInteraction,
            itemAtPoint: CValue<CGPoint>,
        ): UILargeContentViewerItemProtocol? {
            val point = itemAtPoint.useContents { Offset(x.toFloat(), y.toFloat()) }
            return entries.values.firstOrNull { point in it.bounds }?.let(::Item)
        }

        override fun largeContentViewerInteraction(
            interaction: UILargeContentViewerInteraction,
            didEndOnItem: UILargeContentViewerItemProtocol?,
            atPoint: CValue<CGPoint>,
        ) {
            (didEndOnItem as? Item)?.entry?.onSelect?.invoke()
        }
    }

    private class Item(val entry: Entry) : NSObject(), UILargeContentViewerItemProtocol {
        override fun showsLargeContentViewer(): Boolean = true

        override fun largeContentTitle(): String = entry.title

        override fun largeContentImage(): UIImage? = entry.symbol?.let { UIImage.systemImageNamed(it) }

        override fun scalesLargeContentImage(): Boolean = true

        override fun largeContentImageInsets(): CValue<UIEdgeInsets> = UIEdgeInsetsMake(0.0, 0.0, 0.0, 0.0)
    }
}
