package com.example.data.service

/**
 * Google AdMob Production IDs for JOMAMBO - Watch, Task & Earn.
 */
object AdService {
    const val APP_ID: String = "ca-app-pub-5595332733826231~5187808065"
    const val bannerAdUnitId: String = "ca-app-pub-5595332733826231/7663771475"
    const val interstitialAdUnitId: String = "ca-app-pub-5595332733826231/2178501341"
    const val rewardedAdUnitId: String = "ca-app-pub-5595332733826231/2178501341"

    fun getBannerAdUnitId(): String = bannerAdUnitId
    fun getInterstitialAdUnitId(): String = interstitialAdUnitId
    fun getRewardedAdUnitId(): String = rewardedAdUnitId
}
