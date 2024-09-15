package com.scrapwala.screens.pickups.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AddressData(
    @SerializedName("addressPincode")
    var addressPincode: String?="",
    @SerializedName("fullAddress")
    var fullAddress: String?="",
    @SerializedName("addressTag")
    var addressTag: String?= "",
    @SerializedName("addressLandmark")
    var addressLandmark: String?="",
): Serializable