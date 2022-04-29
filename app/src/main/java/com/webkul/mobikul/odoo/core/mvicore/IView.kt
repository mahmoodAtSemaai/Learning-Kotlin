package com.webkul.mobikul.odoo.core.mvicore

/**
 * Base View Interface to be implemented by every activity
 */
interface IView<I : IIntent, S : IState> {
    fun render(state: S)
    fun triggerIntent(intent: I)
}