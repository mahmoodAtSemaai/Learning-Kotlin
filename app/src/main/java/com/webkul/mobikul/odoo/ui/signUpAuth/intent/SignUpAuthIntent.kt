package com.webkul.mobikul.odoo.ui.signUpAuth.intent

import com.webkul.mobikul.odoo.core.mvicore.IIntent
import com.webkul.mobikul.odoo.ui.auth.SignUpIntent

sealed class SignUpAuthIntent : IIntent {
    data class Continue(val phoneNumber: String) : SignUpAuthIntent()
    data class Verify(val phoneNumber: String): SignUpAuthIntent()
    data class NewUser(val phoneNumber: String): SignUpAuthIntent()
    object ViewMarketPlaceTnC : SignUpAuthIntent()
    object Login: SignUpAuthIntent()
}