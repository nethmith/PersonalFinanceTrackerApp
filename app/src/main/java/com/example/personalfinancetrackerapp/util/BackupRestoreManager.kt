package com.example.personalfinancetrackerapp.util

import android.content.Context
import com.example.personalfinancetrackerapp.data.Transaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class BackupRestoreManager(private val context: Context) {
    private val gson = Gson()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault())

    fun backupData(transactions: List<Transaction>): String {
        val backupFileName = "finance_backup_${dateFormat.format(Date())}.json"
        val backupFile = File(context.filesDir, backupFileName)
        
        try {
            val jsonData = gson.toJson(transactions)
            backupFile.writeText(jsonData)
            return backupFile.absolutePath
        } catch (e: IOException) {
            throw IOException("Failed to backup data: ${e.message}")
        }
    }

    fun restoreData(filePath: String): List<Transaction> {
        try {
            val file = File(filePath)
            if (!file.exists()) {
                throw IOException("Backup file not found")
            }

            val jsonData = file.readText()
            val type = object : TypeToken<List<Transaction>>() {}.type
            return gson.fromJson(jsonData, type)
        } catch (e: Exception) {
            throw IOException("Failed to restore data: ${e.message}")
        }
    }

    fun getBackupFiles(): List<File> {
        return context.filesDir.listFiles { file ->
            file.name.startsWith("finance_backup_") && file.name.endsWith(".json")
        }?.toList() ?: emptyList()
    }

    fun deleteBackupFile(filePath: String): Boolean {
        return try {
            File(filePath).delete()
        } catch (e: Exception) {
            false
        }
    }
} 