package com.webkul.mobikul.odoo.ui.authentication.intent

import com.webkul.mobikul.odoo.core.mvicore.IIntent

sealed class AuthActivityIntent : IIntent {
    object SetIdle : AuthActivityIntent()
    object SetupFragmentBackStack : AuthActivityIntent()
    object SetupPhoneNumberScreen : AuthActivityIntent()
    object InitToolBar : AuthActivityIntent()
    data class SetupToolbarContent(val title: String, val showBackButton: Boolean) : AuthActivityIntent()
}
