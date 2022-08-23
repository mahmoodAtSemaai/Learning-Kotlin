package com.webkul.mobikul.odoo.data.request

import com.webkul.mobikul.odoo.constant.ApplicationConstant
import org.json.JSONObject

data class UserAddressRequest(
    var street: String = "",
    var street2: String = "",
    var zip: String = "",
    var stateId: String = "",
    var districtId: String = "",
    var subDistrictId: String = "",
    var villageId: String = "",
    var countryId: String = ApplicationConstant.COUNTY_ID.toString(),
    var isOnboarding: Boolean = true
) {
    override fun toString(): String {
        return JSONObject().put(STREET, street).put(STREET2, street2).put(ZIP, zip)
            .put(COUNTRY_ID, countryId).put(STATE_ID, stateId).put(DISTRICT_ID, districtId)
            .put(SUB_DISTRICT_ID, subDistrictId).put(VILLAGE_ID, villageId)
            .put(ISONBOARDING, isOnboarding).toString()
    }

    companion object {
        val STREET = "street"
        val STREET2 = "street2"
        val ZIP = "zip"
        val COUNTRY_ID = "country_id"
        val STATE_ID = "state_id"
        val DISTRICT_ID = "district_id"
        val SUB_DISTRICT_ID = "sub_district_id"
        val VILLAGE_ID = "village_id"
        val ISONBOARDING = "isOnboarding"
    }
}