package com.webkul.mobikul.odoo.features.auth.presentation

import com.webkul.mobikul.odoo.core.mvicore.IIntent

sealed class SignUpIntent: IIntent {
    data class SignUp(val signUpData: SignUpData) : SignUpIntent()
    data class IsSeller(val isSeller: Boolean): SignUpIntent()
}
