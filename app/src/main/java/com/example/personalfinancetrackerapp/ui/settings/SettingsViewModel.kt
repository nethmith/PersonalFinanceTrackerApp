package com.example.personalfinancetrackerapp.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.personalfinancetrackerapp.util.PreferencesManager

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val preferencesManager = PreferencesManager(application)
    
    private val _settingsSaved = MutableLiveData<Boolean>()
    val settingsSaved: LiveData<Boolean> = _settingsSaved
    
    fun saveSettings(currency: String, monthlyBudget: Double) {
        preferencesManager.setSelectedCurrency(currency)
        preferencesManager.setMonthlyBudget(monthlyBudget)
        _settingsSaved.value = true
    }
    
    fun areNotificationsEnabled(): Boolean {
        return preferencesManager.areNotificationsEnabled()
    }
    
    fun setNotificationsEnabled(enabled: Boolean) {
        preferencesManager.setNotificationsEnabled(enabled)
    }
} 