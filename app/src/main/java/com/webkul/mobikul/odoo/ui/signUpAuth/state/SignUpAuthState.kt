package com.webkul.mobikul.odoo.ui.signUpAuth.state

import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.TermsAndConditionsEntity

sealed class SignUpAuthState : IState {
    object Idle : SignUpAuthState()
    object Loading : SignUpAuthState()
    object EmptyPhoneNumber : SignUpAuthState()
    object IncorrectPhoneNumberFormat : SignUpAuthState()
    object IncorrectPhoneNumber : SignUpAuthState()
    object ValidPhoneNumber : SignUpAuthState()
    object ExistingUser : SignUpAuthState()
    object Login : SignUpAuthState()
    data class MarketPlaceTnCSuccess(val termAndConditionResponse: TermsAndConditionsEntity) : SignUpAuthState()
    data class Error(val message: String?, val failureStatus: FailureStatus) : SignUpAuthState()

}
