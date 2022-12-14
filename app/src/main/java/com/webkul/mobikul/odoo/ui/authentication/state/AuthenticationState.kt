package com.webkul.mobikul.odoo.ui.authentication.state

import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.core.utils.FailureStatus

sealed class AuthenticationState : IState {
    object Idle : AuthenticationState()
    object Loading : AuthenticationState()
    data class VerifiedPhoneNumber(val isPasswordEnabled : Boolean) : AuthenticationState()
    object ContinuePhoneNumber : AuthenticationState()
    object EmptyPhoneNumber : AuthenticationState()
    object IncorrectPhoneNumberFormat : AuthenticationState()
    object IncorrectPhoneNumber : AuthenticationState()
    object ValidPhoneNumber : AuthenticationState()
    data class Error(val message: String?, val failureStatus: FailureStatus) : AuthenticationState()
    object SetFocusOnInputField : AuthenticationState()
    object InvalidNumber : AuthenticationState()
}
