package com.flightinfo.app.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.flightinfo.app.ui.fragment.FlightListFragment
import com.flightinfo.app.ui.viewmodel.FlightSearchViewModel

class FlightPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val viewModel: FlightSearchViewModel,
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FlightListFragment.newInstance(false) // Search results
            1 -> FlightListFragment.newInstance(true) // Real-time
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}
