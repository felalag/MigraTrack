import ComposeApp
import SwiftUI
import UIKit

/// Hosts the Compose Multiplatform UI. Everything above this view is native SwiftUI.
struct ContentView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        ComposeHostController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

/// Embeds the Compose view controller and tells it what Compose cannot see of the window: the horizontal size class,
/// the status bar column on the right edge of iPhone Duo, and the fold while the device is partly folded.
/// Kotlin/Native has no bindings for the iOS 27.1 reserved regions, so Swift reads them.
final class ComposeHostController: UIViewController {
    private let compose = MainViewControllerKt.MainViewController()
    private var reported: [Double] = []

    override func viewDidLoad() {
        super.viewDidLoad()
        addChild(compose)
        compose.view.frame = view.bounds
        compose.view.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        view.addSubview(compose.view)
        compose.didMove(toParent: self)
        if #available(iOS 27.1, *) {
            view.addInteraction(UIHingeInteraction { [weak self] _, _ in self?.reportRegions() })
        }
    }

    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        reportRegions()
    }

    private func reportRegions() {
        var status = (left: 0.0, right: 0.0)
        var fold = (left: 0.0, right: 0.0)
        if #available(iOS 27.1, *) {
            // The status bar column of iPhone Duo is an occlusion region from the top of the right edge.
            // UIStatusBarManager still reports a horizontal bar there.
            let bounds = view.bounds
            let column = view.reservedRegions(kind: .occlusion).first { region in
                region.isActive && region.frame.height > region.frame.width
                    && region.frame.minY <= bounds.minY + 1 && region.frame.maxX >= bounds.maxX - 1
            }
            if let column { status = (column.frame.minX, bounds.maxX) }
            let division = view.reservedRegions(kind: .division).first { $0.isActive && $0.frame.height > $0.frame.width }
            if let division { fold = (division.frame.minX, division.frame.maxX) }
        }
        let regularWidth = traitCollection.horizontalSizeClass == .regular
        let values = [regularWidth ? 1 : 0, status.left, status.right, fold.left, fold.right]
        guard values != reported else { return }
        reported = values
        HostWindowBridgeKt.updateHostWindow(
            regularWidth: regularWidth,
            statusBarLeft: status.left,
            statusBarRight: status.right,
            foldLeft: fold.left,
            foldRight: fold.right
        )
    }
}
