package com.webkul.mobikul.odoo.ui.checkout.address

import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.AddressEntity
import com.webkul.mobikul.odoo.data.entity.DeleteAddressEntity
import com.webkul.mobikul.odoo.model.customer.address.AddressData

sealed class CheckoutAddressState : IState {

    object Idle : CheckoutAddressState()
    object Loading : CheckoutAddressState()
    object UpdateAddress : CheckoutAddressState()
    object SetInitialLayout : CheckoutAddressState()
    data class FetchAddress(val addressEntity: AddressEntity) : CheckoutAddressState()
    data class Error(val message: String?, val failureStatus: FailureStatus) : CheckoutAddressState()
    data class DeleteAddress(val deleteAddressEntity: DeleteAddressEntity) : CheckoutAddressState()
    data class WarningDialog(val title: String?, val message: String?) : CheckoutAddressState()
    data class AlertDialog(val title: String?, val message: String?) : CheckoutAddressState()
    data class ProgressDialog(val message: String?) : CheckoutAddressState()
}
