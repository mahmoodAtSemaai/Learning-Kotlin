package com.webkul.mobikul.odoo.domain.usecase.checkout

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.OrderPlaceEntity
import com.webkul.mobikul.odoo.domain.repository.OrderRepository
import com.webkul.mobikul.odoo.model.request.PlaceOrderRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PlaceOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {

    operator fun invoke(placeOrderRequest: PlaceOrderRequest): Flow<Resource<OrderPlaceEntity>> = flow {

        emit(Resource.Loading)
        val result = orderRepository.placeOrder(placeOrderRequest)
        emit(result)

    }.flowOn(Dispatchers.IO)

}