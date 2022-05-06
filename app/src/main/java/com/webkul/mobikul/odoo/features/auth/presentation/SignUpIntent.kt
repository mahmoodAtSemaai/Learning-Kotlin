package com.webkul.mobikul.odoo.features.auth.presentation

import com.webkul.mobikul.odoo.core.mvicore.IIntent
import com.webkul.mobikul.odoo.features.auth.data.models.SignUpData
import com.webkul.mobikul.odoo.model.customer.address.MyAddressesResponse
import com.webkul.mobikul.odoo.model.customer.signup.SignUpResponse

sealed class SignUpIntent: IIntent {
    data class SignUp(val signUpData: SignUpData) : SignUpIntent()
    data class IsSeller(val isSeller: Boolean): SignUpIntent()
    object GetCountryStateData : SignUpIntent()
    object GetBillingAddress : SignUpIntent()
    object ViewMarketPlaceTnC : SignUpIntent()
}
