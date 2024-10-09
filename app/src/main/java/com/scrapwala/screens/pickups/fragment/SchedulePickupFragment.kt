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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.scrapwala.R
import com.scrapwala.databinding.FragmentSchedulePickupBinding
import com.scrapwala.databinding.LayoutScheduledPickupBinding
import com.scrapwala.screens.pickups.PickupsActivity
import com.scrapwala.screens.pickups.category.ui.CategoryActivity
import com.scrapwala.screens.pickups.SelectAddressActivity
import com.scrapwala.screens.pickups.category.model.CategoryResponse
import com.scrapwala.screens.pickups.model.AddressListResponse
import com.scrapwala.screens.pickups.model.CreateCategoryData
import com.scrapwala.screens.pickups.model.InProgressListResponse
import com.scrapwala.screens.pickups.model.SuccessResponse
import com.scrapwala.screens.pickups.viewmodel.PickupViewModel
import com.scrapwala.utils.ErrorResponse
import com.scrapwala.utils.extensionclass.hideSpinner
import com.scrapwala.utils.extensionclass.setErrorMessage
import com.scrapwala.utils.extensionclass.showCustomToast
import com.scrapwala.utils.extensionclass.showSpinner
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class SchedulePickupFragment : Fragment() {
    lateinit var binding: FragmentSchedulePickupBinding

    private val viewModel: PickupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSchedulePickupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getExtras()
        initView()
        observeApiCreateCategory()
    }


    private var editPickupObj: InProgressListResponse.Data? = null
    private fun getExtras() {
        if (arguments != null) {
            val editPickupObjString = arguments?.getString("editPickup")

            if (editPickupObjString.isNullOrEmpty().not()) {
                editPickupObj = Gson().fromJson(editPickupObjString, InProgressListResponse.Data::class.java)
                setPickupData(editPickupObj)
            }
        }

    }

    private fun setPickupData(editPickupObj: InProgressListResponse.Data?) {
        if (editPickupObj != null) {

        }
    }

    private fun initView() {
        // binding.edtWeight.addTextChangedListener(weightTextWatcher)
        binding.autoCompleteTextView.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val intent = Intent(requireContext(), CategoryActivity::class.java)
                someActivityResultLauncher.launch(intent)
            }
            false
        })


        binding.slider.setLabelFormatter { value: Float ->
            "${value.toInt()}$weightUnt"
        }
        binding.slider.addOnChangeListener { slider, value, fromUser ->
            // value is the current value of the slider
            val sliderValue = value.toInt()
            println("Slider value: $sliderValue $weightUnt")

            // You can update a TextView or any other UI element with the value
            binding.edtWeight.setText("$sliderValue $weightUnt".toString())
            binding.edtWeight.setSelection(binding.edtWeight.text.toString().length)
        }
//        val sliderValue = binding.slider.value.toInt()
//        binding.edtWeight.setText("$sliderValue $weightUnt")


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

    }

    private fun callApiCreateCategory() {
        val addressId = binding.edtAddress.getTag(R.id.edt_Address) as? Int
        val createCategoryData = CreateCategoryData(
            userId = "1",
            categoryId = selectCategoryObj?.id.toString(),
            weight = binding.edtWeight.text.toString().filter { it.isDigit() }.trim(),
            weightId = selectCategoryObj?.weightId.toString(),
            addressId = addressId.toString() ?: "",
            message = binding.edtMessage.text.toString().trim(),
            date = binding.edtDate.text.toString().trim(),
            time = binding.edtTime.text.toString().trim()
        )
        var ss = Gson().toJson(createCategoryData)
        println("----------$ss")
        showSpinner(requireContext())
        viewModel.createCategory(createCategoryData)
    }

    private fun observeApiCreateCategory() {
        viewModel.responseCreatePickUp.observe(requireActivity(), Observer {
            when (it) {
                is SuccessResponse -> {
                    hideSpinner()
                    if (it.success == 1) {
                        binding.edtCategory.setText("")
                        binding.edtWeight.setText("")
                        binding.edtDate.setText("")
                        binding.edtTime.setText("")
                        binding.edtAddress.setText("")
                        binding.edtMessage.setText("")
                        openDialog()
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
            binding.address.setErrorMessage("Please select address")
            formValid = false
        }
        return formValid
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


    public fun setCatgory(item: String) {
        if (item.isNullOrEmpty().not()) {
            binding.edtCategory.setText(item)
            binding.edtCategory.setSelection(binding.edtCategory.text.toString().length)
        }

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
                        binding.slider.value = 10F
                        binding.edtWeight.setText("")
                        weightUnt = selectCategoryObj!!.weight ?: "Kg"
                        setCatgory(selectCategoryObj!!.name ?: "")
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


    /**text watcher convert KG getFormattedPrice**/
    private val weightTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (s != null && s.toString().isNotEmpty() && !s.toString().endsWith("Kg")) {

                binding.edtWeight.removeTextChangedListener(this)
                val newText = s.toString().replace("Kg", "").trim() + " Kg"
                binding.edtWeight.setText(newText)
                binding.edtWeight.setSelection(newText.length - 3)
                binding.edtWeight.addTextChangedListener(this)
            } else {
                binding.edtWeight.setText("")
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }

}