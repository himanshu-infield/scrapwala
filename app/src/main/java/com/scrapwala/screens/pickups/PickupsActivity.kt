package com.scrapwala.screens.pickups

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.scrapwala.databinding.ActivityPickupsBinding
import com.scrapwala.screens.pickups.adapter.PickupPagerAdapter
import com.scrapwala.screens.pickups.fragment.CompletedFragment
import com.scrapwala.screens.pickups.fragment.SchedulePickupFragment
import com.scrapwala.screens.pickups.fragment.UpcomingFragment
import com.scrapwala.screens.pickups.model.InProgressListResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PickupsActivity : AppCompatActivity() {
    public var adapter: PickupPagerAdapter?=null
    lateinit var binding: ActivityPickupsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPickupsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getExtras()
        initView()
        setUPTabLayout()
    }


    public var editPickupObj: InProgressListResponse.Data? = null
    private fun getExtras() {
        if (intent.extras != null) {
            if (intent.hasExtra("editPickup")) {
                val editPickupObjString = intent.extras?.getString("editPickup")
                if (editPickupObjString.isNullOrEmpty().not()) {
                    editPickupObj = Gson().fromJson(editPickupObjString, InProgressListResponse.Data::class.java)
                }
            }

        }

    }

    private fun setUPTabLayout() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Schedule Pickup"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("In Progress"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Completed"))


        for (i in 0 until binding.tabLayout.tabCount) {
            val tab = (binding.tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(0, 0, 12, 0)
            tab.requestLayout()
        }



        var fragmentList= arrayListOf<Fragment>()

        fragmentList.add(SchedulePickupFragment())

        fragmentList.add(UpcomingFragment())

        fragmentList.add(CompletedFragment())

         adapter = PickupPagerAdapter(this, supportFragmentManager, binding.tabLayout.tabCount,fragmentList)
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