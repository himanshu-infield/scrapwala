package com.scrapwala.screens.pickups.category.adapter

import com.scrapwala.screens.pickups.category.model.CategoryResponse

interface ClickedItemCallback {

    fun clickedItem(position: Int, item: CategoryResponse.Data)
}