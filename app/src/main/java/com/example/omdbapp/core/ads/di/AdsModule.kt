package com.example.omdbapp.core.ads.di

import com.example.sdk.ads.AdsConfig
import com.example.sdk.ads.AdsSdk
import com.example.sdk.ads.InterstitialConfig
import com.example.sdk.ads.admob.AdMobAdProvider
import com.example.sdk.ads.admob.AdMobConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AdsModule {

    @Provides
    @Singleton
    fun provideAdsSdk(): AdsSdk {
        return AdsSdk(
            AdsConfig(
                // Add more providers here; each ad request falls through them in this order
                providers = listOf(
                    AdMobAdProvider(AdMobConfig.Test)
                ),
                interstitial = InterstitialConfig(showOnAppOpen = 2)
            )
        )
    }
}
