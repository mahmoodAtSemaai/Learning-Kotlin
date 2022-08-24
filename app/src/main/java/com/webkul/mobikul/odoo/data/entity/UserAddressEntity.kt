package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName

data class UserAddressEntity(

    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("street")
    val street: String,
    @SerializedName("street2")
    val street2: String,
    @SerializedName("zip")
    val zip: String,
    @SerializedName("country_id")
    val countryId: Int,
    @SerializedName("state_id")
    val stateId: Int,
    @SerializedName("district_id")
    val districtId: Int,
    @SerializedName("sub_district_id")
    val subDistrictId: Int,
    @SerializedName("village_id")
    val villageId: Int,
)