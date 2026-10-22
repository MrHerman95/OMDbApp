package com.example.sdk.ads.provider

import android.app.Activity

interface InterstitialAd {
    fun show(activity: Activity, onDismissed: () -> Unit)
}
