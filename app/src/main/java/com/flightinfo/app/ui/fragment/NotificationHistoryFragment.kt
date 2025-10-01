package com.flightinfo.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
    private var showUnreadOnly: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        recyclerView = RecyclerView(requireContext())
        swipeRefreshLayout = SwipeRefreshLayout(requireContext()).apply {
            addView(
                recyclerView,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                ),
            )
        }
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
                val flow = if (showUnreadOnly) {
                    notificationRepository.getUnreadNotifications()
                } else {
                    notificationRepository.getAllNotifications()
                }
                flow.collect { notificationList ->
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.add(0, 1, 0, if (showUnreadOnly) "显示全部" else "仅看未读")
        menu.add(0, 2, 1, "全部标记已读")
        menu.add(0, 3, 2, "清空历史")
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(1)?.title = if (showUnreadOnly) "显示全部" else "仅看未读"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            1 -> {
                showUnreadOnly = !showUnreadOnly
                activity?.invalidateOptionsMenu()
                loadNotifications()
                true
            }
            2 -> {
                lifecycleScope.launch {
                    notificationRepository.markAllAsRead()
                }
                true
            }
            3 -> {
                lifecycleScope.launch {
                    notificationRepository.deleteAllNotifications()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
