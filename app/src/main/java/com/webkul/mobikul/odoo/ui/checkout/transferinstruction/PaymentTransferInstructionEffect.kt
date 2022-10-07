package com.webkul.mobikul.odoo.ui.checkout.transferinstruction

import com.webkul.mobikul.odoo.core.mvicore.IEffect

sealed class PaymentTransferInstructionEffect: IEffect {
    data class ShowOrHideInstruction(val position: Int): PaymentTransferInstructionEffect()
    data class ErrorSnackBar(val message: String?) : PaymentTransferInstructionEffect()
}