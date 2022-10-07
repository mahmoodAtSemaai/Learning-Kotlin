package com.webkul.mobikul.odoo.domain.usecase.checkout

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.OrderReviewEntity
import com.webkul.mobikul.odoo.domain.repository.OrderRepository
import com.webkul.mobikul.odoo.model.request.OrderReviewRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FetchOrderReviewDataUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {

    operator fun invoke(orderReviewRequest: OrderReviewRequest): Flow<Resource<OrderReviewEntity>> = flow {

        emit(Resource.Loading)
        val result = orderRepository.getOrderReviewData(orderReviewRequest)
        emit(result)

    }.flowOn(Dispatchers.IO)

}