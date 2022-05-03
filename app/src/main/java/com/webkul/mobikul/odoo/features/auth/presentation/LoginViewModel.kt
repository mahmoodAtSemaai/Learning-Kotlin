package com.webkul.mobikul.odoo.features.auth.presentation


import android.util.Log
import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.features.auth.domain.enums.AuthFieldsValidation
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

    val loginIntent = Channel<LoginIntent>(Channel.UNLIMITED)

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
                        Log.d("testing" , "Intent recived 1 ")

                    }
                }
            }
        }
    }

    private fun viewPrivacyPolicy() {
        viewModelScope.launch {
            Log.d("testing" , "Intent recived 2 ")

            _state.value = LoginState.Loading
            _state.value = try {
                val intent = viewPrivacyPolicyUseCase.invoke()
                var loginState: LoginState = LoginState.Idle

                intent.collect{
                    loginState = when(it){
                        is  Resource.Default -> TODO()
                        is Resource.Failure -> LoginState.Error("Error Message")
                        is  Resource.Loading -> LoginState.Loading
                        is Resource.Success -> LoginState.PrivacyPolicy(it.value)
                    }
                }

                loginState
            } catch (e: Exception) {
                LoginState.Error(e.localizedMessage)
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
                    when (it.message?.toInt()) {
                        AuthFieldsValidation.EMPTY_EMAIL.value -> loginState =
                            LoginState.InvalidLoginDetailsError(AuthFieldsValidation.EMPTY_EMAIL)
                        AuthFieldsValidation.EMPTY_PASSWORD.value -> loginState =
                            LoginState.InvalidLoginDetailsError(AuthFieldsValidation.EMPTY_PASSWORD)
                        AuthFieldsValidation.INVALID_PASSWORD.value -> loginState =
                            LoginState.InvalidLoginDetailsError(AuthFieldsValidation.INVALID_PASSWORD)
                    }
                }.collect {
                    when (it) {
                        is Resource.Default -> {}
                        is Resource.Failure -> loginState = LoginState.Error("Error Message")
                        is Resource.Loading -> loginState = LoginState.Loading
                        is Resource.Success -> loginState = LoginState.Login(it.value)
                    }
                }
                loginState
            } catch (e: Exception) {
                LoginState.Error(e.localizedMessage)
            }
        }
    }


}