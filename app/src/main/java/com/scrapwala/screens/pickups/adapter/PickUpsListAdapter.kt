package com.scrapwala.screens.pickups.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scrapwala.databinding.LayoutUpcomingBinding
import com.scrapwala.screens.pickups.model.InProgressListResponse
import java.text.SimpleDateFormat
import java.util.Locale


class PickUpsListAdapter(
    private val context: Context,
    private val mData: List<Any?>,
    private val listener: PickupListener
) : RecyclerView.Adapter<PickUpsListAdapter.ViewHolder>() {

    class ViewHolder(val binding: LayoutUpcomingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = LayoutUpcomingBinding.inflate(inflater, viewGroup, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        when (val item = mData[position]) {
            is InProgressListResponse.Data -> {
                viewHolder.binding.success.visibility = View.GONE
                viewHolder.binding.rlEdit.visibility = View.VISIBLE


                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val dayFormat = SimpleDateFormat("dd", Locale.getDefault())
                val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())
                val date = inputFormat.parse(item.date)
                val day = dayFormat.format(date)
                val month = monthFormat.format(date).uppercase()
                println("Day: $day")   // Output: 30
                println("Month: $month") // Output: OCT
                viewHolder.binding.tvMonth.text = ""+month
                viewHolder.binding.tvDate.text = ""+day

                viewHolder.binding.tvaddressType.text = ""+item.addressType

                var address = StringBuilder("")
                if (item.addressLine1.isNullOrEmpty().not()){
                    address.append(item?.addressLine1)
                }
                if (item.addressLine2.isNullOrEmpty().not()){
                    if (address.isNotEmpty()) {
                        address.append(", ")
                    }
                    address.append(""+item?.addressLine2)
                }

                viewHolder.binding.tvAddressLine1.text = address
                viewHolder.binding.tvAddressPin.text = item.pincode



                viewHolder.binding.llEdit.setOnClickListener {
                    listener.onItemSelected(item, position)
                }

            }
//            is InProgressListResponse -> {
//
//
//            }
        }


    }

    override fun getItemCount(): Int {
        return mData?.size ?: 0
    }

    interface PickupListener {
        fun onItemSelected(item: Any?, position: Int)
    }
}
