package com.webkul.mobikul.odoo.ui.checkout.paymentacquire

import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.PaymentAcquireCheckoutEntity
import com.webkul.mobikul.odoo.data.entity.PaymentAcquirerMethodProviderCheckoutEntity
import com.webkul.mobikul.odoo.data.entity.PaymentAcquirerMethodCheckoutEntity
import com.webkul.mobikul.odoo.model.payments.SelectedPaymentMethod

sealed class PaymentAcquireState : IState {

    object Idle : PaymentAcquireState()
    object SetInitialLayout : PaymentAcquireState()
    data class ProgressDialog(val message: String?) : PaymentAcquireState()
    data class Error(val message: String?, val failureStatus: FailureStatus) : PaymentAcquireState()
    data class EndResult(val selectedPaymentMethod: SelectedPaymentMethod) : PaymentAcquireState()
    data class PaymentAcquirersResult(val paymentAcquireCheckoutEntity : PaymentAcquireCheckoutEntity) : PaymentAcquireState()
    data class PaymentAcquirerMethodsResult(val paymentAcquirerMethodCheckoutEntity: PaymentAcquirerMethodCheckoutEntity, val paymentVendorId: Int) : PaymentAcquireState()
    data class PaymentAcquirerMethodProviderResult(val paymentAcquirerMethodProviderCheckoutEntity : PaymentAcquirerMethodProviderCheckoutEntity, val paymentVendorId: Int) : PaymentAcquireState()
}