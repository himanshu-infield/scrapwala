package com.scrapwala.screens.profile.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scrapwala.screens.login.model.SendOtpRequest
import com.scrapwala.screens.login.model.VerifyOtpRequest
import com.scrapwala.screens.login.repository.LoginRepository
import com.scrapwala.screens.login.repository.ProfileRepository
import com.scrapwala.screens.pickups.repository.PickupRepository
import com.scrapwala.utils.extensionclass.onError
import com.scrapwala.utils.extensionclass.onFailureCallback
import com.scrapwala.utils.extensionclass.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    var logOutResponse: MutableLiveData<Any> = MutableLiveData()

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








}