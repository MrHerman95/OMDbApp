package com.example.sdk.ads.admob

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.isVisible
import com.example.sdk.R
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.gms.ads.nativead.NativeAd as GmsNativeAd

/**
 * AdMob only tracks clicks and impressions on real Android views registered with a
 * [NativeAdView], so the ad is inflated from XML and hosted in Compose. Colors come from the
 * surrounding Material theme so the ad matches the list in light and dark mode.
 */
@Composable
internal fun AdMobNativeAdView(
    nativeAd: GmsNativeAd,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    Card(modifier = modifier.fillMaxWidth()) {
        AndroidView(
            factory = { context ->
                // AndroidView sets the layout params itself, so there's no parent to inflate into
                @SuppressLint("InflateParams")
                val view = LayoutInflater.from(context).inflate(R.layout.admob_native_ad, null)
                view as NativeAdView
            },
            update = { view -> view.bind(nativeAd, colorScheme) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private fun NativeAdView.bind(ad: GmsNativeAd, colors: ColorScheme) {
    val headline = findViewById<TextView>(R.id.ad_headline)
    val attribution = findViewById<TextView>(R.id.ad_attribution)
    val advertiser = findViewById<TextView>(R.id.ad_advertiser)
    val body = findViewById<TextView>(R.id.ad_body)
    val media = findViewById<MediaView>(R.id.ad_media)
    val callToAction = findViewById<Button>(R.id.ad_call_to_action)

    headline.setTextColor(colors.onSurface.toArgb())
    advertiser.setTextColor(colors.onSurfaceVariant.toArgb())
    body.setTextColor(colors.onSurfaceVariant.toArgb())
    media.backgroundTintList = ColorStateList.valueOf(colors.surfaceVariant.toArgb())
    attribution.setTextColor(colors.onTertiaryContainer.toArgb())
    attribution.backgroundTintList = ColorStateList.valueOf(colors.tertiaryContainer.toArgb())
    callToAction.setTextColor(colors.onPrimary.toArgb())
    callToAction.backgroundTintList = ColorStateList.valueOf(colors.primary.toArgb())

    // Re-registering the same ad on every recomposition could count extra impressions
    if (tag === ad) return
    tag = ad

    headlineView = headline
    advertiserView = advertiser
    bodyView = body
    mediaView = media
    callToActionView = callToAction

    headline.text = ad.headline
    // The second line has room for one of them; the advertiser is shorter, so it goes first
    advertiser.text = ad.advertiser
    advertiser.isVisible = ad.advertiser != null
    body.text = ad.body
    body.isVisible = ad.advertiser == null && ad.body != null
    // Scaled to fit so the whole image is visible; wide images leave bands of the slot color
    media.clipToOutline = true
    media.setImageScaleType(ImageView.ScaleType.FIT_CENTER)
    ad.mediaContent?.let { media.mediaContent = it }
    callToAction.isAllCaps = false
    callToAction.text = ad.callToAction
    callToAction.isVisible = ad.callToAction != null

    setNativeAd(ad)
}
