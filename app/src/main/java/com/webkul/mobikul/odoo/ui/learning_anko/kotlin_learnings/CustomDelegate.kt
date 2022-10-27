package com.webkul.mobikul.odoo.ui.learning_anko.kotlin_learnings

import kotlin.reflect.KProperty

class CustomDelegate<T> {

	private var value: T? = null

	operator fun getValue(thisRef: Any, property: KProperty<*>): T {
		return value ?: throw IllegalStateException("${property.name} " + "not initialized")

	}

	operator fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
		this.value = if (this.value == null) value
		else throw IllegalStateException("${property.name} already initialized")
	}

}

