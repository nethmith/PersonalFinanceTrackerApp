package com.example.personalfinancetrackerapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY date DESC")
    fun getTransactionsByType(type: TransactionType): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE category = :category ORDER BY date DESC")
    fun getTransactionsByCategory(category: String): Flow<List<Transaction>>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = :type")
    fun getTotalAmountByType(type: TransactionType): Flow<Double?>

    @Insert
    fun insertTransaction(transaction: Transaction): Long

    @Update
    fun updateTransaction(transaction: Transaction): Int

    @Delete
    fun deleteTransaction(transaction: Transaction): Int

    @Query("DELETE FROM transactions")
    fun deleteAllTransactions(): Int
} 