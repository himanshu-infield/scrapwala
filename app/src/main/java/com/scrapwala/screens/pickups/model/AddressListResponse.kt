package com.scrapwala.screens.pickups.model


import com.google.gson.annotations.SerializedName

data class AddressListResponse(
    @SerializedName("data")
    var `data`: MutableList<Data?>?,
    @SerializedName("success")
    var success: Int?
){
    data class Data(
        @SerializedName("address_line_1")
        var addressLine1: String?,
        @SerializedName("address_line_2")
        var addressLine2: String?,
        @SerializedName("address_type")
        var addressType: String?,
        @SerializedName("city")
        var city: String?,
        @SerializedName("city_id")
        var cityId: Int?,
        @SerializedName("created_at")
        var createdAt: String?,
        @SerializedName("id")
        var id: Int?,
        @SerializedName("pincode")
        var pincode: String?,
        @SerializedName("user_id")
        var userId: Int?
    )
}