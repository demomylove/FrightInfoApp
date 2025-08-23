package com.flightinfo.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.flightinfo.app.databinding.FragmentNotificationSettingsBinding

class NotificationSettingsFragment : Fragment() {
    private var _binding: FragmentNotificationSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Load current settings and update UI
        loadNotificationSettings()
        
        // Set up listeners for preference changes
        setupPreferenceListeners()
    }

    private fun loadNotificationSettings() {
        // In a real implementation, you would load settings from SharedPreferences or a database
        // For now, we'll just set default values
        binding.delayNotificationsSwitch.isChecked = true
        binding.cancellationNotificationsSwitch.isChecked = true
        binding.gateChangeNotificationsSwitch.isChecked = true
        binding.doNotDisturbSwitch.isChecked = false
    }

    private fun setupPreferenceListeners() {
        binding.delayNotificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Save preference
            savePreference("delay_notifications", isChecked)
        }
        
        binding.cancellationNotificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Save preference
            savePreference("cancellation_notifications", isChecked)
        }
        
        binding.gateChangeNotificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Save preference
            savePreference("gate_change_notifications", isChecked)
        }
        
        binding.doNotDisturbSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Enable/disable time pickers
            binding.startTimePicker.isEnabled = isChecked
            binding.endTimePicker.isEnabled = isChecked
            
            // Save preference
            savePreference("do_not_disturb", isChecked)
        }
    }

    private fun savePreference(key: String, value: Boolean) {
        // In a real implementation, you would save to SharedPreferences or a database
        // For now, we'll just log the change
        // Log.d("NotificationSettings", "Saving preference: $key = $value")
    }

    private fun savePreference(key: String, value: String) {
        // In a real implementation, you would save to SharedPreferences or a database
        // For now, we'll just log the change
        // Log.d("NotificationSettings", "Saving preference: $key = $value")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}