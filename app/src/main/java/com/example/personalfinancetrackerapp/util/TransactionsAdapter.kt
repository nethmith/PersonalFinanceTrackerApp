// filepath: c:\Users\MSI\AndroidStudioProjects\PersonalFinanceTrackerApp\app\src\main\java\com\example\personalfinancetrackerapp\ui\transactions\TransactionsAdapter.kt
package com.example.personalfinancetrackerapp.ui.transactions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.personalfinancetrackerapp.data.Transaction
import com.example.personalfinancetrackerapp.databinding.ItemTransactionBinding

class TransactionsAdapter(
    private val onItemClick: (Transaction) -> Unit,
    private val onDeleteClick: (Transaction) -> Unit,
    private val currencyProvider: () -> String
) : RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>() {

    private val transactions = mutableListOf<Transaction>()

    fun submitList(newTransactions: List<Transaction>) {
        transactions.clear()
        transactions.addAll(newTransactions)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemTransactionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemCount(): Int = transactions.size

    inner class TransactionViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: Transaction) {
            binding.textViewTitle.text = transaction.title
            binding.textViewAmount.text = "${currencyProvider()} ${transaction.amount}"
            binding.textViewCategory.text = transaction.category
            binding.textViewDate.text = transaction.date.toString()

            binding.root.setOnClickListener { onItemClick(transaction) }
            binding.buttonDelete.setOnClickListener { onDeleteClick(transaction) }
        }
    }
}