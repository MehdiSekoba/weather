package com.mehdisekoba.weather.ui.location.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mehdisekoba.weather.ui.location.hourly.HourlyFragment
import com.mehdisekoba.weather.ui.location.weekly.WeeklyFragment

class PagerAdapter(manager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(manager, lifecycle) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HourlyFragment()
            1 -> WeeklyFragment()
            else -> HourlyFragment()
        }
    }
}
