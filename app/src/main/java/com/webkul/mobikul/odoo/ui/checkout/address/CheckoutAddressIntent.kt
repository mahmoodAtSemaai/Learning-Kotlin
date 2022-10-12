package com.webkul.mobikul.odoo.ui.checkout.address

import android.os.Bundle
import com.webkul.mobikul.odoo.core.mvicore.IIntent
import com.webkul.mobikul.odoo.model.customer.address.AddressData

sealed class CheckoutAddressIntent : IIntent {
    data class SetBundleData(val bundle: Bundle?) : CheckoutAddressIntent()
    data class UpdateAddress(val orderId: Int, val address: AddressData?) : CheckoutAddressIntent()
    data class DeleteAddress(val address: AddressData) : CheckoutAddressIntent()
    object FetchAddress : CheckoutAddressIntent()
    object SetInitialLayout : CheckoutAddressIntent()
    object NavigateToAddAddress : CheckoutAddressIntent()
    data class NavigateToEditAddress(val address: AddressData) : CheckoutAddressIntent()
}