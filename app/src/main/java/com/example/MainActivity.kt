package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {

    private var mInterstitial: InterstitialAd? = null
    private var mRewarded: RewardedAd? = null
    private var mRewardedInterstitial: RewardedInterstitialAd? = null
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this)
        loadAllAds()

        setContent {
            var coins by remember { mutableStateOf(0) }
            var nativeAd by remember { mutableStateOf<NativeAd?>(null) }

            // Load native ad once
            LaunchedEffect(Unit) {
                val adLoader = AdLoader.Builder(this@MainActivity, Constants.NATIVE_ID)
                    .forNativeAd { ad -> nativeAd = ad }
                    .withAdListener(object : AdListener(){})
                    .withNativeAdOptions(NativeAdOptions.Builder().build())
                    .build()
                adLoader.loadAd(AdRequest.Builder().build())
            }

            Scaffold(
                bottomBar = {
                    AndroidView(
                        modifier = Modifier.fillMaxWidth().height(60.dp),
                        factory = { ctx ->
                            AdView(ctx).apply {
                                setAdSize(AdSize.BANNER)
                                adUnitId = Constants.BANNER_ID // 7663771475 - REAL
                                loadAd(AdRequest.Builder().build())
                            }
                        }
                    )
                }
            ) { padding ->
                Column(Modifier.padding(padding).padding(16.dp)) {
                    Text("JOMAMBO - Balance: ₦$coins", style = MaterialTheme.typography.headlineMedium)
                    Spacer(Modifier.height(16.dp))

                    // TASK BUTTON - INTERSTITIAL - NO REWARD HERE (PROFIT SAFE)
                    Button(onClick = {
                        if (mInterstitial != null) {
                            mInterstitial?.fullScreenContentCallback = object: FullScreenContentCallback(){
                                override fun onAdDismissedFullScreenContent() {
                                    loadInterstitial()
                                }
                            }
                            mInterstitial?.show(this@MainActivity)
                        }
                        // Task completion logic here, but NO COIN for interstitial
                    }, modifier = Modifier.fillMaxWidth()) { 
                        Text("View Task (Interstitial Ad)") 
                    }

                    Spacer(Modifier.height(10.dp))

                    // SPIN & WIN - REWARDED - COIN ONLY AFTER VIDEO COMPLETE
                    Button(onClick = {
                        mRewarded?.let { ad ->
                            ad.fullScreenContentCallback = object: FullScreenContentCallback(){
                                override fun onAdDismissedFullScreenContent() { loadRewarded() }
                            }
                            ad.show(this@MainActivity) { rewardItem ->
                                // *** MONEY GIVE ONLY HERE - AFTER USER WATCH FINISH - NO LOSS ***
                                coins += Constants.SPIN_REWARD
                                saveToFirebase(Constants.SPIN_REWARD)
                            }
                        }
                    }, modifier = Modifier.fillMaxWidth()) { 
                        Text("Spin & Win +${Constants.SPIN_REWARD} (Rewarded)") 
                    }

                    Spacer(Modifier.height(10.dp))

                    // BONUS - REWARDED INTERSTITIAL - ALSO SAFE
                    Button(onClick = {
                        mRewardedInterstitial?.let { ad ->
                            ad.fullScreenContentCallback = object: FullScreenContentCallback(){
                                override fun onAdDismissedFullScreenContent() { loadRewardedInterstitial() }
                            }
                            ad.show(this@MainActivity) { rewardItem ->
                                coins += 30
                                saveToFirebase(30)
                            }
                        }
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("Bonus Video +30 (Rewarded Interstitial)")
                    }

                    Spacer(Modifier.height(20.dp))

                    // REAL NATIVE AD DISPLAY - 9212567574
                    nativeAd?.let {
                        Text("Sponsored:")
                        AndroidView(
                            modifier = Modifier.fillMaxWidth().height(120.dp),
                            factory = { ctx ->
                                com.google.android.gms.ads.nativead.NativeAdView(ctx).apply {
                                    // Add native ad layout here
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    fun saveToFirebase(amount: Int) {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid)
            .update("wallet", FieldValue.increment(amount.toLong()))
    }

    fun loadAllAds() { loadInterstitial(); loadRewarded(); loadRewardedInterstitial() }

    fun loadInterstitial() {
        InterstitialAd.load(this, Constants.INTERSTITIAL_ID, AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) { mInterstitial = ad }
            })
    }
    fun loadRewarded() {
        RewardedAd.load(this, Constants.REWARDED_ID, AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) { mRewarded = ad }
            })
    }
    fun loadRewardedInterstitial() {
        RewardedInterstitialAd.load(this, Constants.REWARDED_INTERSTITIAL_ID, AdRequest.Builder().build(),
            object : RewardedInterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedInterstitialAd) { mRewardedInterstitial = ad }
            })
    }
}
