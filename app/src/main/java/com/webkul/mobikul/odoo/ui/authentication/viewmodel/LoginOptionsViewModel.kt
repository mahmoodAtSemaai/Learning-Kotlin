package com.webkul.mobikul.odoo.ui.authentication.viewmodel

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.ui.authentication.effect.LoginOptionsEffect
import com.webkul.mobikul.odoo.ui.authentication.intent.LoginOptionsIntent
import com.webkul.mobikul.odoo.ui.authentication.state.LoginOptionsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginOptionsViewModel @Inject constructor() : BaseViewModel(),
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
                    is LoginOptionsIntent.GetArguments -> setArguments(it.arguments)
                    is LoginOptionsIntent.OtpLogin -> launchOtpLoginScreen(it.phoneNumber)
                    is LoginOptionsIntent.PasswordLogin -> launchPasswordLoginScreen(it.phoneNumber)
                    is LoginOptionsIntent.ChangePhoneNumber -> launchAuthenticationScreen()
                    is LoginOptionsIntent.SetIdle -> setIdleState()
                }
            }
        }
    }

    private fun setArguments(arguments: Bundle) {
        val phoneNumber = arguments.getString(BundleConstant.BUNDLE_KEY_PHONE_NUMBER) ?: ""
        val enablePassword = arguments.getBoolean(BundleConstant.BUNDLE_KEY_ENABLE_PASSWORD)
        _state.value = LoginOptionsState.SetPhoneNumber(phoneNumber, enablePassword)
    }

    private fun setIdleState() {
        _state.value = LoginOptionsState.Idle
    }

    private suspend fun launchOtpLoginScreen(phoneNumber: String) {
        _effect.send(LoginOptionsEffect.OtpLogin(phoneNumber))
    }

    private suspend fun launchPasswordLoginScreen(phoneNumber: String) {
         _effect.send(LoginOptionsEffect.PasswordLogin(phoneNumber))
    }

    private suspend fun launchAuthenticationScreen() {
        _effect.send(LoginOptionsEffect.ChangePhoneNumber)
    }

}