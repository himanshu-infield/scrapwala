package com.scrapwala.screens.pickups

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.scrapwala.databinding.FragmentPickUpsBinding


class PickUpsFragment : Fragment() {
lateinit var binding: FragmentPickUpsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPickUpsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setUPTabLayout()
    }

    private fun setUPTabLayout() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Schedule Pickup"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Upcoming"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Completed"))


        for (i in 0 until binding.tabLayout.tabCount) {
            val tab = (binding.tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as MarginLayoutParams
            p.setMargins(0, 0, 12, 0)
            tab.requestLayout()
        }


        val adapter = PickupPagerAdapter(requireContext(), parentFragmentManager, binding.tabLayout.tabCount)
        binding.viewpager.adapter = adapter

        binding.viewpager.addOnPageChangeListener(TabLayoutOnPageChangeListener(binding.tabLayout))

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
            requireActivity().finish()
        }

    }
}