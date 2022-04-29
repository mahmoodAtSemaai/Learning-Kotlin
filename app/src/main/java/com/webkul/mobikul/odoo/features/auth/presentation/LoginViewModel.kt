package com.webkul.mobikul.odoo.features.auth.presentation


import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.features.auth.domain.usecase.LogInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val logInUseCase: LogInUseCase,
) : BaseViewModel() {

    val loginIntent = Channel<LoginIntent>(Channel.UNLIMITED)

    private val loginAction = Channel<LoginAction>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state: StateFlow<LoginState>
        get() = _state


    init {
        handleLoginIntent()
        handleLoginAction()
    }

    private fun handleLoginIntent() {
        viewModelScope.launch {
            loginIntent.consumeAsFlow().collect {
                when (it) {
                    is LoginIntent.Login -> loginAction.send(
                        LoginAction.Login(
                            it.username,
                            it.password
                        )
                    )
                }
            }
        }
    }

    private fun handleLoginAction() {
        viewModelScope.launch {
            loginAction.consumeAsFlow().collect {
                when (it) {
                    is LoginAction.Login -> loginUser(it.username, it.password)
                }
            }
        }
    }

    private fun loginUser(username: String, password: String) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            _state.value = try {
                val login = logInUseCase.invoke(username, password)
                var loginState: LoginState = LoginState.Idle
                login.collect {
                    when (it) {
                        is Resource.Default -> {
                        }
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