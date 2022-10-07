package com.webkul.mobikul.odoo.ui.checkout.paymentpending

import com.webkul.mobikul.odoo.core.mvicore.IEffect
import com.webkul.mobikul.odoo.data.entity.BankEntity
import com.webkul.mobikul.odoo.model.checkout.Bank

sealed class PaymentPendingEffect: IEffect {
    object DisplayCopyToClipBoardToast : PaymentPendingEffect()
    data class OrderDetails(val orderId: Int) : PaymentPendingEffect()
    data class TransferInstructionFragment(val bank: BankEntity) : PaymentPendingEffect()
    data class DisplayPaymentStatusScreen(val paymentStatusMsgConst: Int) : PaymentPendingEffect()
    data class ErrorSnackBar(val message: String?) : PaymentPendingEffect()
}