package com.webkul.mobikul.odoo.ui.learning_anko.data.models

data class City(
	val coordinates: Coordinates,
	val country: String,
	val id: Int,
	val name: String,
	val population: Int,
	val timezone: Int
)