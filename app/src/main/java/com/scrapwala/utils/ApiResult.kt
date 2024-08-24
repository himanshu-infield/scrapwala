package com.scrapwala.utils

import okhttp3.Headers


sealed interface ApiResult<T>


class ApiSuccess<T : Any>(val data: T, val headers: Headers) : ApiResult<T>
//class ApiError<T : Any>(val code: Int, val message: String?) : ApiResult<T>
class ApiError<T : Any>(val error: ErrorResponse) : ApiResult<T>


class FailureException<T : Any>(val e: String) : ApiResult<T>




//class ApiSuccess<T : Any>(val data: T, val headers: Headers) : ApiResult<T>
////class ApiError<T : Any>(val code: Int, val message: String?) : ApiResult<T>
//class ApiError<T:Any>(val errorResponse: ErrorResponse) : ApiResult<T>
//class ApiException<T : Any>(val e: Throwable) : ApiResult<T>


//class ApiSuccess<T : Any,E:Any?>(val data: T, val error: E ?= null) : ApiResult<T,E>
//class ApiError<T:Any, E:Any?>(val error: E, val data:T?= null) : ApiResult<T,E>
//class ApiException<T : Any, E:Any?>(val e: Throwable) : ApiResult<T,E>