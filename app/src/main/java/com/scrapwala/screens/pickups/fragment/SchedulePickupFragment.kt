package com.scrapwala.screens.pickups.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.scrapwala.R
import com.scrapwala.databinding.FragmentSchedulePickupBinding
import com.scrapwala.databinding.LayoutScheduledPickupBinding
import com.scrapwala.databinding.LayoutWasteCategoryBinding
import com.scrapwala.screens.login.model.VerifyOtpResponse
import com.scrapwala.screens.pickups.PickupsActivity
import com.scrapwala.screens.pickups.category.ui.CategoryActivity
import com.scrapwala.screens.pickups.SelectAddressActivity
import com.scrapwala.screens.pickups.adapter.WasteTypeAdapter
import com.scrapwala.screens.pickups.category.model.CategoryResponse
import com.scrapwala.screens.pickups.model.AddressListResponse
import com.scrapwala.screens.pickups.model.CreateCategoryData
import com.scrapwala.screens.pickups.model.InProgressListResponse
import com.scrapwala.screens.pickups.model.SuccessResponse
import com.scrapwala.screens.pickups.model.WasteTypeCategoryModel
import com.scrapwala.screens.pickups.viewmodel.PickupViewModel
import com.scrapwala.utils.ErrorResponse
import com.scrapwala.utils.Preferences
import com.scrapwala.utils.extensionclass.hideSpinner
import com.scrapwala.utils.extensionclass.setErrorMessage
import com.scrapwala.utils.extensionclass.showCustomToast
import com.scrapwala.utils.extensionclass.showSpinner
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@AndroidEntryPoint
class SchedulePickupFragment : Fragment(),WasteTypeAdapter.OnItemClickListener {
    lateinit var binding: FragmentSchedulePickupBinding
    private val viewModel: PickupViewModel by viewModels()
    private var editPickupObj: InProgressListResponse.Data? = null
    private var pref: VerifyOtpResponse.Data? = null
    private val wasteTypeCategoryModel = mutableListOf<WasteTypeCategoryModel>()
    private lateinit var wasteTypeAdapter: WasteTypeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSchedulePickupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = Preferences.getUserDataObj(requireContext())
        addDefaultWasteCategory()
        getExtras()
        initView()
        observeApiCreateCategory()
    }

    private fun addDefaultWasteCategory() {
//        wasteTypeCategoryModel.add(WasteTypeCategoryModel())
//        var itemBinding = DataBindingUtil.inflate<LayoutWasteCategoryBinding>(
//            LayoutInflater.from(requireContext()),
//            R.layout.layout_waste_category,
//            null,
//            false)
//
//        binding.linearContainerWasteCategory.addView(itemBinding.root)
            wasteTypeCategoryModel.clear()
            wasteTypeCategoryModel.add(WasteTypeCategoryModel())
            wasteTypeAdapter = WasteTypeAdapter(requireActivity(), wasteTypeCategoryModel,this)
            binding.recWasteCategory.layoutManager = LinearLayoutManager(requireActivity())
            binding.recWasteCategory.adapter = wasteTypeAdapter

    }


    private fun getExtras() {
        if ((activity as PickupsActivity).editPickupObj!=null){
            editPickupObj = (activity as PickupsActivity).editPickupObj
            setPickupData((activity as PickupsActivity).editPickupObj)
        }
    }

    private fun setPickupData(editPickupObj: InProgressListResponse.Data?) {
        if (editPickupObj != null) {
//            viewModel.weight_id = editPickupObj.weightId?:0
//            viewModel.category_id = editPickupObj.categoryId?:0
//            weightUnt = editPickupObj!!.weightName ?: "Kg"

//            binding.wasteCategory.slider.value = editPickupObj.weight!!.toFloat()
//            binding.wasteCategory.edtCategory.setText(""+editPickupObj.categoryName)
//            binding.wasteCategory.edtWeight.setText(""+editPickupObj.weight+" "+editPickupObj.weightName)
            wasteTypeCategoryModel.clear()
            wasteTypeCategoryModel.add(WasteTypeCategoryModel(
                ""+editPickupObj.categoryName,
                ""+editPickupObj.weight,
                ("" + editPickupObj.weightName) ?: "Kg",
                editPickupObj.weightId?:0,
                editPickupObj.categoryId?:0,
                editPickupObj.weight!!.toFloat()?:10F,)

            )
            wasteTypeAdapter = WasteTypeAdapter(requireActivity(), wasteTypeCategoryModel,this)
            binding.recWasteCategory.layoutManager = LinearLayoutManager(requireActivity())
            binding.recWasteCategory.adapter = wasteTypeAdapter


            binding.edtAddress.setTag(R.id.edt_Address, editPickupObj.addressId ?: 0)

            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = inputFormat.parse(editPickupObj.date)
            val formattedDate = outputFormat.format(date)
            binding.edtDate.setText(""+formattedDate)


            binding.edtTime.setText(""+editPickupObj.time)
            binding.edtAddress.setText(""+editPickupObj.addressLine1+" "+editPickupObj.addressLine2+" "+editPickupObj.pincode)

            binding.edtMessage.setText(""+editPickupObj.message)


        }
    }

    private fun initView() {

        binding.btnSubmit.setOnClickListener {
            if (isValidate()) {
                callApiCreateCategory()
                val imm =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(requireView().windowToken, 0)

            }
        }


        binding.linearSelectAddres.setOnClickListener {
            val intent = Intent(requireContext(), SelectAddressActivity::class.java)
            someActivityResultLauncher.launch(intent)
        }

        /**select waste category**/


        binding.edtDate.setOnClickListener {
            showDatePickerDialog()
        }
        /* binding.edtTime.setOnClickListener {
             val calendar = Calendar.getInstance()
             val hour = calendar.get(Calendar.HOUR_OF_DAY)
             val minute = calendar.get(Calendar.MINUTE)

             val timePickerDialog = TimePickerDialog(
                 requireContext(),
                 { _, selectedHour, selectedMinute ->
                     // Determine AM/PM
                     val amPm: String = if (selectedHour < 12) "AM" else "PM"
                     // Convert selectedHour to 12-hour format
                     val hourIn12Format = if (selectedHour % 12 == 0) 12 else selectedHour % 12
                     // Format the time string
                     val formattedTime =
                         String.format("%02d:%02d %s", hourIn12Format, selectedMinute, amPm)
                     // Set the time to your TextView (or EditText)
                     binding.edtTime.setText(formattedTime)
                 },
                 hour,
                 minute,
                 false // Set false for 12-hour format (AM/PM)
             )
             timePickerDialog.show()

         }*/

        binding.edtTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val second = calendar.get(Calendar.SECOND)

            // Open TimePickerDialog
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { _, selectedHour, selectedMinute ->
                    // Get the current second when the time is selected
                    val currentSeconds = Calendar.getInstance().get(Calendar.SECOND)

                    // Format the time as "HH:mm:ss" (24-hour format)
                    val formattedTime = String.format(
                        "%02d:%02d:%02d",
                        selectedHour,
                        selectedMinute,
                        currentSeconds
                    )

                    // Set the time to your EditText
                    binding.edtTime.setText(formattedTime)
                },
                hour,
                minute,
                true // Set true for 24-hour format
            )

            timePickerDialog.show()
        }



        binding.tvAddCategory.setOnClickListener {
            val newItem = WasteTypeCategoryModel()
             wasteTypeAdapter.addItem(newItem)


        }
    }


    private fun convertTonsToKilograms(tons: Double): Double {
        return tons * 1000
    }
    private fun callApiCreateCategory() {
       val list = wasteTypeAdapter.getSourceIncomeList()
//        println("----------$list")

        val addressId = binding.edtAddress.getTag(R.id.edt_Address) as? Int
        val jsonArray = JsonArray()
        list.forEachIndexed { index, wasteTypeCategoryModel ->
            if (list[index].selectedCategory.isNullOrEmpty().not()){
                val jsonObject = JsonObject()
                if (editPickupObj?.id!=null){
                    jsonObject.addProperty("pickupId",editPickupObj?.id.toString()?:"")
                }else{
                    jsonObject.addProperty("pickupId","")
                }

                jsonObject.addProperty("userId",pref?.id!!.toString())
                jsonObject.addProperty("categoryId",list[index].categoryId)
                jsonObject.addProperty("weightId",list[index].weightId)

                if (list[index].weightUnt.contains("Tone",true)){
                    val tons = list[index].edtWeight.toDouble()
                    if (tons!=null){
                        val kilograms = convertTonsToKilograms(tons)
                        jsonObject.addProperty("weight",kilograms.roundToInt().toString())
                    }else{
                        jsonObject.addProperty("weight",list[index].edtWeight)
                    }

                }else{
                    jsonObject.addProperty("weight",list[index].edtWeight)
                }


                jsonObject.addProperty("addressId",addressId.toString() ?: "")
                jsonObject.addProperty("message",binding.edtMessage.text.toString().trim())
                jsonObject.addProperty("date",binding.edtDate.text.toString().trim())
                jsonObject.addProperty("time",binding.edtTime.text.toString().trim())
                jsonArray.add(jsonObject)
            }

        }

        println("----------$jsonArray")
        showSpinner(requireContext())
        viewModel.responseCreatePickUp.observe(requireActivity(), observer!!)
        viewModel.createCategory(jsonArray)

    }

    private var observer: Observer<Any>? = null
    private fun observeApiCreateCategory() {
//        viewModel.responseCreatePickUp.observe(requireActivity(), Observer {
        observer = Observer<Any> { it ->
            when (it) {
                is SuccessResponse -> {
                    hideSpinner()
                    if (it.success == 1) {
                        wasteTypeAdapter.clearItem()
                        wasteTypeCategoryModel.clear()
//                        wasteTypeCategoryModel.add(WasteTypeCategoryModel())
//                        wasteTypeAdapter = WasteTypeAdapter(requireActivity(), wasteTypeCategoryModel,this)
//                        binding.recWasteCategory.layoutManager = LinearLayoutManager(requireActivity())
//                        binding.recWasteCategory.adapter = wasteTypeAdapter

                        binding.edtDate.setText("")
                        binding.edtTime.setText("")
                        binding.edtAddress.setText("")
                        binding.edtMessage.setText("")
                        openDialog()
                    }
                    viewModel.responseCreatePickUp.removeObserver(observer!!)

                }

                is ErrorResponse -> {
                    hideSpinner()
                    if (it.message.isNullOrEmpty().not()) {
                        showCustomToast(binding.root, requireActivity(), it.message)
                    }
                    viewModel.responseCreatePickUp.removeObserver(observer!!)
                }

                is String -> {
                    hideSpinner()
                    showCustomToast(binding.root, requireActivity(), it)
                    viewModel.responseCreatePickUp.removeObserver(observer!!)
                }
            }

        }
//        )
    }


    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDayOfMonth)
                val selected_Date = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"

                val inputDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val outputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date: Date? = inputDateFormat.parse(selected_Date)
                val outputDate = date?.let { outputDateFormat.format(it) }
                binding.edtDate.setText(outputDate.toString())

            },
            year, month, day
        )

        // Disable previous dates
        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        datePickerDialog.show()


    }

    private fun isValidate(): Boolean {
        val isValid = wasteTypeAdapter.validateInputs(binding.recWasteCategory)
        var formValid: Boolean = true

        if (binding.edtDate.text.toString().trim().isNullOrEmpty()) {
            binding.date.setErrorMessage("Please select date")
            formValid = false
        }
        if (binding.edtTime.text.toString().trim().isNullOrEmpty()) {
            binding.time.setErrorMessage("Please select time")
            formValid = false
        }
        if (binding.edtAddress.text.toString().trim().isNullOrEmpty()) {
            binding.address.setErrorMessage("Please select address")
            formValid = false
        }
        return formValid && isValid

    }


    private fun openDialog() {
        val dialog = Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        val inflater = LayoutInflater.from(requireContext())
        val layoutScheduledPickupBinding: LayoutScheduledPickupBinding =
            DataBindingUtil.inflate(inflater, R.layout.layout_scheduled_pickup, null, false)
        dialog.setContentView(layoutScheduledPickupBinding.root)


        layoutScheduledPickupBinding.imgClose.setOnClickListener {
            // finish()
            (activity as PickupsActivity).binding.viewpager.currentItem = 1
            addDefaultWasteCategory()
            dialog.dismiss()

        }

        layoutScheduledPickupBinding.btnScheduleAnother.setOnClickListener {
            addDefaultWasteCategory()
            dialog.dismiss()

        }
        layoutScheduledPickupBinding.viewPickup.setOnClickListener {
            (activity as PickupsActivity).binding.viewpager.currentItem = 1
            addDefaultWasteCategory()
            dialog.dismiss()
        }

        dialog.show()

    }


    private var selectCategoryObj: CategoryResponse.Data? = null
    private var weightUnt = "Kg"
    private val someActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == 100) {
                val data: Intent? = result.data
                var item = data?.getStringExtra("clickedItem")
                if (item.isNullOrEmpty().not()) {
                    selectCategoryObj = Gson().fromJson(item, CategoryResponse.Data::class.java)
                    if (selectCategoryObj != null && selectCategoryObj!!.name.isNullOrEmpty()
                            .not()
                    ) {


                        selectWasteTypeCategoryModel?.weightId = selectCategoryObj?.weightId?:0
                        selectWasteTypeCategoryModel?.categoryId = selectCategoryObj?.id?:0
                        selectWasteTypeCategoryModel?.sliderValue = 10F
                        selectWasteTypeCategoryModel?.weightUnt = selectCategoryObj!!.weight ?: "Kg"
                        selectWasteTypeCategoryModel?.selectedCategory = selectCategoryObj!!.name ?: ""
                        selectWasteTypeCategoryModel?.edtWeight =  ""
                        wasteTypeAdapter.notifyItemChanged(selectPosition)



                    }
                }
            } else if (result.resultCode == 101) {
                val data: Intent? = result.data
                var item = data?.getStringExtra("selectedAddress")
                if (item.isNullOrEmpty().not()) {
                    var addressObj = Gson().fromJson<AddressListResponse.Data>(
                        item,
                        AddressListResponse.Data::class.java
                    )

                    if (addressObj != null && addressObj.addressLine1.isNullOrEmpty().not()) {
                        binding.edtAddress
                        binding.edtAddress.setTag(R.id.edt_Address, addressObj.id ?: 0)
                        binding.edtAddress.setText(addressObj.addressLine1 + " " + addressObj.addressLine2 + " " + addressObj.pincode)
                    }

                }
            }

        }



    public fun setValuesOnView(item: InProgressListResponse.Data?) {
        editPickupObj = item
        setPickupData(editPickupObj)
    }


    private var selectWasteTypeCategoryModel:WasteTypeCategoryModel? = null
    private var selectPosition = 0
    override fun onItemCategorySelect(data: WasteTypeCategoryModel?, position: Int) {
        selectWasteTypeCategoryModel = data
        selectPosition = position
        val intent = Intent(requireContext(), CategoryActivity::class.java)
        someActivityResultLauncher.launch(intent)
    }

}