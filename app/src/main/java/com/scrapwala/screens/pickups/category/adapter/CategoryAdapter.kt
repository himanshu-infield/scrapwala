package com.scrapwala.screens.pickups.category.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.scrapwala.R
import com.scrapwala.databinding.ItemCategoryBinding
import com.scrapwala.screens.pickups.category.model.CategoryResponse

class CategoryAdapter(
    var context: Context,
    var cityList: List<CategoryResponse.Data?>,
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
    fun filterList(filterlist: ArrayList<CategoryResponse.Data>,isFilterList:Boolean) {

        cityList = filterlist

        notifyDataSetChanged()

    this.isFilteredList=isFilterList
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        cityList.get(position)?.let { holder.bindData(it, position) }
    }

    override fun getItemCount(): Int {

        return cityList.size
    }

    inner class CategoryViewHolder(var itemViewBinding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root) {

        fun bindData(item: CategoryResponse.Data, position: Int) {

            itemViewBinding.root.setOnClickListener {
                clickedItemCallback.clickedItem(position, item)
            }





            if (item.category.isNullOrEmpty().not() && position == 0&&!isFilteredList) {

                itemViewBinding.txtCategoryName.visibility= View.VISIBLE
                itemViewBinding.txtCategoryName.setText(item.category)
            }


            else if (item.category.isNullOrEmpty().not() && position != 0&&item.category?.equals(cityList.get(position-1)?.category)!!.not()&&!isFilteredList ) {

                itemViewBinding.txtCategoryName.visibility= View.VISIBLE
                itemViewBinding.txtCategoryName.setText(item.category)
            }



            else{
                itemViewBinding.txtCategoryName.visibility= View.GONE

            }

            if (item.name.isNullOrEmpty().not()) {
                itemViewBinding.txtItemName.setText(item.name)
            }
            else{
                itemViewBinding.txtItemName.setText("")
            }


            if (item.price!=null ) {
                itemViewBinding.txtItemPrice.setText(""+item.price+"/"+item.weight)
            }
            else{
                itemViewBinding.txtItemPrice.setText("")
            }


        }

    }
}