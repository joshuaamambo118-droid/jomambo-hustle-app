package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.DirectAd

@Entity(tableName = "direct_ads")
data class DirectAdEntity(
    @PrimaryKey val id: String,
    val ownerId: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val videoUrl: String,
    val targetUrl: String,
    val pricePerView: Double,
    val totalViews: Int,
    val remainingViews: Int,
    val status: String,
    val category: String,
    val brandName: String
) {
    fun toModel(): DirectAd = DirectAd(
        id = id,
        ownerId = ownerId,
        title = title,
        description = description,
        imageUrl = imageUrl,
        videoUrl = videoUrl,
        targetUrl = targetUrl,
        pricePerView = pricePerView,
        totalViews = totalViews,
        remainingViews = remainingViews,
        status = status,
        category = category,
        brandName = brandName
    )

    companion object {
        fun fromModel(ad: DirectAd): DirectAdEntity = DirectAdEntity(
            id = ad.id,
            ownerId = ad.ownerId,
            title = ad.title,
            description = ad.description,
            imageUrl = ad.imageUrl,
            videoUrl = ad.videoUrl,
            targetUrl = ad.targetUrl,
            pricePerView = ad.pricePerView,
            totalViews = ad.totalViews,
            remainingViews = ad.remainingViews,
            status = ad.status,
            category = ad.category,
            brandName = ad.brandName
        )
    }
}
