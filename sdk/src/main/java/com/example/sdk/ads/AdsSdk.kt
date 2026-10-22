package com.example.sdk.ads

import android.app.Application
import androidx.annotation.MainThread
import com.example.sdk.ads.internal.AppOpenInterstitialController
import com.example.sdk.ads.internal.WaterfallAdLoader
import com.example.sdk.ads.nativead.NativeAdPlacement
import com.example.sdk.ads.nativead.NativeAdsController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Entry point of the ads SDK. Create one instance for the whole app, call [initialize] from
 * `Application.onCreate`, and the SDK takes care of the rest:
 * - the interstitial shown when the user opens the app again (see [InterstitialConfig]),
 * - native ads for lists, through [createNativeAdsController].
 */
class AdsSdk(private val config: AdsConfig) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var adLoader: WaterfallAdLoader? = null

    @MainThread
    fun initialize(application: Application) {
        if (adLoader != null) return

        val loader = WaterfallAdLoader(config.providers, application)
        adLoader = loader
        scope.launch { loader.initializeProviders() }

        AppOpenInterstitialController(config.interstitial, loader, scope).start(application)
    }

    /**
     * @param scope usually a `viewModelScope`; loading stops when it's cancelled.
     */
    fun createNativeAdsController(
        placement: NativeAdPlacement,
        scope: CoroutineScope
    ): NativeAdsController {
        val loader = checkNotNull(adLoader) { "Call AdsSdk.initialize() before requesting ads" }
        return NativeAdsController(placement = placement, adLoader = loader, scope = scope)
    }
}
