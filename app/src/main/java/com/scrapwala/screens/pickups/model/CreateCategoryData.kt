package com.scrapwala.screens.pickups.model


import com.google.gson.annotations.SerializedName

data class CreateCategoryData(
    @SerializedName("user_id")
    var userId: String?,
    @SerializedName("category_id")
    var categoryId: String? = "",
    @SerializedName("weight")
    var weight: String?,
    @SerializedName("weight_id")
    var weightId: String? = "",
    @SerializedName("address_id")
    var addressId: String?,
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("date")
    var date: String?,
    @SerializedName("time")
    var time: String?,
    @SerializedName("pickup_id")
    var pickupId: String? = ""
)