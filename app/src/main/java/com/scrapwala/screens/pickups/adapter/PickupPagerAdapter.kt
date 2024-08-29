package com.scrapwala.screens.pickups.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.scrapwala.screens.pickups.fragment.SchedulePickupFragment

class PickupPagerAdapter(
    private val myContext: Context,
    fm: FragmentManager,
    private val totalTabs: Int
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    // This is for fragment tabs
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> SchedulePickupFragment()
            1 -> SchedulePickupFragment()
            2 -> SchedulePickupFragment()
            else -> throw IllegalArgumentException("Invalid tab position")
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}
