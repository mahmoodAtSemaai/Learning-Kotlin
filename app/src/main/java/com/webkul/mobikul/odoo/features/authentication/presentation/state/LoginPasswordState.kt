package com.webkul.mobikul.odoo.features.authentication.presentation.state

import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.domain.enums.LoginFieldsValidation
import com.webkul.mobikul.odoo.features.authentication.data.models.BaseOtpLoginResponse
import com.webkul.mobikul.odoo.features.authentication.data.models.OtpAuthenticationResponse
import com.webkul.mobikul.odoo.model.extra.SplashScreenResponse
import com.webkul.mobikul.odoo.model.home.HomePageResponse

sealed class LoginPasswordState : IState {
    object Idle : LoginPasswordState()
    object Loading : LoginPasswordState()
    data class Splash(val splashScreenResponse: SplashScreenResponse) : LoginPasswordState()
    data class HomePage(val homePageResponse: HomePageResponse) : LoginPasswordState()
    data class LoggedIn(val data: BaseOtpLoginResponse<OtpAuthenticationResponse>) : LoginPasswordState()

    object EnableButton : LoginPasswordState()
    object DisableButton : LoginPasswordState()


    data class InvalidLoginDetailsError(val uiError: LoginFieldsValidation) : LoginPasswordState()
    data class Error(val message: String?, val failureStatus: FailureStatus) : LoginPasswordState()
    object ForgotPassword : LoginPasswordState()
    object UnauthorisedUser : LoginPasswordState()
}
