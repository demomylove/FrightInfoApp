package com.flightinfo.app.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.flightinfo.app.databinding.DialogPriceRangeFilterBinding
import com.flightinfo.app.ui.viewmodel.FlightSearchViewModel
import com.google.android.material.slider.RangeSlider
import java.text.NumberFormat
import java.util.Locale

class PriceRangeFilterDialog : DialogFragment() {

    private var _binding: DialogPriceRangeFilterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FlightSearchViewModel by activityViewModels()
    private val numberFormat = NumberFormat.getCurrencyInstance(Locale.US)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DialogPriceRangeFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPriceRangeSlider()
        setupInputFields()
        setupButtons()
        loadCurrentFilter()
    }

    private fun setupPriceRangeSlider() {
        binding.priceRangeSlider.addOnChangeListener { slider, value, fromUser ->
            if (fromUser) {
                val values = slider.values
                updatePriceDisplay(values[0], values[1])
                updateInputFields(values[0], values[1])
            }
        }

        binding.priceRangeSlider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
                // User started interacting with the slider
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                val values = slider.values
                updatePriceDisplay(values[0], values[1])
                updateInputFields(values[0], values[1])
            }
        })
    }

    private fun setupInputFields() {
        binding.minPriceInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                updateSliderFromInputs()
            }
        }

        binding.maxPriceInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                updateSliderFromInputs()
            }
        }
    }

    private fun setupButtons() {
        binding.resetButton.setOnClickListener {
            resetFilter()
        }

        binding.applyButton.setOnClickListener {
            applyFilter()
        }
    }

    private fun loadCurrentFilter() {
        val currentFilter = viewModel.priceRangeFilter.value
        if (currentFilter != null) {
            binding.priceRangeSlider.setValues(currentFilter.minPrice, currentFilter.maxPrice)
            updatePriceDisplay(currentFilter.minPrice, currentFilter.maxPrice)
            updateInputFields(currentFilter.minPrice, currentFilter.maxPrice)
        }
    }

    private fun updatePriceDisplay(minPrice: Float, maxPrice: Float) {
        val minPriceText = numberFormat.format(minPrice.toDouble())
        val maxPriceText = numberFormat.format(maxPrice.toDouble())
        binding.priceRangeText.text = "$minPriceText - $maxPriceText"
    }

    private fun updateInputFields(minPrice: Float, maxPrice: Float) {
        binding.minPriceInput.setText(minPrice.toInt().toString())
        binding.maxPriceInput.setText(maxPrice.toInt().toString())
    }

    private fun updateSliderFromInputs() {
        try {
            val minPrice = binding.minPriceInput.text.toString().toFloatOrNull() ?: 0f
            val maxPrice = binding.maxPriceInput.text.toString().toFloatOrNull() ?: 2000f

            // Ensure min is not greater than max
            val adjustedMin = minOf(minPrice, maxPrice)
            val adjustedMax = maxOf(minPrice, maxPrice)

            binding.priceRangeSlider.setValues(adjustedMin, adjustedMax)
            updatePriceDisplay(adjustedMin, adjustedMax)
            updateInputFields(adjustedMin, adjustedMax)
        } catch (e: Exception) {
            // Invalid input, ignore
        }
    }

    private fun resetFilter() {
        binding.priceRangeSlider.setValues(0f, 2000f)
        updatePriceDisplay(0f, 2000f)
        updateInputFields(0f, 2000f)

        viewModel.clearPriceRangeFilter()
        dismiss()
    }

    private fun applyFilter() {
        val values = binding.priceRangeSlider.values
        val minPrice = values[0]
        val maxPrice = values[1]

        viewModel.setPriceRangeFilter(minPrice, maxPrice)
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
