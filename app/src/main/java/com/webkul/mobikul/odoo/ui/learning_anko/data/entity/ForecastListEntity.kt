package com.webkul.mobikul.odoo.ui.learning_anko.data.entity

data class ForecastListEntity(
	val city: String,
	val country: String,
	val dailyForecast: List<ForecastEntity>,
)