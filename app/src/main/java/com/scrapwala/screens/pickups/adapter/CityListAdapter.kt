package com.scrapwala.screens.pickups.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.scrapwala.R
import com.scrapwala.databinding.ItemGenericsearchBinding
import com.scrapwala.screens.pickups.model.CityListResponse

class CityListAdapter(
    var context:Context,
    var cityList: List<CityListResponse.Data>,
    var clickedItemCallback:ClickedCityItemCallback
) : RecyclerView.Adapter<CityListAdapter.CountryViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CountryViewHolder {

        val binding =
            DataBindingUtil.inflate<ItemGenericsearchBinding>(
                LayoutInflater.from(parent.context), R.layout.item_genericsearch, parent, false
            )


        return CountryViewHolder(binding)
    }

    // method for filtering our recyclerview items.
    fun filterList(filterlist: ArrayList<CityListResponse.Data>) {

        cityList = filterlist

        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {

        holder.bindData(cityList.get(position),position)
    }

    override fun getItemCount(): Int {

        return cityList.size
    }

    inner class CountryViewHolder(var itemViewBinding: ItemGenericsearchBinding) : RecyclerView.ViewHolder(itemViewBinding.root) {

        fun bindData(item: CityListResponse.Data, position: Int) {

            itemViewBinding.root.setOnClickListener {
                clickedItemCallback.clickedItem(position,item)
            }


            if(item.name!=null){
                itemViewBinding.itemName.setText(item.name)
            }



        }

    }
}