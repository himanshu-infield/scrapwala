package com.scrapwala.screens.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scrapwala.R
import com.scrapwala.screens.home.model.BannerResponse

class ViewPagerAdapter(var context:FragmentActivity,private val items: List<String>?) : RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       // val textView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewpager_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       // holder.textView.text = items[position]

//        Glide.with(context).load(items!![position].url).into( holder.itemView.findViewById<ImageView>(R.id.imgBannerImage))


    }

    override fun getItemCount(): Int = items!!.size
}
