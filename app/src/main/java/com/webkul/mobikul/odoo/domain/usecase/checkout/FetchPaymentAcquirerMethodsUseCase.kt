package com.webkul.mobikul.odoo.domain.usecase.checkout

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.PaymentAcquirerMethodCheckoutEntity
import com.webkul.mobikul.odoo.domain.repository.PaymentAcquireRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FetchPaymentAcquirerMethodsUseCase @Inject constructor(
    private val paymentAcquireRepository: PaymentAcquireRepository
) {

    operator fun invoke(acquirerId: Int): Flow<Resource<PaymentAcquirerMethodCheckoutEntity>> = flow {

        emit(Resource.Loading)
        val result = paymentAcquireRepository.getPaymentAcquirerMethods(acquirerId)
        emit(result)

    }.flowOn(Dispatchers.IO)

}