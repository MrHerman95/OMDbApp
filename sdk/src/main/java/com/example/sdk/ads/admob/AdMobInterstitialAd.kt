package com.example.sdk.ads.admob

import android.app.Activity
import com.example.sdk.ads.provider.InterstitialAd
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.AdError as GmsAdError
import com.google.android.gms.ads.interstitial.InterstitialAd as GmsInterstitialAd

internal class AdMobInterstitialAd(
    private val ad: GmsInterstitialAd
) : InterstitialAd {

    override fun show(activity: Activity, onDismissed: () -> Unit) {
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                onDismissed()
            }

            override fun onAdFailedToShowFullScreenContent(error: GmsAdError) {
                onDismissed()
            }
        }
        ad.show(activity)
    }
}
