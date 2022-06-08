package com.webkul.mobikul.odoo.features.authentication.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.features.authentication.data.models.OtpAuthenticationRequest
import com.webkul.mobikul.odoo.features.authentication.domain.usecase.GenerateOtpUseCase
import com.webkul.mobikul.odoo.features.authentication.domain.usecase.HomePageDataUseCase
import com.webkul.mobikul.odoo.features.authentication.domain.usecase.SplashPageUseCase
import com.webkul.mobikul.odoo.features.authentication.domain.usecase.VerifyOtpUseCase
import com.webkul.mobikul.odoo.features.authentication.presentation.intent.LoginOtpIntent
import com.webkul.mobikul.odoo.features.authentication.presentation.state.LoginOtpState
import com.webkul.mobikul.odoo.features.onboarding.domain.usecase.CountdownTimerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginOtpViewModel @Inject constructor(
    private val generateOtpUseCase: GenerateOtpUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val splashPageUseCase: SplashPageUseCase,
    private val homePageDataUseCase: HomePageDataUseCase,
    private val countdownTimerUseCase: CountdownTimerUseCase
) : BaseViewModel(), IModel<LoginOtpState, LoginOtpIntent> {

    override val intents: Channel<LoginOtpIntent> = Channel(Channel.UNLIMITED)
    private val _state = MutableStateFlow<LoginOtpState>(LoginOtpState.Idle)
    override val state: StateFlow<LoginOtpState>
        get() = _state

    private var countDownTimer: Job = Job()


    init {
        handlerIntent()
    }

    override fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect {
                when (it) {
                    is LoginOtpIntent.GenerateOtp -> generateOTP(it.phoneNumber)
                    is LoginOtpIntent.Login -> loginViaOtpJWTToken(
                        it.phoneNumber,
                        it.otpAuthenticationRequest
                    )
                    is LoginOtpIntent.ResendOtp -> generateOTP(it.phoneNumber)
                    is LoginOtpIntent.GetOTPFromInput -> verifyOTP(it.phoneNumber, it.otp)
                    is LoginOtpIntent.OTPExpired -> otpExpired()
                    is LoginOtpIntent.GetSplashData -> getSplashData()
                    is LoginOtpIntent.GetHomePageData -> getHomePageData()
                    is LoginOtpIntent.StartTimer -> startTimer(it.time)
                    is LoginOtpIntent.StopTimer -> stopTimer()
                }
            }
        }
    }

    private fun startTimer(time: Int) {
        countDownTimer = viewModelScope.launch {
            val timerData = countdownTimerUseCase.invoke(time,1_000)

            timerData.collect {
                _state.value = LoginOtpState.CountDownTimer(it)
                if (it == 0) {
                    otpExpired()
                }
            }
        }
    }

    private fun stopTimer(){
        countDownTimer.cancel()
    }


    private fun otpExpired() {
        viewModelScope.launch {
            _state.value = LoginOtpState.OTPExpired
        }
    }

    private fun verifyOTP(phoneNumber: String, otp: String) {
        viewModelScope.launch {
            if (otp.length == 4) {
                _state.value = LoginOtpState.Loading

                _state.value = try {
                    val verifyOTP =
                        verifyOtpUseCase.invoke(phoneNumber, OtpAuthenticationRequest(otp = otp))
                    var verifyOTPState: LoginOtpState = LoginOtpState.Idle

                    verifyOTP.collect {
                        verifyOTPState = when (it) {
                            is Resource.Default -> LoginOtpState.Idle
                            is Resource.Loading -> LoginOtpState.Loading
                            is Resource.Success -> LoginOtpState.OTPVerified
                            is Resource.Failure -> LoginOtpState.InvalidOTP
                        }
                    }
                    verifyOTPState
                } catch (e: Exception) {
                    LoginOtpState.Error(e.localizedMessage, FailureStatus.OTHER)
                }
            }
        }
    }

    private fun generateOTP(phoneNumber: String) {
        viewModelScope.launch {
            _state.value = LoginOtpState.Loading
            _state.value = try {
                val generatedOtp = generateOtpUseCase.invoke(phoneNumber)
                var generateOtpState: LoginOtpState = LoginOtpState.Idle

                generatedOtp.collect {
                    generateOtpState = when (it) {
                        is Resource.Default -> LoginOtpState.Idle
                        is Resource.Loading -> LoginOtpState.Loading
                        is Resource.Success -> LoginOtpState.OTPSent
                        is Resource.Failure -> LoginOtpState.Error(it.message, it.failureStatus)
                    }
                }
                generateOtpState
            } catch (e: Exception) {
                LoginOtpState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun loginViaOtpJWTToken(
        phoneNumber: String,
        otpAuthenticationRequest: OtpAuthenticationRequest
    ) {
        viewModelScope.launch {
            _state.value = LoginOtpState.Loading
            _state.value = try {
                val verifyOtp = verifyOtpUseCase.invoke(phoneNumber, otpAuthenticationRequest)
                var verifyOtpState: LoginOtpState = LoginOtpState.Idle

                verifyOtp.collect {
                    verifyOtpState = when (it) {
                        is Resource.Default -> LoginOtpState.Idle
                        is Resource.Loading -> LoginOtpState.Loading
                        is Resource.Success -> LoginOtpState.Login(it.value)
                        is Resource.Failure -> LoginOtpState.Error(it.message, it.failureStatus)
                    }
                }
                verifyOtpState
            } catch (e: Exception) {
                LoginOtpState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun getSplashData() {
        viewModelScope.launch {
            _state.value = LoginOtpState.Loading
            _state.value = try {
                val splash = splashPageUseCase.invoke()
                var loginOtpState: LoginOtpState = LoginOtpState.Loading

                splash.collect {
                    loginOtpState = when (it) {
                        is Resource.Default -> LoginOtpState.Idle
                        is Resource.Loading -> LoginOtpState.Loading
                        is Resource.Failure -> LoginOtpState.Error(it.message, it.failureStatus)
                        is Resource.Success -> {
                            if (it.value.isUserApproved)
                                LoginOtpState.Splash(it.value)
                            else
                                LoginOtpState.UnauthorisedUser
                        }
                    }
                }
                loginOtpState
            } catch (e: Exception) {
                LoginOtpState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun getHomePageData() {
        viewModelScope.launch {
            _state.value = LoginOtpState.Loading
            _state.value = try {
                val homePage = homePageDataUseCase.invoke()
                var loginOtpState: LoginOtpState = LoginOtpState.Loading

                homePage.collect {
                    loginOtpState = when (it) {
                        is Resource.Default -> LoginOtpState.Idle
                        is Resource.Loading -> LoginOtpState.Loading
                        is Resource.Failure -> LoginOtpState.Error(
                            it.message,
                            it.failureStatus
                        )
                        is Resource.Success -> LoginOtpState.HomePage(it.value)
                    }
                }
                loginOtpState
            } catch (e: Exception) {
                LoginOtpState.Error(e.localizedMessage, FailureStatus.OTHER)
            }

        }
    }


}