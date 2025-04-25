package com.example.personalfinancetrackerapp.ui.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.personalfinancetrackerapp.R
import com.example.personalfinancetrackerapp.data.Transaction
import com.example.personalfinancetrackerapp.data.TransactionType
import com.example.personalfinancetrackerapp.databinding.FragmentAddTransactionBinding
import com.example.personalfinancetrackerapp.util.PreferencesManager
import com.example.personalfinancetrackerapp.viewmodel.TransactionViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class AddTransactionFragment : Fragment() {
    private var _binding: FragmentAddTransactionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TransactionViewModel by viewModels()
    private lateinit var preferencesManager: PreferencesManager
    private var selectedDate: Date = Date()

    private val categories = listOf(
        "Food & Dining",
        "Transportation",
        "Shopping",
        "Bills & Utilities",
        "Entertainment",
        "Health",
        "Education",
        "Other"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferencesManager = PreferencesManager(requireContext())

        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        // Setup category spinner
        val categoryAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            categories
        )
        binding.spinnerCategory.adapter = categoryAdapter

        // Setup transaction type radio group
        binding.radioGroupType.check(R.id.radioButtonExpense)

        // Set initial date
        updateDateButtonText()
    }

    private fun setupListeners() {
        binding.buttonSelectDate.setOnClickListener {
            showDatePicker()
        }

        binding.buttonSave.setOnClickListener {
            saveTransaction()
        }
    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(selectedDate.time)
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            selectedDate = Date(selection)
            updateDateButtonText()
        }

        datePicker.show(parentFragmentManager, "date_picker")
    }

    private fun updateDateButtonText() {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        binding.buttonSelectDate.text = dateFormat.format(selectedDate)
    }

    private fun saveTransaction() {
        val title = binding.editTextTitle.text.toString()
        val amountStr = binding.editTextAmount.text.toString()
        val category = binding.spinnerCategory.selectedItem.toString()
        val type = when (binding.radioGroupType.checkedRadioButtonId) {
            R.id.radioButtonIncome -> TransactionType.INCOME
            else -> TransactionType.EXPENSE
        }

        if (title.isBlank() || amountStr.isBlank()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val amount = amountStr.toDouble()
            val transaction = Transaction(
                title = title,
                amount = amount,
                category = category,
                date = selectedDate,
                type = type
            )

            viewModel.insert(transaction)
            findNavController().navigateUp()
        } catch (e: NumberFormatException) {
            Toast.makeText(context, "Invalid amount", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 