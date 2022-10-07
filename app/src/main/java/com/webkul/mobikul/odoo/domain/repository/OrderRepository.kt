package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.DiscountOrderPriceEntity
import com.webkul.mobikul.odoo.data.entity.OrderEntity
import com.webkul.mobikul.odoo.data.entity.OrderPlaceEntity
import com.webkul.mobikul.odoo.data.entity.OrderReviewEntity
import com.webkul.mobikul.odoo.model.request.OrderReviewRequest
import com.webkul.mobikul.odoo.model.request.PlaceOrderRequest
import java.util.ArrayList

interface OrderRepository : Repository<Any,Any,Any> {

    suspend fun getOrderDataAfterCheckout(orderID: Int, pointsRedeem: Boolean): Resource<OrderEntity>

    suspend fun getOrderData(orderID: Int, lineIds: ArrayList<Int>): Resource<OrderEntity>

    suspend fun getDiscountPrice(orderID: Int, lineIds: ArrayList<Int>): Resource<DiscountOrderPriceEntity>

    suspend fun getOrderReviewData(orderReviewRequest: OrderReviewRequest): Resource<OrderReviewEntity>

    suspend fun placeOrder(placeOrderRequest: PlaceOrderRequest): Resource<OrderPlaceEntity>
}