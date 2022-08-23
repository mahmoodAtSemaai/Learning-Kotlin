package com.webkul.mobikul.odoo.ui.signUpOnboarding.viewModel

import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.ui.signUpOnboarding.effect.NoInternetEffect
import com.webkul.mobikul.odoo.ui.signUpOnboarding.intent.NoInternetIntent
import com.webkul.mobikul.odoo.ui.signUpOnboarding.state.NoInternetState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoInternetViewModel @Inject constructor() : BaseViewModel(),
    IModel<NoInternetState, NoInternetIntent, NoInternetEffect> {
    override val intents: Channel<NoInternetIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableStateFlow<NoInternetState>(NoInternetState.Idle)
    override val state: StateFlow<NoInternetState>
        get() = _state

    private val _effect = Channel<NoInternetEffect>()
    override val effect: Flow<NoInternetEffect>
        get() = _effect.receiveAsFlow()

    init {
        handlerIntent()
    }

    override fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect {
                when (it) {
                    is NoInternetIntent.Refresh -> {
                        _state.value = NoInternetState.Refresh
                    }
                }
            }
        }
    }
}