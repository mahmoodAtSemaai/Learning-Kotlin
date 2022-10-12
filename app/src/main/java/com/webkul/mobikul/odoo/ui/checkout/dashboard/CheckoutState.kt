package com.webkul.mobikul.odoo.ui.checkout.dashboard

import android.content.Intent
import android.view.View
import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.DeliveryEntity
import com.webkul.mobikul.odoo.data.entity.OrderEntity
import com.webkul.mobikul.odoo.data.entity.OrderProductEntity
import com.webkul.mobikul.odoo.data.entity.OrderReviewEntity
import com.webkul.mobikul.odoo.model.payments.SelectedPaymentMethod
import com.webkul.mobikul.odoo.ui.checkout.address.UpdateAddressState

sealed class CheckoutState : IState {

    object Idle : CheckoutState()
    object Loading : CheckoutState()
    data class ProgressDialog(val message: String?) : CheckoutState()
    data class Error(val message: String?, val failureStatus: FailureStatus) : CheckoutState()
    object ShippingAddressSelection : CheckoutState()
    object ShippingMethodSelection : CheckoutState()
    data class PaymentMethodSelection(val selectedPaymentMethod: SelectedPaymentMethod?) : CheckoutState()
    data class ShowError(val view: View, val message: String?) : CheckoutState()
    data class FetchOrderDetails(val orderEntity: OrderEntity,
                                 val refreshRecyclerView: Boolean,
                                 val refreshPaymentDetails: Boolean,) : CheckoutState()
    object OnPlacedCODOrder : CheckoutState()
    data class OnPlacedVirtualOrder(
        val transactionId: String,
        val orderId: Int,
        val paymentAcquirerProviderId: String
    ) : CheckoutState()
    data class GetOrderReviewResponse(val orderReviewEntity: OrderReviewEntity) : CheckoutState()

}
