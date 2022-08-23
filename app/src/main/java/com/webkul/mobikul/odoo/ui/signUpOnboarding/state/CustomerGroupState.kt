package com.webkul.mobikul.odoo.ui.signUpOnboarding.state

import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.CustomerGroupEntity

sealed class CustomerGroupState : IState {
    object Idle : CustomerGroupState()
    object Loading : CustomerGroupState()
    object FetchCustomerGroups : CustomerGroupState()
    object GroupSelected : CustomerGroupState()
    object CompletedCustomerGroup : CustomerGroupState()
    object CloseApp : CustomerGroupState()
    data class CustomerGroups(val customerGroupList: List<CustomerGroupEntity>) : CustomerGroupState()
    data class Error(val message: String?, val failureStatus: FailureStatus) : CustomerGroupState()
}
