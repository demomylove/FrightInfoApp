package com.flightinfo.app.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.flightinfo.app.R
import com.flightinfo.app.databinding.FragmentNotificationSettingsBinding

class NotificationSettingsFragment : Fragment() {

    private var _binding: FragmentNotificationSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var prefs: SharedPreferences

    companion object {
        const val PREFS_NAME = "NotificationPrefs"
        const val KEY_GATE_CHANGE = "notification_gate_change"
        const val KEY_DELAY = "notification_delay"
        const val KEY_CANCELLATION = "notification_cancellation"
        const val KEY_BOARDING_TIME = "notification_boarding_time"
        const val KEY_LEAVE_FOR_AIRPORT = "reminder_leave_for_airport"
        const val KEY_REMINDER_TIME_INDEX = "reminder_time_index"
        const val KEY_DO_NOT_DISTURB = "dnd_enabled"
        const val KEY_DND_START_HOUR = "dnd_start_hour"
        const val KEY_DND_START_MINUTE = "dnd_start_minute"
        const val KEY_DND_END_HOUR = "dnd_end_hour"
        const val KEY_DND_END_MINUTE = "dnd_end_minute"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNotificationSettingsBinding.inflate(inflater, container, false)
        prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpinner()
        loadSettings()
        setupListeners()
    }

    private fun setupSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.reminder_time_options,
            android.R.layout.simple_spinner_item,
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.reminderTimeSpinner.adapter = adapter
        }
    }

    private fun loadSettings() {
        binding.gateChangeSwitch.isChecked = prefs.getBoolean(KEY_GATE_CHANGE, true)
        binding.delaySwitch.isChecked = prefs.getBoolean(KEY_DELAY, true)
        binding.cancellationSwitch.isChecked = prefs.getBoolean(KEY_CANCELLATION, true)
        binding.boardingTimeSwitch.isChecked = prefs.getBoolean(KEY_BOARDING_TIME, true)

        val isLeaveReminderEnabled = prefs.getBoolean(KEY_LEAVE_FOR_AIRPORT, false)
        binding.leaveForAirportSwitch.isChecked = isLeaveReminderEnabled
        binding.reminderTimeSpinner.isEnabled = isLeaveReminderEnabled
        binding.reminderTimeSpinner.setSelection(prefs.getInt(KEY_REMINDER_TIME_INDEX, 1)) // Default to 3 hours

        val isDndEnabled = prefs.getBoolean(KEY_DO_NOT_DISTURB, false)
        binding.doNotDisturbSwitch.isChecked = isDndEnabled
        binding.timePickerLayout.isVisible = isDndEnabled
        binding.startTimePicker.hour = prefs.getInt(KEY_DND_START_HOUR, 22)
        binding.startTimePicker.minute = prefs.getInt(KEY_DND_START_MINUTE, 0)
        binding.endTimePicker.hour = prefs.getInt(KEY_DND_END_HOUR, 7)
        binding.endTimePicker.minute = prefs.getInt(KEY_DND_END_MINUTE, 0)
    }

    private fun setupListeners() {
        val editor = prefs.edit()

        binding.gateChangeSwitch.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean(KEY_GATE_CHANGE, isChecked).apply()
        }
        binding.delaySwitch.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean(KEY_DELAY, isChecked).apply()
        }
        binding.cancellationSwitch.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean(KEY_CANCELLATION, isChecked).apply()
        }
        binding.boardingTimeSwitch.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean(KEY_BOARDING_TIME, isChecked).apply()
        }

        binding.leaveForAirportSwitch.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean(KEY_LEAVE_FOR_AIRPORT, isChecked).apply()
            binding.reminderTimeSpinner.isEnabled = isChecked
        }

        binding.reminderTimeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                editor.putInt(KEY_REMINDER_TIME_INDEX, position).apply()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.doNotDisturbSwitch.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean(KEY_DO_NOT_DISTURB, isChecked).apply()
            binding.timePickerLayout.isVisible = isChecked
        }

        binding.startTimePicker.setOnTimeChangedListener { _, hour, minute ->
            editor.putInt(KEY_DND_START_HOUR, hour)
                .putInt(KEY_DND_START_MINUTE, minute)
                .apply()
        }

        binding.endTimePicker.setOnTimeChangedListener { _, hour, minute ->
            editor.putInt(KEY_DND_END_HOUR, hour)
                .putInt(KEY_DND_END_MINUTE, minute)
                .apply()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
