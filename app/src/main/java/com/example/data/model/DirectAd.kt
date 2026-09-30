package com.example.data.model

data class DirectAd(
    val id: String,
    val ownerId: String,
    val title: String,
    val description: String = "",
    val imageUrl: String = "",
    val videoUrl: String = "",
    val targetUrl: String,
    val pricePerView: Double = 5.0, // Coins per 30 sec view
    val totalViews: Int,
    val remainingViews: Int,
    val status: String = "approved", // "approved", "pending", "completed"
    val category: String = "entertainment",
    val brandName: String = "Jomambo Partner"
)
