package com.example.personalfinancetrackerapp.ui.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personalfinancetrackerapp.R
import com.example.personalfinancetrackerapp.databinding.FragmentTransactionsBinding
import com.example.personalfinancetrackerapp.util.PreferencesManager
import com.example.personalfinancetrackerapp.viewmodel.TransactionViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TransactionsFragment : Fragment() {
    private var _binding: FragmentTransactionsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TransactionViewModel by viewModels()
    private lateinit var transactionsAdapter: TransactionsAdapter
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferencesManager = PreferencesManager(requireContext())

        setupRecyclerView()
        setupViews()
        observeData()
    }

    private fun setupRecyclerView() {
        transactionsAdapter = TransactionsAdapter(
            onItemClick = { transaction ->
                // Handle transaction click (e.g., show details or edit)
            },
            onDeleteClick = { transaction ->
                viewModel.delete(transaction)
            },
            currencyProvider = { preferencesManager.getSelectedCurrency() }
        )

        binding.recyclerViewTransactions.apply {
            adapter = transactionsAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    private fun setupViews() {
        binding.fabAddTransaction.setOnClickListener {
            findNavController().navigate(R.id.action_transactions_to_add_transaction)
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allTransactions.collectLatest { transactions ->
                transactionsAdapter.submitList(transactions)
                binding.textViewEmpty.visibility = 
                    if (transactions.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 