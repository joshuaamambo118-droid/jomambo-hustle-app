package com.example.data.model

data class AppConfig(
    val latestVersion: String = "1.0.0",
    val forceUpdate: Boolean = false,
    val maintenance: Boolean = false,
    val minWithdraw: Long = 1000L, // 1000 coins (100 Naira)
    val conversionRate: Double = 0.10, // 100 coins = 10 Naira
    val coinPerVideo: Long = 5L,
    val dailyVideoLimit: Int = 50,
    val vipCostNaira: Long = 1000L,
    val unlock50VideosCostNaira: Long = 500L
)

data class LeaderboardEntry(
    val rank: Int,
    val name: String,
    val coins: Long,
    val naira: Double,
    val isVip: Boolean,
    val avatarLetter: String
)
