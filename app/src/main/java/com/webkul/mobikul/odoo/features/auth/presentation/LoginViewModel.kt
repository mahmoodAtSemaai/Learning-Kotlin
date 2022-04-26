package com.webkul.mobikul.odoo.features.auth.presentation


import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.features.auth.domain.usecase.LogInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val logInUseCase: LogInUseCase,
    private val appPreferences: AppPreferences
) : BaseViewModel() {

    val userIntent = Channel<LoginIntent>(Channel.UNLIMITED)
    private val userAction = Channel<LoginAction>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state: StateFlow<LoginState>
        get() = _state


    init {
        handleIntent()
        handleAction()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is LoginIntent.Login -> userAction.send(LoginAction.Login(it.username, it.password))
                }
            }
        }
    }

    private fun handleAction() {
        viewModelScope.launch {
            userAction.consumeAsFlow().collect {
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
                LoginState.Login(logInUseCase.invoke())
            } catch (e: Exception) {
                LoginState.Error(e.localizedMessage)
            }
        }
    }


}