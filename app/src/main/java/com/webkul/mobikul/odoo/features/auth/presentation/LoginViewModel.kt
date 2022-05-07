package com.webkul.mobikul.odoo.features.auth.presentation


import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.features.auth.domain.enums.LoginFieldsValidation
import com.webkul.mobikul.odoo.features.auth.domain.usecase.LogInUseCase
import com.webkul.mobikul.odoo.features.auth.domain.usecase.ViewPrivacyPolicyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val logInUseCase: LogInUseCase,
    private val viewPrivacyPolicyUseCase: ViewPrivacyPolicyUseCase
) : BaseViewModel(), IModel<LoginState, LoginIntent> {


    override val intents: Channel<LoginIntent> = Channel<LoginIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    override val state: StateFlow<LoginState>
        get() = _state


    init {
        handlerIntent()
    }

    override fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect {
                when (it) {
                    is LoginIntent.Login -> loginUser(it.username, it.password)
                    is LoginIntent.PrivacyPolicy -> {
                        viewPrivacyPolicy()
                    }
                }
            }
        }
    }

    private fun viewPrivacyPolicy() {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            _state.value = try {
                val intent = viewPrivacyPolicyUseCase.invoke()
                var loginState: LoginState = LoginState.Idle

                intent.collect{
                    loginState = when(it){
                        is  Resource.Default -> TODO()
                        is Resource.Failure -> LoginState.Error(it.message , it.failureStatus)
                        is  Resource.Loading -> LoginState.Loading
                        is Resource.Success -> LoginState.PrivacyPolicy(it.value)
                    }
                }

                loginState
            } catch (e: Exception) {
                LoginState.Error(e.localizedMessage , FailureStatus.OTHER)
            }

        }
    }


    private fun loginUser(username: String, password: String) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            _state.value = try {
                val login = logInUseCase.invoke(username, password)
                var loginState: LoginState = LoginState.Idle

                login.catch {
                        when(it.message?.toInt()){
                            LoginFieldsValidation.EMPTY_EMAIL.value -> loginState =  LoginState.InvalidLoginDetailsError(LoginFieldsValidation.EMPTY_EMAIL)
                            LoginFieldsValidation.EMPTY_PASSWORD.value ->loginState =  LoginState.InvalidLoginDetailsError(LoginFieldsValidation.EMPTY_PASSWORD)
                            LoginFieldsValidation.INVALID_PASSWORD.value ->loginState =  LoginState.InvalidLoginDetailsError(LoginFieldsValidation.INVALID_PASSWORD)
                          //  LoginFieldsValidation.INVALID_LOGIN_DETAILS.value ->loginState =  LoginState.InvalidLoginDetailsError(LoginFieldsValidation.INVALID_LOGIN_DETAILS)

                        }
                }.collect {
                    when (it) {
                        is Resource.Default -> {}
                        is Resource.Failure -> loginState = LoginState.Error(it.message, it.failureStatus)
                        is Resource.Loading -> loginState = LoginState.Loading
                        is Resource.Success -> loginState = LoginState.Login(it.value)
                    }
                }
                loginState
            } catch (e: Exception) {
                LoginState.Error(e.localizedMessage , FailureStatus.OTHER)
            }
        }
    }



}