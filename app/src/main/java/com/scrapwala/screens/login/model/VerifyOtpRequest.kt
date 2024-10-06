package com.scrapwala.screens.login.model

import com.google.gson.annotations.SerializedName

data class VerifyOtpRequest(
    @SerializedName("mobile")
    var mobile: String?="",


    @SerializedName("otp")
    var otp: String?="",

    )