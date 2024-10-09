package com.scrapwala.screens.home.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scrapwala.screens.home.repository.DashboardRepository
import com.scrapwala.screens.login.repository.LoginRepository
import com.scrapwala.screens.pickups.repository.PickupRepository
import com.scrapwala.utils.extensionclass.onError
import com.scrapwala.utils.extensionclass.onFailureCallback
import com.scrapwala.utils.extensionclass.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dashRepository: DashboardRepository,
) : ViewModel() {

    var bannerResponse: MutableLiveData<Any> = MutableLiveData()

    fun getBanners() {
        viewModelScope.launch {
            val response = dashRepository.getBanners()
            response.onSuccess { data ->
                bannerResponse.value = data
                Log.d("APICALL", "optimisedApiCall: data $data")
            }.onError { error ->
                bannerResponse.value = error
                Log.d("APICALL", "optimisedApiCall: message $error")
            }.onFailureCallback {
                bannerResponse.value = it
            }
        }
    }








}