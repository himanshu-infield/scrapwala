package com.scrapwala.screens.pickups


import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.moengage.core.internal.utils.showToast
import com.scrapwala.R
import com.scrapwala.databinding.ActivityAddaddressBinding
import com.scrapwala.databinding.DialogGenericsearchBinding
import com.scrapwala.screens.pickups.adapter.CityListAdapter
import com.scrapwala.screens.pickups.adapter.ClickedCityItemCallback
import com.scrapwala.screens.pickups.adapter.SelectAddressAdapter
import com.scrapwala.screens.pickups.model.AddAddressData
import com.scrapwala.screens.pickups.model.CityListResponse
import com.scrapwala.screens.pickups.model.SuccessResponse
import com.scrapwala.screens.pickups.viewmodel.PickupViewModel
import com.scrapwala.utils.ErrorResponse
import com.scrapwala.utils.extensionclass.hideKeyboard
import com.scrapwala.utils.extensionclass.hideSpinner
import com.scrapwala.utils.extensionclass.setErrorMessage
import com.scrapwala.utils.extensionclass.setupFullHeight
import com.scrapwala.utils.extensionclass.showCustomToast
import com.scrapwala.utils.extensionclass.showSpinner
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddAddressActivity : AppCompatActivity() {

    private var adapter: SelectAddressAdapter? = null
    var selectedAddressType = "Home"
    private lateinit var binding: ActivityAddaddressBinding

    private val viewModel: PickupViewModel by viewModels()

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
                selectedAddressType = tab.text.toString()

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
        setCityPhone()
        observeApiSaveAddress()
        observeCityListResponse()
        callCityApi()
    }


    private fun observeCityListResponse() {
        viewModel.cityListResponse.observe(this) {

            when (it) {
                is CityListResponse -> {
                    if (it.success == 1 && it.data.isNullOrEmpty().not()) {
                        viewModel.cityList = it.data as ArrayList<CityListResponse.Data>
                    }
                }

                is ErrorResponse -> {
                    Toast.makeText(this, "Error ${it}", Toast.LENGTH_LONG).show()
                }
            }

        }
    }

    private fun callCityApi() {
        viewModel.getCityList()
    }

    private fun setCityPhone() {
        binding.edtCity.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
            when (motionEvent.action) {

                MotionEvent.ACTION_DOWN -> {
                    showCityDialog()
                    hideKeyboard(this@AddAddressActivity)
                }

            }
            return@OnTouchListener true
        })
    }

    private fun showCityDialog() {

        if (viewModel.cityList.isNullOrEmpty().not()) {
            viewModel.cityList

            val mAlertDialog = BottomSheetDialog(this)

            val dialog_Binding =
                DataBindingUtil.inflate<DialogGenericsearchBinding>(
                    LayoutInflater.from(this), R.layout.dialog_genericsearch, null, false
                )

            dialog_Binding.toolbar.toolbarTitleGeneric.setText("City List")
            dialog_Binding.searchView.queryHint = "Enter City Name"

            // Clear focus from EditText
            dialog_Binding.searchView.clearFocus()
            dialog_Binding.searchView.requestFocus()
            dialog_Binding.searchView.hideKeyboard()


            mAlertDialog.setContentView(dialog_Binding.root)
            mAlertDialog.setCanceledOnTouchOutside(false)
            setupFullHeight(this, mAlertDialog)


            dialog_Binding.toolbar.dialogBack.setOnClickListener {

                mAlertDialog.dismiss()

            }

            var adapter = CityListAdapter(this, viewModel.cityList,
                object : ClickedCityItemCallback {
                    override fun clickedItem(
                        position: Int,
                        item: Any
                    ) {

                        viewModel.selectedCityItem = (item as CityListResponse.Data)
                        binding.edtCity.setText((item as CityListResponse.Data).name)

                        mAlertDialog.dismiss()


                    }
                })

            var layoutManager = LinearLayoutManager(this)



            dialog_Binding.recyclerView?.layoutManager = layoutManager
            dialog_Binding.recyclerView?.adapter = adapter
            dialog_Binding.searchView.setOnQueryTextListener(object :
                SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false;
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    adapter.filterList(
                        filterCity(
                            newText ?: "",
                            viewModel?.cityList!!
                        ) as ArrayList<CityListResponse.Data>
                    )
                    return false;
                }

            })


            // Show the AlertDialog
            mAlertDialog.show()
        }

    }

    private fun filterCity(
        searchText: String,
        cities: ArrayList<CityListResponse.Data>
    ): List<CityListResponse.Data> {

        return cities.filter {
            it.name!!.lowercase().contains(searchText.lowercase())
        }

    }


    private fun submitFuncionality() {
        binding.txtSubmitButton.setOnClickListener {
            if (validate()) {
              //  showToast(this, "Address Added Successfully!!")
               // this.finish()
                if (selectedAddressType.equals("Other",true)){
                    selectedAddressType = binding.edtOther.text.toString().trim()
                }
                val addAddressData = AddAddressData(
                    userId = "1",
                    addressType = selectedAddressType,
                    address = binding.edtAdddress.text.toString().trim(),
                    landmark = binding.edtLandmark.text.toString().trim(),
                    pincode = binding.edtPincode.text.toString().trim(),
                    cityId = viewModel.selectedCityItem?.id.toString().trim()
                )
                var ss = Gson().toJson(addAddressData)
                callApiSaveAddress(addAddressData)
            }
        }
    }

    private fun callApiSaveAddress(addAddressData: AddAddressData) {
        showSpinner(this@AddAddressActivity)
        viewModel.saveAddress(addAddressData)
    }
    private fun observeApiSaveAddress() {
        viewModel.responseSaveAddress.observe(this as AppCompatActivity, Observer {
            when (it) {
                is SuccessResponse -> {
                    hideSpinner()
                    if (it.success==1){
                          showToast(this, "Address Added Successfully!!")
                         this.finish()
                    }
                }

                is ErrorResponse -> {
                    hideSpinner()
                    if (it.message.isNullOrEmpty().not()) {
                        showCustomToast(findViewById(android.R.id.content),this,it.message)
                    }
                }

                is String -> {
                    hideSpinner()
                    showCustomToast(findViewById(android.R.id.content),this,it)
                }
            }

        })
    }


    private fun validate(): Boolean {

        var formValid: Boolean = true

        if (binding.edtAdddress.text.toString().isNullOrEmpty()) {
            binding.inputAddress.setErrorMessage("Please enter Full Address")


            formValid = false
        }


        if (binding.edtLandmark.text.toString().isNullOrEmpty()) {

            binding.inputLandmark.setErrorMessage("Please enter Landmark")

            formValid = false

        }


        if (binding.edtPincode.text.toString().isNullOrEmpty()) {
            formValid = false
            binding.inputPincode.setErrorMessage("Please enter PinCode")


        }
        if (binding.edtCity.text.toString().isNullOrEmpty()) {
            formValid = false
            binding.city.setErrorMessage("Please enter City")


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