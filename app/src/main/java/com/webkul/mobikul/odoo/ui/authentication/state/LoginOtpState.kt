package com.webkul.mobikul.odoo.ui.authentication.state

import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.OtpAuthenticationEntity
import com.webkul.mobikul.odoo.data.entity.SplashEntity

sealed class LoginOtpState : IState {
    object Idle : LoginOtpState()
    object Loading : LoginOtpState()
    object OTPSent : LoginOtpState()
    object OTPExpired : LoginOtpState()
    data class Login(val data: OtpAuthenticationEntity) : LoginOtpState()
    data class Error(val message: String?, val failureStatus: FailureStatus) : LoginOtpState()
    data class GetOTPFromInput(val otp: String) : LoginOtpState()
    object OTPVerified : LoginOtpState()
    data class Splash(val splashEntity: SplashEntity) : LoginOtpState()
    data class CountDownTimer(val timeRemaining: Int) : LoginOtpState()
    object SetupBackPressedListener : LoginOtpState()
    object InvalidOTP : LoginOtpState()
    object SetFocus : LoginOtpState()
    object UserFetched : LoginOtpState()
    data class OTPCleared(val isFirstTimeLaunched: Boolean) : LoginOtpState()
    data class SetPhoneNumber(val phoneNumber: String) : LoginOtpState()
    object RegisterFCMTokenState : LoginOtpState()
}
