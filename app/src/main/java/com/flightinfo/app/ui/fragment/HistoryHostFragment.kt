package com.flightinfo.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.flightinfo.app.R
import com.flightinfo.app.databinding.FragmentHistoryHostBinding
import com.google.android.material.tabs.TabLayoutMediator

class HistoryHostFragment : Fragment() {

    private var _binding: FragmentHistoryHostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHistoryHostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 2

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> FavoriteRoutesFragment()
                    else -> HistoryFragment()
                }
            }
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager) {
                tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.favorite_routes)
                else -> getString(R.string.search_history)
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
