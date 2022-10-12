package com.webkul.mobikul.odoo.data.request

import org.json.JSONObject

data class UserRequest(
	var customerLoginToken: String? = null,
	var customerJWTToken: String? = null,
	var customerName: String? = null,
	var customerId: String? = null,
	var customerEmail: String? = null,
	var customerPhoneNumber: String? = null,
	var customerProfileImage: String? = null,
	var customerBannerImage: String? = null,
	var userAnalyticsId: String? = null,
	var isSeller: Boolean? = null,
	var isUserApproved: Boolean? = null,
	var isUserLoggedIn: Boolean? = null,
	var isUserOnboarded: Boolean? = null,
	var customerGroupName: String? = null,
	var customerGroupId: Int? = null,
	var groupName: String? = null,
	var referralCode: String = "",
	var isOnboarding: Boolean = true,
	var userId: String? = null,
	var fcmTokenSynced: Boolean = false
) {
	override fun toString(): String {
		val valJsonObject = JSONObject()
		valJsonObject.put(NAME, customerName)
		valJsonObject.put(GROUP_NAME, customerGroupName)
		valJsonObject.put(IMAGE, customerProfileImage)
		valJsonObject.put(REFERRAL_CODE, referralCode)
		valJsonObject.put(IS_ONBOARDING, isOnboarding)

		val params = JSONObject()
		params.put(PARAMS, valJsonObject)

		return params.toString()
	}

	companion object {
		val NAME = "name"
		val GROUP_NAME = "group_name"
		val IMAGE = "image"
		val REFERRAL_CODE = "referral_code"
		val IS_ONBOARDING = "isOnboarding"
		val PARAMS = "params"
	}
}
