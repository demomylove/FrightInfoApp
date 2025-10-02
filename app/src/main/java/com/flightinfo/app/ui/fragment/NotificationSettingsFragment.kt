package com.flightinfo.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.flightinfo.app.data.model.NotificationPreferences
import com.flightinfo.app.databinding.FragmentNotificationSettingsBinding
import com.flightinfo.app.ui.adapter.NotificationSettingsAdapter
import com.flightinfo.app.ui.viewmodel.NotificationSettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * 通知设置界面 - 用户体验增强版
 * 提供直观的通知偏好设置界面，支持个性化配置
 */
@AndroidEntryPoint
class NotificationSettingsFragment : Fragment() {
    companion object {
        const val PREFS_NAME = "notification_prefs"
        const val KEY_LEAVE_FOR_AIRPORT = "leave_for_airport"
        const val KEY_REMINDER_TIME_INDEX = "reminder_time_index"
    }

    private var _binding: FragmentNotificationSettingsBinding? = null
    val binding get() = _binding!!

    private val viewModel: NotificationSettingsViewModel by viewModels()
    private lateinit var adapter: NotificationSettingsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNotificationSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        setupClickListeners()

        // 加载通知设置
        viewModel.loadNotificationSettings()
    }

    private fun setupRecyclerView() {
        adapter = NotificationSettingsAdapter(
            onSettingChanged = { settingType, enabled ->
                viewModel.updateNotificationSetting(settingType, enabled)
            },
            onWatchedItemAdded = { type, item ->
                viewModel.addWatchedItem(type, item)
            },
            onWatchedItemRemoved = { type, item ->
                viewModel.removeWatchedItem(type, item)
            },
            onDndSettingsChanged = { startHour, startMinute, endHour, endMinute ->
                viewModel.updateDndSettings(startHour, startMinute, endHour, endMinute)
            },
        )

        binding.recyclerViewSettings.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@NotificationSettingsFragment.adapter
        }
    }

    private fun setupObservers() {
        // 观察通知偏好设置
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.notificationPreferences.collect { preferences ->
                preferences?.let { updateUI(it) }
            }
        }

        // 观察关注的航班列表
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.watchedFlights.collect { flights ->
                adapter.updateWatchedFlights(flights)
            }
        }

        // 观察关注的机场列表
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.watchedAirports.collect { airports ->
                adapter.updateWatchedAirports(airports)
            }
        }

        // 观察关注的航空公司列表
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.watchedAirlines.collect { airlines ->
                adapter.updateWatchedAirlines(airlines)
            }
        }
    }

    private fun setupClickListeners() {
        // 勿扰模式开关
        binding.switchDnd.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setDndEnabled(isChecked)
        }

        // 通知总开关
        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setNotificationsEnabled(isChecked)
        }

        // 时间选择器
        binding.timePickerStart.setOnTimeChangedListener { _, hourOfDay, minute ->
            // 更新开始时间
        }

        binding.timePickerEnd.setOnTimeChangedListener { _, hourOfDay, minute ->
            // 更新结束时间
        }

        // 保存按钮
        binding.btnSave.setOnClickListener {
            saveSettings()
        }

        // 重置按钮
        binding.btnReset.setOnClickListener {
            showResetConfirmationDialog()
        }
    }

    private fun updateUI(preferences: NotificationPreferences) {
        // 更新总开关
        binding.switchNotifications.isChecked = preferences.notificationsEnabled

        // 更新勿扰模式
        binding.switchDnd.isChecked = preferences.dndEnabled
        binding.timePickerStart.hour = preferences.dndStartHour
        binding.timePickerStart.minute = preferences.dndStartMinute
        binding.timePickerEnd.hour = preferences.dndEndHour
        binding.timePickerEnd.minute = preferences.dndEndMinute

        // 更新各个通知类型开关
        adapter.updateNotificationSettings(preferences)

        // 更新振动、声音、LED设置
        binding.switchVibration.isChecked = preferences.vibrationEnabled
        binding.switchSound.isChecked = preferences.soundEnabled
        binding.switchLed.isChecked = preferences.ledEnabled

        // 显示勿扰时间段
        updateDndTimeDisplay(preferences)
    }

    private fun updateDndTimeDisplay(preferences: NotificationPreferences) {
        val startTime = String.format("%02d:%02d", preferences.dndStartHour, preferences.dndStartMinute)
        val endTime = String.format("%02d:%02d", preferences.dndEndHour, preferences.dndEndMinute)
        binding.textDndTime.text = "勿扰时间: $startTime - $endTime"
    }

    private fun saveSettings() {
        // 保存当前设置到数据库
        viewModel.saveSettings()
        showSaveSuccessMessage()
    }

    private fun showResetConfirmationDialog() {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("重置设置")
            .setMessage("确定要恢复默认通知设置吗？")
            .setPositiveButton("重置") { _, _ ->
                viewModel.resetToDefault()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun showSaveSuccessMessage() {
        android.widget.Toast.makeText(
            requireContext(),
            "设置已保存",
            android.widget.Toast.LENGTH_SHORT,
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
