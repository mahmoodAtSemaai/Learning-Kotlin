package com.webkul.mobikul.odoo.features.authentication.presentation.state

import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.core.utils.FailureStatus

sealed class AuthenticationState : IState {
    object Idle : AuthenticationState()
    object Loading : AuthenticationState()
    object VerifiedPhoneNumber : AuthenticationState()
    object ContinuePhoneNumber : AuthenticationState()
    object EmptyPhoneNumber : AuthenticationState()
    object IncorrectPhoneNumberFormat : AuthenticationState()
    object IncorrectPhoneNumber : AuthenticationState()
    object ValidPhoneNumber : AuthenticationState()
    object SignUp : AuthenticationState()
    data class Error(val message: String?, val failureStatus: FailureStatus) : AuthenticationState()
    object PrivacyPolicy : AuthenticationState()
}
