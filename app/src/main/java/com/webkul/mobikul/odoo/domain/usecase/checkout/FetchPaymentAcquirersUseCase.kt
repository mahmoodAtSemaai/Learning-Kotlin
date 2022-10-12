package com.webkul.mobikul.odoo.domain.usecase.checkout

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.PaymentAcquireCheckoutEntity
import com.webkul.mobikul.odoo.domain.repository.PaymentAcquireRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FetchPaymentAcquirersUseCase @Inject constructor(
    private val paymentAcquireRepository: PaymentAcquireRepository
) {

    operator fun invoke(companyId: Int): Flow<Resource<PaymentAcquireCheckoutEntity>> = flow {

        emit(Resource.Loading)
        val result = paymentAcquireRepository.getPaymentAcquirers(companyId)
        emit(result)

    }.flowOn(Dispatchers.IO)

}