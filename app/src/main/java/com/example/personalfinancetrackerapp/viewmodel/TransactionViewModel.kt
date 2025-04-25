package com.example.personalfinancetrackerapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalfinancetrackerapp.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TransactionRepository
    val allTransactions: Flow<List<Transaction>>

    init {
        val dao = AppDatabase.getDatabase(application).transactionDao()
        repository = TransactionRepository(dao)
        allTransactions = repository.allTransactions
    }

    fun getTransactionsByType(type: TransactionType): Flow<List<Transaction>> =
        repository.getTransactionsByType(type)

    fun getTransactionsByCategory(category: String): Flow<List<Transaction>> =
        repository.getTransactionsByCategory(category)

    fun getTotalAmountByType(type: TransactionType): Flow<Double?> =
        repository.getTotalAmountByType(type)

    fun insert(transaction: Transaction) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(transaction)
    }

    fun update(transaction: Transaction) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(transaction)
    }

    fun delete(transaction: Transaction) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(transaction)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }
} 