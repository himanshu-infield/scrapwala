package com.scrapwala.screens.pickups.model

data class WasteTypeCategoryModel(
    var selectedCategory: String = "",
    var edtWeight: String = "",

    var weightUnt: String = "Kg",

    var weightId: Int = 0,
    var categoryId: Int = 0,
    var sliderValue: Float = 10F
)

