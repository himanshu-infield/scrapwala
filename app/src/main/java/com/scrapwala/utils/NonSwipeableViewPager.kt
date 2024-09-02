package com.scrapwala.utils

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NonSwipeableViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {

    // Flag to control whether swiping is enabled
    private var isPagingEnabled = false

    // Override this method to intercept touch events
    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        // If paging is enabled, allow swipe gestures, otherwise intercept them
        return isPagingEnabled && super.onInterceptTouchEvent(event)
    }

    // Override this method to handle touch events
    override fun onTouchEvent(event: MotionEvent): Boolean {
        // If paging is enabled, handle touch events, otherwise ignore them
        return isPagingEnabled && super.onTouchEvent(event)
    }

    // Method to enable or disable swiping
    fun setPagingEnabled(enabled: Boolean) {
        isPagingEnabled = enabled
    }
}
