package com.webkul.mobikul.odoo.ui.home

import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.AddressEntity
import com.webkul.mobikul.odoo.data.entity.AddressFormEntity
import com.webkul.mobikul.odoo.data.entity.StateListEntity
import com.webkul.mobikul.odoo.model.customer.address.AddressData

sealed class HomeState : IState {

    object Idle : HomeState()
    object Loading : HomeState()
    data class BillingAddressDataSuccess(val addressEntity: AddressEntity) : HomeState()
    data class AddressFormDataSuccess(
        val addressFormEntity: AddressFormEntity,
        val addressData: AddressData
    ) : HomeState()

    data class StateListSuccess(
        val stateListEntity: StateListEntity,
        val addressFormEntity: AddressFormEntity,
        val addressData: AddressData
    ) : HomeState()

    data class Error(val message: String?, val failureStatus: FailureStatus) : HomeState()

    data class RefreshState(val isRefreshEnable: Boolean) : HomeState()
}
