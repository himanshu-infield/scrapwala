package com.scrapwala.screens.referearn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.scrapwala.MainActivity
import com.sq.yrd.squareyards.R
import com.sq.yrd.squareyards.databinding.FragmentDashBoardBinding
import com.sq.yrd.squareyards.databinding.FragmentReferEarnBinding

class ReferEarnFragment : Fragment() {

private lateinit var binding:FragmentReferEarnBinding
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

    }

}