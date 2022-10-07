package com.webkul.mobikul.odoo.ui.checkout.paymentacquire

import com.webkul.mobikul.odoo.core.mvicore.IIntent
import com.webkul.mobikul.odoo.model.payments.SelectedPaymentMethod

sealed class PaymentAcquireIntent : IIntent {

    object SetInitialLayout : PaymentAcquireIntent()
    data class FetchPaymentAcquirers(val companyId: Int) : PaymentAcquireIntent()
    data class FetchPaymentAcquirerMethods(val acquirerId: Int) : PaymentAcquireIntent()
    data class FetchPaymentAcquirerMethodProvider(val acquirerId: Int,
                                                  val paymentMethodId: Int) : PaymentAcquireIntent()
    object SetEndResult : PaymentAcquireIntent()

}