package com.webkul.mobikul.odoo.ui.checkout.address

import com.webkul.mobikul.odoo.core.mvicore.IEffect
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.AddressEntity
import com.webkul.mobikul.odoo.data.entity.DeleteAddressEntity
import com.webkul.mobikul.odoo.model.customer.address.AddressData

sealed class CheckoutAddressEffect : IEffect {
    object NavigateToAddAddress : CheckoutAddressEffect()
    data class NavigateToEditAddress(val addressData: AddressData) : CheckoutAddressEffect()
}