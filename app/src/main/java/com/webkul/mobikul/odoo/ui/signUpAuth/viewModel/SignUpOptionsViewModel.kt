package com.webkul.mobikul.odoo.ui.signUpAuth.viewModel

import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.ui.signUpAuth.effect.SignUpOptionsEffect
import com.webkul.mobikul.odoo.ui.signUpAuth.intent.SignUpOptionsIntent
import com.webkul.mobikul.odoo.ui.signUpAuth.state.SignUpOptionsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpOptionsViewModel @Inject constructor() : BaseViewModel(),
    IModel<SignUpOptionsState, SignUpOptionsIntent, SignUpOptionsEffect> {

    override val intents: Channel<SignUpOptionsIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableStateFlow<SignUpOptionsState>(SignUpOptionsState.Idle)
    override val state: StateFlow<SignUpOptionsState>
        get() = _state

    private val _effect = Channel<SignUpOptionsEffect>()
    override val effect: Flow<SignUpOptionsEffect>
        get() = _effect.receiveAsFlow()

    init {
        handlerIntent()
    }

    override fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect {
                when (it) {
                    is SignUpOptionsIntent.OtpSignUp -> launchOtpSignUpScreen(it.phoneNumber)
                    is SignUpOptionsIntent.ChangePhoneNumber -> launchAuthenticationScreen()
                    is SignUpOptionsIntent.SetIdle -> setIdleState()
                }
            }
        }
    }

    private fun setIdleState() {
        _state.value = SignUpOptionsState.Idle
    }

    private suspend fun launchOtpSignUpScreen(phoneNumber: String) {
        _effect.send(SignUpOptionsEffect.NavigateToOTPSignUp(phoneNumber))
    }

    private suspend fun launchAuthenticationScreen() {
        _effect.send(SignUpOptionsEffect.NavigateToPhoneNumberValidation)
    }
}