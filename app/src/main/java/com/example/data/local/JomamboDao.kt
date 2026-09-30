package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface JomamboDao {
    // User
    @Query("SELECT * FROM users LIMIT 1")
    fun observeCurrentUser(): Flow<UserEntity?>

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getCurrentUser(): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("DELETE FROM users")
    suspend fun clearUsers()

    // Direct Ads
    @Query("SELECT * FROM direct_ads WHERE status = 'approved' AND remainingViews > 0")
    fun observeActiveAds(): Flow<List<DirectAdEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAds(ads: List<DirectAdEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAd(ad: DirectAdEntity)

    @Update
    suspend fun updateAd(ad: DirectAdEntity)

    // Social Tasks
    @Query("SELECT * FROM social_tasks WHERE remaining > 0")
    fun observeTasks(): Flow<List<SocialTaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<SocialTaskEntity>)

    @Update
    suspend fun updateTask(task: SocialTaskEntity)

    // Transactions
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun observeTransactions(): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)
}
