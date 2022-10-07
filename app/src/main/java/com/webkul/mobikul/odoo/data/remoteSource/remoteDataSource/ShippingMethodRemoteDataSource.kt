package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.ShippingMethodListEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.ShippingMethodServices
import com.webkul.mobikul.odoo.model.checkout.UpdateOrderRequest
import javax.inject.Inject

class ShippingMethodRemoteDataSource @Inject constructor(
    private val apiService: ShippingMethodServices,
    gson: Gson,
    appPreferences: AppPreferences
) : BaseRemoteDataSource(gson, appPreferences) {

    suspend fun updateShippingMethodForOrder(orderId: Int, updateOrderRequest: UpdateOrderRequest?) = safeApiCall(
        Any::class.java) {
        apiService.updateShippingMethodForOrder(orderId, updateOrderRequest.toString())
    }


    suspend fun getActiveShippingMethods(partnerId: String) = safeApiCall(
        ShippingMethodListEntity::class.java) {
        apiService.getActiveShippingMethods(partnerId)
    }

}