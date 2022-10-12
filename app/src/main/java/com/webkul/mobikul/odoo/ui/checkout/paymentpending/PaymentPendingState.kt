package com.webkul.mobikul.odoo.ui.checkout.paymentpending

import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.BankEntity
import com.webkul.mobikul.odoo.data.entity.PendingPaymentEntity
import com.webkul.mobikul.odoo.model.checkout.Bank
import com.webkul.mobikul.odoo.model.payments.PendingPaymentData
import com.webkul.mobikul.odoo.ui.checkout.shippingmethod.ShippingMethodState

sealed class PaymentPendingState: IState {
    object Idle : PaymentPendingState()
    object StopProgressDialog: PaymentPendingState()
    data class Loading(val message: String) : PaymentPendingState()
    data class SetUpInitialUI(val pendingPaymentData: PendingPaymentEntity) : PaymentPendingState()
    data class Error(val message: String?, val failureStatus: FailureStatus) : PaymentPendingState()
}