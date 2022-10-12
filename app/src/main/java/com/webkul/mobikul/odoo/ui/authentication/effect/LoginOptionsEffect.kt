package com.webkul.mobikul.odoo.ui.authentication.effect

import com.webkul.mobikul.odoo.core.mvicore.IEffect

sealed class LoginOptionsEffect : IEffect {
    data class OtpLogin(val phoneNumber: String) : LoginOptionsEffect()
    data class PasswordLogin(val phoneNumber: String) : LoginOptionsEffect()
    object ChangePhoneNumber : LoginOptionsEffect()
}
