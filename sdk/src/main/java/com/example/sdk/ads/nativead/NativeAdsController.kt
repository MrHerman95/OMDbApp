package com.example.sdk.ads.nativead

import com.example.sdk.ads.internal.WaterfallAdLoader
import com.example.sdk.ads.provider.AdResult
import com.example.sdk.ads.provider.NativeAd
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Owns the native ads of one screen: loads one ad per slot of [placement] as soon as it's
 * created, and keeps them while the list scrolls or the screen rotates.
 *
 * Create it with [com.example.sdk.ads.AdsSdk.createNativeAdsController] from a ViewModel and
 * call [destroy] in `onCleared`.
 */
class NativeAdsController internal constructor(
    val placement: NativeAdPlacement,
    adLoader: WaterfallAdLoader,
    scope: CoroutineScope
) {

    private val slotAds = List(placement.slotCount) { MutableStateFlow<NativeAd?>(null) }
    private var isDestroyed = false

    init {
        slotAds.forEach { slotAd ->
            scope.launch {
                val ad = (adLoader.loadNativeAd() as? AdResult.Success)?.ad ?: return@launch
                if (isDestroyed) ad.destroy() else slotAd.value = ad
            }
        }
    }

    /** The ad of [slot], or `null` while it loads or when no provider could fill it. */
    fun ad(slot: Int): StateFlow<NativeAd?> {
        return slotAds.getOrNull(slot)?.asStateFlow() ?: NO_AD
    }

    /** Releases every loaded ad. The controller can't be used afterwards. */
    fun destroy() {
        isDestroyed = true
        slotAds.forEach { slotAd ->
            slotAd.value?.destroy()
            slotAd.value = null
        }
    }

    private companion object {
        val NO_AD: StateFlow<NativeAd?> = MutableStateFlow(null)
    }
}
