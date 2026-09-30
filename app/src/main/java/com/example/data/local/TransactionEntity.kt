package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.Transaction

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val type: String,
    val amount: Long,
    val nairaAmount: Double,
    val method: String,
    val status: String,
    val timestamp: Long,
    val description: String
) {
    fun toModel(): Transaction = Transaction(
        id = id,
        userId = userId,
        type = type,
        amount = amount,
        nairaAmount = nairaAmount,
        method = method,
        status = status,
        timestamp = timestamp,
        description = description
    )

    companion object {
        fun fromModel(tx: Transaction): TransactionEntity = TransactionEntity(
            id = tx.id,
            userId = tx.userId,
            type = tx.type,
            amount = tx.amount,
            nairaAmount = tx.nairaAmount,
            method = tx.method,
            status = tx.status,
            timestamp = tx.timestamp,
            description = tx.description
        )
    }
}
