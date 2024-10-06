package com.scrapwala.screens.pickups.clickinterface

import com.scrapwala.screens.pickups.category.model.CategoryResponse

interface ClickedItemCallback {

    fun clickedItem(position: Int, item: CategoryResponse.Data)
}