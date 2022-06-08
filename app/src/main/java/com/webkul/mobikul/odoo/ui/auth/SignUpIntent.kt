package com.webkul.mobikul.odoo.ui.auth

import com.webkul.mobikul.odoo.core.mvicore.IIntent
import com.webkul.mobikul.odoo.data.entity.SignUpData

sealed class SignUpIntent: IIntent {
    data class SignUp(val signUpData: SignUpData) : SignUpIntent()
    data class IsSeller(val isSeller: Boolean): SignUpIntent()
    object GetCountryStateData : SignUpIntent()
    object GetBillingAddress : SignUpIntent()
    object ViewMarketPlaceTnC : SignUpIntent()
    object ViewSignUpTnC : SignUpIntent()
    object NavigateToLogin : SignUpIntent()
    object CloseSignUpScreen : SignUpIntent()
}
