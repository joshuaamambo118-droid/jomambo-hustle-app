package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val uid: String,
    val fullName: String,
    val emailOrPhone: String,
    val coins: Long,
    val naira: Double,
    val verified: Boolean,
    val createdAt: Long,
    val appVersion: String,
    val referralCode: String,
    val referredBy: String?,
    val isVip: Boolean,
    val dailySpinsLeft: Int,
    val dailyVideosWatched: Int,
    val unlockedBonusVideos: Int
) {
    fun toUser(): User = User(
        uid = uid,
        fullName = fullName,
        emailOrPhone = emailOrPhone,
        coins = coins,
        naira = naira,
        verified = verified,
        createdAt = createdAt,
        appVersion = appVersion,
        referralCode = referralCode,
        referredBy = referredBy,
        isVip = isVip,
        dailySpinsLeft = dailySpinsLeft,
        dailyVideosWatched = dailyVideosWatched,
        unlockedBonusVideos = unlockedBonusVideos
    )

    companion object {
        fun fromUser(u: User): UserEntity = UserEntity(
            uid = u.uid,
            fullName = u.fullName,
            emailOrPhone = u.emailOrPhone,
            coins = u.coins,
            naira = u.naira,
            verified = u.verified,
            createdAt = u.createdAt,
            appVersion = u.appVersion,
            referralCode = u.referralCode,
            referredBy = u.referredBy,
            isVip = u.isVip,
            dailySpinsLeft = u.dailySpinsLeft,
            dailyVideosWatched = u.dailyVideosWatched,
            unlockedBonusVideos = u.unlockedBonusVideos
        )
    }
}
