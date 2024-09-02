package com.scrapwala.screens.pickups.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.scrapwala.screens.pickups.fragment.CompletedFragment
import com.scrapwala.screens.pickups.fragment.SchedulePickupFragment
import com.scrapwala.screens.pickups.fragment.UpcomingFragment

class PickupPagerAdapter(
    private val myContext: Context,
    fm: FragmentManager,
    private val totalTabs: Int
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    // This is for fragment tabs
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> SchedulePickupFragment()
            1 -> UpcomingFragment()
            2 -> CompletedFragment()
            else -> throw IllegalArgumentException("Invalid tab position")
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}
