package com.webkul.mobikul.odoo.data.request

import org.json.JSONObject

data class UserDetailsRequest(
    var name: String = "",
    var groupName: String = "",
    var profileImage: String = "",
    var referralCode: String = "",
    var isOnboarding: Boolean = true
) {
    override fun toString(): String {
        val valJsonObject = JSONObject()
        valJsonObject.put(NAME, name)
        valJsonObject.put(GROUP_NAME, groupName)
        valJsonObject.put(IMAGE, profileImage)
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