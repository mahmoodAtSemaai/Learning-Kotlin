package com.webkul.mobikul.odoo.features.authentication.presentation.intent

import com.webkul.mobikul.odoo.core.mvicore.IIntent
import com.webkul.mobikul.odoo.features.authentication.data.models.LoginOtpAuthenticationRequest

sealed class LoginPasswordIntent : IIntent {
    object ForgetPassword : LoginPasswordIntent()
    data class Login(val loginOtpAuthRequest: LoginOtpAuthenticationRequest) : LoginPasswordIntent()
    data class PasswordReceived(val password: String) : LoginPasswordIntent()
    object SplashPage : LoginPasswordIntent()
    object HomePage : LoginPasswordIntent()
    object Default : LoginPasswordIntent()
}
