package com.webkul.mobikul.odoo.ui.auth

import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.AddressEntity
import com.webkul.mobikul.odoo.data.entity.CountryEntity
import com.webkul.mobikul.odoo.data.entity.SignUpEntity
import com.webkul.mobikul.odoo.data.entity.TermsAndConditionsEntity
import com.webkul.mobikul.odoo.domain.enums.SignUpFieldsValidation
import com.webkul.mobikul.odoo.model.customer.address.MyAddressesResponse
import com.webkul.mobikul.odoo.model.customer.signup.SignUpResponse
import com.webkul.mobikul.odoo.model.customer.signup.TermAndConditionResponse
import com.webkul.mobikul.odoo.model.generic.CountryStateData

sealed class SignUpState : IState {
    object Idle : SignUpState()
    object Loading : SignUpState()
    data class SignUp(val data: SignUpEntity) : SignUpState()
    data class InvalidSignUpDetailsError(val invalidDetails: SignUpFieldsValidation) : SignUpState()
    data class Error(val message: String?, val failureStatus: FailureStatus) : SignUpState()
    data class AccessDenied(val message: String?) : SignUpState()
    data class IsSeller(val isSeller: Boolean) : SignUpState()
    data class CountryStateDataSuccess(val countryStateData: CountryEntity) : SignUpState()
    data class BillingAddressDataSuccess(val myAddressResponse: AddressEntity) : SignUpState()
    data class MarketPlaceTnCSuccess(val termAndConditionResponse: TermsAndConditionsEntity) :
            SignUpState()

    data class SignUpTnCSuccess(val termAndConditionResponse: TermsAndConditionsEntity) :
            SignUpState()

    object NavigateToLogin : SignUpState()
    object CloseSignUpScreen : SignUpState()
}

