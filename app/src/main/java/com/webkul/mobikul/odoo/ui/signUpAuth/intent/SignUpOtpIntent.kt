package com.webkul.mobikul.odoo.ui.signUpAuth.intent

import com.webkul.mobikul.odoo.core.mvicore.IIntent

sealed class SignUpOtpIntent : IIntent {
    data class GenerateOtp(val phoneNumber: String) : SignUpOtpIntent()
    data class ResendOtp(val phoneNumber: String) : SignUpOtpIntent()
    data class GetOTPFromInput(val phoneNumber: String, val otp: String) : SignUpOtpIntent()
    object OTPExpired : SignUpOtpIntent()
    object StopTimer : SignUpOtpIntent()
    data class StartTimer(val time: Int) : SignUpOtpIntent()
    data class ClearOTP(val firstTimeLaunched: Boolean) : SignUpOtpIntent()
}