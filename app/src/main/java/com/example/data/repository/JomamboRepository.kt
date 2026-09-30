package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.DirectAdEntity
import com.example.data.local.SocialTaskEntity
import com.example.data.local.TransactionEntity
import com.example.data.local.UserEntity
import com.example.data.model.AppConfig
import com.example.data.model.DirectAd
import com.example.data.model.LeaderboardEntry
import com.example.data.model.SocialTask
import com.example.data.model.Transaction
import com.example.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class JomamboRepository(private val database: AppDatabase) {
    private val dao = database.jomamboDao()

    val currentUser: Flow<User?> = dao.observeCurrentUser().map { it?.toUser() }
    val activeAds: Flow<List<DirectAd>> = dao.observeActiveAds().map { list -> list.map { it.toModel() } }
    val socialTasks: Flow<List<SocialTask>> = dao.observeTasks().map { list -> list.map { it.toModel() } }
    val transactions: Flow<List<Transaction>> = dao.observeTransactions().map { list -> list.map { it.toModel() } }

    val appConfig = AppConfig()

    suspend fun initSeedDataIfEmpty() {
        val user = dao.getCurrentUser()
        // Seed ads if empty
        val initialAds = listOf(
            DirectAd(
                id = "ad_1",
                ownerId = "kuda_ng",
                title = "Kuda Bank - The Bank of the Free",
                description = "Get 25 free bank transfers every month & 15% p.a interest on smart spend savings!",
                imageUrl = "https://images.unsplash.com/photo-1559526324-4b87b5e36e44?auto=format&fit=crop&w=600&q=80",
                videoUrl = "https://jomambo.app/videos/kuda_promo.mp4",
                targetUrl = "https://kuda.com",
                pricePerView = 5.0,
                totalViews = 10000,
                remainingViews = 9480,
                category = "crypto",
                brandName = "Kuda Microfinance Bank"
            ),
            DirectAd(
                id = "ad_2",
                ownerId = "palmpay_app",
                title = "PalmPay 100% Cashback Bonanza",
                description = "Pay bills, buy airtime with zero transaction fees and instant daily rewards up to ₦10,000!",
                imageUrl = "https://images.unsplash.com/photo-1563986768609-322da13575f3?auto=format&fit=crop&w=600&q=80",
                videoUrl = "https://jomambo.app/videos/palmpay_cashback.mp4",
                targetUrl = "https://palmpay.com",
                pricePerView = 5.0,
                totalViews = 8000,
                remainingViews = 7210,
                category = "shopping",
                brandName = "PalmPay Nigeria"
            ),
            DirectAd(
                id = "ad_3",
                ownerId = "binance_africa",
                title = "Binance Africa - Trade Crypto Zero Fee",
                description = "Learn how to trade USDT, BTC and earn passive yield through Binance Earn staking today!",
                imageUrl = "https://images.unsplash.com/photo-1621416894569-0f39ed31d247?auto=format&fit=crop&w=600&q=80",
                videoUrl = "https://jomambo.app/videos/binance_learn.mp4",
                targetUrl = "https://binance.com",
                pricePerView = 5.0,
                totalViews = 15000,
                remainingViews = 14320,
                category = "crypto",
                brandName = "Binance Africa"
            ),
            DirectAd(
                id = "ad_4",
                ownerId = "jumia_ng",
                title = "Jumia Mega Brand Festival Discounts",
                description = "Up to 70% off on smartphones, electronics, fashion and fast express doorstep delivery!",
                imageUrl = "https://images.unsplash.com/photo-1472851294608-062f824d29cc?auto=format&fit=crop&w=600&q=80",
                videoUrl = "https://jomambo.app/videos/jumia_deals.mp4",
                targetUrl = "https://jumia.com.ng",
                pricePerView = 5.0,
                totalViews = 5000,
                remainingViews = 4850,
                category = "shopping",
                brandName = "Jumia Nigeria"
            ),
            DirectAd(
                id = "ad_5",
                ownerId = "piggyvest_ng",
                title = "PiggyVest Safelock - Build Wealth",
                description = "Lock funds for 30 to 1000 days and earn up to 18% annual return paid upfront to you!",
                imageUrl = "https://images.unsplash.com/photo-1579621970563-ebec7560ff3e?auto=format&fit=crop&w=600&q=80",
                videoUrl = "https://jomambo.app/videos/piggyvest_save.mp4",
                targetUrl = "https://piggyvest.com",
                pricePerView = 5.0,
                totalViews = 12000,
                remainingViews = 11150,
                category = "tech",
                brandName = "Piggytech Global"
            )
        )
        dao.insertAds(initialAds.map { DirectAdEntity.fromModel(it) })

        val initialTasks = listOf(
            SocialTask(
                id = "task_ig_1",
                type = "ig_follow",
                title = "Follow @jomambo on Instagram",
                description = "Follow our official verified Instagram page for daily bonus codes and promotions.",
                url = "https://instagram.com/jomamboapp",
                price = 30L,
                totalSlots = 5000,
                remaining = 4120
            ),
            SocialTask(
                id = "task_yt_1",
                type = "youtube_sub",
                title = "Subscribe to Jomambo Official YouTube",
                description = "Subscribe and ring the notification bell to learn online hustle strategies and payout proofs.",
                url = "https://youtube.com/@jomambo",
                price = 45L,
                totalSlots = 4000,
                remaining = 3690
            ),
            SocialTask(
                id = "task_app_1",
                type = "app_install",
                title = "Download & Install FairMoney Loan App",
                description = "Install the app from Google Play Store and complete rapid signup for guaranteed coins.",
                url = "https://play.google.com/store/apps/details?id=co.branch.io.android",
                price = 80L,
                totalSlots = 2500,
                remaining = 1940
            ),
            SocialTask(
                id = "task_tg_1",
                type = "telegram_join",
                title = "Join Jomambo VIP Telegram Community",
                description = "Connect with 50,000+ active earners in Nigeria, Ghana and Kenya sharing daily withdrawal receipts.",
                url = "https://t.me/jomambo_community",
                price = 35L,
                totalSlots = 10000,
                remaining = 8740
            ),
            SocialTask(
                id = "task_x_1",
                type = "x_repost",
                title = "Repost JOMAMBO Launch Announcement on X",
                description = "Like and retweet our pinned post with hashtag #JomamboHustle to win weekly jackpot slots.",
                url = "https://twitter.com/intent/retweet?tweet_id=123456789",
                price = 25L,
                totalSlots = 3000,
                remaining = 2410
            )
        )
        dao.insertTasks(initialTasks.map { SocialTaskEntity.fromModel(it) })
    }

    // Auth
    suspend fun signUp(
        fullName: String,
        emailOrPhone: String,
        referredBy: String?
    ): Result<User> {
        val uid = "jm_" + UUID.randomUUID().toString().replace("-", "").take(12)
        val newUser = User(
            uid = uid,
            fullName = fullName.trim(),
            emailOrPhone = emailOrPhone.trim(),
            coins = 50L, // 50 welcome bonus coins
            naira = 5.0,
            verified = false,
            createdAt = System.currentTimeMillis(),
            appVersion = "1.0.0",
            referralCode = User.generateReferralCode(),
            referredBy = referredBy?.trim()?.ifEmpty { null },
            isVip = false,
            dailySpinsLeft = 5,
            dailyVideosWatched = 0,
            unlockedBonusVideos = 0
        )
        dao.insertUser(UserEntity.fromUser(newUser))

        // Welcome bonus transaction
        dao.insertTransaction(
            TransactionEntity(
                id = "tx_" + System.currentTimeMillis(),
                userId = uid,
                type = "earn",
                amount = 50L,
                nairaAmount = 5.0,
                method = "welcome_bonus",
                status = "completed",
                timestamp = System.currentTimeMillis(),
                description = "Welcome Bonus Coins for signing up on JOMAMBO!"
            )
        )

        return Result.success(newUser)
    }

    suspend fun login(emailOrPhone: String): Result<User> {
        val existing = dao.getCurrentUser()
        if (existing != null) {
            return Result.success(existing.toUser())
        }
        // Create user if not exists on first login
        return signUp(fullName = "Jomambo Hustler", emailOrPhone = emailOrPhone, referredBy = null)
    }

    suspend fun loginWithGoogle(): Result<User> {
        val existing = dao.getCurrentUser()
        if (existing != null) {
            return Result.success(existing.toUser())
        }
        return signUp(
            fullName = "Joshua Amambo",
            emailOrPhone = "joshuaamambo118@gmail.com",
            referredBy = null
        )
    }

    suspend fun logout() {
        dao.clearUsers()
    }

    // Rewarded Video Watch (+5 coins, or +10 if VIP)
    suspend fun rewardVideoWatch(adId: String): Long {
        val userEntity = dao.getCurrentUser() ?: return 0L
        val user = userEntity.toUser()
        val baseCoin = 5L
        val rewardAmount = if (user.isVip) baseCoin * 2 else baseCoin
        val newCoins = user.coins + rewardAmount
        val newNaira = newCoins * 0.10
        val updatedUser = user.copy(
            coins = newCoins,
            naira = newNaira,
            dailyVideosWatched = user.dailyVideosWatched + 1
        )
        dao.updateUser(UserEntity.fromUser(updatedUser))

        dao.insertTransaction(
            TransactionEntity(
                id = "tx_" + System.currentTimeMillis(),
                userId = user.uid,
                type = "earn",
                amount = rewardAmount,
                nairaAmount = rewardAmount * 0.10,
                method = "watch_video",
                status = "completed",
                timestamp = System.currentTimeMillis(),
                description = "Watched 30s Sponsored Video (+${rewardAmount} coins${if (user.isVip) " [VIP 2x]" else ""})"
            )
        )
        return rewardAmount
    }

    // Social Task Completion
    suspend fun completeSocialTask(taskId: String): Long {
        val userEntity = dao.getCurrentUser() ?: return 0L
        val user = userEntity.toUser()

        // Find task from db
        // In local db, we update task state
        val rewardAmount = 35L
        val finalReward = if (user.isVip) rewardAmount * 2 else rewardAmount
        val newCoins = user.coins + finalReward
        val newNaira = newCoins * 0.10

        dao.updateUser(UserEntity.fromUser(user.copy(coins = newCoins, naira = newNaira)))

        dao.insertTransaction(
            TransactionEntity(
                id = "tx_" + System.currentTimeMillis(),
                userId = user.uid,
                type = "earn",
                amount = finalReward,
                nairaAmount = finalReward * 0.10,
                method = "social_task",
                status = "completed",
                timestamp = System.currentTimeMillis(),
                description = "Completed Social Task (+${finalReward} coins)"
            )
        )
        return finalReward
    }

    // Spin Wheel
    suspend fun claimSpinReward(wonCoins: Long): Result<Long> {
        val userEntity = dao.getCurrentUser() ?: return Result.failure(Exception("No user logged in"))
        val user = userEntity.toUser()
        if (user.dailySpinsLeft <= 0) {
            return Result.failure(Exception("No daily spins left! Resets tomorrow."))
        }

        val finalReward = if (user.isVip) wonCoins * 2 else wonCoins
        val newCoins = user.coins + finalReward
        val newNaira = newCoins * 0.10
        val updatedUser = user.copy(
            coins = newCoins,
            naira = newNaira,
            dailySpinsLeft = user.dailySpinsLeft - 1
        )
        dao.updateUser(UserEntity.fromUser(updatedUser))

        dao.insertTransaction(
            TransactionEntity(
                id = "tx_" + System.currentTimeMillis(),
                userId = user.uid,
                type = "earn",
                amount = finalReward,
                nairaAmount = finalReward * 0.10,
                method = "daily_spin",
                status = "completed",
                timestamp = System.currentTimeMillis(),
                description = "Won Daily Lucky Spin (+${finalReward} coins${if (user.isVip) " [VIP 2x]" else ""})"
            )
        )
        return Result.success(finalReward)
    }

    // Offerwall & Survey Earn
    suspend fun claimPartnerReward(partnerName: String, coins: Long, taskTitle: String) {
        val userEntity = dao.getCurrentUser() ?: return
        val user = userEntity.toUser()
        val finalReward = if (user.isVip) coins * 2 else coins
        val newCoins = user.coins + finalReward
        val newNaira = newCoins * 0.10
        dao.updateUser(UserEntity.fromUser(user.copy(coins = newCoins, naira = newNaira)))

        dao.insertTransaction(
            TransactionEntity(
                id = "tx_" + System.currentTimeMillis(),
                userId = user.uid,
                type = "earn",
                amount = finalReward,
                nairaAmount = finalReward * 0.10,
                method = partnerName.lowercase(),
                status = "completed",
                timestamp = System.currentTimeMillis(),
                description = "$partnerName: $taskTitle (+${finalReward} coins)"
            )
        )
    }

    // Pay to Unlock: 50 More Videos (₦500)
    suspend fun unlockBonusVideos(): Result<Unit> {
        val userEntity = dao.getCurrentUser() ?: return Result.failure(Exception("No user logged in"))
        val user = userEntity.toUser()
        val updated = user.copy(
            unlockedBonusVideos = user.unlockedBonusVideos + 50
        )
        dao.updateUser(UserEntity.fromUser(updated))

        dao.insertTransaction(
            TransactionEntity(
                id = "tx_" + System.currentTimeMillis(),
                userId = user.uid,
                type = "purchase",
                amount = 0L,
                nairaAmount = 500.0,
                method = "pay_to_unlock",
                status = "completed",
                timestamp = System.currentTimeMillis(),
                description = "Unlocked 50 extra daily rewarded video slots (₦500 paid via Paystack)"
            )
        )
        return Result.success(Unit)
    }

    // VIP Hustler Club: ₦1,000 / month
    suspend fun upgradeToVip(): Result<Unit> {
        val userEntity = dao.getCurrentUser() ?: return Result.failure(Exception("No user logged in"))
        val user = userEntity.toUser()
        val updated = user.copy(isVip = true)
        dao.updateUser(UserEntity.fromUser(updated))

        dao.insertTransaction(
            TransactionEntity(
                id = "tx_" + System.currentTimeMillis(),
                userId = user.uid,
                type = "purchase",
                amount = 0L,
                nairaAmount = 1000.0,
                method = "vip_club",
                status = "completed",
                timestamp = System.currentTimeMillis(),
                description = "Upgraded to JOMAMBO VIP Hustler Club! 2x multiplier activated (₦1,000 paid)"
            )
        )
        return Result.success(Unit)
    }

    // Phone Verification
    suspend fun verifyPhoneNumber(otp: String): Result<Unit> {
        val userEntity = dao.getCurrentUser() ?: return Result.failure(Exception("No user logged in"))
        if (otp.length != 6) {
            return Result.failure(Exception("Please enter a valid 6-digit verification OTP"))
        }
        val user = userEntity.toUser()
        dao.updateUser(UserEntity.fromUser(user.copy(verified = true)))
        return Result.success(Unit)
    }

    // Withdraw Airtime (VTU)
    // Rule: User requests ₦1,000 airtime -> Cost to app ₦970, app retains ₦30 profit, deducts 10,000 coins (₦1,000) or 970 coins (cost value)
    suspend fun withdrawAirtime(phone: String, network: String, airtimeAmountNaira: Long): Result<Unit> {
        val userEntity = dao.getCurrentUser() ?: return Result.failure(Exception("No user logged in"))
        val user = userEntity.toUser()

        if (!user.verified) {
            return Result.failure(Exception("Please verify your phone number before making withdrawals"))
        }

        val requiredCoins = airtimeAmountNaira * 10L // ₦1,000 = 10,000 coins, or min ₦100 = 1,000 coins
        if (user.coins < requiredCoins) {
            return Result.failure(Exception("Insufficient coins! You need $requiredCoins coins for ₦$airtimeAmountNaira airtime."))
        }

        // Deduct coins (keeping 3% profit advantage)
        val newCoins = user.coins - requiredCoins
        val newNaira = newCoins * 0.10
        dao.updateUser(UserEntity.fromUser(user.copy(coins = newCoins, naira = newNaira)))

        dao.insertTransaction(
            TransactionEntity(
                id = "tx_" + System.currentTimeMillis(),
                userId = user.uid,
                type = "withdraw",
                amount = requiredCoins,
                nairaAmount = airtimeAmountNaira.toDouble(),
                method = "airtime_vtu",
                status = "completed",
                timestamp = System.currentTimeMillis(),
                description = "VTU Airtime: ₦$airtimeAmountNaira sent to $phone ($network) [Cost: ₦${(airtimeAmountNaira * 0.97).toInt()}, Profit: ₦${(airtimeAmountNaira * 0.03).toInt()}]"
            )
        )
        return Result.success(Unit)
    }

    // Withdraw Bank Transfer
    suspend fun withdrawBank(
        bankName: String,
        accountNumber: String,
        accountName: String,
        amountCoins: Long
    ): Result<Unit> {
        val userEntity = dao.getCurrentUser() ?: return Result.failure(Exception("No user logged in"))
        val user = userEntity.toUser()

        if (!user.verified) {
            return Result.failure(Exception("Please verify your phone number before making withdrawals"))
        }

        if (amountCoins < appConfig.minWithdraw) {
            return Result.failure(Exception("Minimum withdrawal is ${appConfig.minWithdraw} coins (₦${(appConfig.minWithdraw * 0.1).toInt()})"))
        }

        if (user.coins < amountCoins) {
            return Result.failure(Exception("Insufficient coins! You currently have ${user.coins} coins."))
        }

        val newCoins = user.coins - amountCoins
        val newNaira = newCoins * 0.10
        val withdrawNaira = amountCoins * 0.10
        dao.updateUser(UserEntity.fromUser(user.copy(coins = newCoins, naira = newNaira)))

        dao.insertTransaction(
            TransactionEntity(
                id = "tx_" + System.currentTimeMillis(),
                userId = user.uid,
                type = "withdraw",
                amount = amountCoins,
                nairaAmount = withdrawNaira,
                method = "bank_transfer",
                status = "processing",
                timestamp = System.currentTimeMillis(),
                description = "Bank Transfer: ₦$withdrawNaira to $accountName ($bankName - $accountNumber)"
            )
        )
        return Result.success(Unit)
    }

    // Direct Ads Creation: User creates custom ad, pays first, platform keeps 60% profit
    suspend fun createDirectAd(
        title: String,
        description: String,
        targetUrl: String,
        viewsRequested: Int,
        category: String
    ): Result<DirectAd> {
        val userEntity = dao.getCurrentUser() ?: return Result.failure(Exception("No user logged in"))
        val user = userEntity.toUser()

        val costInNaira = viewsRequested * 5.0 // ₦5 per view
        val costInCoins = (viewsRequested * 50L) // Or pay in coins

        // Deduct from coins if user has enough, else check payment
        if (user.coins >= costInCoins) {
            val updated = user.copy(
                coins = user.coins - costInCoins,
                naira = (user.coins - costInCoins) * 0.10
            )
            dao.updateUser(UserEntity.fromUser(updated))
        }

        val adId = "ad_user_" + UUID.randomUUID().toString().take(8)
        val newAd = DirectAd(
            id = adId,
            ownerId = user.uid,
            title = title,
            description = description,
            imageUrl = "https://images.unsplash.com/photo-1460925895917-afdab827c52f?auto=format&fit=crop&w=600&q=80",
            videoUrl = "https://jomambo.app/videos/user_campaign.mp4",
            targetUrl = targetUrl,
            pricePerView = 5.0,
            totalViews = viewsRequested,
            remainingViews = viewsRequested,
            status = "approved",
            category = category,
            brandName = user.fullName
        )
        dao.insertAd(DirectAdEntity.fromModel(newAd))

        dao.insertTransaction(
            TransactionEntity(
                id = "tx_" + System.currentTimeMillis(),
                userId = user.uid,
                type = "campaign_purchase",
                amount = costInCoins,
                nairaAmount = costInNaira,
                method = "direct_ad_launch",
                status = "completed",
                timestamp = System.currentTimeMillis(),
                description = "Created Ad Campaign: '$title' ($viewsRequested views). Platform keeps 60% profit."
            )
        )

        return Result.success(newAd)
    }

    // Leaderboard Data
    fun getLeaderboard(): List<LeaderboardEntry> {
        return listOf(
            LeaderboardEntry(1, "Emeka Nnamdi (Lagos)", 245000L, 24500.0, true, "E"),
            LeaderboardEntry(2, "Amina Bello (Abuja)", 189400L, 18940.0, true, "A"),
            LeaderboardEntry(3, "Chidi Okafor (Enugu)", 142000L, 14200.0, false, "C"),
            LeaderboardEntry(4, "Tunde Adeyemi (Ibadan)", 98500L, 9850.0, true, "T"),
            LeaderboardEntry(5, "Blessing Kalu (Port Harcourt)", 76400L, 7640.0, false, "B"),
            LeaderboardEntry(6, "Fatima Sanusi (Kano)", 54200L, 5420.0, false, "F"),
            LeaderboardEntry(7, "Joshua Amambo (Benin)", 39800L, 3980.0, true, "J")
        )
    }
}
