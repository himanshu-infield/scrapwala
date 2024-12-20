package com.scrapwala.screens.pickups.adapter

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.scrapwala.R
import com.scrapwala.databinding.LayoutWasteCategoryBinding
import com.scrapwala.screens.pickups.category.ui.CategoryActivity
import com.scrapwala.screens.pickups.model.WasteTypeCategoryModel
import com.scrapwala.utils.extensionclass.setErrorMessage

class WasteTypeAdapter(
    private val context: Context,
    private val wasteTypeCategoryModelObj: MutableList<WasteTypeCategoryModel>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<WasteTypeAdapter.IncomeTypeViewHolder>() {

    // ViewHolder class for the adapter
    class IncomeTypeViewHolder(val binding: LayoutWasteCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeTypeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutWasteCategoryBinding.inflate(inflater, parent, false)
        return IncomeTypeViewHolder(binding)
    }

    var errorPositions = mutableListOf<Int>()
    private val errorMap = mutableMapOf<Int, Boolean>()

    override fun onBindViewHolder(holder: IncomeTypeViewHolder, position: Int) {
        val currentItem = wasteTypeCategoryModelObj[position]

        // Remove any existing TextWatcher to avoid duplication
        holder.binding.edtWeight.clearFocus() // Clear focus to prevent recycling issues
        holder.binding.edtWeight.removeTextChangedListener(holder.binding.edtWeight.tag as? TextWatcher)

        if (errorMap[position] == true) {
//            println("=============1")
//            holder?.binding?.category?.setErrorMessage("Please select category")
//            holder?.binding?.weight?.setErrorMessage("Please enter estimate weight")
        } else {
//            println("=============2")
//            holder?.binding?.category?.setErrorMessage("Please select category")
//            holder?.binding?.weight?.setErrorMessage("Please enter estimate weight")
        }

        if (position>0){
            holder.binding.separator.visibility = View.VISIBLE
        }else{
            holder.binding.separator.visibility = View.GONE
        }

        if (currentItem.edtWeight.isNullOrEmpty().not()){
            if (currentItem.edtWeight.toInt()>=1000 && currentItem.weightUnt.equals("Kg",true)){

                var ss = (currentItem.edtWeight.toInt())/1000


                currentItem.edtWeight = ss.toString()
                currentItem.weightUnt = "Tone"
                currentItem.sliderValue = ss.toFloat()

            }else{

            }

        }

        holder.binding.edtCategory.isCursorVisible = false
        holder.binding.spinnerUnitType.inputType = InputType.TYPE_NULL
        holder.binding.spinnerUnitType.setText(""+currentItem.weightUnt,false)

        try {


            if(currentItem.sliderValue>100){
                holder.binding.slider.value=100F
                currentItem.sliderValue = 100F
            }
            else{
                holder.binding.slider.value = currentItem.sliderValue
            }

        }catch (e:Exception){
            holder.binding.slider.value = 1F
        }


        if (currentItem.edtWeight.isNullOrEmpty().not()){
            holder.binding.edtWeight.setText(currentItem.edtWeight)
        }else{
            holder.binding.edtWeight.setText("")
        }

         if (currentItem.selectedCategory.isNullOrEmpty().not()){
             holder.binding.edtCategory.setText(currentItem.selectedCategory)
             holder.binding.edtCategory.setSelection(holder.binding.edtCategory.text.toString().length)
         }else{
             holder.binding.edtCategory.setText("")
         }

        // Create a new TextWatcher
        val weightTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                println("----------"+holder.adapterPosition+" " +s.toString())
                wasteTypeCategoryModelObj[holder.adapterPosition].edtWeight = s.toString() // Use adapter position
            }

            override fun afterTextChanged(s: Editable) {}
        }

        // Attach the TextWatcher and store it as a tag
        holder.binding.edtWeight.addTextChangedListener(weightTextWatcher)
        holder.binding.edtWeight.tag = weightTextWatcher



try {
    holder.binding.slider.setLabelFormatter { value: Float ->
        "${value.toInt()}${currentItem.weightUnt}"
    }
}catch (e:Exception){
    e.printStackTrace()
//    holder.binding.slider.setLabelFormatter { value: Float ->
//        "${value.toInt()}${1}"
//}
}


        holder.binding.slider.addOnChangeListener { slider, value, fromUser ->
            // value is the current value of the slider
            val sliderValue = value.toInt()
            // You can update a TextView or any other UI element with the value
            holder.binding.edtWeight.setText("$sliderValue".toString())
            holder.binding.edtWeight.setSelection(holder.binding.edtWeight.text.toString().length)
        }


        holder.binding.autoCompleteTextView.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                listener.onItemCategorySelect(currentItem, position)
            }
            false
        })



        /**spinner data set**/
        var incomeType: List<String> = listOf()
        if (currentItem.weightUnt.contains("kg",true)){
             incomeType = listOf("Kg","Tone")
        }else  if (currentItem.weightUnt.contains("Piece",true)){
             incomeType = listOf("Piece")
        }else  if (currentItem.weightUnt.contains("Tone",true)){
             incomeType = listOf("Kg","Tone")
        }else{
            incomeType = listOf("Kg","Tone","Piece")
        }


        val incomeTypeAdapter = ArrayAdapter(context, R.layout.dropdown_menu_popup_item, incomeType)
        incomeTypeAdapter.setDropDownViewResource(R.layout.dropdown_menu_popup_item)
        holder.binding.spinnerUnitType.setAdapter(incomeTypeAdapter)

        holder.binding.spinnerUnitType.setOnClickListener{
            holder.binding.spinnerUnitType.showDropDown()
        }

        holder.binding.spinnerUnitType.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = incomeType[position]
            currentItem.weightUnt = selectedItem
        }


    }

    override fun getItemCount(): Int {
        return wasteTypeCategoryModelObj.size
    }

    private var pos = 0
    // Method to add a new item to the list
     fun addItem(incomeType: WasteTypeCategoryModel) {
        wasteTypeCategoryModelObj.add(incomeType)
        notifyItemInserted(wasteTypeCategoryModelObj.size - 1) // Notify the adapter of the new item
    }

    fun clearItem() {
        wasteTypeCategoryModelObj.clear()
        val newItem = WasteTypeCategoryModel()
        addItem(newItem)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        if (position >= 0 && position < wasteTypeCategoryModelObj.size) {
            wasteTypeCategoryModelObj.removeAt(position)
            notifyItemRemoved(position) // Notify the adapter of item removal
            notifyItemRangeChanged(
                position,
                wasteTypeCategoryModelObj.size
            ) // Update the rest of the list
        }
    }

    fun getSourceIncomeList(): MutableList<WasteTypeCategoryModel> {
        return wasteTypeCategoryModelObj
    }


    interface OnItemClickListener {
        fun onItemCategorySelect(data: WasteTypeCategoryModel?, position: Int)
    }



    fun validateInputs(recyclerView: RecyclerView): Boolean {
        var isValid = true

        for ((index, item) in wasteTypeCategoryModelObj.withIndex()) {
           if (index==0){

               val isFieldEmpty = item.edtWeight.isNullOrEmpty()
               val selectedCategory = item.selectedCategory.isNullOrEmpty()
               errorMap[index] = isFieldEmpty

               if (selectedCategory){
                   isValid = false
                   val viewHolder = recyclerView.findViewHolderForAdapterPosition(index) as? IncomeTypeViewHolder
                   viewHolder?.binding?.category?.setErrorMessage("Please select category")
               }
                if (item.weightUnt.contains("Piece",true)){
                    if (isFieldEmpty) {
                        isValid = false
                        val viewHolder = recyclerView.findViewHolderForAdapterPosition(index) as? IncomeTypeViewHolder
                        viewHolder?.binding?.weight?.setErrorMessage("Please enter number of pieces")
                    }else if (item.edtWeight.toInt()<=0){
                        isValid = false
                        val viewHolder = recyclerView.findViewHolderForAdapterPosition(index) as? IncomeTypeViewHolder
                        viewHolder?.binding?.weight?.setErrorMessage("Piece must be greater than 0")
                    }

                    else if (item.edtWeight.toInt()>2000){
                        isValid = false
                        val viewHolder = recyclerView.findViewHolderForAdapterPosition(index) as? IncomeTypeViewHolder
                        viewHolder?.binding?.weight?.setErrorMessage("Piece should be less than 2000")
                    }

                }else if (item.weightUnt.contains("Tone",true)){
                    if (isFieldEmpty) {
                        isValid = false
                        val viewHolder = recyclerView.findViewHolderForAdapterPosition(index) as? IncomeTypeViewHolder
                        viewHolder?.binding?.weight?.setErrorMessage("Please enter number of Tone")
                    }else if (item.edtWeight.toInt()<=0){
                        isValid = false
                        val viewHolder = recyclerView.findViewHolderForAdapterPosition(index) as? IncomeTypeViewHolder
                        viewHolder?.binding?.weight?.setErrorMessage("Weight must be greater than 0 Tone")
                    }

                    else if (item.edtWeight.toInt()>2){
                        isValid = false
                        val viewHolder = recyclerView.findViewHolderForAdapterPosition(index) as? IncomeTypeViewHolder
                        viewHolder?.binding?.weight?.setErrorMessage("Weight must be less than 2 Tone")
                    }
                 } else{
                    if (isFieldEmpty) {
                        isValid = false
                        val viewHolder = recyclerView.findViewHolderForAdapterPosition(index) as? IncomeTypeViewHolder
                        viewHolder?.binding?.weight?.setErrorMessage("Please enter estimate weight")
                    }else if (item.edtWeight.toInt()<=0){
                        isValid = false
                        val viewHolder = recyclerView.findViewHolderForAdapterPosition(index) as? IncomeTypeViewHolder
                        viewHolder?.binding?.weight?.setErrorMessage("Weight must be greater than 0 Kg")
                    }

                    else if (item.edtWeight.toInt()>999){
                        isValid = false
                        val viewHolder = recyclerView.findViewHolderForAdapterPosition(index) as? IncomeTypeViewHolder
                        viewHolder?.binding?.weight?.setErrorMessage("Weight must be less than 999 Kg")
                    }
                 }



           }else{

               val isFieldEmpty = item.edtWeight.isNullOrEmpty()
               val selectedCategory = item.selectedCategory.isNullOrEmpty()
               errorMap[index] = isFieldEmpty

               if (!selectedCategory){
                  /* if (isFieldEmpty) {
                       isValid = false
                       val viewHolder = recyclerView.findViewHolderForAdapterPosition(index) as? IncomeTypeViewHolder
                       viewHolder?.binding?.weight?.setErrorMessage("Please enter estimate weight")
                   }else if (item.edtWeight.toInt()<=10){
                       isValid = false
                       val viewHolder = recyclerView.findViewHolderForAdapterPosition(index) as? IncomeTypeViewHolder
                       viewHolder?.binding?.weight?.setErrorMessage("estimate weight grater 10")
                   }*/
                   if (item.weightUnt.contains("Piece",true)){
                       if (isFieldEmpty) {
                           isValid = false
                           val viewHolder = recyclerView.findViewHolderForAdapterPosition(index) as? IncomeTypeViewHolder
                           viewHolder?.binding?.weight?.setErrorMessage("Please enter number of pieces")
                       }
                       else if (item.edtWeight.toInt()<=0){
                           isValid = false
                           val viewHolder = recyclerView.findViewHolderForAdapterPosition(index) as? IncomeTypeViewHolder
                           viewHolder?.binding?.weight?.setErrorMessage("Piece must be greater than 0")
                       }

                       else if (item.edtWeight.toInt()>2000){
                           isValid = false
                           val viewHolder = recyclerView.findViewHolderForAdapterPosition(index) as? IncomeTypeViewHolder
                           viewHolder?.binding?.weight?.setErrorMessage("Piece should be less than 2000")
                       }


                   }else if (item.weightUnt.contains("Tone",true)){
                       if (isFieldEmpty) {
                           isValid = false
                           val viewHolder = recyclerView.findViewHolderForAdapterPosition(index) as? IncomeTypeViewHolder
                           viewHolder?.binding?.weight?.setErrorMessage("Please enter number of Tone")
                       }
                       else if (item.edtWeight.toInt()<=0){
                           isValid = false
                           val viewHolder = recyclerView.findViewHolderForAdapterPosition(index) as? IncomeTypeViewHolder
                           viewHolder?.binding?.weight?.setErrorMessage("Weight must be greater than 0")
                       }


                       else if (item.edtWeight.toInt()>2){
                           isValid = false
                           val viewHolder = recyclerView.findViewHolderForAdapterPosition(index) as? IncomeTypeViewHolder
                           viewHolder?.binding?.weight?.setErrorMessage("Weight must be less than 2 Tone")
                       }
                   } else{
                       if (isFieldEmpty) {
                           isValid = false
                           val viewHolder = recyclerView.findViewHolderForAdapterPosition(index) as? IncomeTypeViewHolder
                           viewHolder?.binding?.weight?.setErrorMessage("Please enter estimate weight")
                       }else if (item.edtWeight.toInt()<=0){
                           isValid = false
                           val viewHolder = recyclerView.findViewHolderForAdapterPosition(index) as? IncomeTypeViewHolder
                           viewHolder?.binding?.weight?.setErrorMessage("Weight must be greater than 0 Kg")
                       }

                       else if (item.edtWeight.toInt()>999){
                           isValid = false
                           val viewHolder = recyclerView.findViewHolderForAdapterPosition(index) as? IncomeTypeViewHolder
                           viewHolder?.binding?.weight?.setErrorMessage("Weight must be less than 999 Kg")
                       }
                   }


               }

           }
        }
        return isValid
    }


}
