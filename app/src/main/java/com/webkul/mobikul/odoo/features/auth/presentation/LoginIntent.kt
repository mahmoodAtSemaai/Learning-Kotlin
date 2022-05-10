package com.webkul.mobikul.odoo.features.auth.presentation

import com.webkul.mobikul.odoo.core.mvicore.IIntent

sealed class LoginIntent : IIntent{
    data class Login(val username: String ,val password: String) : LoginIntent()
    object PrivacyPolicy : LoginIntent()
    object ForgotPassword :LoginIntent()
}
