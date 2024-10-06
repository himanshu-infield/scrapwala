package com.scrapwala.screens.pickups.fragment

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.scrapwala.R
import com.scrapwala.databinding.FragmentSchedulePickupBinding
import com.scrapwala.databinding.LayoutScheduledPickupBinding
import com.scrapwala.screens.pickups.PickupsActivity
import com.scrapwala.screens.pickups.category.ui.CategoryActivity
import com.scrapwala.screens.pickups.SelectAddressActivity
import com.scrapwala.screens.pickups.model.AddressData
import com.scrapwala.screens.pickups.model.AddressListResponse
import com.scrapwala.utils.extensionclass.setErrorMessage
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class SchedulePickupFragment : Fragment() {
    private val wasteCategory = listOf("Paper", "Metal","Plastic","E-Waste")
    lateinit var binding: FragmentSchedulePickupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSchedulePickupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }



    private fun initView() {
        binding.autoCompleteTextView.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val intent = Intent(requireContext(), CategoryActivity::class.java)
                someActivityResultLauncher.launch(intent)
            }
            false
        })


        binding.slider.setLabelFormatter { value: Float ->
            "${value.toInt()}KG"
        }
        binding.slider.addOnChangeListener { slider, value, fromUser ->
            // value is the current value of the slider
            val sliderValue = value.toInt()
            println("Slider value: $sliderValue kg")

            // You can update a TextView or any other UI element with the value
            binding.edtWeight.setText("$sliderValue KG".toString())
        }
        val sliderValue = binding.slider.value.toInt()
        binding.edtWeight.setText("$sliderValue KG")





        binding.btnSubmit.setOnClickListener {
            if (isValidate()){
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(
                    (context as Activity).getWindow().getDecorView().getWindowToken(),
                    0
                )
                binding.edtCategory.setText("")
                binding.edtWeight.setText("")
                binding.edtDate.setText("")
                binding.edtTime.setText("")
                binding.edtAddress.setText("")
                binding.edtMessage.setText("")
                openDialog()

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
        binding.edtTime.setOnClickListener {
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
                    val formattedTime = String.format("%02d:%02d %s", hourIn12Format, selectedMinute, amPm)
                    // Set the time to your TextView (or EditText)
                    binding.edtTime.setText(formattedTime)
                },
                hour,
                minute,
                false // Set false for 12-hour format (AM/PM)
            )
            timePickerDialog.show()

        }
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
                binding.edtDate.setText(selected_Date.toString())
            },
            year, month, day
        )

          // Disable previous dates
        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        datePickerDialog.show()


    }

    private fun isValidate(): Boolean {

        var formValid: Boolean = true
        if (binding.edtCategory.text.toString().trim().isNullOrEmpty()) {
            binding.category.setErrorMessage("Please enter select category")
            formValid = false
        }

        if (binding.edtWeight.text.toString().trim().isNullOrEmpty()) {
            binding.weight.setErrorMessage("Please enter estimate weight")
            formValid = false
        }
        if (binding.edtDate.text.toString().trim().isNullOrEmpty()) {
            binding.date.setErrorMessage("Please select date")
            formValid = false
        }
        if (binding.edtTime.text.toString().trim().isNullOrEmpty()) {
            binding.time.setErrorMessage("Please select time")
            formValid = false
        }
        if (binding.edtAddress.text.toString().trim().isNullOrEmpty()) {
            binding.address.setErrorMessage("Please enter address")
            formValid = false
        }
        return formValid
    }






private fun openDialog() {
    val dialog = Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen)
    val inflater = LayoutInflater.from(requireContext())
    val layoutScheduledPickupBinding: LayoutScheduledPickupBinding = DataBindingUtil.inflate(inflater, R.layout.layout_scheduled_pickup, null, false)
    dialog.setContentView(layoutScheduledPickupBinding.root)


    layoutScheduledPickupBinding.imgClose.setOnClickListener {
        // finish()
        (activity as PickupsActivity).binding.viewpager.currentItem = 1
        dialog.dismiss()
    }

    layoutScheduledPickupBinding.btnScheduleAnother.setOnClickListener {
        dialog.dismiss()
    }
    layoutScheduledPickupBinding.viewPickup.setOnClickListener {
        (activity as PickupsActivity).binding.viewpager.currentItem = 1
        dialog.dismiss()
    }

    dialog.show()

}


public fun setCatgory(item:String){
    if(item.isNullOrEmpty().not()){
        binding.edtCategory.setText(item)
    }

}


private val someActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
    if (result.resultCode == 100) {
        val data: Intent? = result.data

        var item=data?.getStringExtra("clickedItem")


        if(item.isNullOrEmpty().not()){
            setCatgory(item?:"")
        }
    }

    else if (result.resultCode == 101) {
        val data: Intent? = result.data

        var item=data?.getStringExtra("selectedAddress")


        if(item.isNullOrEmpty().not()){
            var addressObj = Gson().fromJson<AddressListResponse.Data>(
                item,
                AddressListResponse.Data::class.java
            )

            if(addressObj!=null &&addressObj.addressLine1.isNullOrEmpty().not()){
                binding.edtAddress.setText(addressObj.addressLine1+" "+addressObj.addressLine2+" "+addressObj.pincode)
            }

        }
    }

}
}