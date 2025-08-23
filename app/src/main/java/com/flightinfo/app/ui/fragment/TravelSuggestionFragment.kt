package com.flightinfo.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.flightinfo.app.R
import com.flightinfo.app.data.model.TravelSuggestion
import com.flightinfo.app.ui.viewmodel.FlightSearchViewModel
import com.flightinfo.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TravelSuggestionFragment : Fragment() {

    private val viewModel: FlightSearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_travel_suggestion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val destination = arguments?.getString("destination") ?: "Unknown"
        viewModel.loadTravelSuggestions(destination)

        lifecycleScope.launch {
            viewModel.travelSuggestions.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        resource.data?.suggestions?.firstOrNull()?.let { suggestion ->
                            updateUi(suggestion)
                        }
                    }
                    is Resource.Error -> {
                        // Handle error
                    }
                    is Resource.Loading -> {
                        // Handle loading
                    }
                }
            }
        }
    }

    private fun updateUi(suggestion: TravelSuggestion) {
        view?.findViewById<TextView>(R.id.destinationTextView)?.text = suggestion.destination
        view?.findViewById<TextView>(R.id.descriptionTextView)?.text = suggestion.description
        val tipsContainer = view?.findViewById<LinearLayout>(R.id.tipsContainer)
        tipsContainer?.removeAllViews()
        suggestion.tips.forEach { tip ->
            val tipTextView = TextView(context).apply {
                text = tip
                textSize = 16f
            }
            tipsContainer?.addView(tipTextView)
        }
    }
}
