# Release plan for MigraTrack 1.0

| | |
|---|---|
| Analysis date | November 14, 2026. The first analysis was on November 8, 2026, at `822ec9f`. |
| Commit | `bfbeed295333dd2f96b70edb4a812e0ccdc3a843` (`origin/main`, merge of PR #49) |
| Toolchain on the build Mac | Xcode 27.2 (27B52), installed on November 4, 2026. iOS 27.2 and watchOS 27.2 SDKs. |
| Earlier reports | [REVIEW_READINESS.md](REVIEW_READINESS.md) (October 14, 2026), [APP_STORE.md](APP_STORE.md), [LOCALIZATION_PLAN.md](LOCALIZATION_PLAN.md) |

This file is the only list of the work that remains before the release. [REVIEW_READINESS.md](REVIEW_READINESS.md) keeps the analysis of October 14, 2026 and links here. [APP_STORE.md](APP_STORE.md) keeps the text that you enter in App Store Connect.

Each step has an owner:

- **Code** is a Claude session in this repository. It ends in a pull request.
- **Owner** is the account holder. The step is in Apple Developer, in App Store Connect, or is a legal decision.

## 1. Conclusion

The code is ready for the submission. PRs #47, #48 and #49 closed the code blockers of November 8: the privacy manifest (N12), the Terms page (N6) and the signing team (B1 in the code). The simulators work again (N7).

The work that remains is in App Store Connect, with one code step:

1. **No store screenshots (B2).** Step 7 takes them. It is the only open code step.
2. **No build in App Store Connect (B1, upload).** The archive signs with team `V3D2X838L7`, but nobody uploaded it.
3. **The App Store Connect forms are not complete (B3).** This includes the two new age rating questions from November 16, 2026 (N8).
4. **This file is not in `main` (N13).** PR #47 links to `RELEASE_PLAN.md` from [APP_STORE.md](APP_STORE.md) and [REVIEW_READINESS.md](REVIEW_READINESS.md). On `main`, the links go to a missing file until this file is merged.

## 2. Current state

### 2.1 Pull requests

On November 14, 2026, `origin/main` contains all pull requests from #1 to #49, except #5.

| PR | State | What is necessary to merge it | Conflicts |
|---|---|---|---|
| #47 Release documents for November 2026 | Merged on November 8, 2026 | — | — |
| #48 Release blockers N12 and N6 | Merged on November 8, 2026 | — | — |
| #49 Signing team `V3D2X838L7` | Merged on November 14, 2026 | — | — |
| #5 Decision on accounts and cloud storage | Open, draft, since October 14, 2026 | Mark it ready for review, then merge it. It adds only `docs/AUTH_AND_SYNC_PROPOSAL.md`. It does not block the release. | None |

### 2.2 Work merged since October 16, 2026

| Area | Pull requests | Effect on the release |
|---|---|---|
| Clinical texts (ICHD-3), developer name in the Privacy Policy | #16 | Closes D6 and D8 |
| iPhone Duo layouts | #17 | Closes N1 |
| New app icon, website favicon, dot-grid mark in the header | #18, #19 | The icon has no Activity rings. The screenshots must show the new icon and mark. |
| User journey report, four small fixes | #20, #21 | No open finding |
| Localization stage 1 (strings, catalogs, dates, report, tools, scripts and direction) | #22–#35 | Closes D10. The app uses the region format for dates and numbers. |
| Translations, batches 2–9: 47 languages | #36, #38–#45 | 46 App Store metadata files in [app-store](app-store/). Translated Terms and Privacy Policy in the app. |
| Navigation guards, Siri medication names, list separator | #31, #33, #34, #37, #46 | No open finding |
| Release documents: new age rating questions, iPhone Duo sizes, decision on the languages | #47 | Closes step 3 and step 5 |
| User Defaults read only in Debug, Pages `exclude` entries end with `/` | #48 | Closes N12 and N6 |
| Signing team | #49 | Closes B1 in the code. Automatic signing registered the App IDs and the App Group. |

### 2.3 Status of each finding

The section "Remaining before release" of October 16, 2026 was never committed. This table replaces it.

| ID | Status | Evidence |
|---|---|---|
| B1 | Open: upload only | [Config.xcconfig:3](../iosApp/Configuration/Config.xcconfig#L3) has `TEAM_ID = V3D2X838L7` (PR #49). The signed archive succeeds. No build is in App Store Connect. |
| B2 | Open | No store screenshots exist. [APP_STORE.md](APP_STORE.md#screenshots) has the plan. |
| B3 | Open | The forms are in App Store Connect (section 5, steps 10–19). |
| R1 | Closed by PRs #13, #18 | The icon is a 5 × 5 day-dot grid. The Summary uses a day strip, and the watch uses a gauge. |
| R2 | Closed by PR #15 | [Info.plist:30](../iosApp/iosApp/Info.plist#L30) promises only last night's sleep on the Summary. |
| R3 | Closed by PRs #13, #15 | No code contains "Biometric". The Summary and the last onboarding step show the doctor reminder. |
| D1 | Open, accepted for 1.0 | [Theme.kt:95](../composeApp/src/commonMain/kotlin/com/migratrack/app/ui/theme/Theme.kt#L95) caps the text at 135%. Do not declare Larger Text. |
| D2 | Open | VoiceOver is not tested on a device. Do not declare VoiceOver. |
| D3 | Closed by PR #13 | [SummaryView.swift:49](../iosApp/watchApp/SummaryView.swift#L49) shows the caption "Pain-free days". |
| D4 | Closed by PR #15 | The complication shows "–" when the read fails. |
| D5 | Closed by PR #15 | The report shows only the start time of an episode. |
| D6 | Closed by PR #16 | [strings.xml:533](../composeApp/src/commonMain/composeResources/values/strings.xml#L533) says "15 or more headache days a month". The ranges are "5–7" and "8+" ([strings.xml:551](../composeApp/src/commonMain/composeResources/values/strings.xml#L551)). |
| D7 | Open, low risk | Guideline 5.1.1(ix) asks for a legal entity in healthcare. The review notes say that the app is a diary with no diagnosis and no treatment. |
| D8 | Closed by PR #16 | [privacy-policy.md:15](legal/files/privacy-policy.md#L15) names the developer. Line 21 gives the retention. |
| D9 | Closed by PR #15 | The Delete All Data dialog names the watch copy and the backups. |
| D10 | Closed by PR #29 | [SystemFormats.kt](../composeApp/src/iosMain/kotlin/com/migratrack/app/ui/SystemFormats.kt) uses the region format. PR #47 updated the D10 row in REVIEW_READINESS.md. |
| D11 | Closed by PR #15 | Settings has no online dot. |
| J1 | Open, for 1.1 | [Episode.kt:10](../shared/domain/src/commonMain/kotlin/com/migratrack/domain/model/Episode.kt#L10) has `startedAt`, but no end. |
| J2 | Open, for 1.1 | [EpisodeLogging.kt:49](../shared/domain/src/commonMain/kotlin/com/migratrack/domain/usecase/EpisodeLogging.kt#L49) gives each medication the start time. [EpisodeLogging.kt:70](../shared/domain/src/commonMain/kotlin/com/migratrack/domain/usecase/EpisodeLogging.kt#L70) keys intakes by medication ID. |
| J3 | Open, for 1.1 | [ClinicalReport.kt](../shared/export/src/commonMain/kotlin/com/migratrack/export/report/ClinicalReport.kt) has no table by month. |
| J4 | Open, for 1.1 | The project does not use `UserNotifications`. |
| J5 | Open, for 1.1 | Only the watch has a widget and a control ([Complications.swift](../iosApp/watchComplications/Complications.swift)). |
| J6 | Open, for 1.1 | The report has no monthly migraine days and no duration. |
| J7 | Open, for 1.1 | The app cannot import data. |
| J8 | Open, for 1.1 | The schema in [shared/data](../shared/data/src/commonMain/sqldelight) stores no UTC offset. |
| J9 | Open, for 1.1 | [HealthKitSleepDataSource.kt:39](../composeApp/src/iosMain/kotlin/com/migratrack/app/platform/HealthKitSleepDataSource.kt#L39) reads only sleep analysis. |
| J10 | Open, for 1.1 | The watch cannot set a past time or a medication. |
| J11 | Open, low | The delete dialog tells the user to export first, but has no Export button. |
| J12 | No gap | Migrations `1.sqm`, `2.sqm` and the schemas `1.db`–`3.db` are in [shared/data](../shared/data/src/commonMain/sqldelight). `MigrationTest` passes. |
| N1 | Closed by PR #17 | The layouts keep out of the side status bar and show a vertical tab bar on the inner display ([iphone-duo](iphone-duo/)). |
| N2 | Closed | [APP_STORE.md](APP_STORE.md#review-risks-found-on-october-14-2026) no longer has the stale line about pain-free days before the first launch. |
| N3 | No gap | From April 2027, uploads need the iOS 27 and watchOS 27 SDKs and iOS 15 as the lowest target. Xcode 27.2 and the targets (iOS 18.5, watchOS 11) agree. |
| N4 | Recurs as N7 | — |
| N6 | Closed by PR #48 | `/terms/` returns HTTP 200 on November 14, 2026. |
| N7 | Closed | `xcrun simctl list runtimes` answers at once on November 14, 2026. |
| N12 | Closed by PR #48 | [MainViewController.kt:43](../composeApp/src/iosMain/kotlin/com/migratrack/app/MainViewController.kt#L43) reads the key only when `Platform.isDebugBinary` is true. Section 6 has the `nm` result. |
| N5 | Closed | PR #17 tested iPhone Duo with the iOS 27.1 runtime. |

### 2.4 Findings of November 8, 2026

- **N6. The Terms page returns 404.** Batch 9 (PR #45) added `legal/files/te` (Telugu) to `exclude` in [_config.yml](_config.yml). Jekyll compares an `exclude` entry as a prefix of the path, so the entry also excludes `legal/files/terms-of-service.md`. The same rule makes `legal/files/pt` match `pt-PT`, but that folder is excluded anyway. PR #48 ends each folder entry with `/`. `TranslationCheck` now reports an entry that hides a file outside its folder.
- **N7. Xcode 27.2 needs its first-launch step.** Run `sudo xcodebuild -runFirstLaunch` once on the build Mac. This is the same problem as N4 after the update to Xcode 27.1. Closed: the owner ran it before November 14, 2026.
- **N8. New age rating questions.** From November 16, 2026, the questionnaire asks about Shared Location and about Dating and Relationships. All apps must answer them by January 12, 2027 ([Apple Developer News](https://developer.apple.com/news/), November 2, 2026). The answer for MigraTrack is "No" for both. PR #47 added them to [APP_STORE.md](APP_STORE.md#age-rating).
- **N9. New agreements.** Apple updated the Apple Developer Program License Agreement on October 13 and on October 29, 2026. App Store Connect blocks new submissions until the account holder accepts the latest version.
- **N10. App Review Guidelines of October 16, 2026.** The new guideline 2.5.19 is about hidden features. Only Debug builds read the `-MigraTrackScreen` launch argument and show the Developer section, so the Release build has no hidden feature. Do not take store screenshots of the Settings screen from a Debug build, because it shows the Developer section.
- **N11. Translations without a native reader.** Nobody who speaks the language read 45 of the 47 translations. The owner reads only Russian and Belarusian (README, "Known limits"). The owner decided on November 8, 2026 that all 47 languages ship in 1.0 (step 3).
- **N12. A required-reason API without a reason.** PR #32 added the screenshot launch argument. [MainViewController.kt:43](../composeApp/src/iosMain/kotlin/com/migratrack/app/MainViewController.kt#L43) reads `NSUserDefaults` in every build, and `takeIf` drops the value only after the read. `nm -u` on the Release binary finds `_OBJC_CLASS_$_NSUserDefaults` next to `_stat` and `_fstat`. The manifest declares only `NSPrivacyAccessedAPICategoryFileTimestamp` (`C617.1`). Since May 1, 2024, App Store Connect refuses such an upload ([Describing use of required reason API](https://developer.apple.com/documentation/bundleresources/describing-use-of-required-reason-api)). PR #48 reads the key only in a Debug build. Kotlin/Native removes the Debug branch from the Release binary, so the binary does not import `NSUserDefaults`, and the manifest needs no new reason. The watch binaries use no required-reason API.

### 2.5 New findings on November 14, 2026

- **N13. Broken links on `main`.** [APP_STORE.md:5](APP_STORE.md#L5), [APP_STORE.md:30](APP_STORE.md#L30) and the D10 row of [REVIEW_READINESS.md](REVIEW_READINESS.md) link to `RELEASE_PLAN.md`. The file is only in the branch `docs/release-plan`. Merge this file to close N13.
- **N14. The iPhone app has no App Group.** Only the watch app and the complications share the database ([MigraTrackWatch.entitlements](../iosApp/watchApp/MigraTrackWatch.entitlements)). The iPhone App ID needs HealthKit, and no App Group. Step 8 now says this.
- **N15. The new age rating questions are not in App Store Connect before November 16, 2026.** If you answer the questionnaire before that date, open it again after November 16 and answer the two new questions. The deadline is January 12, 2027.
- **N16. The name "MigraTrack" is reserved.** On November 14, 2026, App Store Connect refused the name "MigraTrack", because another developer has an app record with it. No storefront of 24 that the iTunes Search API checked has a published app with this name. The owner created the record as "MigraTrack: Migraine Diary". [APP_STORE.md](APP_STORE.md#app-information) and the 46 files in [app-store](app-store/) use this name. The Home Screen still shows "MigraTrack".
- **N17. The repository name had a typo.** The repository was `felalag/MirgaTrack`, so the website was `felalag.github.io/MirgaTrack/`. On November 14, 2026, the repository became `felalag/MigraTrack`. The website is now `https://felalag.github.io/MigraTrack/`, and the old address returns HTTP 404. The app binary contains no website address. Git and the GitHub API redirect the old repository name.

## 3. Apple requirements on November 8, 2026

| Topic | Requirement | Effect on MigraTrack | Source |
|---|---|---|---|
| Xcode and SDK | Now: Xcode 26 or later with the iOS 26 and watchOS 26 SDKs (since April 28, 2026), and iOS 13 or later as the target (since September 9, 2026). From April 2027: the iOS 27 and watchOS 27 SDKs, and iOS 15 or later. | Xcode 27.2 meets both. | [Upcoming requirements](https://developer.apple.com/news/upcoming-requirements/), [Submit your apps](https://developer.apple.com/app-store/submissions/) |
| iPhone screenshots | Required: at least one screenshot for "iPhone with Dynamic Island (medium display)", 1206 × 2622 or 1179 × 2556. Without a set for the large display (1320 × 2868, 1290 × 2796 or 1260 × 2736), App Store Connect requires the older Face ID set. App Store Connect scales the largest set down to smaller sizes. | iPhone 18 Pro gives 1206 × 2622. iPhone 18 Pro Max gives 1320 × 2868. Upload both. | [Screenshot specifications](https://developer.apple.com/help/app-store-connect/reference/app-information/screenshot-specifications) |
| iPhone Duo screenshots | Outer display 1398 × 2034, inner display 2007 × 2853. Optional now. From April 2027, required for apps built with the iOS 27.1 SDK or later. With them, the product page shows a "made for iPhone Duo" badge. | Optional for 1.0. Required for the first update after April 2027, because Xcode 27.2 builds with a later SDK than 27.1. | Same page; [Prepare for iPhone Duo](https://developer.apple.com/iphone-duo/prepare/); Apple Developer News, October 5, 2026 |
| Apple Watch screenshots | One of six sizes, for example 416 × 496 (Series 10–12). Use the same size in all localizations. | Use the Series 12 (46 mm) simulator. | Screenshot specifications |
| Localized metadata | 50 localizations. Each has its own name, subtitle, description, keywords, promotional text and Privacy Policy URL. A new localization takes the screenshots of the primary language until you upload others. A storefront without a matching localization shows the primary language. | 46 files in [app-store](app-store/) + English (U.S.). English (U.K.), (Australia) and (Canada) are optional; without them these storefronts show English (U.S.). | [App Store localizations](https://developer.apple.com/help/app-store-connect/reference/app-store-localizations), [Localize app information](https://developer.apple.com/help/app-store-connect/manage-app-information/localize-app-information) |
| Languages of the binary | Apple separates the metadata languages from the languages of the binary. The product page lists the languages of the binary. | The archive has 51 `.lproj` folders (section 6). | Localize app information |
| Age rating | The questionnaire of 2026 gives 4+, 9+, 13+, 16+ or 18+. "Medical or Treatment Information": Infrequent gives 13+, Frequent gives 16+. "Health or Wellness Topics" gives 9+. New questions from November 16, 2026 (N8). | Expected 4+. The answers are in [APP_STORE.md](APP_STORE.md#age-rating). | [Age ratings values and definitions](https://developer.apple.com/help/app-store-connect/reference/app-information/age-ratings-values-and-definitions) |
| App Privacy | "Collect" means to send data off the device so that the developer or a partner can access it longer than the request takes. Data that the app processes only on the device is not collected. | "Data Not Collected". The transfer between the user's iPhone and watch gives nobody else access. | [App privacy details](https://developer.apple.com/app-store/app-privacy-details/) |
| Accessibility Nutrition Labels | Voluntary now. Apple says that they become mandatory later, with no date. Larger Text needs 200% or the largest system size. | Declare Dark Interface only. Do not declare Larger Text (D1) or VoiceOver (D2). | [Overview of Accessibility Nutrition Labels](https://developer.apple.com/help/app-store-connect/manage-app-accessibility/overview-of-accessibility-nutrition-labels) |
| EU Digital Services Act | Every account declares a trader status, also without EU distribution. A non-trader gives no contact data. EU users then see that consumer law does not apply. | The owner declares "not a trader" (step 6). | [Manage DSA trader requirements](https://developer.apple.com/help/app-store-connect/manage-compliance-information/manage-european-union-digital-services-act-trader-requirements) |
| Regulated medical device | Necessary for apps in the EU/EEA, UK or US with the category Health & Fitness or Medical. | Answer "No". The app has no clearance and no CE or UKCA mark. | [Declare regulated medical device status](https://developer.apple.com/help/app-store-connect/manage-app-information/declare-regulated-medical-device-status/) |
| China mainland | "Some apps" need an ICP filing number from the MIIT. Apple does not say which apps. | The owner decides the China mainland availability (step 3). | [App information](https://developer.apple.com/help/app-store-connect/reference/app-information/app-information) |
| App Review Guidelines | Last updated October 16, 2026. 1.4.1 (doctor reminder), 5.1.1(ix) (regulated fields), 5.1.3 (no health data in iCloud). | No new gap (N10, D7). | [App Review Guidelines](https://developer.apple.com/app-store/review/guidelines/) |

## 4. What blocks the release

| ID | Blocker | Owner | Steps |
|---|---|---|---|
| B1 | Upload of a signed build to TestFlight | Owner | 9 |
| B2 | Store screenshots | Code takes them, owner uploads them | 7, 13 |
| B3 | App Store Connect forms, with the new age rating questions | Owner | 11–14, 18, 19 |
| N13 | This file is not in `main` | Code, then the owner merges | 22 |

## 5. Step-by-step plan

Do the steps in this order. A step can start when the steps in its "Needs" column are done. The "Status" column gives the state on November 14, 2026. "Not known" means that the owner did not report the state.

| # | Step | Owner | Needs | Status | Done when |
|---|---|---|---|---|---|
| 1 | Run `sudo xcodebuild -runFirstLaunch` on the build Mac. | Owner | — | Done | `xcrun simctl list runtimes` answers in a few seconds. |
| 2 | **Membership and agreements.** Make sure that the Individual membership is active. In Business › Agreements, accept the latest Apple Developer Program License Agreement (N9). A free app needs no Paid Apps Agreement, no tax forms and no bank account. | Owner | — | Done. The Free Apps Agreement is Active from November 8, 2026 to October 19, 2027. The Paid Apps Agreement stays "New", because the app is free. | — |
| 3 | **Decide the scope of 1.0.** | Owner | — | Done on November 8, 2026: all 47 languages, no China mainland ([APP_STORE.md](APP_STORE.md#localizations)) | — |
| 4 | **Fix the two code blockers** N12 and N6. | Code | — | Done, PR #48 | — |
| 5 | **Refresh the release documents.** | Code | 3 | Done, PR #47 | — |
| 6 | **DSA.** In Business › Agreements › Compliance, declare "I'm not a trader under the DSA". Change it if the app later earns money. | Owner | 2 | Done. Business › Compliance shows the Digital Services Act as Active for 27 countries or regions, updated on November 8, 2026. | — |
| 7 | **Simulator check and store screenshots.** Do a clean install on iPhone 18 Pro, a run with data on iPhone 18 Pro Max, the watch on Series 12 (46 mm), and one Arabic and one Japanese run. Then take the English screenshots: iPhone 18 Pro (1206 × 2622), iPhone 18 Pro Max (1320 × 2868), and the watch (416 × 496). Use a Release build, or a Debug build without the Settings screen (N10). | Code | 1, 4 | Open. The old images in the session notes (October 14) show the old icon. Do not use them. | The PNG files are in the session notes, outside Git. |
| 8 | **Identifiers.** The App Group `group.com.migratrack.app`, and the App IDs `com.migratrack.app` (HealthKit), `com.migratrack.app.watchkitapp` (App Groups) and `com.migratrack.app.watchkitapp.complications` (App Groups). | Owner | 2 | Done. Automatic signing registered them for PR #49. | — |
| 9 | **Signing and TestFlight.** Archive `main` in Xcode. Open **Generate Privacy Report**. In Organizer, select Distribute App › App Store Connect. Xcode makes the Distribution certificate on the first upload. Install the build from TestFlight on an iPhone with a paired Apple Watch. Do the review notes of APP_STORE.md as a test. | Owner | 2, 8, 10 | `TEAM_ID` done (PR #49). Upload: not done | TestFlight shows the build as "Ready to Test". |
| 10 | **App record.** Add a new iOS app: primary language English (U.S.), bundle ID `com.migratrack.app`, a SKU. | Owner | 8 | Done on November 14, 2026, with the name "MigraTrack: Migraine Diary" (N16). | — |
| 11 | **App information.** Subtitle "Headache Log & Doctor Reports", categories (Health & Fitness, Medical), Privacy Policy URL, Support URL, copyright, description, keywords, promotional text from [APP_STORE.md](APP_STORE.md). | Owner | 10 | App Information done on November 14, 2026. The fields of version 1.0: not known | The English (U.S.) page is complete. |
| 12 | **Localizations.** Add the 46 localizations that have a file in [app-store](app-store/). Copy the fields from the file. The name is "MigraTrack: Migraine Diary" in each localization. | Owner | 11 | Not known | Each added localization has a name, subtitle, description and keywords. |
| 13 | **Screenshots.** Upload the English sets of step 7. The other localizations take them. | Owner | 7, 10 | Open | The iPhone and watch sets show no warning. |
| 14 | **Age rating.** Answer the questionnaire from APP_STORE.md, with "No" for Social Media, Shared Location, and Dating and Relationships (N8, N15). | Owner | 10 | Done on November 14, 2026, without the two questions of November 16. Answer them after that date. | The result is 4+. |
| 15 | **App Privacy.** Answer "No, we do not collect data from this app". | Owner | 10 | Done on November 14, 2026 | The page shows "Data Not Collected". |
| 16 | **Regulated medical device.** Answer "No". | Owner | 10 | Done on November 14, 2026 | The declaration is saved. |
| 17 | **App Accessibility.** Moved after step 21: App Store Connect publishes the labels only for an app with a version on the App Store. | Owner | 21 | Waits for the release | — |
| 18 | **Price and availability.** Free. All countries and regions except China mainland. | Owner | 10 | Not known | The availability is saved. |
| 19 | **App Review information.** Contact data and the notes from APP_STORE.md. Sign-in is not necessary. Export compliance asks nothing, because `ITSAppUsesNonExemptEncryption` is `false` ([Info.plist:25](../iosApp/iosApp/Info.plist#L25)). | Owner | 10 | Not known | The section is complete. |
| 20 | **Submit.** Add the TestFlight build to version 1.0. Select manual release, so that you can check the product page before the release. Submit for review. | Owner | 9, 11–16, 18, 19 | Open | The state is "Waiting for Review". |
| 21 | **After the approval.** Check the product page in two storefronts, for example the US and Germany. Release the version. Declare Dark Interface for iPhone and Apple Watch in App Accessibility (step 17). Merge or close PR #5. | Owner | 20 | Open | The app is on the App Store. |
| 22 | **Merge this file** (N13). | Code, then the owner merges | — | Open | `RELEASE_PLAN.md` is in `main`. |

## 6. Test record on November 14, 2026

All checks ran on `bfbeed2`.

| Check | Command or source | Result |
|---|---|---|
| All tests, with the iOS and watchOS simulators, and the translation check | `./gradlew check --continue` | BUILD SUCCESSFUL. 236 test cases, 0 failures. Of them, 100 ran on the iOS simulator and 41 on the watchOS simulator. |
| Release archive, unsigned | `xcodebuild -scheme iosApp -configuration Release -destination 'generic/platform=iOS' archive CODE_SIGNING_ALLOWED=NO` | Succeeded, 0 warnings. SDK `iphoneos27.2`. Version 1.0.0 (1). |
| Release archive, signed | PR #49, `-allowProvisioningUpdates` | Succeeded. Team `V3D2X838L7` signs the three targets. Not uploaded. |
| Privacy manifests | `find` in the archive | Three manifests: the app, the watch app, the complications. |
| Required-reason API scan | `nm -u` on the three binaries | Only `stat` and `fstat` in the iPhone app, which `C617.1` covers. No `NSUserDefaults`. The binary has no `MigraTrackScreen` string. Nothing in the watch app and the complications. |
| Languages of the binary | `.lproj` folders in the archive | 51 in the iPhone app, 50 in the watch app (no `en.lproj`, as on November 8). |
| Public URLs | `curl` on `/`, `/privacy/`, `/support/`, `/terms/` | 200, 200, 200, 200 |
| GitHub Pages build | GitHub API | Built on November 14, 2026, from `bfbeed2`. |
| Simulator run and store screenshots | — | Not done. Step 7 does them. |

## 7. Postpone to version 1.1

- **The journeys J1–J11.** Start with J1 and J2 (end of an attack, medication times), then J3 and J6 (month table for the doctor). They change the database and the report.
- **Larger Text (D1) and VoiceOver (D2).** Let lists and forms grow to 200%. Test VoiceOver on a device. Then declare both labels.
- **iPhone Duo screenshots.** Necessary for the first update after April 2027.
- **Localized screenshots.** `scripts/l10n-screenshots.sh` makes them. They are optional on the store.
- **A native reader for each language.** Correct the translations from the feedback of users.
- **Siri phrases** in more languages. [AppShortcuts.xcstrings](../iosApp/iosApp/AppShortcuts.xcstrings) has 25 Siri languages now.
- **China mainland**, if the owner starts an ICP filing.
- **Exclude the database from device backups**, only if the owner wants it. A new iPhone then starts without data.

## Sources

All pages were read on November 8, 2026. The update of November 14, 2026 did not read them again.

- [Upcoming requirements](https://developer.apple.com/news/upcoming-requirements/) and [Submit your apps](https://developer.apple.com/app-store/submissions/).
- [Apple Developer News](https://developer.apple.com/news/): iPhone Duo (October 5, 2026), License Agreement (October 13 and 29, 2026), App Review Guidelines (October 16, 2026), age rating descriptors (November 2, 2026).
- [Screenshot specifications](https://developer.apple.com/help/app-store-connect/reference/app-information/screenshot-specifications) and [Prepare for iPhone Duo](https://developer.apple.com/iphone-duo/prepare/).
- [Age ratings values and definitions](https://developer.apple.com/help/app-store-connect/reference/app-information/age-ratings-values-and-definitions).
- [App privacy details on the App Store](https://developer.apple.com/app-store/app-privacy-details/).
- [Overview of Accessibility Nutrition Labels](https://developer.apple.com/help/app-store-connect/manage-app-accessibility/overview-of-accessibility-nutrition-labels) and [Larger Text evaluation criteria](https://developer.apple.com/help/app-store-connect/manage-app-accessibility/larger-text-evaluation-criteria).
- [Manage EU Digital Services Act trader requirements](https://developer.apple.com/help/app-store-connect/manage-compliance-information/manage-european-union-digital-services-act-trader-requirements).
- [Declare regulated medical device status](https://developer.apple.com/help/app-store-connect/manage-app-information/declare-regulated-medical-device-status/).
- [App Store localizations](https://developer.apple.com/help/app-store-connect/reference/app-store-localizations) and [Localize app information](https://developer.apple.com/help/app-store-connect/manage-app-information/localize-app-information).
- [App information](https://developer.apple.com/help/app-store-connect/reference/app-information/app-information): ICP filing number.
- [App Review Guidelines](https://developer.apple.com/app-store/review/guidelines/), last updated October 16, 2026.
