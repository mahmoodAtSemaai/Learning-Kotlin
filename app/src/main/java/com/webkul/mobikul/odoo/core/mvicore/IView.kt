package com.webkul.mobikul.odoo.core.mvicore

/**
 * Base View Interface to be implemented by every activity
 */
interface IView<I : IIntent, S : IState, E : IEffect> {
    fun render(state: S)
    fun render(effect: E)
    fun triggerIntent(intent: I)
}