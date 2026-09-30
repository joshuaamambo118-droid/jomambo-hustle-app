package com.example.data.model

data class Transaction(
    val id: String,
    val userId: String,
    val type: String, // "earn" or "withdraw"
    val amount: Long, // in Coins
    val nairaAmount: Double, // in Naira (100 coins = 10 Naira)
    val method: String, // "watch_video", "social_task", "daily_spin", "offerwall", "survey", "airtime_vtu", "bank_transfer", "vip_unlock"
    val status: String = "completed", // "completed", "pending", "processing"
    val timestamp: Long = System.currentTimeMillis(),
    val description: String = ""
)
