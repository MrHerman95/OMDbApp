package com.example.sdk.ads.internal

import android.app.Application
import android.os.SystemClock
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.sdk.ads.InterstitialConfig
import com.example.sdk.ads.provider.AdResult
import com.example.sdk.ads.provider.InterstitialAd
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

/**
 * Shows one interstitial the [InterstitialConfig.showOnAppOpen]-th time the user opens the app.
 *
 * An "open" is the whole app moving to the foreground, as reported by [ProcessLifecycleOwner]:
 * the launch, then every return from the background. Rotations and moving between screens don't
 * count. The ad is loaded up front so it's ready by the time it's needed.
 */
internal class AppOpenInterstitialController(
    private val config: InterstitialConfig,
    private val adLoader: WaterfallAdLoader,
    private val scope: CoroutineScope
) : DefaultLifecycleObserver {

    private val processLifecycle = ProcessLifecycleOwner.get().lifecycle
    private val activityTracker = ResumedActivityTracker()

    private var openCount = 0
    private var preloadedAd: Deferred<LoadedAd?>? = null

    fun start(application: Application) {
        activityTracker.register(application)
        processLifecycle.addObserver(this)
        preloadedAd = scope.async { loadAd() }
    }

    override fun onStart(owner: LifecycleOwner) {
        openCount++
        AdsLog.d("App open #$openCount")
        if (openCount != config.showOnAppOpen) return

        // The ad shows at most once, so later opens don't need to be counted
        processLifecycle.removeObserver(this)
        AdsLog.d("App open #$openCount: showing interstitial")
        scope.launch { showAd() }
    }

    private suspend fun showAd() {
        val ad = withTimeoutOrNull(config.loadTimeoutMillis) {
            val preloaded = preloadedAd?.await()?.takeIf { it.isFresh }
            (preloaded ?: loadAd())?.ad
        }
        preloadedAd = null
        if (ad == null) {
            AdsLog.d("Interstitial skipped: no ad within ${config.loadTimeoutMillis} ms")
            return
        }

        val activity = withTimeoutOrNull(ACTIVITY_TIMEOUT_MILLIS) {
            activityTracker.awaitResumedActivity()
        }
        // The user may have left again while the ad was loading
        if (activity == null || !processLifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            AdsLog.d("Interstitial skipped: app is no longer in the foreground")
            return
        }
        ad.show(activity) { AdsLog.d("Interstitial dismissed") }
    }

    private suspend fun loadAd(): LoadedAd? {
        val result = adLoader.loadInterstitialAd() as? AdResult.Success ?: return null
        return LoadedAd(ad = result.ad, loadedAtMillis = SystemClock.elapsedRealtime())
    }

    private class LoadedAd(val ad: InterstitialAd, private val loadedAtMillis: Long) {
        // Loaded interstitials expire after an hour, so a slightly younger one is treated as stale
        val isFresh: Boolean
            get() = SystemClock.elapsedRealtime() - loadedAtMillis < AD_EXPIRY_MILLIS
    }

    private companion object {
        const val AD_EXPIRY_MILLIS = 55 * 60 * 1000L
        const val ACTIVITY_TIMEOUT_MILLIS = 2_000L
    }
}
