package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.CartEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.CartServices
import com.webkul.mobikul.odoo.data.request.CartProductsRequest
import javax.inject.Inject

class CartRemoteDataSource @Inject constructor(
    private val apiService: CartServices,
    gson: Gson,
    appPreferences: AppPreferences
) : BaseRemoteDataSource(gson, appPreferences) {

    suspend fun get(partnerId: Int) = safeApiCall(CartEntity::class.java) {
        apiService.getCart(partnerId)
    }

    suspend fun create(partnerId: Int) = safeApiCall(CartEntity::class.java) {
        apiService.createCart(partnerId)
    }

    suspend fun update(cartProductsRequest: CartProductsRequest) =
        safeApiCall(CartEntity::class.java) {
            apiService.addToCart(cartProductsRequest.cartId, cartProductsRequest.getRequestBody())
        }

}