package com.flightinfo.app.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.flightinfo.app.R
import com.flightinfo.app.data.model.NotificationPreferences
import com.flightinfo.app.data.model.NotificationType

/**
 * 通知设置适配器
 * 用于在RecyclerView中显示和管理通知设置选项
 */
class NotificationSettingsAdapter(
    private val onSettingChanged: (NotificationType, Boolean) -> Unit,
    private val onWatchedItemAdded: (String, String) -> Unit,
    private val onWatchedItemRemoved: (String, String) -> Unit,
    private val onDndSettingsChanged: (Int, Int, Int, Int) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val settings = mutableListOf<NotificationSettingItem>()
    private var watchedFlights = emptyList<String>()
    private var watchedAirports = emptyList<String>()
    private var watchedAirlines = emptyList<String>()

    companion object {
        private const val TYPE_NOTIFICATION_TOGGLE = 1
        private const val TYPE_WATCHED_FLIGHTS = 2
        private const val TYPE_WATCHED_AIRPORTS = 3
        private const val TYPE_WATCHED_AIRLINES = 4
    }

    init {
        setupSettingsList()
    }

    private fun setupSettingsList() {
        settings.clear()
        settings.add(NotificationSettingItem.Header("通知类型设置"))

        // 添加各个通知类型的开关
        NotificationType.values().forEach { type ->
            settings.add(NotificationSettingItem.NotificationToggle(type))
        }

        settings.add(NotificationSettingItem.Header("关注列表"))
        settings.add(NotificationSettingItem.WatchedFlights)
        settings.add(NotificationSettingItem.WatchedAirports)
        settings.add(NotificationSettingItem.WatchedAirlines)
    }

    fun updateNotificationSettings(preferences: NotificationPreferences) {
        // 更新各个通知类型的开关状态
        settings.filterIsInstance<NotificationSettingItem.NotificationToggle>()
            .forEach { item ->
                item.isEnabled = when (item.type) {
                    NotificationType.FLIGHT_STATUS -> preferences.flightStatusEnabled
                    NotificationType.FLIGHT_DELAY -> preferences.flightDelayEnabled
                    NotificationType.FLIGHT_CANCELLED -> preferences.flightCancellationEnabled
                    NotificationType.BOARDING_TIME -> preferences.boardingTimeEnabled
                    NotificationType.GATE_CHANGE -> preferences.gateChangeEnabled
                    NotificationType.BAGGAGE_STATUS -> preferences.baggageStatusEnabled
                    NotificationType.PRICE_ALERT -> preferences.priceAlertEnabled
                    NotificationType.TRIP_REMINDER -> preferences.tripReminderEnabled
                    NotificationType.WEATHER_ALERT -> preferences.weatherAlertEnabled
                    NotificationType.CUSTOM -> true
                }
            }
        notifyDataSetChanged()
    }

    fun updateWatchedFlights(flights: List<String>) {
        watchedFlights = flights
        notifyDataSetChanged()
    }

    fun updateWatchedAirports(airports: List<String>) {
        watchedAirports = airports
        notifyDataSetChanged()
    }

    fun updateWatchedAirlines(airlines: List<String>) {
        watchedAirlines = airlines
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (settings[position]) {
            is NotificationSettingItem.Header -> 0
            is NotificationSettingItem.NotificationToggle -> TYPE_NOTIFICATION_TOGGLE
            is NotificationSettingItem.WatchedFlights -> TYPE_WATCHED_FLIGHTS
            is NotificationSettingItem.WatchedAirports -> TYPE_WATCHED_AIRPORTS
            is NotificationSettingItem.WatchedAirlines -> TYPE_WATCHED_AIRLINES
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> HeaderViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(android.R.layout.simple_list_item_1, parent, false),
            )
            TYPE_NOTIFICATION_TOGGLE -> NotificationToggleViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_notification_toggle, parent, false),
            )
            TYPE_WATCHED_FLIGHTS -> WatchedFlightsViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_watched_list, parent, false),
            )
            TYPE_WATCHED_AIRPORTS -> WatchedAirportsViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_watched_list, parent, false),
            )
            TYPE_WATCHED_AIRLINES -> WatchedAirlinesViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_watched_list, parent, false),
            )
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = settings[position]
        when {
            holder is HeaderViewHolder && item is NotificationSettingItem.Header -> {
                holder.bind(item.title)
            }
            holder is NotificationToggleViewHolder && item is NotificationSettingItem.NotificationToggle -> {
                holder.bind(item)
            }
            holder is WatchedFlightsViewHolder && item is NotificationSettingItem.WatchedFlights -> {
                holder.bind(watchedFlights, "航班号", "FLIGHT") { flightNumber ->
                    onWatchedItemAdded("FLIGHT", flightNumber)
                }
            }
            holder is WatchedAirportsViewHolder && item is NotificationSettingItem.WatchedAirports -> {
                holder.bind(watchedAirports, "机场代码", "AIRPORT") { airport ->
                    onWatchedItemAdded("AIRPORT", airport)
                }
            }
            holder is WatchedAirlinesViewHolder && item is NotificationSettingItem.WatchedAirlines -> {
                holder.bind(watchedAirlines, "航空公司", "AIRLINE") { airline ->
                    onWatchedItemAdded("AIRLINE", airline)
                }
            }
        }
    }

    override fun getItemCount() = settings.size

    // 头部视图持有者
    private inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(title: String) {
            textView.text = title
            textView.setTextColor(itemView.context.getColor(android.R.color.black))
            textView.textSize = 16f
            textView.setPadding(0, 16, 0, 8)
        }
    }

    // 通知开关视图持有者
    private inner class NotificationToggleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.text_title)
        private val descriptionText: TextView = itemView.findViewById(R.id.text_description)
        private val switch: SwitchCompat = itemView.findViewById(R.id.switch_toggle)

        fun bind(item: NotificationSettingItem.NotificationToggle) {
            titleText.text = getNotificationTypeTitle(item.type)
            descriptionText.text = getNotificationTypeDescription(item.type)
            switch.isChecked = item.isEnabled

            switch.setOnCheckedChangeListener { _, isChecked ->
                item.isEnabled = isChecked
                onSettingChanged(item.type, isChecked)
            }
        }

        private fun getNotificationTypeTitle(type: NotificationType): String {
            return when (type) {
                NotificationType.FLIGHT_STATUS -> "航班状态通知"
                NotificationType.FLIGHT_DELAY -> "航班延误通知"
                NotificationType.FLIGHT_CANCELLED -> "航班取消通知"
                NotificationType.BOARDING_TIME -> "登机时间提醒"
                NotificationType.GATE_CHANGE -> "登机口变更通知"
                NotificationType.BAGGAGE_STATUS -> "行李状态通知"
                NotificationType.PRICE_ALERT -> "价格提醒通知"
                NotificationType.TRIP_REMINDER -> "行程提醒通知"
                NotificationType.WEATHER_ALERT -> "天气提醒通知"
                NotificationType.CUSTOM -> "自定义通知"
            }
        }

        private fun getNotificationTypeDescription(type: NotificationType): String {
            return when (type) {
                NotificationType.FLIGHT_STATUS -> "接收航班起飞、到达等状态更新"
                NotificationType.FLIGHT_DELAY -> "航班延误时及时提醒"
                NotificationType.FLIGHT_CANCELLED -> "航班取消时立即通知"
                NotificationType.BOARDING_TIME -> "登机开始前提醒"
                NotificationType.GATE_CHANGE -> "登机口变更时通知"
                NotificationType.BAGGAGE_STATUS -> "行李状态变更提醒"
                NotificationType.PRICE_ALERT -> "机票价格变动提醒"
                NotificationType.TRIP_REMINDER -> "行程相关重要提醒"
                NotificationType.WEATHER_ALERT -> "目的地天气变化提醒"
                NotificationType.CUSTOM -> "自定义通知类型"
            }
        }
    }

    // 关注航班视图持有者
    private inner class WatchedFlightsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(flights: List<String>, hint: String, type: String, onAdd: (String) -> Unit) {
            setupWatchedList(itemView, flights, hint, type, onAdd)
        }
    }

    // 关注机场视图持有者
    private inner class WatchedAirportsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(airports: List<String>, hint: String, type: String, onAdd: (String) -> Unit) {
            setupWatchedList(itemView, airports, hint, type, onAdd)
        }
    }

    // 关注航空公司视图持有者
    private inner class WatchedAirlinesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(airlines: List<String>, hint: String, type: String, onAdd: (String) -> Unit) {
            setupWatchedList(itemView, airlines, hint, type, onAdd)
        }
    }

    private fun setupWatchedList(
        itemView: View,
        items: List<String>,
        hint: String,
        type: String,
        onAdd: (String) -> Unit,
    ) {
        val titleText: TextView = itemView.findViewById(R.id.text_title)
        val addEditText: EditText = itemView.findViewById(R.id.edit_add_item)
        val addButton: Button = itemView.findViewById(R.id.btn_add)
        val itemsContainer: LinearLayout = itemView.findViewById(R.id.container_items)

        titleText.text = "关注的$hint"

        // 清空现有项目
        itemsContainer.removeAllViews()

        // 添加现有项目
        items.forEach { item ->
            val itemView = createWatchedItemView(itemsContainer, item, type)
            itemsContainer.addView(itemView)
        }

        // 设置添加功能
        addEditText.hint = "添加$hint"
        addButton.setOnClickListener {
            val newItem = addEditText.text.toString().trim()
            if (newItem.isNotEmpty()) {
                onAdd(newItem)
                addEditText.text.clear()
            }
        }
    }

    private fun createWatchedItemView(
        container: LinearLayout,
        item: String,
        type: String,
    ): View {
        return LayoutInflater.from(container.context)
            .inflate(R.layout.item_watched_tag, container, false)
            .apply {
                findViewById<TextView>(R.id.text_item).text = item
                findViewById<Button>(R.id.btn_remove).setOnClickListener {
                    onWatchedItemRemoved(type, item)
                    container.removeView(this)
                }
            }
    }
}

// 设置项数据类
sealed class NotificationSettingItem {
    data class Header(val title: String) : NotificationSettingItem()
    data class NotificationToggle(
        val type: NotificationType,
        var isEnabled: Boolean = true,
    ) : NotificationSettingItem()

    object WatchedFlights : NotificationSettingItem()
    object WatchedAirports : NotificationSettingItem()
    object WatchedAirlines : NotificationSettingItem()
}
