package com.webkul.mobikul.odoo.domain.usecase.checkout

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.PaymentTransferInstructionEntity
import com.webkul.mobikul.odoo.domain.repository.PaymentTransferInstructionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FetchTransferInstructionUseCase @Inject constructor(
        private val transferInstructionRepository: PaymentTransferInstructionRepository
) {
    operator fun invoke(bankId: Int): Flow<Resource<PaymentTransferInstructionEntity>> = flow {

        emit(Resource.Loading)
        val result = transferInstructionRepository.getPaymentTransferInstruction(bankId)
        emit(result)

    }.flowOn(Dispatchers.IO)

}