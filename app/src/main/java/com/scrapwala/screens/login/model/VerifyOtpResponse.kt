package com.scrapwala.screens.login.model


import com.google.gson.annotations.SerializedName

data class VerifyOtpResponse(
    @SerializedName("success")
    var success: Int?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("token")
    var token: String?,
    @SerializedName("data")
    var `data`: Data?
) {
    data class Data(
        @SerializedName("id")
        var id: Int? =0,
        @SerializedName("name")
        var name: String?,
        @SerializedName("gender")
        var gender: String?,
        @SerializedName("otp")
        var otp: String?,
        @SerializedName("email")
        var email: String?,
        @SerializedName("mobile")
        var mobile: String?,
        @SerializedName("city")
        var city: String?,
        @SerializedName("image")
        var image: String?,
        @SerializedName("userType")
        var userType: String?,
        @SerializedName("referralCode")
        var referralCode: String?,
        @SerializedName("rewardPoint")
        var rewardPoint: String?
    )
}