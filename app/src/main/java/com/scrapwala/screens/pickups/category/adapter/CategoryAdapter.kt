package com.scrapwala.screens.pickups.category.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.scrapwala.R
import com.scrapwala.databinding.ItemCategoryBinding
import com.scrapwala.screens.pickups.category.model.CategoryData

class CategoryAdapter(
    var context: Context,
    var cityList: List<CategoryData>,
    var clickedItemCallback:ClickedItemCallback
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {



    var isFilteredList:Boolean=false
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryViewHolder {

        val binding =
            DataBindingUtil.inflate<ItemCategoryBinding>(
                LayoutInflater.from(parent.context), R.layout.item_category, parent, false
            )


        return CategoryViewHolder(binding)
    }

//    // method for filtering our recyclerview items.
    fun filterList(filterlist: ArrayList<CategoryData>,isFilterList:Boolean) {

        cityList = filterlist

        notifyDataSetChanged()

    this.isFilteredList=isFilterList
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        holder.bindData(cityList.get(position), position)
    }

    override fun getItemCount(): Int {

        return cityList.size
    }

    inner class CategoryViewHolder(var itemViewBinding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root) {

        fun bindData(item: CategoryData, position: Int) {

            itemViewBinding.root.setOnClickListener {
                clickedItemCallback.clickedItem(position, item.itemName?:"")
            }





            if (item.categoryName.isNullOrEmpty().not() && position == 0&&!isFilteredList) {

                itemViewBinding.txtCategoryName.visibility= View.VISIBLE
                itemViewBinding.txtCategoryName.setText(item.categoryName)
            }


            else{
                itemViewBinding.txtCategoryName.visibility= View.GONE

            }

            if (item.itemName.isNullOrEmpty().not()) {
                itemViewBinding.txtItemName.setText(item.itemName)
            }
            else{
                itemViewBinding.txtItemName.setText("")
            }


            if (item.itemPrice.isNullOrEmpty().not() ) {
                itemViewBinding.txtItemPrice.setText(item.itemPrice)
            }
            else{
                itemViewBinding.txtItemPrice.setText("")
            }


        }

    }
}