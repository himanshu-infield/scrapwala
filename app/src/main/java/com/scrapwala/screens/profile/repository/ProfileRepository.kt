package com.scrapwala.screens.login.repository

import com.scrapwala.api.ApiService
import com.scrapwala.screens.login.model.SendOtpRequest
import com.scrapwala.screens.login.model.VerifyOtpRequest
import com.scrapwala.screens.login.model.VerifyOtpResponse
import com.scrapwala.screens.pickups.model.SuccessResponse
import com.scrapwala.utils.ApiResult
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun logout(request:SendOtpRequest): ApiResult<SuccessResponse> {
        return apiService.logOut(request)
    }




}