package com.scrapwala.screens.pickups.model


import com.google.gson.annotations.SerializedName

data class InProgressListResponse(
    @SerializedName("data")
    var `data`: List<Data?>?,
    @SerializedName("success")
    var success: Int?
){
    data class Data(
        @SerializedName("addressId")
        var addressId: Int?,
        @SerializedName("address_line_1")
        var addressLine1: String?,
        @SerializedName("address_line_2")
        var addressLine2: String?,
        @SerializedName("address_type")
        var addressType: String?,
        @SerializedName("categoryId")
        var categoryId: Int?,
        @SerializedName("categoryName")
        var categoryName: String?,
        @SerializedName("city")
        var city: String?,
        @SerializedName("cityId")
        var cityId: Int?,
        @SerializedName("cityName")
        var cityName: String?,
        @SerializedName("created_at")
        var createdAt: String?,
        @SerializedName("date")
        var date: String?,
        @SerializedName("email")
        var email: String?,
        @SerializedName("gender")
        var gender: String?,
        @SerializedName("id")
        var id: Int?,
        @SerializedName("is_active")
        var isActive: Int?,
        @SerializedName("message")
        var message: String?,
        @SerializedName("mobile")
        var mobile: String?,
        @SerializedName("pincode")
        var pincode: String?,
        @SerializedName("status")
        var status: Int?,
        @SerializedName("time")
        var time: String?,
        @SerializedName("user_id")
        var userId: Int?,
        @SerializedName("userName")
        var userName: String?,
        @SerializedName("weight")
        var weight: Int?,
        @SerializedName("weightId")
        var weightId: Int?,
        @SerializedName("weightName")
        var weightName: String?
    )
}