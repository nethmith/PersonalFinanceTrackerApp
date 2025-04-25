package com.example.personalfinancetrackerapp.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _monthlyBudget = MutableStateFlow(getMonthlyBudget())
    val monthlyBudget: Flow<Double> = _monthlyBudget.asStateFlow()

    private val _selectedCurrency = MutableStateFlow(getSelectedCurrency())
    val selectedCurrency: Flow<String> = _selectedCurrency.asStateFlow()
    
    private val _notificationsEnabled = MutableStateFlow(areNotificationsEnabled())
    val notificationsEnabled: Flow<Boolean> = _notificationsEnabled.asStateFlow()

    fun setMonthlyBudget(budget: Double) {
        sharedPreferences.edit {
            putFloat(KEY_MONTHLY_BUDGET, budget.toFloat())
        }
        _monthlyBudget.value = budget
    }

    fun getMonthlyBudget(): Double {
        return sharedPreferences.getFloat(KEY_MONTHLY_BUDGET, 0f).toDouble()
    }

    fun setSelectedCurrency(currency: String) {
        sharedPreferences.edit {
            putString(KEY_SELECTED_CURRENCY, currency)
        }
        _selectedCurrency.value = currency
    }

    fun getSelectedCurrency(): String {
        return sharedPreferences.getString(KEY_SELECTED_CURRENCY, DEFAULT_CURRENCY) ?: DEFAULT_CURRENCY
    }
    
    fun setNotificationsEnabled(enabled: Boolean) {
        sharedPreferences.edit {
            putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled)
        }
        _notificationsEnabled.value = enabled
    }
    
    fun areNotificationsEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_NOTIFICATIONS_ENABLED, true)
    }

    companion object {
        private const val PREFS_NAME = "finance_tracker_prefs"
        private const val KEY_MONTHLY_BUDGET = "monthly_budget"
        private const val KEY_SELECTED_CURRENCY = "selected_currency"
        private const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
        private const val DEFAULT_CURRENCY = "USD"
    }
} 