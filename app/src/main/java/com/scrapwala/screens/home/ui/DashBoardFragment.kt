package com.scrapwala.screens.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.scrapwala.databinding.FragmentDashBoardBinding
import com.scrapwala.redirectionhandler.navigateToCategoryActivity
import com.scrapwala.redirectionhandler.navigateToEditProfileActivity
import com.scrapwala.redirectionhandler.navigateToPickupsActivity
import com.scrapwala.screens.home.adapter.ViewPagerAdapter
import com.scrapwala.screens.home.model.BannerResponse
import com.scrapwala.screens.home.model.DashboardViewModel
import com.scrapwala.screens.login.model.VerifyOtpResponse
import com.scrapwala.utils.ErrorResponse
import com.scrapwala.utils.Preferences
import com.scrapwala.utils.extensionclass.hideSpinner
import com.scrapwala.utils.extensionclass.showCustomToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashBoardFragment : Fragment() {

    lateinit var binding: FragmentDashBoardBinding
    private var userDataObj: VerifyOtpResponse.Data? = null



    private val viewModel: DashboardViewModel by viewModels()

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

        getBanners()

        observeBanners()
    }

    private fun observeBanners() {

            viewModel.bannerResponse .observe(requireActivity() , Observer {
                when (it) {
                    is  BannerResponse -> {
                        hideSpinner()


                        if(it.success==1){

                            setViewPager(it)
                        }







                    }

                    is ErrorResponse -> {
                        if (it.message.isNullOrEmpty().not()) {
                            showCustomToast(binding.root,requireActivity(),it.message)
                        }

                        hideSpinner()
                    }

                    is String -> {
                        showCustomToast(binding.root,requireActivity(),it)
                        hideSpinner()
                    }
                }

            })
        }



    private fun getBanners() {
        viewModel.getBanners()
    }

    private fun initView() {
        userDataObj = Preferences.getUserDataObj(requireContext())


        setUserData()
        binding.linearCategory.setOnClickListener {

            navigateToCategoryActivity(requireActivity(), null)
        }
        binding.btnSubmit.setOnClickListener {
            navigateToPickupsActivity(requireActivity(), null)
        }

        binding.rlEditProfile.setOnClickListener {
            navigateToEditProfileActivity(requireActivity(), null)
        }

    }

    private fun setUserData() {
        if (userDataObj != null) {
            if (userDataObj?.name.isNullOrEmpty().not()) {
                binding.txtUserName.setText(userDataObj?.name)
            }
        }
    }

    private fun setViewPager(response: BannerResponse) {
//        val items = response.data

        val items = listOf("Page 1", "Page 2", "Page 3", "Page 4")

        val adapter = ViewPagerAdapter(requireActivity(),items)

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