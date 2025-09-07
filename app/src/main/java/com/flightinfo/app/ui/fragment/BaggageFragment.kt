package com.flightinfo.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flightinfo.app.R
import com.flightinfo.app.data.model.BaggageItem
import com.flightinfo.app.data.model.BaggageStatus
import com.flightinfo.app.data.model.BaggageTrackingRequest
import com.flightinfo.app.ui.viewmodel.BaggageViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BaggageFragment : Fragment() {

    private val viewModel: BaggageViewModel by viewModels()
    private lateinit var baggageAdapter: BaggageAdapter
    private lateinit var progressBar: CircularProgressIndicator
    private lateinit var errorTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyStateView: LinearLayout
    private lateinit var trackBaggageButton: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_baggage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews(view)
        setupRecyclerView()
        observeViewModel()

        viewModel.createSampleBaggageIfEmpty()

        trackBaggageButton.setOnClickListener {
            showTrackBaggageDialog()
        }
    }

    private fun setupViews(view: View) {
        progressBar = view.findViewById(R.id.progressBar)
        errorTextView = view.findViewById(R.id.errorTextView)
        recyclerView = view.findViewById(R.id.baggageRecyclerView)
        emptyStateView = view.findViewById(R.id.emptyStateView)
        trackBaggageButton = view.findViewById(R.id.trackBaggageButton)
    }

    private fun setupRecyclerView() {
        baggageAdapter = BaggageAdapter { baggageItem ->
            showBaggageOptionsDialog(baggageItem)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = baggageAdapter
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.baggageItems.collect { items ->
                baggageAdapter.submitList(items)
                updateEmptyState(items.isEmpty())
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect { error ->
                errorTextView.visibility = if (error != null) View.VISIBLE else View.GONE
                errorTextView.text = error
            }
        }
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        emptyStateView.visibility = if (isEmpty) View.VISIBLE else View.GONE
        recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun showTrackBaggageDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_track_baggage, null)
        val tagNumberInput = dialogView.findViewById<TextInputEditText>(R.id.tagNumberInput)
        val flightNumberInput = dialogView.findViewById<TextInputEditText>(R.id.flightNumberInput)
        val passengerNameInput = dialogView.findViewById<TextInputEditText>(R.id.passengerNameInput)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("跟踪行李")
            .setView(dialogView)
            .setPositiveButton("跟踪") { _, _ ->
                val tagNumber = tagNumberInput.text?.toString()?.trim()
                val flightNumber = flightNumberInput.text?.toString()?.trim()
                val passengerName = passengerNameInput.text?.toString()?.trim()

                if (tagNumber.isNullOrEmpty() || flightNumber.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), "请填写行李标签号和航班号", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val request = BaggageTrackingRequest(
                    baggageTagNumber = tagNumber,
                    flightNumber = flightNumber,
                    passengerName = passengerName,
                )

                viewModel.trackBaggage(request)
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun showBaggageOptionsDialog(baggageItem: BaggageItem) {
        val options = arrayOf("刷新状态", "查看详情", "编辑备注", "删除")

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("行李选项")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> viewModel.refreshBaggageStatus(baggageItem.baggageTagNumber)
                    1 -> showBaggageDetailsDialog(baggageItem)
                    2 -> showEditNotesDialog(baggageItem)
                    3 -> confirmDeleteBaggage(baggageItem)
                }
            }
            .show()
    }

    private fun showBaggageDetailsDialog(baggageItem: BaggageItem) {
        val details = """
            行李标签号: ${baggageItem.baggageTagNumber}
            航班号: ${baggageItem.flightNumber}
            类型: ${getBaggageTypeDisplayName(baggageItem.baggageType)}
            重量: ${baggageItem.weight} ${baggageItem.weightUnit}
            状态: ${getBaggageStatusDisplayName(baggageItem.status)}
            最后位置: ${baggageItem.lastLocation ?: "未知"}
            最后更新: ${baggageItem.lastUpdated}
            备注: ${baggageItem.notes ?: "无"}
        """.trimIndent()

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("行李详情")
            .setMessage(details)
            .setPositiveButton("确定", null)
            .show()
    }

    private fun showEditNotesDialog(baggageItem: BaggageItem) {
        val input = TextInputEditText(requireContext())
        input.setText(baggageItem.notes ?: "")

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("编辑备注")
            .setView(input)
            .setPositiveButton("保存") { _, _ ->
                val updatedItem = baggageItem.copy(notes = input.text?.toString())
                viewModel.updateBaggageItem(updatedItem)
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun confirmDeleteBaggage(baggageItem: BaggageItem) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("确认删除")
            .setMessage("确定要删除这个行李记录吗？")
            .setPositiveButton("删除") { _, _ ->
                viewModel.deleteBaggageItem(baggageItem)
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun getBaggageTypeDisplayName(type: com.flightinfo.app.data.model.BaggageType): String {
        return when (type) {
            com.flightinfo.app.data.model.BaggageType.CHECKED -> "托运行李"
            com.flightinfo.app.data.model.BaggageType.CARRY_ON -> "随身行李"
            com.flightinfo.app.data.model.BaggageType.OVERSIZED -> "超大行李"
            com.flightinfo.app.data.model.BaggageType.SPECIAL -> "特殊行李"
            com.flightinfo.app.data.model.BaggageType.FRAGILE -> "易碎行李"
        }
    }

    private fun getBaggageStatusDisplayName(status: BaggageStatus): String {
        return when (status) {
            BaggageStatus.CHECKED_IN -> "已办理"
            BaggageStatus.LOADED -> "已装载"
            BaggageStatus.IN_TRANSIT -> "运输中"
            BaggageStatus.UNLOADED -> "已卸载"
            BaggageStatus.ON_CAROUSEL -> "转盘上"
            BaggageStatus.DELIVERED -> "已交付"
            BaggageStatus.DELAYED -> "延误"
            BaggageStatus.LOST -> "丢失"
            BaggageStatus.DAMAGED -> "损坏"
        }
    }

    inner class BaggageAdapter(
        private val onItemClickListener: (BaggageItem) -> Unit,
    ) : RecyclerView.Adapter<BaggageAdapter.BaggageViewHolder>() {

        private var baggageList: List<BaggageItem> = emptyList()

        fun submitList(list: List<BaggageItem>) {
            baggageList = list
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaggageViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_baggage, parent, false)
            return BaggageViewHolder(view)
        }

        override fun onBindViewHolder(holder: BaggageViewHolder, position: Int) {
            holder.bind(baggageList[position])
        }

        override fun getItemCount(): Int = baggageList.size

        inner class BaggageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val cardView: MaterialCardView = itemView.findViewById(R.id.baggageCard)
            private val tagNumberText: TextView = itemView.findViewById(R.id.tagNumberText)
            private val flightNumberText: TextView = itemView.findViewById(R.id.flightNumberText)
            private val statusText: TextView = itemView.findViewById(R.id.statusText)
            private val locationText: TextView = itemView.findViewById(R.id.locationText)
            private val weightText: TextView = itemView.findViewById(R.id.weightText)

            fun bind(baggageItem: BaggageItem) {
                tagNumberText.text = "标签: ${baggageItem.baggageTagNumber}"
                flightNumberText.text = "航班: ${baggageItem.flightNumber}"
                statusText.text = getBaggageStatusDisplayName(baggageItem.status)
                locationText.text = baggageItem.lastLocation ?: "位置未知"
                weightText.text = "${baggageItem.weight} ${baggageItem.weightUnit}"

                // 根据状态设置颜色
                val statusColor = when (baggageItem.status) {
                    BaggageStatus.DELIVERED -> R.color.baggage_delivered
                    BaggageStatus.ON_CAROUSEL -> R.color.baggage_on_carousel
                    BaggageStatus.UNLOADED -> R.color.baggage_unloaded
                    BaggageStatus.LOADED -> R.color.baggage_loaded
                    BaggageStatus.IN_TRANSIT -> R.color.baggage_in_transit
                    BaggageStatus.CHECKED_IN -> R.color.baggage_checked_in
                    BaggageStatus.DELAYED -> R.color.baggage_delayed
                    BaggageStatus.LOST -> R.color.baggage_lost
                    BaggageStatus.DAMAGED -> R.color.baggage_damaged
                }

                statusText.setTextColor(ContextCompat.getColor(requireContext(), statusColor))

                cardView.setOnClickListener {
                    onItemClickListener(baggageItem)
                }
            }
        }
    }
}
