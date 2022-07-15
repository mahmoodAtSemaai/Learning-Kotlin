package com.webkul.mobikul.odoo.core.mvicore

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Base Mode Interface to be implemented by every viewmodels
 */
interface IModel<S : IState, I : IIntent, E: IEffect> {
    val intents: Channel<I>
    val state: StateFlow<S>
    val effect: Flow<E>

    fun handlerIntent()
}