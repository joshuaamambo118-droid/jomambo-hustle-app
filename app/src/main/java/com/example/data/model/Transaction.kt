package com.jomambo.app.data.model

data class Transaction(
    val id: String = "",
    val userId: String = "",
    val type: String = "earn", // "earn" or "withdraw"
    val amount: Long = 0L, // in Coins
    val nairaAmount: Double = 0.0, // in Naira (100 coins = 10 Naira)
    val method: String = "watch_video", // "watch_video", "social_task", "daily_spin", "offerwall", "survey", "airtime_vtu", "bank_transfer", "vip_unlock"
    val status: String = "completed", // "completed", "pending", "processing"
    val timestamp: Long = System.currentTimeMillis(),
    val description: String = "",
    val network: String? = null,
    val phoneNumber: String? = null,
    val bankName: String? = null,
    val accountNumber: String? = null
) {
    val isEarn: Boolean
        get() = type == "earn"
}
