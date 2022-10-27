package com.webkul.mobikul.odoo.ui.learning_anko.kotlin_learnings

import kotlin.reflect.KProperty

class ManualLazy<out T : Any>(
	private val initialise: () -> T,
) {
	private var value: T? = null

	operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
		return if (value == null) {
			value = initialise()
			value!!
		} else value!!
	}


}