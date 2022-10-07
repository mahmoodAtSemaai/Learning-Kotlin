package com.webkul.mobikul.odoo.ui.checkout.paymentacquire

import com.webkul.mobikul.odoo.core.mvicore.IEffect

sealed class PaymentAcquireEffect : IEffect {
    data class ErrorSnackBar(val message: String?) : PaymentAcquireEffect()
}