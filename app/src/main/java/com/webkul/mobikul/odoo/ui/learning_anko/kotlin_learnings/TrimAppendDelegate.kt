package com.webkul.mobikul.odoo.ui.learning_anko.kotlin_learnings

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class TrimAppendDelegate : ReadWriteProperty<Any, String> {
	private var trimAppendedString = ""
	override fun getValue(thisRef: Any, property: KProperty<*>) = trimAppendedString
	override fun setValue(thisRef: Any, property: KProperty<*>, value: String) {
		trimAppendedString = "${value.trim()} is a String!"
	}
}
