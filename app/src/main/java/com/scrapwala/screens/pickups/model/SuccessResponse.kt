package com.scrapwala.screens.pickups.model


import com.google.gson.annotations.SerializedName

data class SuccessResponse(
    @SerializedName("data")
    var `data`: Any,
    @SerializedName("success")
    var success: Int?,
     @SerializedName("message")
    var message: String?

)