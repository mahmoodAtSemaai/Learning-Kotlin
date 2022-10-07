package com.webkul.mobikul.odoo.ui.authentication.intent

import android.os.Bundle
import com.webkul.mobikul.odoo.core.mvicore.IIntent
import com.webkul.mobikul.odoo.data.request.LoginOtpAuthenticationRequest
import com.webkul.mobikul.odoo.model.request.RegisterDeviceTokenRequest

sealed class LoginPasswordIntent : IIntent {
    object ForgetPassword : LoginPasswordIntent()
    data class Login(val loginOtpAuthRequest: LoginOtpAuthenticationRequest) : LoginPasswordIntent()
    data class PasswordReceived(val password: String) : LoginPasswordIntent()
    data class GetArguments(val arguments: Bundle) : LoginPasswordIntent()
    object SplashPage : LoginPasswordIntent()
    object GetUser : LoginPasswordIntent()
    object HomePage : LoginPasswordIntent()
    object CreateChatChannel : LoginPasswordIntent()
    object Default : LoginPasswordIntent()
    object SetupViews : LoginPasswordIntent()
    data class RegisterFCMToken(val registerDeviceTokenRequest: RegisterDeviceTokenRequest) : LoginPasswordIntent()
}
