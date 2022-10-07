package com.webkul.mobikul.odoo.ui.checkout.paymentprocessing

import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.PaymentAcquireCheckoutEntity
import com.webkul.mobikul.odoo.data.entity.PaymentDetailsEntity
import com.webkul.mobikul.odoo.data.entity.PaymentTransactionEntity
import com.webkul.mobikul.odoo.model.payments.PaymentDetails
import com.webkul.mobikul.odoo.ui.checkout.paymentacquire.PaymentAcquireState

sealed class PaymentProcessingState : IState {
    object Idle : PaymentProcessingState()
    object SetInitialLayout : PaymentProcessingState()
    data class PaymentProcessingResult(val paymentDetails: PaymentDetailsEntity, val orderId: Int) : PaymentProcessingState()
    data class ErrorDialog(val title: String?, val message: String?) : PaymentProcessingState()
    data class Error(val message: String?, val failureStatus: FailureStatus) : PaymentProcessingState()
}
