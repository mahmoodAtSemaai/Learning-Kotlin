package com.webkul.mobikul.odoo.features.authentication.presentation.intent

import com.webkul.mobikul.odoo.core.mvicore.IIntent
import com.webkul.mobikul.odoo.features.authentication.data.models.OtpAuthenticationRequest

sealed class LoginOtpIntent : IIntent {
    data class GenerateOtp(val phoneNumber: String) : LoginOtpIntent()
    data class Login(
        val phoneNumber: String,
        val otpAuthenticationRequest: OtpAuthenticationRequest
    ) : LoginOtpIntent()

    data class ResendOtp(val phoneNumber: String) : LoginOtpIntent()
    data class GetOTPFromInput(val phoneNumber: String, val otp: String) : LoginOtpIntent()
    object OTPExpired : LoginOtpIntent()
    object GetSplashData : LoginOtpIntent()
    object GetHomePageData : LoginOtpIntent()
    object StopTimer : LoginOtpIntent()
    data class StartTimer(val time: Int) : LoginOtpIntent()
    data class ClearOTP(val firstTimeLaunched: Boolean) : LoginOtpIntent()
    object RegisterFCMToken : LoginOtpIntent()
}
