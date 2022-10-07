package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.PaymentTransferInstructionEntity

interface PaymentTransferInstructionRepository {

    suspend fun getPaymentTransferInstruction(bankId: Int): Resource<PaymentTransferInstructionEntity>
}