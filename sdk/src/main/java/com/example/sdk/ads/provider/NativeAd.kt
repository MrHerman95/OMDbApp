package com.example.sdk.ads.provider

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface NativeAd {
    @Composable
    fun Content(modifier: Modifier)

    fun destroy()
}
