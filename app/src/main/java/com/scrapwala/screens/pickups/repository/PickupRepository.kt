package com.scrapwala.screens.pickups.repository


import com.scrapwala.api.ApiService
import com.scrapwala.screens.pickups.category.model.CategoryResponse
import com.scrapwala.utils.ApiResult
import javax.inject.Inject

class PickupRepository @Inject constructor(private val apiService: ApiService) {


    suspend fun getCategory(): ApiResult<CategoryResponse> {
        return apiService.userGetCategory()
    }

}