package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.SocialTask

@Entity(tableName = "social_tasks")
data class SocialTaskEntity(
    @PrimaryKey val id: String,
    val type: String,
    val title: String,
    val description: String,
    val url: String,
    val price: Long,
    val totalSlots: Int,
    val remaining: Int,
    val createdBy: String,
    val isCompleted: Boolean
) {
    fun toModel(): SocialTask = SocialTask(
        id = id,
        type = type,
        title = title,
        description = description,
        url = url,
        price = price,
        totalSlots = totalSlots,
        remaining = remaining,
        createdBy = createdBy,
        isCompleted = isCompleted
    )

    companion object {
        fun fromModel(t: SocialTask): SocialTaskEntity = SocialTaskEntity(
            id = t.id,
            type = t.type,
            title = t.title,
            description = t.description,
            url = t.url,
            price = t.price,
            totalSlots = t.totalSlots,
            remaining = t.remaining,
            createdBy = t.createdBy,
            isCompleted = t.isCompleted
        )
    }
}
