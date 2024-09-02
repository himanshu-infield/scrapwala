package com.scrapwala.screens.pickups.fragment

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import com.scrapwala.R
import com.scrapwala.databinding.FragmentSchedulePickupBinding
import com.scrapwala.utils.extensionclass.setErrorMessage

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

        binding.autoCompleteTextView.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                binding.autoCompleteTextView.showDropDown()
            }
            false
        })
        binding.autoCompleteTextView.inputType = InputType.TYPE_NULL
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, wasteCategory)
        binding.autoCompleteTextView.setAdapter(adapter)

        binding.autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            binding.edtCategory.setText(""+selectedItem)
        }
    }


    private fun isValidate(): Boolean {
        var formValid: Boolean = true
        if (binding.edtCategory.text.toString().trim().isNullOrEmpty()) {
            binding.category.setErrorMessage("Please enter select category")
            formValid = false
        }
        return formValid
    }
}