package com.example.sdk.ads.admob

import android.content.Context
import com.example.sdk.ads.internal.AdsLog
import com.example.sdk.ads.provider.AdError
import com.example.sdk.ads.provider.AdProvider
import com.example.sdk.ads.provider.AdResult
import com.example.sdk.ads.provider.InterstitialAd
import com.example.sdk.ads.provider.NativeAd
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAdOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import com.google.android.gms.ads.interstitial.InterstitialAd as GmsInterstitialAd

class AdMobAdProvider(
    private val config: AdMobConfig
) : AdProvider {

    override val name: String = "AdMob"

    override suspend fun initialize(context: Context) {
        // Google recommends initializing off the main thread to avoid slowing down app start
        withContext(Dispatchers.IO) {
            suspendCancellableCoroutine { continuation ->
                MobileAds.initialize(context) { continuation.resume(Unit) }
            }
        }
    }

    override suspend fun loadInterstitialAd(context: Context): AdResult<InterstitialAd> {
        // AdMob requires ad requests to be made on the main thread
        return withContext(Dispatchers.Main) {
            suspendCancellableCoroutine { continuation ->
                GmsInterstitialAd.load(
                    context,
                    config.interstitialAdUnitId,
                    AdRequest.Builder().build(),
                    object : InterstitialAdLoadCallback() {
                        override fun onAdLoaded(ad: GmsInterstitialAd) {
                            continuation.resume(AdResult.Success(AdMobInterstitialAd(ad)))
                        }

                        override fun onAdFailedToLoad(error: LoadAdError) {
                            continuation.resume(error.toFailure(format = "interstitial"))
                        }
                    }
                )
            }
        }
    }

    override suspend fun loadNativeAd(context: Context): AdResult<NativeAd> {
        return withContext(Dispatchers.Main) {
            suspendCancellableCoroutine { continuation ->
                val adLoader = AdLoader.Builder(context, config.nativeAdUnitId)
                    .forNativeAd { ad ->
                        if (continuation.isActive) {
                            continuation.resume(AdResult.Success(AdMobNativeAd(ad)))
                        } else {
                            // Nobody is waiting for it anymore
                            ad.destroy()
                        }
                    }
                    .withAdListener(object : AdListener() {
                        override fun onAdFailedToLoad(error: LoadAdError) {
                            continuation.resume(error.toFailure(format = "native"))
                        }
                    })
                    .withNativeAdOptions(
                        NativeAdOptions.Builder()
                            .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
                            .build()
                    )
                    .build()
                adLoader.loadAd(AdRequest.Builder().build())
            }
        }
    }

    private fun LoadAdError.toFailure(format: String): AdResult.Failure {
        AdsLog.w("$name: $format ad failed to load, code $code: $message")
        val error = when (code) {
            AdRequest.ERROR_CODE_NO_FILL,
            AdRequest.ERROR_CODE_MEDIATION_NO_FILL -> AdError.NO_FILL
            AdRequest.ERROR_CODE_NETWORK_ERROR -> AdError.NETWORK_ERROR
            AdRequest.ERROR_CODE_INVALID_REQUEST,
            AdRequest.ERROR_CODE_REQUEST_ID_MISMATCH -> AdError.INVALID_REQUEST
            AdRequest.ERROR_CODE_APP_ID_MISSING,
            AdRequest.ERROR_CODE_INVALID_AD_STRING -> AdError.NOT_CONFIGURED
            AdRequest.ERROR_CODE_INTERNAL_ERROR -> AdError.INTERNAL_ERROR
            else -> AdError.UNKNOWN
        }
        return AdResult.Failure(error)
    }
}
