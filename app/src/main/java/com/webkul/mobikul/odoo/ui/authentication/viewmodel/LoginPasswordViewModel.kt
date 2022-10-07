package com.webkul.mobikul.odoo.ui.authentication.viewmodel

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.UserEntity
import com.webkul.mobikul.odoo.data.request.LoginOtpAuthenticationRequest
import com.webkul.mobikul.odoo.domain.usecase.auth.LoginPasswordUseCase
import com.webkul.mobikul.odoo.domain.usecase.chat.CreateChatChannelUseCase
import com.webkul.mobikul.odoo.domain.usecase.fcmToken.RegisterFCMTokenUseCase
import com.webkul.mobikul.odoo.domain.usecase.home.HomeUseCase
import com.webkul.mobikul.odoo.domain.usecase.splash.SplashUseCase
import com.webkul.mobikul.odoo.domain.usecase.user.GetUserUseCase
import com.webkul.mobikul.odoo.model.request.RegisterDeviceTokenRequest
import com.webkul.mobikul.odoo.ui.authentication.effect.LoginPasswordEffect
import com.webkul.mobikul.odoo.ui.authentication.intent.LoginPasswordIntent
import com.webkul.mobikul.odoo.ui.authentication.state.LoginPasswordState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginPasswordViewModel @Inject constructor(
    private val loginPasswordUseCase: LoginPasswordUseCase,
    private val splashUseCase: SplashUseCase,
    private val homeUseCase: HomeUseCase,
    private val createChatChannelUseCase: CreateChatChannelUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val registerFCMTokenUseCase: RegisterFCMTokenUseCase
) : BaseViewModel(), IModel<LoginPasswordState, LoginPasswordIntent, LoginPasswordEffect> {

    override val intents: Channel<LoginPasswordIntent> = Channel(Channel.UNLIMITED)
    private val _state = MutableStateFlow<LoginPasswordState>(LoginPasswordState.Idle)
    override val state: StateFlow<LoginPasswordState>
        get() = _state

    private val _effect = Channel<LoginPasswordEffect>()
    override val effect: Flow<LoginPasswordEffect>
        get() = _effect.receiveAsFlow()

    private var userEntity: UserEntity? = null

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
                    is LoginPasswordIntent.CreateChatChannel -> createChatChannel()
                    is LoginPasswordIntent.Default -> setIdleState()
                    is LoginPasswordIntent.GetArguments -> setArguments(it.arguments)
                    is LoginPasswordIntent.SetupViews -> setViews()
                    is LoginPasswordIntent.GetUser -> getUser()
                    is LoginPasswordIntent.RegisterFCMToken -> registerFCMToken(it.registerDeviceTokenRequest)
                }
            }
        }
    }

    private fun setViews() {
        _state.value = LoginPasswordState.InitialiseViews
    }

    private fun setArguments(arguments: Bundle) {
        val phoneNumber = arguments.getString(BundleConstant.BUNDLE_KEY_PHONE_NUMBER) ?: ""
        _state.value = LoginPasswordState.SetPhoneNumber(phoneNumber)
    }

    private fun forgetPassword() {
        _state.value = LoginPasswordState.ForgotPassword
    }

    private fun setIdleState() {
        _state.value = LoginPasswordState.Idle
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
            try {
                val login = loginPasswordUseCase(loginOtpAuthenticationRequest)
                login.catch {
                    _state.value = LoginPasswordState.InvalidPasswordError(it.message!!)
                }.collect {
                    when (it) {
                        is Resource.Default -> _state.value = LoginPasswordState.Idle
                        is Resource.Loading -> _state.value = LoginPasswordState.Loading
                        is Resource.Failure -> _state.value = LoginPasswordState.Error(
                            it.message,
                            it.failureStatus
                        )
                        is Resource.Success -> _state.value = LoginPasswordState.LoggedIn(it.value)
                    }
                }
            } catch (e: Exception) {
                _state.value = LoginPasswordState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun getSplashData() {
        viewModelScope.launch {
            _state.value = LoginPasswordState.Loading

            val splash = splashUseCase()
            splash.collect {
                when (it) {
                    is Resource.Default -> _state.value = LoginPasswordState.Idle
                    is Resource.Loading -> _state.value = LoginPasswordState.Loading
                    is Resource.Failure -> {
                        _state.value = LoginPasswordState.Error(
                            it.message,
                            it.failureStatus
                        )
                    }
                    is Resource.Success -> {
                        _state.value = LoginPasswordState.Splash(it.value)
                    }
                }
            }

        }
    }

    private fun getUser() {
        viewModelScope.launch {
            _state.value = LoginPasswordState.Loading
            try {
                val user = getUserUseCase()
                user.collect {
                    when (it) {
                        is Resource.Success -> {
                            userEntity = it.value
                            _state.value = LoginPasswordState.UserFetched
                        }
                        else -> _state.value = LoginPasswordState.UserFetched
                    }
                }
            } catch (e: Exception) {
                _state.value = LoginPasswordState.UserFetched
            }
        }
    }

    private fun getHomePageData() {
        viewModelScope.launch {
            _state.value = LoginPasswordState.Loading
            try {
                val homePage = homeUseCase()
                homePage.collect {
                    when (it) {
                        is Resource.Default -> _state.value = LoginPasswordState.Idle
                        is Resource.Loading -> _state.value = LoginPasswordState.Loading
                        is Resource.Failure -> _state.value = LoginPasswordState.Error(
                            it.message,
                            it.failureStatus
                        )
                        is Resource.Success -> _effect.send(
                            LoginPasswordEffect.NavigateToUserHomeActivity
                        )

                    }
                }
            } catch (e: Exception) {
                _state.value = LoginPasswordState.Error(e.localizedMessage, FailureStatus.OTHER)
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
            _state.value = LoginPasswordState.Loading
            _state.value = try {
                val registerFCMToken = registerFCMTokenUseCase(registerDeviceTokenRequest)
                var registerFCMTokenState: LoginPasswordState = LoginPasswordState.Loading
                registerFCMToken.collect {
                    registerFCMTokenState = LoginPasswordState.RegisterFCMTokenState
                }
                registerFCMTokenState
            } catch (e: Exception) {
                LoginPasswordState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

}