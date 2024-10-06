package com.scrapwala.screens.pickups.model


import com.google.gson.annotations.SerializedName

data class CityListResponse(
    @SerializedName("data")
    var `data`: List<Data?>?,
    @SerializedName("success")
    var success: Int?
){
    data class Data(
        @SerializedName("created_at")
        var createdAt: String?,
        @SerializedName("id")
        var id: Int?,
        @SerializedName("is_active")
        var isActive: Int?,
        @SerializedName("name")
        var name: String?
    )
}