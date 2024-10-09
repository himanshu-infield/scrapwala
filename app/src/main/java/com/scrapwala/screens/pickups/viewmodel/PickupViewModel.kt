package com.scrapwala.screens.pickups.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.scrapwala.screens.pickups.model.AddAddressData
import com.scrapwala.screens.pickups.model.CityListResponse
import com.scrapwala.screens.pickups.model.CreateCategoryData
import com.scrapwala.screens.pickups.repository.PickupRepository
import com.scrapwala.utils.SingleEvent
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


    var category_id = 0
    var weight_id = 0

    var cityList: ArrayList<CityListResponse.Data> = ArrayList<CityListResponse.Data>()
    var selectedCityItem: CityListResponse.Data? = null
    private val _showDialog = MutableLiveData<Boolean>()
    val getDialog: LiveData<Boolean>
        get() = _showDialog
    private val _cityListResponse = MutableLiveData<Any>()
    val cityListResponse: LiveData<Any>
        get() = _cityListResponse
    public val statusMessage = MutableLiveData<SingleEvent<String>>()



    var responseCategory: MutableLiveData<Any> = MutableLiveData()
    var responseAddressList: MutableLiveData<Any> = MutableLiveData()
    var responseDeleteAddress: MutableLiveData<Any> = MutableLiveData()
    var responseSaveAddress: MutableLiveData<Any> = MutableLiveData()
    var responseCreatePickUp: MutableLiveData<Any> = MutableLiveData()
    var responsePickupsList: MutableLiveData<Any> = MutableLiveData()

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


    /**get address list**/
    fun getAddressList(id: Int) {
        viewModelScope.launch {
            val response = pickupRepository.getAddressList(id)
            response.onSuccess { data ->
                responseAddressList.value = data
                Log.d("APICALL", "optimisedApiCall: data $data")
            }.onError { error ->
                responseAddressList.value = error
                Log.d("APICALL", "optimisedApiCall: message $error")
            }.onFailureCallback {
                responseAddressList.value = it
            }
        }
    }

    /**delete address**/
    fun deleteAddress(id: Int) {
        viewModelScope.launch {
            val response = pickupRepository.deleteAddress(id)
            response.onSuccess { data ->
                responseDeleteAddress.value = data
                Log.d("APICALL", "optimisedApiCall: data $data")
            }.onError { error ->
                responseDeleteAddress.value = error
                Log.d("APICALL", "optimisedApiCall: message $error")
            }.onFailureCallback {
                responseDeleteAddress.value = it
            }
        }
    }


    /**get city list api**/
    fun getCityList() {
        _showDialog.value = true
        viewModelScope.launch {
            val response = pickupRepository.getCity()
            response.onSuccess { data ->
                _cityListResponse.value = data
                _showDialog.value = false
                Log.d("APICALL", "optimisedApiCall: data $data")
            }.onError { error ->
                _cityListResponse.value = error
                _showDialog.value = false
                Log.d("APICALL", "optimisedApiCall: message $error")
            }.onFailureCallback {
                _cityListResponse.value = it
                statusMessage.value= SingleEvent(it)
                _showDialog.value = false

            }
        }
    }


    /**save address**/
    fun saveAddress(request: AddAddressData) {
        viewModelScope.launch {
            val response = pickupRepository.saveAddress(request)
            response.onSuccess { data ->
                responseSaveAddress.value = data
                Log.d("APICALL", "optimisedApiCall: data $data")
            }.onError { error ->
                responseSaveAddress.value = error
                Log.d("APICALL", "optimisedApiCall: message $error")
            }.onFailureCallback {
                responseSaveAddress.value = it
            }
        }
    }


    /**create Category**/
    fun createCategory(request: CreateCategoryData) {
        viewModelScope.launch {
            val response = pickupRepository.createCategory(request)
            response.onSuccess { data ->
                responseCreatePickUp.value = data
                Log.d("APICALL", "optimisedApiCall: data $data")
            }.onError { error ->
                responseCreatePickUp.value = error
                Log.d("APICALL", "optimisedApiCall: message $error")
            }.onFailureCallback {
                responseCreatePickUp.value = it
            }
        }
    }


/**call in progress list api**/
    fun apiInProgressList(id: Int,status:Int) {
        viewModelScope.launch {
            val response = pickupRepository.apiInProgressList(id,status)
            response.onSuccess { data ->
                responsePickupsList.value = data
                Log.d("APICALL", "optimisedApiCall: data $data")
            }.onError { error ->
                responsePickupsList.value = error
                Log.d("APICALL", "optimisedApiCall: message $error")
            }.onFailureCallback {
                responsePickupsList.value = it
            }
        }
    }
}