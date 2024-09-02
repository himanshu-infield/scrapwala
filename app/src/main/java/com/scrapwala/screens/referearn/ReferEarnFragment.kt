package com.scrapwala.screens.referearn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.scrapwala.R
import com.scrapwala.databinding.FragmentReferEarnBinding

class ReferEarnFragment : Fragment() {

private lateinit var binding: FragmentReferEarnBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReferEarnBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.toolbar.tvHeading.text = getString(R.string.refer_and_earn)
        binding.toolbar.imgBack.setOnClickListener{
            requireActivity().onBackPressed()
        }
    }

}