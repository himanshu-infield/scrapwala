package com.scrapwala.screens.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.scrapwala.R
import com.scrapwala.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {

    lateinit var binding:ActivityEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.toolbar.tvHeading.text = getString(R.string.profile_update)
        binding.toolbar.imgBack.setOnClickListener{
            finish()
        }
    }
}