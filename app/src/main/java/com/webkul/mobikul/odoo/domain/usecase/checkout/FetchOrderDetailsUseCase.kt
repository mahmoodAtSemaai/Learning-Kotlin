package com.webkul.mobikul.odoo.domain.usecase.checkout

import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.OrderEntity
import com.webkul.mobikul.odoo.domain.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.ArrayList
import javax.inject.Inject

class FetchOrderDetailsUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {

    operator fun invoke(cartId: Int, pointsRedeem: Boolean, lineIds: ArrayList<Int>): Flow<Resource<OrderEntity>> = flow {

        emit(Resource.Loading)
        if (pointsRedeem) {
            val orderDetails = orderRepository.getOrderData(cartId, lineIds)
            val discountPrice = orderRepository.getDiscountPrice(cartId, lineIds)
            if (orderDetails is Resource.Success && discountPrice is Resource.Success) {
                orderDetails.value.grandTotal = discountPrice.value.discountPrice
                orderDetails.value.pointsRedeemed = discountPrice.value.pointsRedeemed.toString()
                emit(orderDetails)
            } else {
               emit(Resource.Failure(FailureStatus.API_FAIL))
            }
        } else {
            val result = orderRepository.getOrderData(cartId, lineIds)
            emit(result)
        }

    }.flowOn(Dispatchers.IO)

}