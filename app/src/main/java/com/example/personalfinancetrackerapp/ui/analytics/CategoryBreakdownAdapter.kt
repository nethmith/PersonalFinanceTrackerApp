package com.example.personalfinancetrackerapp.ui.analytics

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.personalfinancetrackerapp.databinding.ItemCategoryBreakdownBinding
import java.text.NumberFormat
import java.util.*

class CategoryBreakdownAdapter(
    private val currencyProvider: () -> String
) : RecyclerView.Adapter<CategoryBreakdownAdapter.CategoryViewHolder>() {

    private val categories = mutableListOf<Pair<String, Double>>()
    private val numberFormat = NumberFormat.getCurrencyInstance(Locale.US)

    fun submitList(newCategories: Map<String, Double>) {
        categories.clear()
        categories.addAll(newCategories.toList().sortedByDescending { it.second })
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBreakdownBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size

    inner class CategoryViewHolder(private val binding: ItemCategoryBreakdownBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Pair<String, Double>) {
            binding.textViewCategory.text = category.first
            binding.textViewAmount.text = "${currencyProvider()} ${category.second}"
        }
    }
} 