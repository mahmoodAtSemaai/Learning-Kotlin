package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName

data class AddressFormEntity(

    @SerializedName("state_id") val stateId: String? = null,
    @SerializedName("district_id") val districtId: String? = null,
    @SerializedName("sub_district_id") val subDistrictId: String? = null,
    @SerializedName("village_id") val villageId: String? = null,

    )
