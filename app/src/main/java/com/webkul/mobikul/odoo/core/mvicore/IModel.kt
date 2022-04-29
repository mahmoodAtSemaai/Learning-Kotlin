package com.webkul.mobikul.odoo.core.mvicore

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow

/**
 * Base Mode Interface to be implemented by every viewmodels
 */
interface IModel<S : IState, I : IIntent> {
    val intents: Channel<I>
    val state: StateFlow<S>

    fun handlerIntent()
}