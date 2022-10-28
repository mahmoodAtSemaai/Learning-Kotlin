package com.webkul.mobikul.odoo.ui.learning_anko.domain.mapper

import com.webkul.mobikul.odoo.ui.learning_anko.data.models.Forecast
import com.webkul.mobikul.odoo.ui.learning_anko.data.models.ForecastResult
import com.webkul.mobikul.odoo.ui.learning_anko.data.entity.ForecastEntity
import com.webkul.mobikul.odoo.ui.learning_anko.data.entity.ForecastListEntity
import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class DataMapper {

	fun convertFromDataModel(forecast: ForecastResult): ForecastListEntity {
		return ForecastListEntity(forecast.city.name,
			forecast.city.country,
			convertForecastListToDomain(forecast.list))
	}

	private fun convertForecastListToDomain(list: List<Forecast>): List<ForecastEntity> {
		return list.mapIndexed { i, forecast ->
			val dt = Calendar.getInstance().timeInMillis + TimeUnit.DAYS.toMillis(i.toLong())
			convertForecastItemToDomain(forecast.copy(dt = dt))
		}
	}

	private fun convertForecastItemToDomain(forecast: Forecast): ForecastEntity {
		return ForecastEntity(convertDate(forecast.dt), forecast.weather[0].description,
			forecast.temp.max.toInt(), forecast.temp.min.toInt())
	}

	private fun convertDate(date: Long): String {
		val df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault())
		return df.format(date)
	}


}