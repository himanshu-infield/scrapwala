package com.scrapwala.screens.pickups.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.scrapwala.R
import com.scrapwala.databinding.ItemAddressBinding
import com.scrapwala.screens.pickups.category.adapter.ClickedItemCallback
import com.scrapwala.screens.pickups.model.AddressData

class SelectAddressAdapter(
    var context: Context,
    var cityList: ArrayList<AddressData>,
    var clickedItemCallback: SelectAddressCallback
) : RecyclerView.Adapter<SelectAddressAdapter.AddressViewHolder>() {



    var isFilteredList:Boolean=false
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddressViewHolder {

        val binding =
            DataBindingUtil.inflate<ItemAddressBinding>(
                LayoutInflater.from(parent.context), R.layout.item_address, parent, false
            )


        return AddressViewHolder(binding)
    }

    //    // method for filtering our recyclerview items.
    fun filterList(filterlist: ArrayList<AddressData>, isFilterList:Boolean) {

        cityList = filterlist

        notifyDataSetChanged()

        this.isFilteredList=isFilterList
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {

        holder.bindData(cityList.get(position), position)
    }

    override fun getItemCount(): Int {

        return cityList.size
    }

    inner class AddressViewHolder(var itemViewBinding: ItemAddressBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root) {

        fun bindData(item: AddressData, position: Int) {

            itemViewBinding.root.setOnClickListener {
                clickedItemCallback.selectedAddress( item)
            }




            itemViewBinding.imgDelete.setOnClickListener {


                cityList.remove(item)
                notifyDataSetChanged()
                clickedItemCallback.deleteAddress(item)
            }

            if (item.addressTag.isNullOrEmpty().not()) {

                itemViewBinding.txtAddressTag.setText(item.addressTag)

            }


            else{
                itemViewBinding.txtAddressTag.setText("")

            }

            if (item.fullAddress.isNullOrEmpty().not()) {
                itemViewBinding.txtFullAddress.setText(item.fullAddress)
            }
            else{
                itemViewBinding.txtFullAddress.setText("")
            }


            if (item.addressPincode.isNullOrEmpty().not() ) {
                itemViewBinding.txtPincode.setText(item.addressPincode)
            }
            else{
                itemViewBinding.txtPincode.setText("")
            }


        }

    }
}