package com.webkul.mobikul.odoo.ui.authentication.intent

import com.webkul.mobikul.odoo.core.mvicore.IIntent

sealed class AuthenticationIntent : IIntent {
    data class Continue(val phoneNumber: String) : AuthenticationIntent()
    data class Verify(val phoneNumber: String): AuthenticationIntent()
    object SignUp: AuthenticationIntent()
    object PrivacyPolicy: AuthenticationIntent()
    object SavePrivacyPolicy : AuthenticationIntent()
    object GetFocus : AuthenticationIntent()
    object SetIdle : AuthenticationIntent()
}
