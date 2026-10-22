package com.example.sdk.ads.admob

/**
 * Ad unit IDs from the AdMob console. The app ID goes in the manifest instead, through the
 * `admobAppId` manifest placeholder.
 */
data class AdMobConfig(
    val interstitialAdUnitId: String,
    val nativeAdUnitId: String
) {
    companion object {
        /**
         * Google's public demo ad units. They always serve test ads, which are safe to click
         * and need no AdMob account. Never ship them in a release build.
         */
        val Test = AdMobConfig(
            interstitialAdUnitId = "ca-app-pub-3940256099942544/1033173712",
            nativeAdUnitId = "ca-app-pub-3940256099942544/2247696110"
        )
    }
}
