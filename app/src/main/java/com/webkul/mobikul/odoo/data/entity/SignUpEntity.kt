package com.webkul.mobikul.odoo.data.entity

import com.webkul.mobikul.odoo.model.home.HomePageResponse

data class SignUpEntity(
		//TODO->Add string message for successful signup
		//As of now we are getting something went wrong as message
		//Once figure out we will remove 'message' from 'signUpEntity'
		val message : String,
		val homepage : HomePageResponse?,
		val userId : String
)
