package com.example.personalfinancetrackerapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.personalfinancetrackerapp.databinding.FragmentSettingsBinding
import com.example.personalfinancetrackerapp.util.PreferencesManager

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SettingsViewModel
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        preferencesManager = PreferencesManager(requireContext())
        viewModel = ViewModelProvider(this, SettingsViewModelFactory(requireActivity().application))[SettingsViewModel::class.java]
        
        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        // Setup currency spinner
        val currencies = arrayOf("USD", "EUR", "GBP", "JPY", "INR", "AUD", "CAD")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCurrency.adapter = adapter
        
        // Set current currency
        val currentCurrency = preferencesManager.getSelectedCurrency()
        val currencyPosition = currencies.indexOf(currentCurrency)
        if (currencyPosition != -1) {
            binding.spinnerCurrency.setSelection(currencyPosition)
        }
        
        // Setup monthly budget input
        binding.editTextMonthlyBudget.setText(preferencesManager.getMonthlyBudget().toString())
        
        // Setup save button
        binding.buttonSaveSettings.setOnClickListener {
            val newCurrency = binding.spinnerCurrency.selectedItem.toString()
            val newBudget = binding.editTextMonthlyBudget.text.toString().toDoubleOrNull() ?: 0.0
            
            viewModel.saveSettings(newCurrency, newBudget)
        }
        
        // Setup notification toggle
        binding.switchNotifications.isChecked = viewModel.areNotificationsEnabled()
        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setNotificationsEnabled(isChecked)
        }
    }

    private fun observeViewModel() {
        viewModel.settingsSaved.observe(viewLifecycleOwner) { saved ->
            if (saved) {
                Toast.makeText(requireContext(), "Settings saved successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 