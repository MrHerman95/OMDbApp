package com.example.sdk.ads

import com.example.sdk.ads.provider.AdProvider

/**
 * @param providers ad networks in priority order. Each ad request tries them one after another
 * until one of them fills it (a "waterfall").
 */
data class AdsConfig(
    val providers: List<AdProvider>,
    val interstitial: InterstitialConfig = InterstitialConfig()
)

/**
 * @param showOnAppOpen which time the user opens the app shows the interstitial. The launch is
 * open #1, and every return from the background (after minimizing it or switching apps) is the
 * next one, so the default of 2 shows the ad the first time the user comes back. The ad is shown
 * at most once per app process.
 * @param loadTimeoutMillis how long to wait for an ad that wasn't ready in time before giving up,
 * so an ad doesn't pop up long after the user came back.
 */
data class InterstitialConfig(
    val showOnAppOpen: Int = 2,
    val loadTimeoutMillis: Long = 5_000L
) {
    init {
        require(showOnAppOpen >= 1) { "showOnAppOpen must be 1 or greater" }
    }
}
