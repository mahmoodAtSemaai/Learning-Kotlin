package com.webkul.mobikul.odoo.features.auth.presentation

import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.features.auth.domain.enums.SignUpFieldsValidation
import com.webkul.mobikul.odoo.model.customer.address.MyAddressesResponse
import com.webkul.mobikul.odoo.model.customer.signup.SignUpResponse
import com.webkul.mobikul.odoo.model.customer.signup.TermAndConditionResponse
import com.webkul.mobikul.odoo.model.generic.CountryStateData

sealed class SignUpState : IState {
    object Idle : SignUpState()
    object Loading : SignUpState()
    data class SignUp(val data: SignUpResponse) : SignUpState()
    data class InvalidSignUpDetailsError(val invalidDetails: SignUpFieldsValidation) : SignUpState()
    data class Error(val error: String?) : SignUpState()
    data class IsSeller(val isSeller: Boolean): SignUpState()
    data class CountryStateDataSuccess(val countryStateData: CountryStateData): SignUpState()
    data class BillingAddressDataSuccess(val myAddressResponse: MyAddressesResponse): SignUpState()
    data class MarketPlaceTnCSuccess(val termAndConditionResponse: TermAndConditionResponse ):SignUpState()
    data class SignUpTnCSuccess(val termAndConditionResponse: TermAndConditionResponse ):SignUpState()
}

