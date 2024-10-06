package com.scrapwala.screens.login.repository

import com.scrapwala.api.ApiService
import com.scrapwala.screens.login.model.SendOtpRequest
import com.scrapwala.screens.login.model.VerifyOtpRequest
import com.scrapwala.screens.login.model.VerifyOtpResponse
import com.scrapwala.screens.pickups.category.model.CategoryResponse
import com.scrapwala.screens.pickups.model.SuccessResponse
import com.scrapwala.utils.ApiResult
import javax.inject.Inject

class LoginRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun sendOtp(request:SendOtpRequest): ApiResult<SuccessResponse> {
        return apiService.sendOtp(request)
    }



    suspend fun verifyOtp(request:VerifyOtpRequest): ApiResult<VerifyOtpResponse> {
        return apiService.verifyOtp(request)
    }

}