package com.example.sdk.ads.admob

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.sdk.ads.provider.NativeAd
import com.google.android.gms.ads.nativead.NativeAd as GmsNativeAd

internal class AdMobNativeAd(
    private val ad: GmsNativeAd
) : NativeAd {

    @Composable
    override fun Content(modifier: Modifier) {
        AdMobNativeAdView(nativeAd = ad, modifier = modifier)
    }

    override fun destroy() {
        ad.destroy()
    }
}
