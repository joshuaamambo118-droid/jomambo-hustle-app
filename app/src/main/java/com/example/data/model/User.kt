package com.jomambo.app.data.model

data class User(
    val uid: String = "",
    val fullName: String = "",
    val emailOrPhone: String = "",
    val coins: Long = 0L,
    val naira: Double = 0.0,
    val verified: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val appVersion: String = "1.0.1",
    val referralCode: String = generateReferralCode(),
    val referredBy: String? = null,
    val isVip: Boolean = false,
    val dailySpinsLeft: Int = 5,
    val dailyVideosWatched: Int = 0,
    val unlockedBonusVideos: Int = 0
) {
    // HELPER FOR WALLET - Senior Dev Upgrade
    val nairaBalance: Double
        get() = coins * 0.10

    fun getFormattedNaira(): String {
        return "₦${"%.2f".format(nairaBalance)}"
    }

    fun getFormattedCoins(): String {
        return "$coins Coins"
    }

    companion object {
        fun generateReferralCode(): String {
            val chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
            return (1..6).map { chars.random() }.joinToString("")
        }
    }
}
