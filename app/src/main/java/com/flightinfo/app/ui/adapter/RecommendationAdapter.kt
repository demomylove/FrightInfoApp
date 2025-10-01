package com.flightinfo.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flightinfo.app.R
import com.flightinfo.app.data.model.FlightRecommendation
import com.flightinfo.app.data.model.RecommendationType
import com.flightinfo.app.databinding.ItemRecommendationBinding
import java.text.NumberFormat
import java.util.Locale

class RecommendationAdapter(
    private val onRecommendationClick: (FlightRecommendation) -> Unit,
    private val onBookmarkClick: (FlightRecommendation) -> Unit = {},
    private val onTrackClick: (FlightRecommendation) -> Unit = {},
    private val onBookClick: (FlightRecommendation) -> Unit = {},
) : ListAdapter<FlightRecommendation, RecommendationAdapter.RecommendationViewHolder>(RecommendationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val binding = ItemRecommendationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class RecommendationViewHolder(
        private val binding: ItemRecommendationBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recommendation: FlightRecommendation) {
            val flight = recommendation.flight

            // 绑定航班基本信息
            binding.textViewFlightNumber.text = flight.flightNumber
            binding.textViewAirline.text = flight.airline
            binding.textViewDepartureAirport.text = flight.departureAirport
            binding.textViewArrivalAirport.text = flight.arrivalAirport
            binding.textViewDepartureTime.text = flight.departureTime
            binding.textViewArrivalTime.text = flight.arrivalTime
            binding.textViewStatus.text = flight.status

            // 绑定价格信息
            flight.price?.let { price ->
                val currencyFormat = NumberFormat.getCurrencyInstance(Locale.CHINA)
                binding.textViewPrice.text = currencyFormat.format(price)
            } ?: run {
                binding.textViewPrice.text = "价格待定"
            }

            // 绑定推荐信息
            binding.textViewRecommendationType.text = getRecommendationTypeName(recommendation.recommendationType)
            binding.textViewConfidence.text = "置信度: ${(recommendation.confidenceScore * 100).toInt()}%"
            binding.textViewReasons.text = recommendation.reasons.joinToString(", ")

            // 设置状态颜色
            setStatusColor(flight.status)

            // 设置置信度颜色
            setConfidenceColor(recommendation.confidenceScore)

            // 设置点击事件
            binding.root.setOnClickListener {
                onRecommendationClick(recommendation)
            }

            binding.buttonBookmark.setOnClickListener {
                onBookmarkClick(recommendation)
            }

            binding.buttonTrack.setOnClickListener {
                onTrackClick(recommendation)
            }

            binding.buttonBook.setOnClickListener {
                onBookClick(recommendation)
            }
        }

        private fun setStatusColor(status: String) {
            val colorRes = when (status.lowercase()) {
                "准时", "on time" -> R.color.status_on_time
                "延误", "delayed" -> R.color.status_delayed
                "取消", "cancelled" -> R.color.status_cancelled
                "登机", "boarding" -> R.color.status_boarding
                "起飞", "departed", "飞行中", "in flight" -> R.color.status_departed
                else -> R.color.text_secondary
            }
            binding.textViewStatus.setTextColor(ContextCompat.getColor(binding.root.context, colorRes))
        }

        private fun setConfidenceColor(confidence: Double) {
            val colorRes = when {
                confidence >= 0.8 -> R.color.status_on_time
                confidence >= 0.6 -> R.color.status_delayed
                else -> R.color.text_secondary
            }
            binding.textViewConfidence.setTextColor(ContextCompat.getColor(binding.root.context, colorRes))
        }

        private fun getRecommendationTypeName(type: RecommendationType): String {
            return when (type) {
                RecommendationType.BASED_ON_HISTORY -> "基于历史"
                RecommendationType.POPULAR_ROUTE -> "热门航线"
                RecommendationType.PRICE_DROP -> "价格优惠"
                RecommendationType.SIMILAR_USERS -> "相似用户"
                RecommendationType.TRENDING -> "趋势推荐"
                RecommendationType.PERSONALIZED -> "个性化推荐"
            }
        }
    }
}

class RecommendationDiffCallback : DiffUtil.ItemCallback<FlightRecommendation>() {
    override fun areItemsTheSame(oldItem: FlightRecommendation, newItem: FlightRecommendation): Boolean {
        return oldItem.flight.flightNumber == newItem.flight.flightNumber
    }

    override fun areContentsTheSame(oldItem: FlightRecommendation, newItem: FlightRecommendation): Boolean {
        return oldItem == newItem
    }
}
