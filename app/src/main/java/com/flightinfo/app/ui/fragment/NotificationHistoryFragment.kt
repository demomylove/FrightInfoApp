package com.flightinfo.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.flightinfo.app.data.model.NotificationHistory
import com.flightinfo.app.data.repository.NotificationRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 通知历史界面
 * 显示用户的所有通知历史记录，支持分类查看和管理
 */
@AndroidEntryPoint
class NotificationHistoryFragment : Fragment() {

    @Inject
    lateinit var notificationRepository: NotificationRepository

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val notifications = mutableListOf<NotificationHistory>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(android.R.layout.activity_list_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(android.R.id.list)
        swipeRefreshLayout = SwipeRefreshLayout(requireContext()).apply {
            addView(recyclerView)
        }

        setupRecyclerView()
        setupSwipeRefresh()
        loadNotifications()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(android.R.layout.simple_list_item_2, parent, false)
                return object : RecyclerView.ViewHolder(view) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val notification = notifications[position]
                holder.itemView.findViewById<android.widget.TextView>(android.R.id.text1).text = notification.title
                holder.itemView.findViewById<android.widget.TextView>(android.R.id.text2).text = notification.message
            }

            override fun getItemCount() = notifications.size
        }
    }

    private fun setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener {
            loadNotifications()
        }
    }

    private fun loadNotifications() {
        swipeRefreshLayout.isRefreshing = true
        lifecycleScope.launch {
            try {
                notificationRepository.getAllNotifications().collect { notificationList ->
                    notifications.clear()
                    notifications.addAll(notificationList)
                    recyclerView.adapter?.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                // 处理错误
            } finally {
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }
}
