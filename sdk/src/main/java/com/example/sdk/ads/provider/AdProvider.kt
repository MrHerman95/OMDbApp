package com.example.sdk.ads.provider

import android.content.Context

/**
 * One ad network (AdMob, AppLovin, Unity, ...). Adding a network means implementing this
 * interface and registering it in [com.example.sdk.ads.AdsConfig.providers]; nothing else in
 * the SDK or the app has to change.
 */
interface AdProvider {
    val name: String

    suspend fun initialize(context: Context)

    suspend fun loadInterstitialAd(context: Context): AdResult<InterstitialAd>

    suspend fun loadNativeAd(context: Context): AdResult<NativeAd>
}
