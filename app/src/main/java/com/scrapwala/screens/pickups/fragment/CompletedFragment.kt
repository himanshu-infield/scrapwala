package com.scrapwala.screens.pickups.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.scrapwala.R
import com.scrapwala.databinding.FragmentCompletedBinding
import com.scrapwala.screens.login.model.VerifyOtpResponse
import com.scrapwala.screens.pickups.adapter.PickUpsListAdapter
import com.scrapwala.screens.pickups.model.InProgressListResponse
import com.scrapwala.screens.pickups.viewmodel.PickupViewModel
import com.scrapwala.utils.ErrorResponse
import com.scrapwala.utils.Preferences
import com.scrapwala.utils.extensionclass.hideSpinner
import com.scrapwala.utils.extensionclass.showCustomToast
import com.scrapwala.utils.extensionclass.showSpinner
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompletedFragment : Fragment(),PickUpsListAdapter.PickupListener {

    lateinit var binding: FragmentCompletedBinding
    private val viewModel: PickupViewModel by viewModels()
    private var pref: VerifyOtpResponse.Data? = null
    lateinit var pickUpsListAdapter: PickUpsListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCompletedBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = Preferences.getUserDataObj(requireContext())
        initView()
        observeApiUpcomingList()
    }
    override fun onResume() {
        super.onResume()
        callApiUpcomingList()
    }


    private fun observeApiUpcomingList() {
        viewModel.responsePickupsList.observe(requireActivity(), Observer {
            when (it) {
                is InProgressListResponse -> {
                    hideSpinner()
                    if (it.success == 1) {
                        renderData(it.data)
                    }
                }

                is ErrorResponse -> {
                    hideSpinner()
                    if (it.message.isNullOrEmpty().not()) {
                        showCustomToast(binding.root, requireActivity(), it.message)
                    }
                }

                is String -> {
                    hideSpinner()
                    showCustomToast(binding.root, requireActivity(), it)
                }
            }

        })
    }

    private fun renderData(data: List<InProgressListResponse.Data?>?) {
        if (data.isNullOrEmpty().not()) {
            binding.noData.visibility = View.GONE
            binding.recPickupCom.layoutManager = LinearLayoutManager(activity)
            pickUpsListAdapter = PickUpsListAdapter(
                requireContext(),
                data!!,
                this
            )
            binding.recPickupCom.adapter = pickUpsListAdapter
        } else {
            binding.noData.visibility = View.VISIBLE
        }

    }

    private fun callApiUpcomingList() {
        showSpinner(requireContext())
        if (pref?.id!=null){
            viewModel.apiInProgressList(pref?.id!!,1)
        }

    }




    private fun initView() {

    }

    override fun onItemSelected(item: InProgressListResponse.Data?, position: Int) {

    }

}