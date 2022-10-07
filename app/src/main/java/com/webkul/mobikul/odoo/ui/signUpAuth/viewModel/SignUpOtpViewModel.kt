package com.webkul.mobikul.odoo.ui.signUpAuth.viewModel

import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.request.SignUpOtpAuthRequest
import com.webkul.mobikul.odoo.domain.enums.VerifyOTPValidation
import com.webkul.mobikul.odoo.domain.usecase.auth.GenerateSignUpOtpUseCase
import com.webkul.mobikul.odoo.domain.usecase.auth.VerifySignUpOtpUseCase
import com.webkul.mobikul.odoo.domain.usecase.onboarding.CountdownTimerUseCase
import com.webkul.mobikul.odoo.ui.signUpAuth.effect.SignUpOtpEffect
import com.webkul.mobikul.odoo.ui.signUpAuth.intent.SignUpOtpIntent
import com.webkul.mobikul.odoo.ui.signUpAuth.state.SignUpOtpState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpOtpViewModel @Inject constructor(
    private val countdownTimerUseCase: CountdownTimerUseCase,
    private val generateOtpUseCase: GenerateSignUpOtpUseCase,
    private val verifyOtpUseCase: VerifySignUpOtpUseCase
) : BaseViewModel(),
    IModel<SignUpOtpState, SignUpOtpIntent, SignUpOtpEffect> {

    override val intents: Channel<SignUpOtpIntent> = Channel(Channel.UNLIMITED)
    private val _state = MutableStateFlow<SignUpOtpState>(SignUpOtpState.Idle)
    override val state: StateFlow<SignUpOtpState>
        get() = _state

    private val _effect = Channel<SignUpOtpEffect>()
    override val effect: Flow<SignUpOtpEffect>
        get() = _effect.receiveAsFlow()

    private var countDownTimer: Job = Job()


    init {
        handlerIntent()
    }

    override fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect {
                when (it) {
                    is SignUpOtpIntent.GenerateOtp -> generateOTP(it.phoneNumber)
                    is SignUpOtpIntent.ResendOtp -> generateOTP(it.phoneNumber)
                    is SignUpOtpIntent.GetOTPFromInput -> verifyOTP(it.phoneNumber, it.otp)
                    is SignUpOtpIntent.OTPExpired -> otpExpired()
                    is SignUpOtpIntent.StartTimer -> startTimer(it.time)
                    is SignUpOtpIntent.StopTimer -> stopTimer()
                    is SignUpOtpIntent.ClearOTP -> clearOTP(it.firstTimeLaunched)
                }
            }
        }
    }


    private fun clearOTP(firstTimeLaunched: Boolean) {
        _state.value = SignUpOtpState.OTPCleared(firstTimeLaunched)
    }

    private fun startTimer(time: Int) {
        countDownTimer = viewModelScope.launch {
            val timerData = countdownTimerUseCase(time, 1_000)

            timerData.collect {
                _state.value = SignUpOtpState.CountDownTimer(it)
                if (it == 0) {
                    otpExpired()
                }
            }
        }
    }

    private fun stopTimer() {
        countDownTimer.cancel()
    }


    private fun otpExpired() {
        _state.value = SignUpOtpState.OTPExpired
    }

    private fun verifyOTP(phoneNumber: String, otp: String) {
        viewModelScope.launch {
            if (otp.length == 4) {
                _state.value = SignUpOtpState.Loading
                try {
                    val verifyOTP =
                        verifyOtpUseCase(
                            SignUpOtpAuthRequest(
                                otp = otp,
                                login = phoneNumber
                            )
                        )
                    verifyOTP.catch {
                        when (it.message?.toInt()) {
                            VerifyOTPValidation.INVALID_OTP.value ->
                                _state.value = SignUpOtpState.InvalidOTP
                        }
                    }.collect {
                        when (it) {
                            is Resource.Default -> _state.value = SignUpOtpState.Idle
                            is Resource.Loading -> _state.value = SignUpOtpState.Loading
                            is Resource.Success -> {
                                stopTimer()
                                _effect.send(SignUpOtpEffect.NavigateToUserOnboarding)
                            }
                            is Resource.Failure -> _state.value = SignUpOtpState.Error(
                                it.message,
                                it.failureStatus
                            )
                        }
                    }
                } catch (e: Exception) {
                    _state.value = SignUpOtpState.Error(e.localizedMessage, FailureStatus.OTHER)
                }
            }
        }
    }

    private fun generateOTP(phoneNumber: String) {
        viewModelScope.launch {
            _state.value = SignUpOtpState.Loading
            _state.value = try {
                val generatedOtp = generateOtpUseCase(phoneNumber)
                var generateOtpState: SignUpOtpState = SignUpOtpState.Idle

                generatedOtp.collect {
                    generateOtpState = when (it) {
                        is Resource.Default -> SignUpOtpState.Idle
                        is Resource.Loading -> SignUpOtpState.Loading
                        is Resource.Success -> SignUpOtpState.OTPSent
                        is Resource.Failure -> SignUpOtpState.Error(it.message, it.failureStatus)
                    }
                }
                generateOtpState
            } catch (e: Exception) {
                SignUpOtpState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }
}