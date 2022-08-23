package com.webkul.mobikul.odoo.data.request

import org.json.JSONObject

data class UserLocationRequest(
    var locationPermission: String = "",
    var partnerLatitude: String = "",
    var partnerLongitude: String = "",
    var isOnboarding: Boolean = true
) {
    override fun toString(): String {
        return JSONObject().put(LOCATION_PERMISSION, locationPermission)
            .put(PARTNER_LATITUDE, partnerLatitude).put(PARTNER_LONGITUDE, partnerLongitude)
            .put(IS_ONBOARDING, isOnboarding).toString()
    }

    companion object {
        val LOCATION_PERMISSION = "location_permission"
        val PARTNER_LATITUDE = "partner_latitude"
        val PARTNER_LONGITUDE = "partner_longitude"
        val IS_ONBOARDING = "isOnboarding"
    }
}