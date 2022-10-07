package com.webkul.mobikul.odoo.ui.checkout.transferinstruction

import android.os.Bundle
import com.webkul.mobikul.odoo.core.mvicore.IIntent

sealed class PaymentTransferInstructionIntent: IIntent {
    data class SetBundleData(val bundle: Bundle) : PaymentTransferInstructionIntent()
    object FetchPaymentTransferInstructions : PaymentTransferInstructionIntent()
    data class ShowOrHideInstruction(val position: Int): PaymentTransferInstructionIntent()
}