package com.example.personalfinancetrackerapp.ui.analytics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personalfinancetrackerapp.data.AppDatabase
import com.example.personalfinancetrackerapp.data.TransactionRepository
import com.example.personalfinancetrackerapp.databinding.FragmentAnalyticsBinding
import com.example.personalfinancetrackerapp.util.PreferencesManager

class AnalyticsFragment : Fragment() {
    private var _binding: FragmentAnalyticsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AnalyticsViewModel
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var categoryAdapter: CategoryBreakdownAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        preferencesManager = PreferencesManager(requireContext())
        
        // Initialize repository and ViewModel
        val database = AppDatabase.getDatabase(requireContext())
        val repository = TransactionRepository(database.transactionDao())
        viewModel = ViewModelProvider(this, AnalyticsViewModelFactory(repository))[AnalyticsViewModel::class.java]
        
        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        // Setup UI components
        binding.textViewAnalyticsTitle.text = "Financial Analytics"
        
        // Setup RecyclerView
        categoryAdapter = CategoryBreakdownAdapter {
            preferencesManager.getSelectedCurrency()
        }
        
        binding.recyclerViewCategories.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = categoryAdapter
        }
    }

    private fun observeViewModel() {
        // Observe ViewModel data
        viewModel.monthlyExpenses.observe(viewLifecycleOwner) { expenses ->
            binding.textViewMonthlyExpenses.text = "${preferencesManager.getSelectedCurrency()} $expenses"
        }
        
        viewModel.monthlyIncome.observe(viewLifecycleOwner) { income ->
            binding.textViewMonthlyIncome.text = "${preferencesManager.getSelectedCurrency()} $income"
        }
        
        viewModel.categoryBreakdown.observe(viewLifecycleOwner) { breakdown ->
            categoryAdapter.submitList(breakdown)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 