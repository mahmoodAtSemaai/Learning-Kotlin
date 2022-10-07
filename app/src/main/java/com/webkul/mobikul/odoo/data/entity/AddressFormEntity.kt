package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName

data class AddressFormEntity(

    val zip: String?,

    val name: String?,

    val phone: String?,

    val street: String?,

    @SerializedName("country_id")
    val countryId: String?,

    @SerializedName("state_id")
    val stateId: String?,

    @SerializedName("district_id")
    val districtId: String?,

    @SerializedName("sub_district_id")
    val subDistrictId: String?,

    @SerializedName("village_id")
    val villageId: String?,

    val city: String?,

    )