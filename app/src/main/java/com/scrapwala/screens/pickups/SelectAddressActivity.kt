package com.scrapwala.screens.pickups

import android.R
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.moengage.core.internal.utils.showToast
import com.scrapwala.databinding.ActivitySelectaddressBinding
import com.scrapwala.redirectionhandler.navigateToAddAdddress
import com.scrapwala.screens.pickups.adapter.SelectAddressAdapter
import com.scrapwala.screens.pickups.adapter.SelectAddressCallback
import com.scrapwala.screens.pickups.category.model.CategoryResponse
import com.scrapwala.screens.pickups.model.AddressData
import com.scrapwala.screens.pickups.model.AddressListResponse
import com.scrapwala.screens.pickups.model.SuccessResponse
import com.scrapwala.screens.pickups.viewmodel.PickupViewModel
import com.scrapwala.utils.ErrorResponse
import com.scrapwala.utils.extensionclass.hideSpinner
import com.scrapwala.utils.extensionclass.showCustomToast
import com.scrapwala.utils.extensionclass.showSpinner
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectAddressActivity: AppCompatActivity() {

    private var data: ArrayList<AddressData>?=null
    private var adapter: SelectAddressAdapter?=null
    private var itemSelectDelete: AddressListResponse.Data? = null
    private lateinit var binding: ActivitySelectaddressBinding

    private val viewModel: PickupViewModel by viewModels()
    private lateinit var addressListResponse: AddressListResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectaddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpToolbar()
        binding.txtAddAdddress.setOnClickListener {
            navigateToAddAdddress(this,null)
        }


    }

    override fun onResume() {
        super.onResume()
        observeApiDeleteAddress()
        observeApiAddressList()
        apiAddressList()
    }

    private fun observeApiAddressList() {
        viewModel.responseAddressList.observe(this as AppCompatActivity, Observer {
            when (it) {
                is AddressListResponse -> {
                    hideSpinner()
                    addressListResponse = it
                    if (addressListResponse.success==1 && addressListResponse.data.isNullOrEmpty().not()){
                        renderData(addressListResponse)
                    }
                }

                is ErrorResponse -> {
                    hideSpinner()
                    if (it.message.isNullOrEmpty().not()) {
                        showCustomToast(findViewById(R.id.content),this,it.message)
                    }
                }

                is String -> {
                    hideSpinner()
                    showCustomToast(findViewById(R.id.content),this,it)
                }
            }

        })
    }

    private fun renderData(addressListResponse: AddressListResponse) {
        var layoutManager = LinearLayoutManager(this)
        data= arrayListOf<AddressData>()


        adapter=SelectAddressAdapter(this,addressListResponse.data!!,object:SelectAddressCallback{
            override fun selectedAddress(item: AddressListResponse.Data) {
                var intent= Intent()
                intent.putExtra("selectedAddress", Gson().toJson(item))
                setResult(101,intent)
                finish()
            }

            override fun deleteAddress(item: AddressListResponse.Data) {
                itemSelectDelete = item
                showSpinner(this@SelectAddressActivity)
                viewModel.deleteAddress(item.id!!)
            }


        })

        binding.recyclerVw.layoutManager=layoutManager
        binding.recyclerVw.adapter=adapter
    }

    private fun observeApiDeleteAddress() {
        viewModel.responseDeleteAddress.observe(this as AppCompatActivity, Observer {
            when (it) {
                is SuccessResponse -> {
                    hideSpinner()
                    if (it.success==1){
                        showToast(this, it.message?:"User deleted successfully!")
                        if (itemSelectDelete!=null){
                            adapter?.removeItem(itemSelectDelete!!)
                        }

                    }
                }

                is ErrorResponse -> {
                    hideSpinner()
                    if (it.message.isNullOrEmpty().not()) {
                        showCustomToast(findViewById(R.id.content),this,it.message)
                    }
                }

                is String -> {
                    hideSpinner()
                    showCustomToast(findViewById(R.id.content),this,it)
                }
            }

        })
    }

    private fun apiAddressList() {
        showSpinner(this@SelectAddressActivity)
        viewModel.getAddressList(1)
    }

    private fun setUpToolbar() {
        binding.toolbar.toolbarTitle.setText("Select Address")

        binding.toolbar.imgBack.setOnClickListener {
            this.finish()
        }
    }
}