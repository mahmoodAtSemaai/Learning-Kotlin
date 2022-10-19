package com.webkul.mobikul.odoo.ui.learning_anko.data

data class ForecastResult(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Forecast>,
    val message: Double
)