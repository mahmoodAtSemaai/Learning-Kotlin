package com.webkul.mobikul.odoo.ui.learning_anko.domain.commands

import com.webkul.mobikul.odoo.ui.learning_anko.data.ForecastRequest
import com.webkul.mobikul.odoo.ui.learning_anko.data.entity.ForecastListEntity
import com.webkul.mobikul.odoo.ui.learning_anko.domain.Command
import com.webkul.mobikul.odoo.ui.learning_anko.domain.mapper.DataMapper

class RequestForecastCommand(private val zipCode : String) : Command<ForecastListEntity> {

	override fun execute(): ForecastListEntity {
		val request = ForecastRequest(zipCode)
		return DataMapper().convertFromDataModel(request.execute())
	}


}