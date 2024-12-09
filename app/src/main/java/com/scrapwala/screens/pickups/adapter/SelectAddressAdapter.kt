package com.scrapwala.screens.pickups.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.scrapwala.R
import com.scrapwala.databinding.ItemAddressBinding
import com.scrapwala.screens.pickups.model.AddressListResponse


class SelectAddressAdapter(
    var context: Context,
    var cityList: MutableList<AddressListResponse.Data?>,
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
    fun filterList(filterlist: MutableList<AddressListResponse.Data?>, isFilterList:Boolean) {

        cityList = filterlist

        notifyDataSetChanged()

        this.isFilteredList=isFilterList
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {

        holder.bindData(cityList.get(position)!!, position)
    }

    override fun getItemCount(): Int {

        return cityList.size
    }



    inner class AddressViewHolder(var itemViewBinding: ItemAddressBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root) {

        fun bindData(item: AddressListResponse.Data, position: Int) {

            itemViewBinding.root.setOnClickListener {
                clickedItemCallback.selectedAddress( item)
            }




            itemViewBinding.imgDelete.setOnClickListener {


                showYesNoDialog(item)


            }

            if (item.addressType.isNullOrEmpty().not()) {

                itemViewBinding.txtAddressTag.setText(item.addressType)

            }


            else{
                itemViewBinding.txtAddressTag.setText("")

            }

            if (item.addressLine1.isNullOrEmpty().not()) {
                itemViewBinding.txtFullAddress.setText(item.addressLine1+" "+item.addressLine2)
            }
            else{
                itemViewBinding.txtFullAddress.setText("")
            }


            if (item.pincode.isNullOrEmpty().not() ) {
                itemViewBinding.txtPincode.setText(item.pincode)
            }
            else{
                itemViewBinding.txtPincode.setText("")
            }


        }

    }

    fun removeItem(item: AddressListResponse.Data){
        cityList.remove(item)
        notifyDataSetChanged()
    }


    private fun showYesNoDialog(item: AddressListResponse.Data) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirmation")
        builder.setMessage("Do you want to proceed?")

        // Set Yes button action
        builder.setPositiveButton("Yes") { dialog, _ ->
            // Handle Yes button action

            clickedItemCallback.deleteAddress(item)
            dialog.dismiss()
        }

        // Set No button action
        builder.setNegativeButton("No") { dialog, _ ->
            // Handle No button action
            dialog.dismiss()
        }

        // Create and show the dialog
        val dialog = builder.create()
        dialog.show()
    }

}