package com.webkul.mobikul.odoo.ui.learning_anko.data.models

data class Forecast(
	val clouds: Int,
	val deg: Int,
	val dt: Long,
	val feels_like: FeelsLike,
	val gust: Double,
	val humidity: Int,
	val pop: Int,
	val pressure: Int,
	val speed: Double,
	val sunrise: Int,
	val sunset: Int,
	val temp: Temp,
	val weather: List<Weather>
)