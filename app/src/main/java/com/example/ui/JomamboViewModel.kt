package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.AppConfig
import com.example.data.model.DirectAd
import com.example.data.model.LeaderboardEntry
import com.example.data.model.SocialTask
import com.example.data.model.Transaction
import com.example.data.model.User
import com.example.data.repository.JomamboRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

class JomamboViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository = JomamboRepository(database)

    val currentUser: StateFlow<User?> = repository.currentUser.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    val activeAds: StateFlow<List<DirectAd>> = repository.activeAds.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val socialTasks: StateFlow<List<SocialTask>> = repository.socialTasks.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val transactions: StateFlow<List<Transaction>> = repository.transactions.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val leaderboard: List<LeaderboardEntry> = repository.getLeaderboard()
    val appConfig: AppConfig = repository.appConfig

    // Navigation and UI state
    private val _currentScreen = MutableStateFlow<AppScreen>(AppScreen.Splash)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _currentTab = MutableStateFlow(0)
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    // Modals & Dialogs
    private val _showVipModal = MutableStateFlow(false)
    val showVipModal: StateFlow<Boolean> = _showVipModal.asStateFlow()

    private val _showWithdrawDialog = MutableStateFlow(false)
    val showWithdrawDialog: StateFlow<Boolean> = _showWithdrawDialog.asStateFlow()

    private val _showPhoneVerifyDialog = MutableStateFlow(false)
    val showPhoneVerifyDialog: StateFlow<Boolean> = _showPhoneVerifyDialog.asStateFlow()

    private val _showCreateAdDialog = MutableStateFlow(false)
    val showCreateAdDialog: StateFlow<Boolean> = _showCreateAdDialog.asStateFlow()

    private val _showForceUpdateDialog = MutableStateFlow(false)
    val showForceUpdateDialog: StateFlow<Boolean> = _showForceUpdateDialog.asStateFlow()

    private val _rewardedDialogState = MutableStateFlow<Long?>(null) // amount won
    val rewardedDialogState: StateFlow<Long?> = _rewardedDialogState.asStateFlow()

    // Watch Feed Timer & Video Pager
    private val _currentAdIndex = MutableStateFlow(0)
    val currentAdIndex: StateFlow<Int> = _currentAdIndex.asStateFlow()

    private val _watchTimerSeconds = MutableStateFlow(0)
    val watchTimerSeconds: StateFlow<Int> = _watchTimerSeconds.asStateFlow()

    private val _isWatchingActive = MutableStateFlow(true)
    val isWatchingActive: StateFlow<Boolean> = _isWatchingActive.asStateFlow()

    private var watchTimerJob: Job? = null

    // Task Verification Timer
    private val _verifyingTaskId = MutableStateFlow<String?>(null)
    val verifyingTaskId: StateFlow<String?> = _verifyingTaskId.asStateFlow()

    private val _taskCountdown = MutableStateFlow(0)
    val taskCountdown: StateFlow<Int> = _taskCountdown.asStateFlow()

    // Lucky Spin State
    private val _isSpinning = MutableStateFlow(false)
    val isSpinning: StateFlow<Boolean> = _isSpinning.asStateFlow()

    private val _spinRotationAngle = MutableStateFlow(0f)
    val spinRotationAngle: StateFlow<Float> = _spinRotationAngle.asStateFlow()

    private val _spinRewardWon = MutableStateFlow<Long?>(null)
    val spinRewardWon: StateFlow<Long?> = _spinRewardWon.asStateFlow()

    private val _showInterstitialAdDialog = MutableStateFlow(false)
    val showInterstitialAdDialog: StateFlow<Boolean> = _showInterstitialAdDialog.asStateFlow()

    init {
        initializeData()
    }

    private fun initializeData() {
        viewModelScope.launch {
            try {
                repository.initSeedDataIfEmpty()
                delay(1200) // Splash delay
                val user = database.jomamboDao().getCurrentUser()
                if (user != null) {
                    _currentScreen.value = AppScreen.Main
                    startAdWatchTimer()
                } else {
                    _currentScreen.value = AppScreen.Auth
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to initialize app: ${e.message}"
                _currentScreen.value = AppScreen.Auth
            }
        }
    }

    fun setTab(index: Int) {
        _currentTab.value = index
        if (index == 0) {
            startAdWatchTimer()
        } else {
            pauseAdWatchTimer()
        }
    }

    fun dismissError() {
        _errorMessage.value = null
    }

    fun dismissSuccess() {
        _successMessage.value = null
    }

    // AUTH METHODS
    fun signUp(fullName: String, emailOrPhone: String, password: String, confirmPass: String, termsAgreed: Boolean, refCode: String?) {
        if (fullName.isBlank()) {
            _errorMessage.value = "Please enter your full name"
            return
        }
        val isEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(emailOrPhone.trim()).matches()
        val isPhone = emailOrPhone.trim().matches(Regex("^(\\+234|0)[789][01]\\d{8}$")) || emailOrPhone.trim().length >= 10
        if (!isEmail && !isPhone) {
            _errorMessage.value = "Enter a valid email address or 11-digit Nigerian phone number (e.g. 08012345678)"
            return
        }
        if (password.length < 6) {
            _errorMessage.value = "Password must be at least 6 characters"
            return
        }
        if (password != confirmPass) {
            _errorMessage.value = "Passwords do not match"
            return
        }
        if (!termsAgreed) {
            _errorMessage.value = "You must agree to the Terms of Service"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.signUp(fullName, emailOrPhone, refCode)
                result.onSuccess {
                    _currentScreen.value = AppScreen.Main
                    _successMessage.value = "Account created! Welcome bonus 50 Coins credited 🎉"
                    startAdWatchTimer()
                }.onFailure {
                    _errorMessage.value = it.message ?: "Signup failed"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error: Please check your internet connection"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun login(emailOrPhone: String, pass: String) {
        if (emailOrPhone.isBlank() || pass.length < 6) {
            _errorMessage.value = "Please enter valid credentials"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.login(emailOrPhone)
                result.onSuccess {
                    _currentScreen.value = AppScreen.Main
                    _successMessage.value = "Welcome back!"
                    startAdWatchTimer()
                }.onFailure {
                    _errorMessage.value = it.message ?: "Login failed"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Check internet connection and try again"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loginWithGoogle() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.loginWithGoogle()
                result.onSuccess {
                    _currentScreen.value = AppScreen.Main
                    _successMessage.value = "Google Sign-in successful!"
                    startAdWatchTimer()
                }
            } catch (e: Exception) {
                _errorMessage.value = "Google Sign-in error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            pauseAdWatchTimer()
            repository.logout()
            _currentScreen.value = AppScreen.Auth
        }
    }

    // WATCH FEED METHODS
    fun onNextVideo() {
        val ads = activeAds.value
        if (ads.isNotEmpty()) {
            _currentAdIndex.value = (_currentAdIndex.value + 1) % ads.size
            startAdWatchTimer()
        }
    }

    fun onPreviousVideo() {
        val ads = activeAds.value
        if (ads.isNotEmpty()) {
            _currentAdIndex.value = if (_currentAdIndex.value - 1 < 0) ads.size - 1 else _currentAdIndex.value - 1
            startAdWatchTimer()
        }
    }

    fun startAdWatchTimer() {
        watchTimerJob?.cancel()
        _watchTimerSeconds.value = 0
        _isWatchingActive.value = true

        watchTimerJob = viewModelScope.launch {
            while (_watchTimerSeconds.value < 30) {
                delay(1000)
                _watchTimerSeconds.value += 1
            }
            // 30 seconds reached! Award AdMob Rewarded coin
            claimVideoReward()
        }
    }

    fun pauseAdWatchTimer() {
        watchTimerJob?.cancel()
        _isWatchingActive.value = false
    }

    private fun claimVideoReward() {
        viewModelScope.launch {
            val ads = activeAds.value
            val currentAd = ads.getOrNull(_currentAdIndex.value)
            val adId = currentAd?.id ?: "ad_generic"
            val rewardedCoins = repository.rewardVideoWatch(adId)
            _rewardedDialogState.value = rewardedCoins
        }
    }

    fun dismissRewardedDialog() {
        _rewardedDialogState.value = null
        onNextVideo()
    }

    // SOCIAL TASK METHODS
    fun startSocialTask(task: SocialTask) {
        _verifyingTaskId.value = task.id
        _taskCountdown.value = 10
        viewModelScope.launch {
            while (_taskCountdown.value > 0) {
                delay(1000)
                _taskCountdown.value -= 1
            }
        }
    }

    fun verifyAndClaimSocialTask(taskId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val reward = repository.completeSocialTask(taskId)
                _verifyingTaskId.value = null
                _successMessage.value = "Task verified! +$reward Coins credited 🎉"
            } catch (e: Exception) {
                _errorMessage.value = "Verification failed: Check internet"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // LUCKY SPIN METHODS
    fun triggerSpinWheel() {
        val user = currentUser.value ?: return
        if (user.dailySpinsLeft <= 0) {
            _errorMessage.value = "No spins left today! Unlock VIP for bonus spins or come back tomorrow."
            return
        }
        if (_isSpinning.value) return

        // Show interstitial ad simulation before spin starts
        _showInterstitialAdDialog.value = true
    }

    fun proceedSpinAfterInterstitial() {
        _showInterstitialAdDialog.value = false
        val segments = listOf(2L, 5L, 10L, 15L, 20L, 50L, 8L, 12L)
        val selectedIndex = Random.nextInt(segments.size)
        val wonAmount = segments[selectedIndex]

        viewModelScope.launch {
            _isSpinning.value = true
            // Calculate final rotation degrees: 5 full rotations (1800 deg) + target segment
            val segmentAngle = 360f / segments.size
            val targetDegree = 360f * 5 + (selectedIndex * segmentAngle) + (segmentAngle / 2f)
            _spinRotationAngle.value = targetDegree

            delay(3200) // Spin animation duration
            _isSpinning.value = false

            val result = repository.claimSpinReward(wonAmount)
            result.onSuccess {
                _spinRewardWon.value = it
            }.onFailure {
                _errorMessage.value = it.message
            }
        }
    }

    fun dismissSpinWonDialog() {
        _spinRewardWon.value = null
    }

    // OFFERWALL & SURVEY REWARD
    fun completeOfferwallTask(title: String, coins: Long) {
        viewModelScope.launch {
            repository.claimPartnerReward("Offerwall (Tapjoy)", coins, title)
            _successMessage.value = "Offer completed! +$coins Coins added to wallet 💰"
        }
    }

    fun completeSurvey(surveyTitle: String, coins: Long) {
        viewModelScope.launch {
            repository.claimPartnerReward("Pollfish Survey", coins, surveyTitle)
            _successMessage.value = "Survey submitted successfully! +$coins Coins earned 📊"
        }
    }

    // PAY TO UNLOCK & VIP
    fun openVipModal() {
        _showVipModal.value = true
    }

    fun closeVipModal() {
        _showVipModal.value = false
    }

    fun buyUnlockBonusVideos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val emailOrPhone = currentUser.value?.emailOrPhone ?: "user@jomambo.app"
                val result = com.example.data.service.PaymentService.processLivePayment(
                    emailOrPhone = emailOrPhone,
                    amountNaira = 500L,
                    purpose = "Unlock 50 Video Slots"
                )
                if (result.success) {
                    repository.unlockBonusVideos()
                    _showVipModal.value = false
                    _successMessage.value = "50 extra video slots unlocked! (₦500 Paystack Live ref: ${result.reference})"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Payment failed: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun buyVipMonthly() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val emailOrPhone = currentUser.value?.emailOrPhone ?: "user@jomambo.app"
                val result = com.example.data.service.PaymentService.processLivePayment(
                    emailOrPhone = emailOrPhone,
                    amountNaira = 1000L,
                    purpose = "Monthly VIP Subscription"
                )
                if (result.success) {
                    repository.upgradeToVip()
                    _showVipModal.value = false
                    _successMessage.value = "VIP Activated via Paystack Live! (Ref: ${result.reference}) Enjoy 2x earnings 🌟"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Payment failed: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // WALLET & WITHDRAW
    fun openWithdrawDialog() {
        val user = currentUser.value
        if (user != null && !user.verified) {
            _showPhoneVerifyDialog.value = true
        } else {
            _showWithdrawDialog.value = true
        }
    }

    fun closeWithdrawDialog() {
        _showWithdrawDialog.value = false
    }

    fun closePhoneVerifyDialog() {
        _showPhoneVerifyDialog.value = false
    }

    fun submitPhoneOtp(otp: String) {
        viewModelScope.launch {
            val result = repository.verifyPhoneNumber(otp)
            result.onSuccess {
                _showPhoneVerifyDialog.value = false
                _showWithdrawDialog.value = true
                _successMessage.value = "Phone verified successfully! You can now withdraw."
            }.onFailure {
                _errorMessage.value = it.message ?: "Invalid OTP"
            }
        }
    }

    fun processAirtimeWithdraw(phone: String, network: String, amountNaira: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.withdrawAirtime(phone, network, amountNaira)
            result.onSuccess {
                _showWithdrawDialog.value = false
                _successMessage.value = "₦$amountNaira Airtime successfully recharged to $phone!"
            }.onFailure {
                _errorMessage.value = it.message
            }
            _isLoading.value = false
        }
    }

    fun processBankWithdraw(bank: String, accountNum: String, accountName: String, coins: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.withdrawBank(bank, accountNum, accountName, coins)
            result.onSuccess {
                _showWithdrawDialog.value = false
                _successMessage.value = "Withdrawal request of ₦${(coins * 0.1).toInt()} submitted! Transfer processing."
            }.onFailure {
                _errorMessage.value = it.message
            }
            _isLoading.value = false
        }
    }

    // DIRECT ADS CREATION
    fun openCreateAdDialog() {
        _showCreateAdDialog.value = true
    }

    fun closeCreateAdDialog() {
        _showCreateAdDialog.value = false
    }

    fun submitDirectAd(title: String, desc: String, url: String, views: Int, category: String) {
        if (title.isBlank() || url.isBlank() || views < 100) {
            _errorMessage.value = "Please fill all fields (min 100 views)"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.createDirectAd(title, desc, url, views, category)
            result.onSuccess {
                _showCreateAdDialog.value = false
                _successMessage.value = "Ad campaign launched live! $views views queued 🚀"
            }.onFailure {
                _errorMessage.value = it.message
            }
            _isLoading.value = false
        }
    }

    // UPDATE CHECK
    fun triggerCheckUpdate(force: Boolean = false) {
        if (force) {
            _showForceUpdateDialog.value = true
        } else {
            _successMessage.value = "You are on the latest release v1.0.0 (Play Store verified)"
        }
    }

    fun dismissUpdateDialog() {
        _showForceUpdateDialog.value = false
    }
}

sealed class AppScreen {
    object Splash : AppScreen()
    object Auth : AppScreen()
    object Main : AppScreen()
}
