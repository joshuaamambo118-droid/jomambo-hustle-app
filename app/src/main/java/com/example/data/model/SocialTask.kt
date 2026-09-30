package com.example.data.model

data class SocialTask(
    val id: String,
    val type: String, // ig_follow, youtube_sub, app_install, x_repost, telegram_join
    val title: String,
    val description: String,
    val url: String,
    val price: Long, // Coins payout (e.g. 25-50)
    val totalSlots: Int,
    val remaining: Int,
    val createdBy: String = "Jomambo System",
    val isCompleted: Boolean = false
)
