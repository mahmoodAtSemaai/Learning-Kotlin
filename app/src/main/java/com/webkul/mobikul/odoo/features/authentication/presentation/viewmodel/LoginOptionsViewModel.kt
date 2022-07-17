package com.webkul.mobikul.odoo.features.authentication.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.features.authentication.domain.usecase.GenerateOtpUseCase
import com.webkul.mobikul.odoo.features.authentication.presentation.effect.LoginOptionsEffect
import com.webkul.mobikul.odoo.features.authentication.presentation.intent.LoginOptionsIntent
import com.webkul.mobikul.odoo.features.authentication.presentation.state.LoginOptionsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginOptionsViewModel @Inject constructor(
        private val generateOtpUseCase: GenerateOtpUseCase
) : BaseViewModel(),
        IModel<LoginOptionsState, LoginOptionsIntent, LoginOptionsEffect> {
    override val intents: Channel<LoginOptionsIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableStateFlow<LoginOptionsState>(LoginOptionsState.Idle)
    override val state: StateFlow<LoginOptionsState>
        get() = _state

    private val _effect = Channel<LoginOptionsEffect>()
    override val effect: Flow<LoginOptionsEffect>
        get() = _effect.receiveAsFlow()

    init {
        handlerIntent()
    }

    override fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect {
                when (it) {
                    is LoginOptionsIntent.OtpLogin -> launchOtpLoginScreen(it.phoneNumber)
                    is LoginOptionsIntent.PasswordLogin -> launchPasswordLoginScreen()
                    is LoginOptionsIntent.ChangePhoneNumber -> launchAuthenticationScreen()
                    is LoginOptionsIntent.SetIdle -> setIdleState()
                }
            }
        }
    }

    private fun setIdleState() {
        viewModelScope.launch {
            _state.value = LoginOptionsState.Idle
        }
    }

    private fun launchOtpLoginScreen(phoneNumber: String) {
        viewModelScope.launch {
            _state.value = LoginOptionsState.OtpLogin
        }
    }

    private fun launchPasswordLoginScreen() {
        viewModelScope.launch {
            _state.value = LoginOptionsState.PasswordLogin
        }
    }

    private fun launchAuthenticationScreen() {
        viewModelScope.launch {
            _state.value = LoginOptionsState.ChangePhoneNumber
        }
    }

}