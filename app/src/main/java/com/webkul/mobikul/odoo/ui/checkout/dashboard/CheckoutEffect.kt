package com.webkul.mobikul.odoo.ui.checkout.dashboard

import android.content.Intent
import android.view.View
import com.webkul.mobikul.odoo.core.mvicore.IEffect
import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.DeliveryEntity
import com.webkul.mobikul.odoo.data.entity.OrderEntity
import com.webkul.mobikul.odoo.data.entity.OrderProductEntity
import com.webkul.mobikul.odoo.data.entity.OrderReviewEntity
import com.webkul.mobikul.odoo.model.payments.SelectedPaymentMethod
import com.webkul.mobikul.odoo.ui.checkout.address.UpdateAddressState

sealed class CheckoutEffect : IEffect {
    data class ValidateCheckOutOrderDetails(val result: Boolean) : CheckoutEffect()
    data class WarningDialog(val message: String?) : CheckoutEffect()
    data class ErrorDialog(val message: String?) : CheckoutEffect()
    data class ErrorSnackBar(val message: String?) : CheckoutEffect()
    data class Toast(val message: String?) : CheckoutEffect()
    object OnValidateShippingAddressFailure : CheckoutEffect()
    object OnValidateShippingMethodFailure : CheckoutEffect()
    object OnValidatePaymentMethodFailure : CheckoutEffect()
    data class NavigateToShippingAddress(val cartId: Int) : CheckoutEffect()
    data class NavigateToShippingMethod(val cartId: Int) : CheckoutEffect()
    object NavigateToPaymentMethod : CheckoutEffect()
    object OnSuccessfulValidationForPlaceOrder: CheckoutEffect()
    object SetInitialLayout : CheckoutEffect()
}
