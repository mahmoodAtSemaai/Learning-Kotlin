package com.webkul.mobikul.odoo.ui.checkout.shippingmethod

import com.webkul.mobikul.odoo.core.mvicore.IEffect

sealed class ShippingMethodEffect : IEffect {
    data class ErrorSnackBar(val message: String?) : ShippingMethodEffect()
}