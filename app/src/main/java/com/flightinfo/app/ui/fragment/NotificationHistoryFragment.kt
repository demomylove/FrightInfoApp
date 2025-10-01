package com.flightinfo.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.flightinfo.app.R
import com.flightinfo.app.data.model.NotificationCategory
import com.flightinfo.app.data.model.NotificationHistory
import com.flightinfo.app.data.model.NotificationType
import com.flightinfo.app.data.repository.NotificationRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * 通知历史界面 - 用户体验增强版
 * 显示用户的所有通知历史记录，支持分类查看、多选操作和管理
 */
@AndroidEntryPoint
class NotificationHistoryFragment : Fragment() {

    @Inject
    lateinit var notificationRepository: NotificationRepository

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val notifications = mutableListOf<NotificationHistory>()
    private val selectedNotifications = mutableSetOf<Long>()
    private var isMultiSelectMode = false
    private var currentFilterType: NotificationType? = null
    private var currentFilterCategory: NotificationCategory? = null
    private var showUnreadOnly: Boolean = false

    // 时间格式化器
    private val timeFormatter = SimpleDateFormat("MM-dd HH:mm", Locale.getDefault())
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // 使用SwipeRefreshLayout作为根视图
        swipeRefreshLayout = SwipeRefreshLayout(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
        }

        recyclerView = RecyclerView(requireContext()).apply {
            layoutManager = LinearLayoutManager(requireContext())
        }

