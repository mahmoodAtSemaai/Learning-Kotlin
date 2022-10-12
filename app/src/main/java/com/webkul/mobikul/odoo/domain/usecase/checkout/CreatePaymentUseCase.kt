package com.webkul.mobikul.odoo.domain.usecase.checkout

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.DistrictListEntity
import com.webkul.mobikul.odoo.data.entity.PaymentTransactionEntity
import com.webkul.mobikul.odoo.domain.repository.PaymentProcessingRepository
import com.webkul.mobikul.odoo.model.payments.CreateVirtualAccountPaymentRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CreatePaymentUseCase @Inject constructor(
    private val paymentProcessingRepository: PaymentProcessingRepository
) {

    operator fun invoke(createVirtualAccountPaymentRequest: CreateVirtualAccountPaymentRequest): Flow<Resource<PaymentTransactionEntity>> = flow {

        emit(Resource.Loading)
        val result = paymentProcessingRepository.createPayment(createVirtualAccountPaymentRequest)

        emit(result)

    }.flowOn(Dispatchers.IO)

}