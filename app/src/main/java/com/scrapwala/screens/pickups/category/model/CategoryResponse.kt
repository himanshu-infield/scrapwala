package com.scrapwala.screens.pickups.category.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CategoryResponse(
    @SerializedName("data")
    var `data`: List<Data?>?,
    @SerializedName("success")
    var success: Int?
){
    data class Data(
        @SerializedName("category")
        var category: String?,
        @SerializedName("created_at")
        var createdAt: String?,
        @SerializedName("id")
        var id: Int?,
        @SerializedName("image")
        var image: String?,
        @SerializedName("is_active")
        var isActive: Int?,
        @SerializedName("label")
        var label: String?,
        @SerializedName("name")
        var name: String?,
        @SerializedName("parent_id")
        var parentId: Int?,
        @SerializedName("price")
        var price: Int?,
        @SerializedName("weight")
        var weight: String?,
        @SerializedName("weight_id")
        var weightId: Int?
    ): Serializable
}
