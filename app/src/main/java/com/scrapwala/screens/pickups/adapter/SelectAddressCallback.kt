package com.scrapwala.screens.pickups.adapter

import com.scrapwala.screens.pickups.model.AddressListResponse

interface SelectAddressCallback {

    fun selectedAddress(item:AddressListResponse.Data)

    fun deleteAddress(item:AddressListResponse.Data)
}