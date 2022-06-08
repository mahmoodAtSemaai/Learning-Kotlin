package com.webkul.mobikul.odoo.features.authentication.presentation.intent

import com.webkul.mobikul.odoo.core.mvicore.IIntent

sealed class LoginOptionsIntent : IIntent {
    data class OtpLogin(val phoneNumber: String) : LoginOptionsIntent()
    object PasswordLogin : LoginOptionsIntent()
    object ChangePhoneNumber : LoginOptionsIntent()
    object SetIdle : LoginOptionsIntent()
}
