package com.webkul.mobikul.odoo.core.mvicore

import androidx.lifecycle.LiveData
import kotlinx.coroutines.channels.Channel

/**
 * Base Mode Interface to be implemented by every viewmodels
 */
interface IModel<S : IState, I : IIntent> {
    val intents: Channel<I>
    val state: LiveData<S>

    fun handlerIntent()
}