package com.scrapwala.utils.network

data class BaseResponse(
    val statusCode: Int,
    val message: String="",
    var data: Any?
)
