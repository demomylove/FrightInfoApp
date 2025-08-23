package com.flightinfo.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.flightinfo.app.databinding.FragmentNotificationSettingsBinding
import com.google.android.material.switchmaterial.SwitchMaterial

class NotificationSettingsFragment : Fragment() {
    private var delayNotificationsSwitch: SwitchMaterial? = null
    private var cancellationNotificationsSwitch: SwitchMaterial? = null
    private var gateChangeNotificationsSwitch: SwitchMaterial? = null
    private var doNotDisturbSwitch: SwitchMaterial? = null
    private var startTimePicker: TimePicker? = null
    private var endTimePicker: TimePicker? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notification_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Find views
        delayNotificationsSwitch = view.findViewById(R.id.delayNotificationsSwitch)
        cancellationNotificationsSwitch = view.findViewById(R.id.cancellationNotificationsSwitch)
        gateChangeNotificationsSwitch = view.findViewById(R.id.gateChangeNotificationsSwitch)
        doNotDisturbSwitch = view.findViewById(R.id.doNotDisturbSwitch)
        startTimePicker = view.findViewById(R.id.startTimePicker)
        endTimePicker = view.findViewById(R.id.endTimePicker)
        
        // Load current settings and update UI
        loadNotificationSettings()
        
        // Set up listeners for preference changes
        setupPreferenceListeners()
    }

    private fun loadNotificationSettings() {
        // In a real implementation, you would load settings from SharedPreferences or a database
        // For now, we'll just set default values
        delayNotificationsSwitch?.isChecked = true
        cancellationNotificationsSwitch?.isChecked = true
        gateChangeNotificationsSwitch?.isChecked = true
        doNotDisturbSwitch?.isChecked = false
    }

    private fun setupPreferenceListeners() {
        delayNotificationsSwitch?.setOnCheckedChangeListener { _, isChecked ->
            // Save preference
            savePreference("delay_notifications", isChecked)
        }
        
        cancellationNotificationsSwitch?.setOnCheckedChangeListener { _, isChecked ->
            // Save preference
            savePreference("cancellation_notifications", isChecked)
        }
        
        gateChangeNotificationsSwitch?.setOnCheckedChangeListener { _, isChecked ->
            // Save preference
            savePreference("gate_change_notifications", isChecked)
        }
        
        doNotDisturbSwitch?.setOnCheckedChangeListener { _, isChecked ->
            // Enable/disable time pickers
            startTimePicker?.isEnabled = isChecked
            endTimePicker?.isEnabled = isChecked
            
            // Save preference
            savePreference("do_not_disturb", isChecked)
        }
    }

    private fun savePreference(key: String, value: Boolean) {
        // In a real implementation, you would save to SharedPreferences or a database
        // For now, we'll just log the change
        // Log.d("NotificationSettings", "Saving preference: $key = $value")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        delayNotificationsSwitch = null
        cancellationNotificationsSwitch = null
        gateChangeNotificationsSwitch = null
        doNotDisturbSwitch = null
        startTimePicker = null
        endTimePicker = null
    }
}