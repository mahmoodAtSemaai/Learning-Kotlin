package com.webkul.mobikul.odoo.ui.signUpAuth.intent

import com.webkul.mobikul.odoo.core.mvicore.IIntent

sealed class SignUpOptionsIntent : IIntent {
    data class OtpSignUp(val phoneNumber: String) : SignUpOptionsIntent()
    object ChangePhoneNumber : SignUpOptionsIntent()
    object SetIdle : SignUpOptionsIntent()
}
