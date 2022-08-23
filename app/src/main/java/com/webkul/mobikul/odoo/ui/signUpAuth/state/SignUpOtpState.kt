package com.webkul.mobikul.odoo.ui.signUpAuth.state

import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.core.utils.FailureStatus

sealed class SignUpOtpState : IState {
    object Idle : SignUpOtpState()
    object Loading : SignUpOtpState()
    object OTPSent : SignUpOtpState()
    object OTPExpired : SignUpOtpState()
    data class Error(val message: String?, val failureStatus: FailureStatus) : SignUpOtpState()
    data class CountDownTimer(val timeRemaining: Int) : SignUpOtpState()
    object InvalidOTP : SignUpOtpState()
    data class OTPCleared(val isFirstTimeLaunched: Boolean) : SignUpOtpState()
}