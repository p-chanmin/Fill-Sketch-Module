package com.dev.philo.fillsketch.feature.main.manager

import android.app.Activity
import android.content.Context
import com.dev.philo.fillsketch.feature.main.BuildConfig
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class AdMobManager(private val context: Context) {

    private val adRequest = AdRequest.Builder().build()

    private var sketchRewardedAd: RewardedAd? = null
    private var magicRewardedAd: RewardedAd? = null
    private var interstitialAd: InterstitialAd? = null

    init {
        loadSketchRewardAd()
        loadMagicRewardAd()
        loadInterstitialRewardAd()
    }

    fun showSketchRewardAd(
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        sketchRewardedAd?.let { ad ->
            ad.show(context as Activity) { onSuccess() }
        } ?: run { onFailure() }
        loadSketchRewardAd()
    }

    fun showMagicRewardAd(
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        magicRewardedAd?.let { ad ->
            ad.show(context as Activity) { onSuccess() }
        } ?: run { onFailure() }
        loadMagicRewardAd()
    }

    fun showInterstitialRewardAd() {
        interstitialAd?.show(context as Activity)
        loadInterstitialRewardAd()
    }

    private fun loadSketchRewardAd() {
        RewardedAd.load(
            context,
            BuildConfig.ADMOB_REWARDED_SKETCH_ID,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    sketchRewardedAd = null
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    sketchRewardedAd = ad
                }
            }
        )
    }

    private fun loadMagicRewardAd() {
        RewardedAd.load(
            context,
            BuildConfig.ADMOB_REWARDED_MAGIC_ID,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    magicRewardedAd = null
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    magicRewardedAd = ad
                }
            }
        )
    }

    private fun loadInterstitialRewardAd() {
        InterstitialAd.load(
            context,
            BuildConfig.ADMOB_INTERSTITIAL_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd = null
                }

                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }
            }
        )
    }
}