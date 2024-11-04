package com.scrapwala.screens.profile.model


import com.google.gson.annotations.SerializedName

data class UpdateProfileResponse(
    @SerializedName("success")
    var success: Int?,
     @SerializedName("message")
    var message: String?

)