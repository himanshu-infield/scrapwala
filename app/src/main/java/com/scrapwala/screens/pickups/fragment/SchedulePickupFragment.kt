package com.scrapwala.screens.pickups.fragment

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.scrapwala.R
import com.scrapwala.databinding.FragmentSchedulePickupBinding
import com.scrapwala.databinding.LayoutScheduledPickupBinding
import com.scrapwala.screens.pickups.PickupsActivity
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

        binding.autoCompleteTextView.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                binding.autoCompleteTextView.showDropDown()
            }
            false
        })
        /**select waste category**/
        binding.autoCompleteTextView.inputType = InputType.TYPE_NULL
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, wasteCategory)
        binding.autoCompleteTextView.setAdapter(adapter)

        binding.autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            binding.edtCategory.setText(""+selectedItem)
        }

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
}