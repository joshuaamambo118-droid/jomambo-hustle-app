package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.R
import com.example.ui.auth.AuthScreen
import com.example.ui.components.ForceUpdateDialog
import com.example.ui.components.InterstitialAdDialog
import com.example.ui.components.JomamboBottomNav
import com.example.ui.components.JomamboTopBar
import com.example.ui.components.RewardedAdSuccessDialog
import com.example.ui.home.WatchFeedScreen
import com.example.ui.leaderboard.LeaderboardScreen
import com.example.ui.offerwall.OfferwallScreen
import com.example.ui.spin.SpinWinScreen
import com.example.ui.tasks.TasksScreen
import com.example.ui.theme.JomamboGoldDark
import com.example.ui.theme.JomamboGoldSecondary
import com.example.ui.theme.JomamboGreenEarn
import com.example.ui.theme.JomamboPurpleDark
import com.example.ui.theme.JomamboPurplePrimary
import com.example.ui.vip.VipUnlockDialog
import com.example.ui.wallet.CreateAdDialog
import com.example.ui.wallet.PhoneVerifyDialog
import com.example.ui.wallet.WalletScreen
import com.example.ui.wallet.WithdrawDialog

@Composable
fun MainApp(
    viewModel: JomamboViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val activeAds by viewModel.activeAds.collectAsStateWithLifecycle()
    val socialTasks by viewModel.socialTasks.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val successMessage by viewModel.successMessage.collectAsStateWithLifecycle()

    // Modals
    val showVipModal by viewModel.showVipModal.collectAsStateWithLifecycle()
    val showWithdrawDialog by viewModel.showWithdrawDialog.collectAsStateWithLifecycle()
    val showPhoneVerifyDialog by viewModel.showPhoneVerifyDialog.collectAsStateWithLifecycle()
    val showCreateAdDialog by viewModel.showCreateAdDialog.collectAsStateWithLifecycle()
    val showForceUpdateDialog by viewModel.showForceUpdateDialog.collectAsStateWithLifecycle()
    val rewardedCoinsWon by viewModel.rewardedDialogState.collectAsStateWithLifecycle()
    val showInterstitialAdDialog by viewModel.showInterstitialAdDialog.collectAsStateWithLifecycle()

    // Watch Feed Timer & Video Pager
    val currentAdIndex by viewModel.currentAdIndex.collectAsStateWithLifecycle()
    val watchTimerSeconds by viewModel.watchTimerSeconds.collectAsStateWithLifecycle()
    val isWatchingActive by viewModel.isWatchingActive.collectAsStateWithLifecycle()

    // Tasks State
    val verifyingTaskId by viewModel.verifyingTaskId.collectAsStateWithLifecycle()
    val taskCountdown by viewModel.taskCountdown.collectAsStateWithLifecycle()

    // Spin State
    val isSpinning by viewModel.isSpinning.collectAsStateWithLifecycle()
    val spinAngle by viewModel.spinRotationAngle.collectAsStateWithLifecycle()
    val spinRewardWon by viewModel.spinRewardWon.collectAsStateWithLifecycle()

    when (currentScreen) {
        AppScreen.Splash -> {
            SplashScreen()
        }
        AppScreen.Auth -> {
            AuthScreen(
                isLoading = isLoading,
                onSignUp = { name, emailOrPhone, pass, confirm, terms, ref ->
                    viewModel.signUp(name, emailOrPhone, pass, confirm, terms, ref)
                },
                onLogin = { emailOrPhone, pass ->
                    viewModel.login(emailOrPhone, pass)
                },
                onGoogleSignIn = {
                    viewModel.loginWithGoogle()
                }
            )
        }
        AppScreen.Main -> {
            Scaffold(
                topBar = {
                    JomamboTopBar(
                        user = currentUser,
                        onVipClick = { viewModel.openVipModal() },
                        onLeaderboardClick = { viewModel.setTab(5) },
                        onLogoutClick = { viewModel.logout() }
                    )
                },
                bottomBar = {
                    JomamboBottomNav(
                        selectedTab = if (currentTab > 4) 4 else currentTab,
                        onTabSelected = { viewModel.setTab(it) }
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (currentTab) {
                        0 -> WatchFeedScreen(
                            user = currentUser,
                            ads = activeAds,
                            currentAdIndex = currentAdIndex,
                            watchTimerSeconds = watchTimerSeconds,
                            isWatchingActive = isWatchingActive,
                            onNextAd = { viewModel.onNextVideo() },
                            onPreviousAd = { viewModel.onPreviousVideo() },
                            onOpenVipModal = { viewModel.openVipModal() }
                        )
                        1 -> TasksScreen(
                            user = currentUser,
                            tasks = socialTasks,
                            verifyingTaskId = verifyingTaskId,
                            taskCountdown = taskCountdown,
                            isLoading = isLoading,
                            onStartTask = { viewModel.startSocialTask(it) },
                            onVerifyAndClaim = { viewModel.verifyAndClaimSocialTask(it) }
                        )
                        2 -> OfferwallScreen(
                            onCompleteOffer = { title, coins ->
                                viewModel.completeOfferwallTask(title, coins)
                            },
                            onCompleteSurvey = { title, coins ->
                                viewModel.completeSurvey(title, coins)
                            }
                        )
                        3 -> SpinWinScreen(
                            user = currentUser,
                            isSpinning = isSpinning,
                            spinRotationAngle = spinAngle,
                            rewardWon = spinRewardWon,
                            onTriggerSpin = { viewModel.triggerSpinWheel() },
                            onDismissReward = { viewModel.dismissSpinWonDialog() },
                            onOpenVipModal = { viewModel.openVipModal() }
                        )
                        4 -> WalletScreen(
                            user = currentUser,
                            transactions = transactions,
                            onOpenWithdraw = { viewModel.openWithdrawDialog() },
                            onOpenCreateAd = { viewModel.openCreateAdDialog() }
                        )
                        5 -> LeaderboardScreen(
                            user = currentUser,
                            leaderboard = viewModel.leaderboard
                        )
                    }

                    // Alert / Notification Banners (Errors or Success)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter)
                            .padding(16.dp)
                    ) {
                        AnimatedVisibility(
                            visible = errorMessage != null,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Error,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = errorMessage ?: "",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(
                                        onClick = { viewModel.dismissError() },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Dismiss",
                                            tint = Color.White
                                        )
                                    }
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible = successMessage != null,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = JomamboGreenEarn,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = successMessage ?: "",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(
                                        onClick = { viewModel.dismissSuccess() },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Dismiss",
                                            tint = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // DIALOGS & OVERLAYS
    if (showVipModal) {
        VipUnlockDialog(
            user = currentUser,
            isLoading = isLoading,
            onUnlock50Videos = { viewModel.buyUnlockBonusVideos() },
            onUpgradeVip = { viewModel.buyVipMonthly() },
            onDismiss = { viewModel.closeVipModal() }
        )
    }

    if (showWithdrawDialog) {
        WithdrawDialog(
            user = currentUser,
            isLoading = isLoading,
            onAirtimeWithdraw = { phone, network, amount ->
                viewModel.processAirtimeWithdraw(phone, network, amount)
            },
            onBankWithdraw = { bank, accNum, accName, coins ->
                viewModel.processBankWithdraw(bank, accNum, accName, coins)
            },
            onDismiss = { viewModel.closeWithdrawDialog() }
        )
    }

    if (showPhoneVerifyDialog) {
        PhoneVerifyDialog(
            isLoading = isLoading,
            onSubmitOtp = { viewModel.submitPhoneOtp(it) },
            onDismiss = { viewModel.closePhoneVerifyDialog() }
        )
    }

    if (showCreateAdDialog) {
        CreateAdDialog(
            user = currentUser,
            isLoading = isLoading,
            onSubmitAd = { title, desc, url, views, category ->
                viewModel.submitDirectAd(title, desc, url, views, category)
            },
            onDismiss = { viewModel.closeCreateAdDialog() }
        )
    }

    if (rewardedCoinsWon != null) {
        RewardedAdSuccessDialog(
            coinsWon = rewardedCoinsWon!!,
            onDismiss = { viewModel.dismissRewardedDialog() }
        )
    }

    if (showInterstitialAdDialog) {
        InterstitialAdDialog(
            onDismiss = { viewModel.proceedSpinAfterInterstitial() }
        )
    }

    if (showForceUpdateDialog) {
        ForceUpdateDialog(
            onDismiss = { viewModel.dismissUpdateDialog() }
        )
    }
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(JomamboPurpleDark, JomamboPurplePrimary)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "JOMAMBO Logo",
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(24.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "JOMAMBO",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            )

            Text(
                text = "Watch, Task & Earn",
                color = JomamboGoldSecondary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(30.dp))

            CircularProgressIndicator(
                color = JomamboGoldSecondary,
                modifier = Modifier.size(32.dp),
                strokeWidth = 3.dp
            )
        }
    }
}
