package com.scrapwala.screens.pickups.adapter

import com.scrapwala.screens.pickups.model.AddressData

interface SelectAddressCallback {

    fun selectedAddress(item:AddressData)

    fun deleteAddress(item:AddressData)
}