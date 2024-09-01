package com.scrapwala.screens.onboarding.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.scrapwala.R
import com.scrapwala.screens.onboarding.model.LoginCarousal

class LoginCarousalAdapter (
    internal var carousalList: ArrayList<LoginCarousal>
) : PagerAdapter() {
    override fun getCount(): Int {
        return carousalList.size
    }


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`

    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = LayoutInflater.from(container.context).inflate(R.layout.login_carousal, container, false)
        itemView.findViewById<TextView>(R.id.txtDescription).setText(carousalList.get(position).carousalHeading!!)


        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}
