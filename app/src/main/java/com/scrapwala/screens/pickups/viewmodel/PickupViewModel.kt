package com.scrapwala.screens.pickups.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.scrapwala.screens.pickups.repository.PickupRepository
import com.scrapwala.utils.extensionclass.onError
import com.scrapwala.utils.extensionclass.onFailureCallback
import com.scrapwala.utils.extensionclass.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PickupViewModel @Inject constructor(
    private val pickupRepository: PickupRepository,
) : ViewModel() {

    var responseCategory: MutableLiveData<Any> = MutableLiveData()

    fun getCategoryList() {
        viewModelScope.launch {
            val response = pickupRepository.getCategory()
            response.onSuccess { data ->
                responseCategory.value = data
                Log.d("APICALL", "optimisedApiCall: data $data")
            }.onError { error ->
                responseCategory.value = error
                Log.d("APICALL", "optimisedApiCall: message $error")
            }.onFailureCallback {
                responseCategory.value = it
            }
        }
    }
}