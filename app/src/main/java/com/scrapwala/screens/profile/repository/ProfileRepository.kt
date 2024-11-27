package com.scrapwala.screens.profile.repository

import com.scrapwala.api.ApiService
import com.scrapwala.screens.login.model.SendOtpRequest
import com.scrapwala.screens.login.model.VerifyOtpRequest
import com.scrapwala.screens.login.model.VerifyOtpResponse
import com.scrapwala.screens.pickups.model.SuccessResponse
import com.scrapwala.screens.profile.model.UpdateProfileResponse
import com.scrapwala.utils.ApiResult
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun logout(request:SendOtpRequest): ApiResult<SuccessResponse> {
        return apiService.logOut(request)
    }


    suspend fun saveUser(
        token: String, body: HashMap<String, RequestBody>
    ): ApiResult<VerifyOtpResponse> {
        return apiService.saveUser(token,body)
    }


 suspend fun saveUserData(
     token: String,
     id: RequestBody,
     name : RequestBody,
     gender: RequestBody,
     email : RequestBody,
     mobile : RequestBody,
     city : RequestBody,  // Pass null if city is not provided
     image: MultipartBody.Part
 ): ApiResult<UpdateProfileResponse> {
        return apiService.saveUserData(token, id, name, gender, email, mobile, city, image)
    }







}