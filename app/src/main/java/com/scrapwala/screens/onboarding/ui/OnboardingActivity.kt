package com.scrapwala.screens.onboarding.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.scrapwala.databinding.ActivityOnboardingBinding
import com.scrapwala.redirectionhandler.navigateToLoginActivity
import com.scrapwala.redirectionhandler.navigateToOnboardingActivity
import com.scrapwala.screens.onboarding.adapter.LoginCarousalAdapter
import com.scrapwala.screens.onboarding.model.LoginCarousal
import com.scrapwala.utils.Preferences

class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    private var utmSource: String? = ""
    private var cpCode: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setLoginCarousalAdapter()

        binding.imageCarousalPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

                Log.d("Onboarding", "onPageScrolled: $position")
            }

            override fun onPageSelected(position: Int) {}

            override fun onPageScrollStateChanged(state: Int) {}

        })
        binding.txtContinueButton.setOnClickListener {


            if(Preferences.getUserData(this).isNullOrEmpty()){
                navigateToLoginActivity(this,null)

            }

//            Log.d("Onboarding", "onCreate: ${imageCarousalPager.adapter!!.count}")
//            if (imageCarousalPager.currentItem < imageCarousalPager.adapter!!.count-1) imageCarousalPager.currentItem = imageCarousalPager.currentItem+1
//            else launchLoginActivity()
        }

    }


    private fun setLoginCarousalAdapter() {
        val loginCarousal = ArrayList<LoginCarousal>()
        val obj1 = LoginCarousal()
        obj1.carousalHeading = "India’s first digital platform to sell\n" +
                "recyclables"
        obj1.carousalDescription = "India’s first digital platform to sell\n" +
                "recyclables"
        loginCarousal.add(obj1)

        val obj2 = LoginCarousal()
        obj2.carousalHeading = "USell recyclables now"
        obj2.carousalDescription = "Sell recyclables now"
        loginCarousal.add(obj2)



        val loginCarousalAdapter = LoginCarousalAdapter(loginCarousal)
        binding.imageCarousalPager.adapter = loginCarousalAdapter
        binding.dotsIndicator.setViewPager( binding.imageCarousalPager)
    }



//    private fun launchLoginActivity() {
//        val intent = Intent(this, LoginActivity::class.java)
//        if (utmSource.isNullOrEmpty().not()) {
//            intent.putExtra("utmSource", utmSource)
//        }
//        if (cpCode.isNullOrBlank().not()) {
//            intent.putExtra("cpCode", cpCode)
//        }
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//        startActivity(intent)
//        this.finish()
//
//    }

}