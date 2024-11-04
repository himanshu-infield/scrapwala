package com.scrapwala.screens.profile.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scrapwala.screens.login.model.SendOtpRequest
import com.scrapwala.screens.profile.repository.ProfileRepository
import com.scrapwala.utils.extensionclass.onError
import com.scrapwala.utils.extensionclass.onFailureCallback
import com.scrapwala.utils.extensionclass.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    var logOutResponse: MutableLiveData<Any> = MutableLiveData()
    var saveUserResponse: MutableLiveData<Any> = MutableLiveData()

    fun logoutRequest(request: SendOtpRequest) {
        viewModelScope.launch {
            val response = profileRepository.logout(request)
            response.onSuccess { data ->
                logOutResponse.value = data
                Log.d("APICALL", "optimisedApiCall: data $data")
            }.onError { error ->
                logOutResponse.value = error
                Log.d("APICALL", "optimisedApiCall: message $error")
            }.onFailureCallback {
                logOutResponse.value = it
            }
        }
    }




    fun saveUserRequest(
        token: String, body: HashMap<String, RequestBody>
    ) {
        viewModelScope.launch {
            val response = profileRepository.saveUser(token,body)
            response.onSuccess { data ->
                saveUserResponse.value = data
                Log.d("APICALL", "optimisedApiCall: data $data")
            }.onError { error ->
                saveUserResponse.value = error
                Log.d("APICALL", "optimisedApiCall: message $error")
            }.onFailureCallback {
                saveUserResponse.value = it
            }
        }
    }

    fun saveUserRequest(
        token: String,
        id: RequestBody,
        name : RequestBody,
        gender: RequestBody,
        email : RequestBody,
        mobile : RequestBody,
        city : RequestBody,  // Pass null if city is not provided
        image: MultipartBody.Part
    ) {
        viewModelScope.launch {
            val response = profileRepository.saveUserData(token, id, name, gender, email, mobile, city, image)
            response.onSuccess { data ->
                saveUserResponse.value = data
                Log.d("APICALL", "optimisedApiCall: data $data")
            }.onError { error ->
                saveUserResponse.value = error
                Log.d("APICALL", "optimisedApiCall: message $error")
            }.onFailureCallback {
                saveUserResponse.value = it
            }
        }
    }



}