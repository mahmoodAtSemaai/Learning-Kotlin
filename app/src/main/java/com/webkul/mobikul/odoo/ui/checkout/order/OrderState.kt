package com.webkul.mobikul.odoo.ui.checkout.order

import android.content.Intent
import android.view.View
import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.*
import com.webkul.mobikul.odoo.model.payments.PendingPaymentData
import com.webkul.mobikul.odoo.model.payments.SelectedPaymentMethod
import com.webkul.mobikul.odoo.ui.checkout.address.UpdateAddressState
import com.webkul.mobikul.odoo.ui.checkout.dashboard.CheckoutState

sealed class OrderState : IState {
    object Idle : OrderState()
    object SetInitialLayout : OrderState()
    data class Error(val message: String?, val failureStatus: FailureStatus) : OrderState()
    data class ShowProgressDialog(val message: String?) : OrderState()
    data class ShowErrorDialog(val title: String?, val message: String?) : OrderState()
    data class ShowWarningDialog(val message: String?) : OrderState()
    data class OnOrderDetailsReceived(val orderEntity: OrderEntity) : OrderState()
    object OnCancelledTransactionStatus : OrderState()
    object OnCompletedTransactionStatus : OrderState()
    data class OnPendingTransactionStatus(val paymentStatusEntity: PaymentStatusEntity, val orderId: Int) : OrderState()
}
