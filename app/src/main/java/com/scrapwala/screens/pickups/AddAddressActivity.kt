package com.scrapwala.screens.pickups


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.moengage.core.internal.utils.showToast
import com.scrapwala.databinding.ActivityAddaddressBinding
import com.scrapwala.databinding.ActivitySelectaddressBinding
import com.scrapwala.screens.pickups.adapter.SelectAddressAdapter
import com.scrapwala.screens.pickups.adapter.SelectAddressCallback
import com.scrapwala.screens.pickups.model.AddressData
import com.scrapwala.utils.extensionclass.setErrorMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddAddressActivity : AppCompatActivity() {

    private var data: ArrayList<AddressData>? = null
    private var adapter: SelectAddressAdapter? = null
    private lateinit var binding: ActivityAddaddressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddaddressBinding.inflate(layoutInflater)

        setContentView(binding.root)


        setUpToolbar()



        submitFuncionality()




        binding.imgRemove.setOnClickListener {
            binding.tabs.visibility = View.VISIBLE
            binding.tabs.getTabAt(0)?.select()
            binding.linearOther.visibility = View.GONE
        }


        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // Called when a tab is selected
                val position = tab.position


                if (tab.text!!.equals("Other")) {
                    binding.tabs.visibility = View.GONE

                    binding.linearOther.visibility = View.VISIBLE

                }


            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // Called when a tab is unselected
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Called when a tab that is already selected is clicked again
            }
        })

    }

    private fun submitFuncionality() {
        binding.txtSubmitButton.setOnClickListener {

            if(validate()){
                showToast(this,"Address Added Successfully!!")
                this.finish()
            }
        }
    }

    private fun validate(): Boolean {

        var formValid:Boolean=true

        if(binding.edtAdddress.text.toString().isNullOrEmpty()){
            binding.inputAddress.setErrorMessage("Please enter Full Address")


            formValid= false
        }


        if(binding.edtLandmark.text.toString().isNullOrEmpty()){

            binding.inputLandmark.setErrorMessage("Please enter Landmark")

            formValid= false

        }


        if(binding.edtPincode.text.toString().isNullOrEmpty()){
            formValid= false
            binding.inputPincode.setErrorMessage("Please enter PinCode")


        }



        return formValid

    }

    private fun setUpToolbar() {
        binding.toolbar.toolbarTitle.setText("Add Address")


        binding.toolbar.imgBack.setOnClickListener {
            this.finish()
        }


    }


}