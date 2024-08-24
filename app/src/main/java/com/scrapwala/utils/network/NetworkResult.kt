package com.scrapwala.utils.network

sealed class NetworkResult<T>(val data: T? = null, val errorBody: Any? = null) {

    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(errorBody: Any?, data: T? = null) : NetworkResult<T>(data, errorBody)
    class Loading<T> : NetworkResult<T>()

}