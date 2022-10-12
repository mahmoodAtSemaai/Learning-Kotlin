package com.webkul.mobikul.odoo.ui.authentication.viewmodel

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.UserEntity
import com.webkul.mobikul.odoo.data.request.OtpAuthenticationRequest
import com.webkul.mobikul.odoo.domain.enums.VerifyOTPValidation
import com.webkul.mobikul.odoo.domain.usecase.auth.GenerateOtpUseCase
import com.webkul.mobikul.odoo.domain.usecase.auth.VerifyOtpUseCase
import com.webkul.mobikul.odoo.domain.usecase.chat.CreateChatChannelUseCase
import com.webkul.mobikul.odoo.domain.usecase.fcmToken.RegisterFCMTokenUseCase
import com.webkul.mobikul.odoo.domain.usecase.home.HomeUseCase
import com.webkul.mobikul.odoo.domain.usecase.onboarding.CountdownTimerUseCase
import com.webkul.mobikul.odoo.domain.usecase.splash.SplashUseCase
import com.webkul.mobikul.odoo.domain.usecase.user.GetUserUseCase
import com.webkul.mobikul.odoo.model.request.RegisterDeviceTokenRequest
import com.webkul.mobikul.odoo.ui.authentication.effect.LoginOtpEffect
import com.webkul.mobikul.odoo.ui.authentication.intent.LoginOtpIntent
import com.webkul.mobikul.odoo.ui.authentication.state.LoginOtpState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginOtpViewModel @Inject constructor(
        private val generateOtpUseCase: GenerateOtpUseCase,
        private val verifyOtpUseCase: VerifyOtpUseCase,
        private val splashPageUseCase: SplashUseCase,
        private val homeUseCase: HomeUseCase,
        private val countdownTimerUseCase: CountdownTimerUseCase,
        private val createChatChannelUseCase: CreateChatChannelUseCase,
        private val getUserUseCase: GetUserUseCase,
        private val registerFCMTokenUseCase: RegisterFCMTokenUseCase
) : BaseViewModel(), IModel<LoginOtpState, LoginOtpIntent, LoginOtpEffect> {

    override val intents: Channel<LoginOtpIntent> = Channel(Channel.UNLIMITED)
    private val _state = MutableStateFlow<LoginOtpState>(LoginOtpState.Idle)
    override val state: StateFlow<LoginOtpState>
        get() = _state

    private val _effect = Channel<LoginOtpEffect>()
    override val effect: Flow<LoginOtpEffect>
        get() = _effect.receiveAsFlow()

    private var countDownTimer: Job = Job()
    private var userEntity: UserEntity? = null


    init {
        handlerIntent()
    }

    override fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect {
                when (it) {
                    is LoginOtpIntent.GenerateOtp -> generateOTP(it.phoneNumber)
                    is LoginOtpIntent.ResendOtp -> generateOTP(it.phoneNumber)
                    is LoginOtpIntent.GetOTPFromInput -> verifyOTP(it.otp)
                    is LoginOtpIntent.OTPExpired -> otpExpired()
                    is LoginOtpIntent.GetSplashData -> getSplashData()
                    is LoginOtpIntent.GetHomePageData -> getHomePageData()
                    is LoginOtpIntent.CreateChatChannel -> createChatChannel()
                    is LoginOtpIntent.StartTimer -> startTimer(it.time)
                    is LoginOtpIntent.StopTimer -> stopTimer()
                    is LoginOtpIntent.ClearOTP -> clearOTP(it.firstTimeLaunched)
                    is LoginOtpIntent.GetArguments -> setArguments(it.arguments)
                    is LoginOtpIntent.DispatchBackPressed -> setupBackPressedAction()
                    is LoginOtpIntent.RequestFocus -> setFocusOnInputField()
                    is LoginOtpIntent.GetUser -> getUser()
                    is LoginOtpIntent.RegisterFCMToken -> registerFCMToken(it.registerDeviceTokenRequest)
                }
            }
        }
    }

    private fun setFocusOnInputField() {
        _state.value = LoginOtpState.SetFocus
    }

    private fun setupBackPressedAction() {
        _state.value = LoginOtpState.SetupBackPressedListener
    }


    private fun setArguments(arguments: Bundle) {
        val phoneNumber = arguments.getString(BundleConstant.BUNDLE_KEY_PHONE_NUMBER) ?: ""
        _state.value = LoginOtpState.SetPhoneNumber(phoneNumber)
    }

    private fun clearOTP(firstTimeLaunched: Boolean) {
        _state.value = LoginOtpState.OTPCleared(firstTimeLaunched)
    }

    private fun startTimer(time: Int) {
        countDownTimer = viewModelScope.launch {
            val timerData = countdownTimerUseCase(time, 1_000)
            timerData.collect {
                _state.value = LoginOtpState.CountDownTimer(it)
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
        _state.value = LoginOtpState.OTPExpired
    }

    private fun generateOTP(phoneNumber: String) {
        viewModelScope.launch {
            _state.value = LoginOtpState.Loading
            try {
                val generatedOtp = generateOtpUseCase(phoneNumber)
                generatedOtp.collect {
                    when (it) {
                        is Resource.Default -> _state.value = LoginOtpState.Idle
                        is Resource.Loading -> _state.value = LoginOtpState.Loading
                        is Resource.Success -> _state.value = LoginOtpState.OTPSent
                        is Resource.Failure -> _state.value =
                                LoginOtpState.Error(it.message, it.failureStatus)
                    }
                }
            } catch (e: Exception) {
                _state.value = LoginOtpState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun verifyOTP(otpRequest: OtpAuthenticationRequest) {
        viewModelScope.launch {
            try {
                val verifyOTP =
                        verifyOtpUseCase(otpRequest)
                verifyOTP.catch {
                    when (it.message?.toInt()) {
                        VerifyOTPValidation.INVALID_OTP.value ->
                            _state.value = LoginOtpState.InvalidOTP
                    }
                }.collect {
                    when (it) {
                        is Resource.Default -> _state.value = LoginOtpState.Idle
                        is Resource.Loading -> _state.value = LoginOtpState.Loading
                        is Resource.Success -> _state.value = LoginOtpState.OTPVerified
                        is Resource.Failure -> _state.value = LoginOtpState.Error(
                                it.message,
                                it.failureStatus
                        )
                    }
                }
            } catch (e: Exception) {
                _state.value = LoginOtpState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun getSplashData() {
        viewModelScope.launch {
            _state.value = LoginOtpState.Loading
            try {
                val splash = splashPageUseCase()
                splash.collect {
                    when (it) {
                        is Resource.Default -> _state.value = LoginOtpState.Idle
                        is Resource.Loading -> _state.value = LoginOtpState.Loading
                        is Resource.Failure -> _state.value =
                                LoginOtpState.Error(it.message, it.failureStatus)
                        is Resource.Success -> _state.value = LoginOtpState.Splash(it.value)
                    }
                }
            } catch (e: Exception) {
                _state.value = LoginOtpState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun getUser() {
        viewModelScope.launch {
            _state.value = LoginOtpState.Loading
            try {
                val user = getUserUseCase()
                user.collect {
                    when (it) {
                        is Resource.Success -> {
                            userEntity = it.value
                            _state.value = LoginOtpState.UserFetched
                        }
                        else -> _state.value = LoginOtpState.UserFetched
                    }
                }
            }catch (e: Exception) {
                _state.value = LoginOtpState.UserFetched
            }
        }
    }

    private fun getHomePageData() {
        viewModelScope.launch {
            _state.value = LoginOtpState.Loading
            try {
                val homePage = homeUseCase()
                homePage.collect {
                    when (it) {
                        is Resource.Default -> _state.value = LoginOtpState.Idle
                        is Resource.Loading -> _state.value = LoginOtpState.Loading
                        is Resource.Failure -> _state.value = LoginOtpState.Error(
                                it.message,
                                it.failureStatus
                        )
                        is Resource.Success -> _effect.send(
                                LoginOtpEffect.NavigateToUserHomeActivity
                        )
                    }
                }
            } catch (e: Exception) {
                _state.value = LoginOtpState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun createChatChannel() {
        viewModelScope.launch {
            try {
                createChatChannelUseCase(userEntity).collect()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun registerFCMToken(registerDeviceTokenRequest: RegisterDeviceTokenRequest) {
        viewModelScope.launch {
            _state.value = LoginOtpState.Loading
            _state.value = try {
                val registerFCMToken = registerFCMTokenUseCase(registerDeviceTokenRequest)
                var registerFCMTokenState : LoginOtpState = LoginOtpState.Loading
                registerFCMToken.collect{
                    registerFCMTokenState = LoginOtpState.RegisterFCMTokenState
                }
                registerFCMTokenState
            } catch (e: Exception) {
                LoginOtpState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

}