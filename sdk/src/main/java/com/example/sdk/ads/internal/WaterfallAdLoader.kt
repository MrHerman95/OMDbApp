package com.example.sdk.ads.internal

import android.content.Context
import com.example.sdk.ads.provider.AdError
import com.example.sdk.ads.provider.AdProvider
import com.example.sdk.ads.provider.AdResult
import com.example.sdk.ads.provider.InterstitialAd
import com.example.sdk.ads.provider.NativeAd
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred

/**
 * Owns the providers: initializes them, then asks them one by one, in priority order, for an
 * ad and returns the first one that loads. Requests wait until initialization has finished.
 */
internal class WaterfallAdLoader(
    private val providers: List<AdProvider>,
    private val context: Context
) {

    private val initialization = CompletableDeferred<Unit>()

    suspend fun initializeProviders() {
        providers.forEach { provider ->
            try {
                provider.initialize(context)
                AdsLog.d("${provider.name}: initialized")
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                AdsLog.w("${provider.name}: initialization failed", e)
            }
        }
        initialization.complete(Unit)
    }

    suspend fun loadInterstitialAd(): AdResult<InterstitialAd> {
        return load(format = "interstitial") { loadInterstitialAd(context) }
    }

    suspend fun loadNativeAd(): AdResult<NativeAd> {
        return load(format = "native") { loadNativeAd(context) }
    }

    private suspend fun <T> load(
        format: String,
        request: suspend AdProvider.() -> AdResult<T>
    ): AdResult<T> {
        initialization.await()

        var lastFailure = AdResult.Failure(AdError.NOT_CONFIGURED)
        for (provider in providers) {
            val result = try {
                provider.request()
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                AdsLog.w("${provider.name}: $format ad crashed while loading", e)
                AdResult.Failure(AdError.INTERNAL_ERROR)
            }
            when (result) {
                is AdResult.Success -> {
                    AdsLog.d("${provider.name}: $format ad loaded")
                    return result
                }
                is AdResult.Failure -> {
                    AdsLog.d("${provider.name}: $format ad failed (${result.error}), trying next provider")
                    lastFailure = result
                }
            }
        }
        return lastFailure
    }
}
