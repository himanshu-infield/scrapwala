package com.scrapwala.screens.pickups.fragment

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.scrapwala.databinding.FragmentSchedulePickupBinding
import com.scrapwala.screens.pickups.category.ui.CategoryActivity
import com.scrapwala.screens.pickups.category.ui.SelectAddressActivity
import com.scrapwala.screens.pickups.model.AddressData
import com.scrapwala.utils.extensionclass.setErrorMessage
import java.util.Calendar


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

        binding.btnSubmit.setOnClickListener {
            if (isValidate()){

            }
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













binding.linearSelectAddres.setOnClickListener {
    val intent = Intent(requireContext(), SelectAddressActivity::class.java)
    someActivityResultLauncher.launch(intent)
}


        binding.edtDate.setOnClickListener {
            showDatePickerDialog()
        }


            binding.autoCompleteTextView.setOnTouchListener(View.OnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    val intent = Intent(requireContext(), CategoryActivity::class.java)
                    someActivityResultLauncher.launch(intent)
                }
                false
            })
//        binding.autoCompleteTextView.inputType = InputType.TYPE_NULL
//        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, wasteCategory)
//        binding.autoCompleteTextView.setAdapter(adapter)
//
//        binding.autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
//            val selectedItem = parent.getItemAtPosition(position).toString()
//            binding.edtCategory.setText(""+selectedItem)
//        }
    }


    private fun isValidate(): Boolean {
        var formValid: Boolean = true
        if (binding.edtCategory.text.toString().trim().isNullOrEmpty()) {
            binding.category.setErrorMessage("Please enter select category")
            formValid = false
        }
        return formValid
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
                var addressObj = Gson().fromJson<AddressData>(
                    item,
                    AddressData::class.java
                )

                if(addressObj!=null &&addressObj.fullAddress.isNullOrEmpty().not()){
                    binding.edtAddress.setText(addressObj.fullAddress)
                }

            }
        }

    }
}