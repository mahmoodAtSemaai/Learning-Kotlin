package com.webkul.mobikul.odoo.ui.learning_anko

import timber.log.Timber
import java.net.URL

class Request(val url : String) {

	fun run(): String {
		val forecastJsonStr = URL(url).readText()
		Timber.tag(javaClass.simpleName).d(forecastJsonStr)
		return forecastJsonStr
	}

}