package com.scrapwala.screens.login.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scrapwala.screens.login.repository.LoginRepository
import com.scrapwala.screens.pickups.repository.PickupRepository
import com.scrapwala.utils.extensionclass.onError
import com.scrapwala.utils.extensionclass.onFailureCallback
import com.scrapwala.utils.extensionclass.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val pickupRepository: LoginRepository,
) : ViewModel() {

    var sendOtpResponse: MutableLiveData<Any> = MutableLiveData()
    var verifyOtpResponse: MutableLiveData<Any> = MutableLiveData()

    fun sendOtpRequest(request: SendOtpRequest) {
        viewModelScope.launch {
            val response = pickupRepository.sendOtp(request)
            response.onSuccess { data ->
                sendOtpResponse.value = data
                Log.d("APICALL", "optimisedApiCall: data $data")
            }.onError { error ->
                sendOtpResponse.value = error
                Log.d("APICALL", "optimisedApiCall: message $error")
            }.onFailureCallback {
                sendOtpResponse.value = it
            }
        }
    }






    fun verifyOtpRequest(request: VerifyOtpRequest) {
        viewModelScope.launch {
            val response = pickupRepository.verifyOtp(request)
            response.onSuccess { data ->
                verifyOtpResponse.value = data
                Log.d("APICALL", "optimisedApiCall: data $data")
            }.onError { error ->
                verifyOtpResponse.value = error
                Log.d("APICALL", "optimisedApiCall: message $error")
            }.onFailureCallback {
                verifyOtpResponse.value = it
            }
        }
    }


}