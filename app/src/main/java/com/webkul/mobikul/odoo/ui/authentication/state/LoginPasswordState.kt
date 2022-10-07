package com.webkul.mobikul.odoo.ui.authentication.state

import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.OtpAuthenticationEntity
import com.webkul.mobikul.odoo.data.entity.SplashEntity
import com.webkul.mobikul.odoo.domain.enums.LoginFieldsValidation

sealed class LoginPasswordState : IState {
    object Idle : LoginPasswordState()
    object Loading : LoginPasswordState()
    data class Splash(val splashEntity: SplashEntity) : LoginPasswordState()
    data class LoggedIn(val data: OtpAuthenticationEntity) : LoginPasswordState()
    object EnableButton : LoginPasswordState()
    object DisableButton : LoginPasswordState()
    data class InvalidLoginDetailsError(val uiError: LoginFieldsValidation) : LoginPasswordState()
    data class InvalidPasswordError(val errorMessage: String) : LoginPasswordState()
    data class Error(val message: String?, val failureStatus: FailureStatus) : LoginPasswordState()
    data class SetPhoneNumber(val phoneNumber: String) : LoginPasswordState()
    object ForgotPassword : LoginPasswordState()
    object InitialiseViews : LoginPasswordState()
    object UserFetched : LoginPasswordState()
    object RegisterFCMTokenState : LoginPasswordState()
}