        swipeRefreshLayout.addView(recyclerView)
        return swipeRefreshLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        setupRecyclerView()
        setupSwipeRefresh()
        loadNotifications()
    }

    private fun setupRecyclerView() {
        recyclerView.adapter = NotificationAdapter(
            notifications = notifications,
            selectedNotifications = selectedNotifications,
            isMultiSelectMode = isMultiSelectMode,
            timeFormatter = timeFormatter,
            onNotificationClick = { notification ->
                if (isMultiSelectMode) {
                    toggleSelection(notification.id)
                } else {
                    markAsRead(notification.id)
                }
            },
            onNotificationLongClick = { notification ->
                if (!isMultiSelectMode) {
                    enterMultiSelectMode()
                    toggleSelection(notification.id)
                }
            },
        )
    }

    private fun setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener {
            loadNotifications()
        }
        // 设置刷新时的颜色主题
        swipeRefreshLayout.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light,
        )
    }

    private fun loadNotifications() {
        swipeRefreshLayout.isRefreshing = true
        lifecycleScope.launch {
            try {
                val flow = when {
                    showUnreadOnly -> notificationRepository.getUnreadNotifications()
                    currentFilterType != null -> notificationRepository.getNotificationsByType(currentFilterType!!)
                    currentFilterCategory != null -> notificationRepository.getNotificationsByCategory(currentFilterCategory!!)
                    else -> notificationRepository.getAllNotifications()
                }

                flow.collect { notificationList ->
                    notifications.clear()
                    notifications.addAll(notificationList)
                    recyclerView.adapter?.notifyDataSetChanged()
                    updateEmptyState()
                }
            } catch (e: Exception) {
                showError("加载通知失败: ${e.message}")
            } finally {
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun markAsRead(notificationId: Long) {
        lifecycleScope.launch {
            try {
                notificationRepository.markAsRead(notificationId)
            } catch (e: Exception) {
                showError("标记已读失败: ${e.message}")
            }
        }
    }

    private fun toggleSelection(notificationId: Long) {
        if (selectedNotifications.contains(notificationId)) {
            selectedNotifications.remove(notificationId)
        } else {
            selectedNotifications.add(notificationId)
        }
        recyclerView.adapter?.notifyDataSetChanged()
        updateActionModeTitle()
    }

    private fun enterMultiSelectMode() {
        isMultiSelectMode = true
        selectedNotifications.clear()
        recyclerView.adapter?.notifyDataSetChanged()
        // 这里可以启动ActionMode进行批量操作
    }

    private fun exitMultiSelectMode() {
        isMultiSelectMode = false
        selectedNotifications.clear()
        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun updateEmptyState() {
        // 可以在这里添加空状态视图
    }

    private fun updateActionModeTitle() {
        // 更新批量操作模式标题
    }

    private fun showError(message: String) {
        // 显示错误信息给用户
        android.widget.Toast.makeText(requireContext(), message, android.widget.Toast.LENGTH_SHORT).show()
    }

    private fun showFilterDialog() {
        val filterOptions = arrayOf(
            "全部通知",
            "航班状态通知",
            "行李跟踪通知",
            "价格提醒通知",
            "行程提醒通知",
            "天气提醒通知",
            "仅未读通知",
        )

        AlertDialog.Builder(requireContext())
            .setTitle("筛选通知")
            .setItems(filterOptions) { _, which ->
                when (which) {
                    0 -> {
                        currentFilterType = null
                        currentFilterCategory = null
                        showUnreadOnly = false
                    }
                    1 -> {
                        currentFilterType = NotificationType.FLIGHT_STATUS
                        currentFilterCategory = null
                    }
                    2 -> {
                        currentFilterType = NotificationType.BAGGAGE_STATUS
                        currentFilterCategory = null
                    }
                    3 -> {
                        currentFilterType = NotificationType.PRICE_ALERT
                        currentFilterCategory = null
                    }
                    4 -> {
                        currentFilterType = NotificationType.TRIP_REMINDER
                        currentFilterCategory = null
                    }
                    5 -> {
                        currentFilterType = NotificationType.WEATHER_ALERT
                        currentFilterCategory = null
                    }
                    6 -> {
                        showUnreadOnly = true
                        currentFilterType = null
                        currentFilterCategory = null
                    }
                }
                loadNotifications()
            }
            .show()
    }

    private fun showBatchOperationsDialog() {
        if (selectedNotifications.isEmpty()) return

        val operations = arrayOf("标记为已读", "删除选中项", "取消选择")

        AlertDialog.Builder(requireContext())
            .setTitle("批量操作 (${selectedNotifications.size}项)")
            .setItems(operations) { _, which ->
                when (which) {
                    0 -> {
                        lifecycleScope.launch {
                            selectedNotifications.forEach { id ->
                                notificationRepository.markAsRead(id)
                            }
                            exitMultiSelectMode()
                        }
                    }
                    1 -> {
                        AlertDialog.Builder(requireContext())
                            .setTitle("确认删除")
                            .setMessage("确定要删除选中的 ${selectedNotifications.size} 条通知吗？")
                            .setPositiveButton("删除") { _, _ ->
                                lifecycleScope.launch {
                                    selectedNotifications.forEach { id ->
                                        notificationRepository.deleteNotification(id)
                                    }
                                    exitMultiSelectMode()
                                }
                            }
                            .setNegativeButton("取消", null)
                            .show()
                    }
                    2 -> exitMultiSelectMode()
                }
            }
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_notification_history, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                showFilterDialog()
                true
            }
            R.id.action_mark_all_read -> {
                lifecycleScope.launch {
                    notificationRepository.markAllAsRead()
                }
                true
            }
            R.id.action_clear_all -> {
                AlertDialog.Builder(requireContext())
                    .setTitle("清空历史")
                    .setMessage("确定要清空所有通知历史吗？此操作不可撤销。")
                    .setPositiveButton("清空") { _, _ ->
                        lifecycleScope.launch {
                            notificationRepository.deleteAllNotifications()
                        }
                    }
                    .setNegativeButton("取消", null)
                    .show()
                true
            }
            R.id.action_multi_select -> {
                if (isMultiSelectMode) {
                    exitMultiSelectMode()
                } else {
                    enterMultiSelectMode()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // 通知适配器内部类
    private inner class NotificationAdapter(
        private val notifications: List<NotificationHistory>,
        private val selectedNotifications: Set<Long>,
        private val isMultiSelectMode: Boolean,
        private val timeFormatter: SimpleDateFormat,
        private val onNotificationClick: (NotificationHistory) -> Unit,
        private val onNotificationLongClick: (NotificationHistory) -> Unit,
    ) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_notification_enhanced, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val notification = notifications[position]
            holder.bind(notification)
        }

        override fun getItemCount() = notifications.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val titleText: android.widget.TextView = itemView.findViewById(R.id.text_title)
            private val messageText: android.widget.TextView = itemView.findViewById(R.id.text_message)
            private val timeText: android.widget.TextView = itemView.findViewById(R.id.text_time)
            private val categoryText: android.widget.TextView = itemView.findViewById(R.id.text_category)
            private val checkBox: CheckBox = itemView.findViewById(R.id.checkbox_select)

            fun bind(notification: NotificationHistory) {
                titleText.text = notification.title
                messageText.text = notification.message
                timeText.text = timeFormatter.format(Date(notification.timestamp))

                // 设置分类标签
                categoryText.text = when (notification.category) {
                    NotificationCategory.FLIGHT_INFO -> "航班"
                    NotificationCategory.BAGGAGE_TRACKING -> "行李"
                    NotificationCategory.PRICE_TRACKING -> "价格"
                    NotificationCategory.TRIP_REMINDER -> "提醒"
                    NotificationCategory.WEATHER -> "天气"
                    NotificationCategory.SYSTEM -> "系统"
                }

                // 设置优先级颜色
                val priorityColor = when (notification.priority) {
                    com.flightinfo.app.data.model.NotificationPriority.LOW -> android.graphics.Color.GRAY
                    com.flightinfo.app.data.model.NotificationPriority.NORMAL -> android.graphics.Color.BLACK
                    com.flightinfo.app.data.model.NotificationPriority.HIGH -> android.graphics.Color.BLUE
                    com.flightinfo.app.data.model.NotificationPriority.URGENT -> android.graphics.Color.RED
                }
                titleText.setTextColor(priorityColor)

                // 设置未读状态
                val alpha = if (notification.isRead) 0.5f else 1.0f
                itemView.alpha = alpha

                // 多选模式
                checkBox.visibility = if (isMultiSelectMode) View.VISIBLE else View.GONE
                checkBox.isChecked = selectedNotifications.contains(notification.id)

                itemView.setOnClickListener {
                    onNotificationClick(notification)
                }

                itemView.setOnLongClickListener {
                    onNotificationLongClick(notification)
                    true
                }
            }
        }
    }
}
