package com.webkul.mobikul.odoo.data.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShippingAddressEntity(

    @SerializedName("country_id")
    val countryId: Int,

    @SerializedName("district_id")
    val districtId: Int,

    @SerializedName("district_name")
    val districtName: String,

    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("phone")
    val phone: String,

    @SerializedName("state_id")
    val stateId: Int,

    @SerializedName("state_name")
    val stateName: String,

    @SerializedName("street")
    val street: String,

    @SerializedName("street2")
    val street2: String,

    @SerializedName("sub_district_id")
    val subDistrictId: Int,

    @SerializedName("sub_district_name")
    val subDistrictName: String,

    @SerializedName("village_id")
    val villageId: Int,

    @SerializedName("village_name")
    val villageName: String,

    @SerializedName("zip")
    val zip: String

) : Parcelable {

    fun getCompleteAddressDescription(): String {
        return if (street.isEmpty())
            "$villageName\n$stateName $districtName $subDistrictName"
        else
            "$street\n$villageName\n$stateName $districtName $subDistrictName"
    }

    fun getIncompleteAddressDescription(): String {
        return stateName
    }

}