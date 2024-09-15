package com.scrapwala.screens.pickups.category.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.scrapwala.databinding.ActivitySelectaddressBinding
import com.scrapwala.redirectionhandler.navigateToAddAdddress
import com.scrapwala.redirectionhandler.navigateToMainActivity
import com.scrapwala.screens.pickups.adapter.SelectAddressAdapter
import com.scrapwala.screens.pickups.adapter.SelectAddressCallback
import com.scrapwala.screens.pickups.model.AddressData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectAddressActivity: AppCompatActivity() {

    private var data: ArrayList<AddressData>?=null
    private var adapter: SelectAddressAdapter?=null
    private lateinit var binding: ActivitySelectaddressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySelectaddressBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setRecyclerAdapter()

        setUpToolbar()


        binding.txtAddAdddress.setOnClickListener {
            navigateToAddAdddress(this,null)
        }

    }

    private fun setUpToolbar() {
        binding.toolbar.toolbarTitle.setText("Select Address")


        binding.toolbar.imgBack.setOnClickListener {
            this.finish()
        }


    }

    private fun setRecyclerAdapter() {
        var layoutManager = LinearLayoutManager(this)



        data= arrayListOf<AddressData>()


        for(i in 0 until 3){
            var categoryData=AddressData()

            categoryData.addressPincode="121004"
            categoryData.fullAddress="F-20 Sector 3 "
            categoryData.addressTag="Home"
            data!!.add(categoryData)
        }




        for(i in 0 until 4){
            var categoryData=AddressData()

            categoryData.addressPincode="121004"
            categoryData.fullAddress="A-20 Sector 1 "
            categoryData.addressTag="Office"
            data!!.add(categoryData)
        }

        adapter=SelectAddressAdapter(this,data!!,object:SelectAddressCallback{
            override fun selectedAddress(item: AddressData) {
                var intent= Intent()

                intent.putExtra("selectedAddress", Gson().toJson(item))

                setResult(101,intent)
                finish()


            }

            override fun deleteAddress(item: AddressData) {



            }


        })

        binding.recyclerVw.layoutManager=layoutManager

        binding.recyclerVw.adapter=adapter




    }


}