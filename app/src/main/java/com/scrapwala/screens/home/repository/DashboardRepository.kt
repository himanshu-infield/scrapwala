package com.scrapwala.screens.home.repository

import com.scrapwala.api.ApiService
import com.scrapwala.screens.home.model.BannerResponse
import com.scrapwala.screens.login.model.SendOtpRequest
import com.scrapwala.screens.login.model.VerifyOtpRequest
import com.scrapwala.screens.login.model.VerifyOtpResponse
import com.scrapwala.screens.pickups.category.model.CategoryResponse
import com.scrapwala.screens.pickups.model.SuccessResponse
import com.scrapwala.utils.ApiResult
import javax.inject.Inject

class DashboardRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getBanners(): ApiResult<BannerResponse> {
        return apiService.getBanners()
    }




}