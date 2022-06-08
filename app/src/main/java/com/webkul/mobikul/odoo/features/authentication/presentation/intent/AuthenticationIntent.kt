package com.webkul.mobikul.odoo.features.authentication.presentation.intent

import com.webkul.mobikul.odoo.core.mvicore.IIntent

sealed class AuthenticationIntent : IIntent {
    data class Continue(val phoneNumber: String) : AuthenticationIntent()
    data class Verify(val phoneNumber: String): AuthenticationIntent()
    object SignUp: AuthenticationIntent()
    object PrivacyPolicy: AuthenticationIntent()
    object SavePrivacyPolicy : AuthenticationIntent()
}
