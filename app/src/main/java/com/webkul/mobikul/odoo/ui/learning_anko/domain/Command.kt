package com.webkul.mobikul.odoo.ui.learning_anko.domain

interface Command<out T> {

	fun execute(): T

}