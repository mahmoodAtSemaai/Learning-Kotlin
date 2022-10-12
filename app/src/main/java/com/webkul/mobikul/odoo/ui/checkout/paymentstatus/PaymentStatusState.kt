package com.webkul.mobikul.odoo.ui.checkout.paymentstatus

import com.webkul.mobikul.odoo.core.mvicore.IState

sealed class PaymentStatusState: IState {
    object Idle : PaymentStatusState()
    object ContinueShopping : PaymentStatusState()
    object ShowCompletePayment: PaymentStatusState()
    object ShowFailurePayment: PaymentStatusState()
    object ShowCODPayment: PaymentStatusState()
    object ShowPaymentShopeeActivated: PaymentStatusState()
    object SetInitialLayout: PaymentStatusState()

}