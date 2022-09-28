package com.webkul.mobikul.odoo.features.authentication.presentation.state

import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.features.authentication.data.models.BaseOtpLoginResponse
import com.webkul.mobikul.odoo.features.authentication.data.models.OtpAuthenticationResponse
import com.webkul.mobikul.odoo.features.authentication.presentation.intent.LoginOtpIntent
import com.webkul.mobikul.odoo.model.extra.SplashScreenResponse
import com.webkul.mobikul.odoo.model.home.HomePageResponse

sealed class LoginOtpState : IState {
    object Idle : LoginOtpState()
    object Loading : LoginOtpState()
    object OTPSent : LoginOtpState()
    object OTPExpired : LoginOtpState()
    data class Login(val data: BaseOtpLoginResponse<OtpAuthenticationResponse>) : LoginOtpState()
    data class Error(val message: String?, val failureStatus: FailureStatus) : LoginOtpState()
    data class GetOTPFromInput(val otp: String) : LoginOtpState()
    object OTPVerified : LoginOtpState()
    data class Splash(val splashScreenResponse: SplashScreenResponse) : LoginOtpState()
    object HomePage : LoginOtpState()
    data class CountDownTimer(val timeRemaining: Int) : LoginOtpState()
    object UnauthorisedUser : LoginOtpState()
    data class InvalidOTP(val message: String?, val failureStatus: FailureStatus) : LoginOtpState()
    data class OTPCleared(val isFirstTimeLaunched: Boolean) : LoginOtpState()
    object RegisterFCMTokenState : LoginOtpState()
}
