package com.scrapwala.screens.pickups.repository


import com.scrapwala.api.ApiService
import com.scrapwala.screens.pickups.category.model.CategoryResponse
import com.scrapwala.screens.pickups.model.AddAddressData
import com.scrapwala.screens.pickups.model.AddressListResponse
import com.scrapwala.screens.pickups.model.CityListResponse
import com.scrapwala.screens.pickups.model.CreateCategoryData
import com.scrapwala.screens.pickups.model.InProgressListResponse
import com.scrapwala.screens.pickups.model.SuccessResponse
import com.scrapwala.utils.ApiResult
import javax.inject.Inject

class PickupRepository @Inject constructor(private val apiService: ApiService) {


    suspend fun getCategory(): ApiResult<CategoryResponse> {
        return apiService.userGetCategory()
    }


    suspend fun getAddressList(id:Int): ApiResult<AddressListResponse> {
        return apiService.getAddressList(id)
    }

    suspend fun deleteAddress(id:Int): ApiResult<SuccessResponse> {
        return apiService.deleteAddress(id)
    }

    suspend fun getCity(): ApiResult<CityListResponse> {
        return apiService.getCityList()
    }

    suspend fun saveAddress(leadRequest:AddAddressData): ApiResult<SuccessResponse> {
        return apiService.saveAddress(leadRequest)
    }

    suspend fun createCategory(leadRequest: CreateCategoryData): ApiResult<SuccessResponse> {
        return apiService.createCategory(leadRequest)
    }


    suspend fun apiInProgressList(id:Int): ApiResult<InProgressListResponse> {
        return apiService.pickupList(id)
    }

}