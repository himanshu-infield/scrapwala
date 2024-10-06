package com.scrapwala.screens.login.model

import com.google.gson.annotations.SerializedName

data class SendOtpRequest(
    @SerializedName("mobile")
    var mobile: String?="",

)