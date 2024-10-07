package com.scrapwala

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.scrapwala.databinding.ActivityMainBinding
import com.scrapwala.screens.home.adapter.HomePagerAdapter
import com.scrapwala.screens.home.ui.DashBoardFragment
import com.scrapwala.screens.pickups.PickUpsFragment
import com.scrapwala.screens.profile.ProfileFragment
import com.scrapwala.screens.referearn.ReferEarnFragment
import dagger.hilt.android.AndroidEntryPoint


var firstFragment: Fragment? = null

var secondFragment: Fragment? = null

var thirdFragment: Fragment? = null

var forthFragment: Fragment? = null

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

     lateinit var bindingBase: ActivityMainBinding
    private var adapter: HomePagerAdapter? = null
    private var bottomTabIndex: Int? = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingBase = ActivityMainBinding.inflate(layoutInflater)
        // changeStatusBarColor()
        setContentView(bindingBase.root)
        setUpMainPageTasks()
        initView()
    }

     fun bottomNavVisibility(visibility:Boolean =false) {
        if (visibility){
        bindingBase.llBottom.visibility = View.GONE
        }
    }

    private fun initView() {

    }

    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color._f2f2f2, this.theme);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = resources.getColor(R.color._f2f2f2);
        }
    }

    private fun setUpMainPageTasks() {
        setUpBottomNav()
        bottomNavListener()
    }

    private fun setUpBottomNav() {
        firstFragment = DashBoardFragment()
        secondFragment = ReferEarnFragment()
        thirdFragment = PickUpsFragment()
        forthFragment = ProfileFragment()

        addFragments()
    }

    private fun addFragments() {
        adapter = HomePagerAdapter(supportFragmentManager, lifecycle)

        adapter?.addFragment(firstFragment!!)

        adapter?.addFragment(secondFragment!!)

        adapter?.addFragment(thirdFragment!!)

        adapter?.addFragment(forthFragment!!)

        bindingBase.bottomFragframeLayout.orientation = ViewPager2.ORIENTATION_HORIZONTAL;

        bindingBase.bottomFragframeLayout.offscreenPageLimit = 3
        bindingBase.bottomFragframeLayout.adapter = adapter
        bindingBase.bottomFragframeLayout.isUserInputEnabled = false;
    }

    private fun bottomNavListener() {
        bindingBase.navView.setOnItemSelectedListener { item ->

            when (item.itemId) {
                R.id.navigation_home -> {
                    bottomTabIndex = 0
                    setCurrentFragment(0)
                }

                R.id.navigation_refer_earn -> {
                    bottomTabIndex = 1
                    setCurrentFragment(1)
                }

                R.id.navigation_pickups -> {
                    bottomTabIndex = 2
                 //   bottomNavVisibility(true)
                    setCurrentFragment(2)
                }

                R.id.navigation_profile -> {
                    bottomTabIndex = 3
                    setCurrentFragment(3)
                }
            }
            true
        }

    }

    private fun setCurrentFragment(index: Int) {
        if (index != -1) {
            bindingBase.bottomFragframeLayout.setCurrentItem(index, false)
        }
    }

    override fun onBackPressed() {
        if (bottomTabIndex != 0) {
            bottomTabIndex = 0
            bindingBase.navView.selectedItemId = 0
            bindingBase.navView.menu.getItem(0).isChecked = true
            bindingBase.bottomFragframeLayout.setCurrentItem(0, false)
        } else {
            finish()
        }
    }
}