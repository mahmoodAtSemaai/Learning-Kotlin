package com.webkul.mobikul.odoo.ui.signUpOnboarding.intent

import com.webkul.mobikul.odoo.core.mvicore.IIntent

sealed class CustomerGroupIntent : IIntent {
    object FetchCustomer : CustomerGroupIntent()
    data class GroupSelected(val id:String,val name:String) : CustomerGroupIntent()
    object GetUserId : CustomerGroupIntent()
    object Continue : CustomerGroupIntent()
    object CloseApp : CustomerGroupIntent()
}