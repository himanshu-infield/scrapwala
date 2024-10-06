package com.scrapwala.screens.pickups.category.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CategoryData(
    @SerializedName("categoryName")
    var categoryName: String?="",
    @SerializedName("itemName")
    var itemName: String?="",
    @SerializedName("itemIcon")
    var itemIcon: String?="",


    @SerializedName("itemPrice")
    var itemPrice: String?="",
): Serializable