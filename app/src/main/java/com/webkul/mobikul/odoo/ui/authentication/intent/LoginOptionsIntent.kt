package com.webkul.mobikul.odoo.ui.authentication.intent

import android.os.Bundle
import com.webkul.mobikul.odoo.core.mvicore.IIntent

sealed class LoginOptionsIntent : IIntent {
    data class OtpLogin(val phoneNumber: String) : LoginOptionsIntent()
    data class PasswordLogin(val phoneNumber: String) : LoginOptionsIntent()
    object ChangePhoneNumber : LoginOptionsIntent()
    object SetIdle : LoginOptionsIntent()
    data class GetArguments(val arguments: Bundle) : LoginOptionsIntent()
}
