package com.webkul.mobikul.odoo.ui.authentication.effect

import android.content.Intent
import com.webkul.mobikul.odoo.core.mvicore.IEffect

sealed class AuthenticationEffect : IEffect {
    data class NavigateToLoginOptionsScreen(val phoneNumber: String, val enablePassword: Boolean) :
        AuthenticationEffect()
    object NavigateToSignUp : AuthenticationEffect()
    data class ShowPrivacyPolicy(val intent: Intent) : AuthenticationEffect()
}

