package com.scrapwala.screens.home.model


import com.google.gson.annotations.SerializedName

data class BannerResponse(
    @SerializedName("success")
    var success: Int?,
    @SerializedName("data")
    var `data`: List<Data>?
) {
    data class Data(
        @SerializedName("id")
        var id: Int?,
        @SerializedName("name")
        var name: String?,
        @SerializedName("image")
        var image: String?,
        @SerializedName("url")
        var url: String?,
        @SerializedName("is_active")
        var isActive: Int?,
        @SerializedName("created_at")
        var createdAt: String?
    )
}