package com.pause.app.ads

import android.content.Context
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class AdManager(private val context: Context) {
    companion object {
        // AdMob Ad Unit IDs for Pause App
        // App ID: ca-app-pub-5374221916375781~8498128651
        private const val NATIVE_AD_UNIT_ID = "ca-app-pub-5374221916375781~8498128651" // Native ads for digest screen
        private const val BANNER_AD_UNIT_ID = "ca-app-pub-5374221916375781~8498128651" // Banner ads for settings
        private const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-5374221916375781~8498128651" // Interstitial ads on exit
    }

    init {
        MobileAds.initialize(context) {}
    }

    /**
     * Load a native ad for displaying in the digest screen
     * @param onAdLoaded Callback when ad is successfully loaded
     * @param onAdFailed Callback if ad loading fails
     */
    fun loadNativeAd(
        onAdLoaded: (NativeAd) -> Unit,
        onAdFailed: (String) -> Unit = {}
    ) {
        val adLoader = AdLoader.Builder(context, NATIVE_AD_UNIT_ID)
            .forNativeAd { nativeAd ->
                onAdLoaded(nativeAd)
            }
            .withAdListener(object : com.google.android.gms.ads.AdListener() {
                override fun onAdFailedToLoad(error: com.google.android.gms.ads.LoadAdError) {
                    super.onAdFailedToLoad(error)
                    onAdFailed("Ad failed to load: ${error.message}")
                }
            })
            .build()
        
        adLoader.loadAd(AdRequest.Builder().build())
    }

    /**
     * Load an interstitial ad for displaying full-screen ads
     * @param onAdLoaded Callback when ad is successfully loaded
     * @param onAdFailed Callback if ad loading fails
     */
    fun loadInterstitialAd(
        onAdLoaded: (InterstitialAd) -> Unit,
        onAdFailed: (String) -> Unit = {}
    ) {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            INTERSTITIAL_AD_UNIT_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    super.onAdLoaded(interstitialAd)
                    onAdLoaded(interstitialAd)
                }

                override fun onAdFailedToLoad(adError: com.google.android.gms.ads.LoadAdError) {
                    super.onAdFailedToLoad(adError)
                    onAdFailed("Interstitial ad failed to load: ${adError.message}")
                }
            }
        )
    }

    /**
     * Get banner ad unit ID for banner ads
     */
    fun getBannerAdUnitId(): String = BANNER_AD_UNIT_ID

    /**
     * Get interstitial ad unit ID for full-screen ads
     */
    fun getInterstitialAdUnitId(): String = INTERSTITIAL_AD_UNIT_ID
}
