package com.example.sdk.ads.nativead

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * Renders the ad of [slot], or nothing while it's loading or when no ad is available, so a
 * missing ad never leaves an empty gap in the list.
 */
@Composable
fun NativeAdSlot(
    controller: NativeAdsController,
    slot: Int,
    modifier: Modifier = Modifier
) {
    val adFlow = remember(controller, slot) { controller.ad(slot) }
    val ad by adFlow.collectAsStateWithLifecycle()
    ad?.Content(modifier)
}
