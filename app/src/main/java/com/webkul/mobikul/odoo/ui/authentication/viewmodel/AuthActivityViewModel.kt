package com.webkul.mobikul.odoo.ui.authentication.viewmodel

import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.ui.authentication.effect.AuthActivityEffect
import com.webkul.mobikul.odoo.ui.authentication.intent.AuthActivityIntent
import com.webkul.mobikul.odoo.ui.authentication.state.AuthActivityState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthActivityViewModel @Inject constructor() : BaseViewModel(),
    IModel<AuthActivityState, AuthActivityIntent, AuthActivityEffect> {

    override val intents: Channel<AuthActivityIntent> = Channel(Channel.UNLIMITED)
    private val _state = MutableStateFlow<AuthActivityState>(AuthActivityState.Idle)
    override val state: StateFlow<AuthActivityState>
        get() = _state

    private val _effect = Channel<AuthActivityEffect>()
    override val effect: Flow<AuthActivityEffect>
        get() = _effect.receiveAsFlow()

    init {
        handlerIntent()
    }

    override fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect {
                when (it) {
                    is AuthActivityIntent.SetIdle -> { }
                    is AuthActivityIntent.InitToolBar -> _state.value = AuthActivityState.InitToolBar
                    is AuthActivityIntent.SetupFragmentBackStack -> _state.value = AuthActivityState.SetBackStackListener
                    is AuthActivityIntent.SetupPhoneNumberScreen -> _effect.send(AuthActivityEffect.SetupPhoneScreen)
                    is AuthActivityIntent.SetupToolbarContent -> _state.value = AuthActivityState.SetToolBarContent(it.title, it.showBackButton)
                }
            }
        }
    }

}