package com.webkul.mobikul.odoo.ui.checkout.shippingmethod

import android.os.Bundle
import com.webkul.mobikul.odoo.core.mvicore.IIntent
import com.webkul.mobikul.odoo.data.entity.ShippingMethodEntity
import com.webkul.mobikul.odoo.ui.checkout.address.CheckoutAddressIntent

sealed class ShippingMethodIntent : IIntent {

    data class SetBundleData(val bundle: Bundle?) : ShippingMethodIntent()
    object SetInitialLayout : ShippingMethodIntent()
    object FetchShippingMethod : ShippingMethodIntent()
    data class UpdateShippingMethod(val orderId: Int,
                                    val shippingMethodEntity: ShippingMethodEntity?) : ShippingMethodIntent()

}
