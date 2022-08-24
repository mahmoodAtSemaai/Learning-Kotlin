package com.webkul.mobikul.odoo.features.authentication.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.domain.usecase.fcmToken.RegisterFCMTokenUseCase
import com.webkul.mobikul.odoo.features.authentication.data.models.LoginOtpAuthenticationRequest
import com.webkul.mobikul.odoo.features.authentication.domain.usecase.HomePageDataUseCase
import com.webkul.mobikul.odoo.features.authentication.domain.usecase.LoginPasswordUseCase
import com.webkul.mobikul.odoo.features.authentication.domain.usecase.SplashPageUseCase
import com.webkul.mobikul.odoo.features.authentication.domain.usecase.VerifyPasswordUseCase
import com.webkul.mobikul.odoo.features.authentication.presentation.effect.LoginPasswordEffect
import com.webkul.mobikul.odoo.features.authentication.presentation.intent.LoginPasswordIntent
import com.webkul.mobikul.odoo.features.authentication.presentation.state.LoginPasswordState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginPasswordViewModel @Inject constructor(
        private val loginPasswordUseCase: LoginPasswordUseCase,
        private val verifyPasswordUseCase: VerifyPasswordUseCase,
        private val splashPageUseCase: SplashPageUseCase,
        private val homePageDataUseCase: HomePageDataUseCase,
        private val registerFCMTokenUseCase: RegisterFCMTokenUseCase
) : BaseViewModel(), IModel<LoginPasswordState, LoginPasswordIntent, LoginPasswordEffect> {

    override val intents: Channel<LoginPasswordIntent> = Channel(Channel.UNLIMITED)
    private val _state = MutableStateFlow<LoginPasswordState>(LoginPasswordState.Idle)
    override val state: StateFlow<LoginPasswordState>
        get() = _state

    private val _effect = Channel<LoginPasswordEffect>()
    override val effect: Flow<LoginPasswordEffect>
        get() = _effect.receiveAsFlow()

    init {
        handlerIntent()
    }

    override fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect {
                when (it) {
                    is LoginPasswordIntent.Login -> loginViaJWTToken(it.loginOtpAuthRequest)
                    is LoginPasswordIntent.ForgetPassword -> forgetPassword()
                    is LoginPasswordIntent.PasswordReceived -> setButtonState(it.password)
                    is LoginPasswordIntent.SplashPage -> getSplashData()
                    is LoginPasswordIntent.HomePage -> getHomePageData()
                    is LoginPasswordIntent.Default -> setIdleState()
                    is LoginPasswordIntent.RegisterFCMToken -> registerFCMToken()
                }
            }
        }
    }

    private fun forgetPassword() {
        viewModelScope.launch {
            _state.value = LoginPasswordState.ForgotPassword
        }
    }

    private fun setIdleState() {
        viewModelScope.launch {
            _state.value = LoginPasswordState.Idle
        }
    }

    private fun setButtonState(password: String) {
        _state.value =
                if (password.isEmpty()) LoginPasswordState.DisableButton
                else LoginPasswordState.EnableButton
    }


    private fun loginViaJWTToken(
            loginOtpAuthenticationRequest: LoginOtpAuthenticationRequest
    ) {
        viewModelScope.launch {
            _state.value = LoginPasswordState.Loading

            _state.value = try {
                val login = loginPasswordUseCase.invoke(loginOtpAuthenticationRequest)
                var loginPasswordState: LoginPasswordState = LoginPasswordState.Loading

                login.collect {
                    loginPasswordState = when (it) {
                        is Resource.Default -> LoginPasswordState.Idle
                        is Resource.Loading -> LoginPasswordState.Loading
                        is Resource.Failure -> LoginPasswordState.Error(
                                it.message,
                                it.failureStatus
                        )
                        is Resource.Success -> LoginPasswordState.LoggedIn(it.value)
                    }
                }
                loginPasswordState
            } catch (e: Exception) {
                LoginPasswordState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun getSplashData() {
        viewModelScope.launch {
            _state.value = LoginPasswordState.Loading
            _state.value = try {
                val splash = splashPageUseCase.invoke()
                var loginPasswordState: LoginPasswordState = LoginPasswordState.Loading

                splash.collect {
                    loginPasswordState = when (it) {
                        is Resource.Default -> LoginPasswordState.Idle
                        is Resource.Loading -> LoginPasswordState.Loading
                        is Resource.Failure -> LoginPasswordState.Error(
                                it.message,
                                it.failureStatus
                        )
                        is Resource.Success -> {
                            LoginPasswordState.Splash(it.value)
                        }
                    }
                }
                loginPasswordState
            } catch (e: Exception) {
                LoginPasswordState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun getHomePageData() {
        viewModelScope.launch {
            _state.value = LoginPasswordState.Loading
            _state.value = try {
                val homePage = homePageDataUseCase.invoke()
                var loginPasswordState: LoginPasswordState = LoginPasswordState.Loading

                homePage.collect {
                    loginPasswordState = when (it) {
                        is Resource.Default -> LoginPasswordState.Idle
                        is Resource.Loading -> LoginPasswordState.Loading
                        is Resource.Failure -> LoginPasswordState.Error(
                                it.message,
                                it.failureStatus
                        )
                        is Resource.Success -> LoginPasswordState.HomePage(it.value)
                    }
                }
                loginPasswordState
            } catch (e: Exception) {
                LoginPasswordState.Error(e.localizedMessage, FailureStatus.OTHER)
            }

        }
    }


    private fun registerFCMToken() {
        viewModelScope.launch {
            _state.value = LoginPasswordState.Loading
            _state.value = try {
                val registerFCMToken = registerFCMTokenUseCase()
                var registerFCMTokenState : LoginPasswordState = LoginPasswordState.Loading
                registerFCMToken.collect{
                    registerFCMTokenState = LoginPasswordState.RegisterFCMTokenState
                }
                registerFCMTokenState
            } catch (e: Exception) {
                LoginPasswordState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

}