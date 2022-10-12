package com.webkul.mobikul.odoo.ui.checkout.transferinstruction

import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.PaymentTransferInstructionEntity
import com.webkul.mobikul.odoo.model.checkout.TransferInstruction
import com.webkul.mobikul.odoo.model.payments.TransferInstructionResponse
import com.webkul.mobikul.odoo.ui.checkout.paymentpending.PaymentPendingState

sealed class PaymentTransferInstructionState: IState {
    object Idle : PaymentTransferInstructionState()
    object Loading : PaymentTransferInstructionState()
    object SetInitialLayout : PaymentTransferInstructionState()
    data class DisplayPaymentTransferInstruction(val transferInstructions: PaymentTransferInstructionEntity): PaymentTransferInstructionState()
    data class Error(val message: String?, val failureStatus: FailureStatus) : PaymentTransferInstructionState()
}