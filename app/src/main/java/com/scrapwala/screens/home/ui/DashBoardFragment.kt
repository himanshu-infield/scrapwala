package com.scrapwala.screens.home.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.scrapwala.MainActivity
import com.scrapwala.screens.home.adapter.ViewPagerAdapter
import com.sq.yrd.squareyards.R
import com.sq.yrd.squareyards.databinding.FragmentDashBoardBinding

class DashBoardFragment : Fragment() {

    lateinit var binding: FragmentDashBoardBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setViewPager()
    }

    private fun initView() {

    }

    private fun setViewPager() {
        val items = listOf("Page 1", "Page 2", "Page 3","Page 4")
        val adapter = ViewPagerAdapter(items)

        binding.viewPager.adapter = adapter
        binding.dotsIndicator.attachTo(binding.viewPager)

       /* // Setup TabLayout with ViewPager2
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            // Set a custom indicator for each tab
            val customTab = LayoutInflater.from(requireActivity()).inflate(R.layout.custom_tab, null)
            val indicator = customTab.findViewById<View>(R.id.indicator)
            if (position == binding.viewPager.currentItem) {
                indicator.setBackgroundResource(R.drawable.baseline_horizontal_rule_24) // Selected state drawable
            } else {
                indicator.setBackgroundResource(R.drawable.unselected_dot) // Unselected state drawable
            }
            tab.customView = customTab
        }.attach()




        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                for (i in 0 until binding.tabLayout.tabCount) {
                    val tab = binding.tabLayout.getTabAt(i)
                    val indicator = tab?.customView?.findViewById<View>(R.id.indicator)
                    if (i == position) {
                        indicator?.setBackgroundResource(R.drawable.baseline_horizontal_rule_24)
                    } else {
                        indicator?.setBackgroundResource(R.drawable.unselected_dot)
                    }
                }
            }
        })*/

    }


}