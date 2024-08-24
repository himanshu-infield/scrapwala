package com.scrapwala.utils

import com.google.gson.JsonObject
import org.json.JSONObject

data class ErrorResponse(
    val statusCode: Int,
    val message: String="",
    var data: Any?
)



