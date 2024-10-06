package com.scrapwala.screens.pickups

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.tabs.TabLayout
import com.scrapwala.databinding.ActivityPickupsBinding
import com.scrapwala.screens.pickups.adapter.PickupPagerAdapter

class PickupsActivity : AppCompatActivity() {
    lateinit var binding: ActivityPickupsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPickupsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        setUPTabLayout()
    }

    private fun setUPTabLayout() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Schedule Pickup"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Upcoming"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Completed"))


        for (i in 0 until binding.tabLayout.tabCount) {
            val tab = (binding.tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(0, 0, 12, 0)
            tab.requestLayout()
        }


        val adapter = PickupPagerAdapter(this, supportFragmentManager, binding.tabLayout.tabCount)
        binding.viewpager.adapter = adapter

        binding.viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewpager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // Do something when a tab is unselected
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Do something when a tab is reselected
            }
        })



    }

    private fun initView() {
        binding.toolbar.tvHeading.text = "Pickup"
        binding.toolbar.imgBack.setOnClickListener{
            finish()
        }

    }
}