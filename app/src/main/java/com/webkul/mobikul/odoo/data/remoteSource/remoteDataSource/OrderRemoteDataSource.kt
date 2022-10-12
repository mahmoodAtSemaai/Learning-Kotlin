package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.*
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.OrderServices
import com.webkul.mobikul.odoo.model.request.BaseLazyRequest
import com.webkul.mobikul.odoo.model.request.OrderReviewRequest
import com.webkul.mobikul.odoo.model.request.PlaceOrderRequest
import java.util.ArrayList
import javax.inject.Inject

class OrderRemoteDataSource @Inject constructor(
    private val apiService: OrderServices,
    gson: Gson,
    appPreferences: AppPreferences
) : BaseRemoteDataSource(gson, appPreferences) {

    suspend fun getOrderData(cartId: Int, lineIds: ArrayList<Int>) = safeApiCall(OrderEntity::class.java) {
        apiService.getOrderData(cartId, lineIds.toString().replace(" ", ""))
    }

    suspend fun getDiscountPrice(cartId: Int, lineIds: ArrayList<Int>) = safeApiCall(
        DiscountOrderPriceEntity::class.java) {
        apiService.getDiscountPrice(cartId, lineIds.toString().replace(" ", ""))
    }

    suspend fun getOrderDataAfterCheckout(orderID: Int, pointsRedeem: Boolean) = safeApiCall(OrderEntity::class.java) {
        apiService.getOrderDataAfterCheckout(orderID, pointsRedeem)
    }

    suspend fun getOrderReviewData(orderReviewRequest: OrderReviewRequest) = safeApiCall(OrderReviewEntity::class.java) {
        apiService.getOrderReviewData(orderReviewRequest.toString())
    }

    suspend fun placeOrder(placeOrderRequest: PlaceOrderRequest) = safeApiCall(OrderPlaceEntity::class.java) {
        apiService.placeOrder(placeOrderRequest.toString())
    }

}