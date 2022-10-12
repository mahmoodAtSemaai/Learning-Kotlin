package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.DiscountOrderPriceEntity
import com.webkul.mobikul.odoo.data.entity.OrderEntity
import com.webkul.mobikul.odoo.data.entity.OrderPlaceEntity
import com.webkul.mobikul.odoo.data.entity.OrderReviewEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.OrderRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.OrderRepository
import com.webkul.mobikul.odoo.model.request.OrderReviewRequest
import com.webkul.mobikul.odoo.model.request.PlaceOrderRequest
import java.util.ArrayList
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val remoteDataSource: OrderRemoteDataSource
) : OrderRepository {

    override suspend fun getOrderDataAfterCheckout(
        orderID: Int,
        pointsRedeem: Boolean
    ): Resource<OrderEntity> {
        return remoteDataSource.getOrderDataAfterCheckout(orderID, pointsRedeem)
    }

    override suspend fun getOrderData(orderID: Int, lineIds: ArrayList<Int>): Resource<OrderEntity> {
        return remoteDataSource.getOrderData(orderID, lineIds)
    }

    override suspend fun getDiscountPrice(orderID: Int, lineIds: ArrayList<Int>): Resource<DiscountOrderPriceEntity> {
        return remoteDataSource.getDiscountPrice(orderID, lineIds)
    }

    override suspend fun getOrderReviewData(orderReviewRequest: OrderReviewRequest): Resource<OrderReviewEntity> {
        return remoteDataSource.getOrderReviewData(orderReviewRequest)
    }

    override suspend fun placeOrder(placeOrderRequest: PlaceOrderRequest): Resource<OrderPlaceEntity> {
        return remoteDataSource.placeOrder(placeOrderRequest)
    }
}