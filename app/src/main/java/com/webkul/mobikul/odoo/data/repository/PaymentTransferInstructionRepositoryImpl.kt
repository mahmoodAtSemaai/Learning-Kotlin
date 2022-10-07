package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.PaymentTransferInstructionEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.PaymentTransferInstructionRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.PaymentTransferInstructionRepository
import javax.inject.Inject

class PaymentTransferInstructionRepositoryImpl @Inject constructor(
        private val remoteDataSource: PaymentTransferInstructionRemoteDataSource
) : PaymentTransferInstructionRepository {

    override suspend fun getPaymentTransferInstruction(bankId: Int): Resource<PaymentTransferInstructionEntity> {
        return remoteDataSource.getTransferInstruction(bankId)
    }

}