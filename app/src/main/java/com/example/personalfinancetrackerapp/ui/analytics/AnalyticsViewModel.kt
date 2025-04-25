package com.example.personalfinancetrackerapp.ui.analytics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalfinancetrackerapp.data.Transaction
import com.example.personalfinancetrackerapp.data.TransactionRepository
import com.example.personalfinancetrackerapp.data.TransactionType
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

class AnalyticsViewModel(private val repository: TransactionRepository) : ViewModel() {
    private val _monthlyExpenses = MutableLiveData<Double>()
    val monthlyExpenses: LiveData<Double> = _monthlyExpenses

    private val _monthlyIncome = MutableLiveData<Double>()
    val monthlyIncome: LiveData<Double> = _monthlyIncome

    private val _categoryBreakdown = MutableLiveData<Map<String, Double>>()
    val categoryBreakdown: LiveData<Map<String, Double>> = _categoryBreakdown

    init {
        loadMonthlyData()
        loadCategoryBreakdown()
    }

    private fun loadMonthlyData() {
        viewModelScope.launch {
            repository.allTransactions.collectLatest { transactions ->
                val calendar = Calendar.getInstance()
                val currentMonth = calendar.get(Calendar.MONTH)
                val currentYear = calendar.get(Calendar.YEAR)

                // Filter transactions for current month
                val monthlyTransactions = transactions.filter { transaction ->
                    val transactionDate = Calendar.getInstance().apply {
                        time = transaction.date
                    }
                    transactionDate.get(Calendar.MONTH) == currentMonth && 
                    transactionDate.get(Calendar.YEAR) == currentYear
                }

                // Calculate total expenses and income
                val expenses = monthlyTransactions
                    .filter { it.type == TransactionType.EXPENSE }
                    .sumOf { it.amount }
                
                val income = monthlyTransactions
                    .filter { it.type == TransactionType.INCOME }
                    .sumOf { it.amount }

                _monthlyExpenses.value = expenses
                _monthlyIncome.value = income
            }
        }
    }

    private fun loadCategoryBreakdown() {
        viewModelScope.launch {
            repository.allTransactions.collectLatest { transactions ->
                // Group expenses by category
                val expensesByCategory = transactions
                    .filter { it.type == TransactionType.EXPENSE }
                    .groupBy { it.category }
                    .mapValues { (_, transactions) -> transactions.sumOf { it.amount } }
                
                _categoryBreakdown.value = expensesByCategory
            }
        }
    }
} 