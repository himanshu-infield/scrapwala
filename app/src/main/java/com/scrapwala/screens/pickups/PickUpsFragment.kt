package com.scrapwala.screens.pickups

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sq.yrd.squareyards.R
import com.sq.yrd.squareyards.databinding.FragmentPickUpsBinding
import com.sq.yrd.squareyards.databinding.FragmentReferEarnBinding


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
    }

    private fun initView() {
    }
}