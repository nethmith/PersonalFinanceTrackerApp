package com.example.personalfinancetrackerapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.personalfinancetrackerapp.R
import com.example.personalfinancetrackerapp.data.TransactionType
import com.example.personalfinancetrackerapp.databinding.FragmentHomeBinding
import com.example.personalfinancetrackerapp.util.PreferencesManager
import com.example.personalfinancetrackerapp.viewmodel.TransactionViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TransactionViewModel by viewModels()
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferencesManager = PreferencesManager(requireContext())

        setupViews()
        observeData()
    }

    private fun setupViews() {
        binding.fabAddTransaction.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_add_transaction)
        }

        // Collect monthly budget from preferences
        viewLifecycleOwner.lifecycleScope.launch {
            preferencesManager.monthlyBudget.collectLatest { budget ->
                binding.progressBarBudget.max = budget.toInt()
                updateBudgetProgress()
            }
        }

        // Collect currency from preferences
        viewLifecycleOwner.lifecycleScope.launch {
            preferencesManager.selectedCurrency.collectLatest { currency ->
                binding.textViewCurrency.text = currency
            }
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getTotalAmountByType(TransactionType.EXPENSE).collectLatest { totalExpense ->
                val expenses = totalExpense ?: 0.0
                binding.textViewTotalExpenses.text = String.format("%.2f", expenses)
                updateBudgetProgress(expenses)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getTotalAmountByType(TransactionType.INCOME).collectLatest { totalIncome ->
                binding.textViewTotalIncome.text = String.format("%.2f", totalIncome ?: 0.0)
            }
        }
    }

    private fun updateBudgetProgress(currentExpenses: Double = 0.0) {
        val budget = preferencesManager.getMonthlyBudget()
        binding.progressBarBudget.progress = currentExpenses.toInt()
        
        val percentage = (currentExpenses / budget) * 100
        binding.textViewBudgetPercentage.text = String.format("%.1f%%", percentage)

        // Update progress bar color based on percentage
        val colorRes = when {
            percentage >= 100 -> R.color.red
            percentage >= 75 -> R.color.orange
            else -> R.color.green
        }
        binding.progressBarBudget.progressTintList = 
            context?.getColorStateList(colorRes)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 