package com.flightinfo.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.flightinfo.app.R
import com.flightinfo.app.data.model.FlightRecommendation
import com.flightinfo.app.data.model.RecommendationType
import com.flightinfo.app.databinding.FragmentRecommendationBinding
import com.flightinfo.app.ui.adapter.RecommendationAdapter
import com.flightinfo.app.ui.viewmodel.RecommendationViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecommendationFragment : Fragment() {

    private var _binding: FragmentRecommendationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecommendationViewModel by viewModels()
    private lateinit var recommendationAdapter: RecommendationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRecommendationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSwipeRefresh()
        setupFilters()
        observeViewModel()

        // 初始加载数据
        viewModel.loadRecommendations()
    }

    private fun setupRecyclerView() {
        recommendationAdapter = RecommendationAdapter(
            onRecommendationClick = { recommendation -> onRecommendationClick(recommendation) },
        )

        binding.recyclerViewRecommendations.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recommendationAdapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshRecommendations()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.refreshing.collect { refreshing ->
                binding.swipeRefreshLayout.isRefreshing = refreshing
            }
        }
    }

    private fun setupFilters() {
        // 添加推荐类型过滤器
        val types = listOf(
            "全部" to null,
            "基于历史" to RecommendationType.BASED_ON_HISTORY,
            "热门航线" to RecommendationType.POPULAR_ROUTE,
            "价格优惠" to RecommendationType.PRICE_DROP,
            "相似用户" to RecommendationType.SIMILAR_USERS,
            "趋势推荐" to RecommendationType.TRENDING,
            "个性化" to RecommendationType.PERSONALIZED,
        )

        types.forEach { (label, type) ->
            val chip = Chip(requireContext()).apply {
                text = label
                isCheckable = true
                isChecked = type == null // 默认选中"全部"
                setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                setChipBackgroundColorResource(R.color.purple_500)

                setOnClickListener {
                    // 更新选中状态
                    binding.chipGroupFilter.clearCheck()
                    (it as Chip).isChecked = true

                    // 过滤推荐
                    filterRecommendations(type)
                }
            }
            binding.chipGroupFilter.addView(chip)
        }
    }

    private fun filterRecommendations(type: RecommendationType?) {
        viewLifecycleOwner.lifecycleScope.launch {
            if (type == null) {
                // 显示全部推荐
                viewModel.recommendations.collect { resource ->
                    if (resource is com.flightinfo.app.utils.Resource.Success) {
                        recommendationAdapter.submitList(resource.data)
                    }
                }
            } else {
                // 按类型过滤
                viewModel.getRecommendationByType(type).collect { recommendations ->
                    recommendationAdapter.submitList(recommendations)
                }
            }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            // 观察推荐列表
            viewModel.recommendations.collect { resource ->
                when (resource) {
                    is com.flightinfo.app.utils.Resource.Idle -> {
                        // Initial state, do nothing
                    }
                    is com.flightinfo.app.utils.Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.textViewEmpty.visibility = View.GONE
                        binding.recyclerViewRecommendations.visibility = View.GONE
                    }
                    is com.flightinfo.app.utils.Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val recommendations = resource.data ?: emptyList()

                        if (recommendations.isEmpty()) {
                            binding.textViewEmpty.visibility = View.VISIBLE
                            binding.recyclerViewRecommendations.visibility = View.GONE
                        } else {
                            binding.textViewEmpty.visibility = View.GONE
                            binding.recyclerViewRecommendations.visibility = View.VISIBLE
                            recommendationAdapter.submitList(recommendations)
                        }

                        updateStats(recommendations)
                    }
                    is com.flightinfo.app.utils.Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.textViewEmpty.visibility = View.VISIBLE
                        binding.textViewEmpty.text = "加载推荐失败: ${resource.message}"
                        binding.recyclerViewRecommendations.visibility = View.GONE
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            // 观察用户统计
            viewModel.userStats.collect { resource ->
                when (resource) {
                    is com.flightinfo.app.utils.Resource.Success -> {
                        updateUserStats(resource.data)
                    }
                    else -> {}
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            // 观察分析数据
            viewModel.analytics.collect { resource ->
                when (resource) {
                    is com.flightinfo.app.utils.Resource.Success -> {
                        updateAnalytics(resource.data)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun updateStats(recommendations: List<FlightRecommendation>) {
        val avgConfidence = if (recommendations.isNotEmpty()) {
            recommendations.map { it.confidenceScore }.average()
        } else {
            0.0
        }

        binding.textViewStats.text = "共 ${recommendations.size} 个推荐 · 平均置信度 %.1f%%".format(avgConfidence * 100)

        // 更新推荐类型统计
        val typeStats = recommendations.groupBy { it.recommendationType }
            .mapValues { it.value.size }

        binding.textViewTypeStats.text = typeStats.map { (type, count) ->
            "${getTypeName(type)}: $count"
        }.joinToString(" · ")
    }

    private fun updateUserStats(stats: com.flightinfo.app.data.model.UserBehaviorStats?) {
        stats?.let {
            binding.textViewUserStats.text = """
                搜索次数: ${it.totalSearchesLastMonth}
                收藏次数: ${it.totalBookmarksLastMonth}
                跟踪次数: ${it.totalTracksLastMonth}
                预订次数: ${it.totalBooksLastMonth}
            """.trimIndent()
        }
    }

    private fun updateAnalytics(analytics: com.flightinfo.app.data.model.RecommendationAnalytics?) {
        analytics?.let {
            binding.textViewAnalytics.text = """
                点击率: %.1f%%
                转化率: %.1f%%
                平均置信度: %.1f%%
            """.trimIndent().format(
                it.clickThroughRate * 100,
                it.conversionRate * 100,
                it.averageConfidence * 100,
            )
        }
    }

    private fun onRecommendationClick(recommendation: FlightRecommendation) {
        viewModel.trackRecommendationClick(recommendation)

        // 显示推荐详情
        showRecommendationDetails(recommendation)

        // 记录用户行为
        viewModel.trackFlightAction("view", recommendation.flight)
    }

    private fun showRecommendationDetails(recommendation: FlightRecommendation) {
        val message = """
            航班: ${recommendation.flight.flightNumber}
            航空公司: ${recommendation.flight.airline}
            路线: ${recommendation.flight.departureAirport} → ${recommendation.flight.arrivalAirport}
            时间: ${recommendation.flight.departureTime} - ${recommendation.flight.arrivalTime}
            状态: ${recommendation.flight.status}
            价格: ${recommendation.flight.price} ${recommendation.flight.currency}
            
            推荐类型: ${getTypeName(recommendation.recommendationType)}
            置信度: ${(recommendation.confidenceScore * 100).toInt()}%
            推荐理由: ${recommendation.reasons.joinToString(", ")}
        """.trimIndent()

        Snackbar.make(binding.root, "已查看推荐详情", Snackbar.LENGTH_SHORT).show()
    }

    private fun getTypeName(type: RecommendationType): String {
        return when (type) {
            RecommendationType.BASED_ON_HISTORY -> "基于历史"
            RecommendationType.POPULAR_ROUTE -> "热门航线"
            RecommendationType.PRICE_DROP -> "价格优惠"
            RecommendationType.SIMILAR_USERS -> "相似用户"
            RecommendationType.TRENDING -> "趋势推荐"
            RecommendationType.PERSONALIZED -> "个性化推荐"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
