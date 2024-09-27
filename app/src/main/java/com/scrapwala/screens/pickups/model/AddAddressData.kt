package com.scrapwala.screens.pickups.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AddAddressData(
    @SerializedName("user_id")
    var userId: String?="",
    @SerializedName("address_type")
    var addressType: String?="",
    @SerializedName("address_line_1")
    var address: String?= "",
    @SerializedName("address_line_2")
    var landmark: String?="",
    @SerializedName("pincode")
    var pincode: String?="" ,
    @SerializedName("city_id")
    var cityId: String?=""
)