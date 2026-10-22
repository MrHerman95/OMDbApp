package com.example.sdk.ads.internal

import android.util.Log

/** Filter Logcat by the "AdsSdk" tag to follow what the SDK loads and shows. */
internal object AdsLog {
    private const val TAG = "AdsSdk"

    fun d(message: String) {
        Log.d(TAG, message)
    }

    fun w(message: String, throwable: Throwable? = null) {
        Log.w(TAG, message, throwable)
    }
}
