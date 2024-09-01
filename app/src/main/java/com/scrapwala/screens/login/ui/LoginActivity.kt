package com.scrapwala.screens.login.ui

import android.os.Bundle
import com.scrapwala.baseactivity.BaseAppCompatActivity
import com.scrapwala.databinding.ActivityLoginBinding
import com.scrapwala.redirectionhandler.navigateToMainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseAppCompatActivity() {


    private lateinit  var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.txtGetOtp.setOnClickListener {
            binding.viewFlipper.displayedChild=1
        }



        binding.txtContinueButton.setOnClickListener {
            navigateToMainActivity(this,null,true)
        }


    }



}