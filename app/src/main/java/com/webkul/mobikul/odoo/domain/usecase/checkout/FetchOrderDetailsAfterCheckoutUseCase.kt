package com.webkul.mobikul.odoo.domain.usecase.checkout

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.OrderEntity
import com.webkul.mobikul.odoo.domain.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.ArrayList
import javax.inject.Inject

class FetchOrderDetailsAfterCheckoutUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {

    operator fun invoke(cartId: Int, pointsRedeem: Boolean): Flow<Resource<OrderEntity>> = flow {

        emit(Resource.Loading)
        val result = orderRepository.getOrderDataAfterCheckout(cartId, pointsRedeem)
        emit(result)

    }.flowOn(Dispatchers.IO)

}