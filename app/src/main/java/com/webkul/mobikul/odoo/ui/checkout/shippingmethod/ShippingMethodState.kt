package com.webkul.mobikul.odoo.ui.checkout.shippingmethod

import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.ShippingMethodListEntity

sealed class ShippingMethodState : IState {

    object Idle : ShippingMethodState()
    object SetInitialLayout : ShippingMethodState()
    data class ProgressDialog(val message: String?) : ShippingMethodState()
    data class Error(val message: String?, val failureStatus: FailureStatus) : ShippingMethodState()
    data class FetchShippingMethod(val shippingMethodListEntity: ShippingMethodListEntity) : ShippingMethodState()
    object UpdateShippingMethodForOrder : ShippingMethodState()
}
