package com.webkul.mobikul.odoo.ui.authentication.intent

import android.os.Bundle
import com.webkul.mobikul.odoo.core.mvicore.IIntent
import com.webkul.mobikul.odoo.data.request.OtpAuthenticationRequest
import com.webkul.mobikul.odoo.model.request.RegisterDeviceTokenRequest

sealed class LoginOtpIntent : IIntent {
    data class GenerateOtp(val phoneNumber: String) : LoginOtpIntent()
    data class ResendOtp(val phoneNumber: String) : LoginOtpIntent()
    data class GetOTPFromInput(val otp: OtpAuthenticationRequest) : LoginOtpIntent()
    object OTPExpired : LoginOtpIntent()
    object GetSplashData : LoginOtpIntent()
    object GetHomePageData : LoginOtpIntent()
    object GetUser : LoginOtpIntent()
    object CreateChatChannel : LoginOtpIntent()
    object StopTimer : LoginOtpIntent()
    object DispatchBackPressed : LoginOtpIntent()
    object RequestFocus : LoginOtpIntent()
    data class StartTimer(val time: Int) : LoginOtpIntent()
    data class ClearOTP(val firstTimeLaunched: Boolean) : LoginOtpIntent()
    data class GetArguments(val arguments: Bundle) : LoginOtpIntent()
    data class RegisterFCMToken(val registerDeviceTokenRequest: RegisterDeviceTokenRequest) : LoginOtpIntent()
}
