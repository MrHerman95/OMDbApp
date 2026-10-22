package com.example.omdbapp

import android.app.Application
import com.example.sdk.ads.AdsSdk
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class OmdbApplication : Application() {

    @Inject
    lateinit var adsSdk: AdsSdk

    override fun onCreate() {
        super.onCreate()
        adsSdk.initialize(this)
    }
}
