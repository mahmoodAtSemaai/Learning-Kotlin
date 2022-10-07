package com.webkul.mobikul.odoo.domain.usecase.checkout

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.PaymentStatusEntity
import com.webkul.mobikul.odoo.domain.repository.PaymentStatusRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FetchTransactionStatusUseCase @Inject constructor(
        private val paymentTransactionStatusRepository: PaymentStatusRepository
) {
    operator fun invoke(orderId: Int): Flow<Resource<PaymentStatusEntity>> = flow {

        emit(Resource.Loading)
        val result = paymentTransactionStatusRepository.getPaymentTransactionStatus(orderId = orderId)
        emit(result)

    }.flowOn(Dispatchers.IO)
}