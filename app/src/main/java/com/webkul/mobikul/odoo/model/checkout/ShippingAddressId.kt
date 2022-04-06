package com.webkul.mobikul.odoo.model.checkout

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShippingAddressId(
    @SerializedName("country_id")
    @Expose
    val countryId: Int,
    @SerializedName("district_id")
    @Expose
    val districtId: Int,
    @SerializedName("district_name")
    @Expose
    val districtName: String,
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("phone")
    @Expose
    val phone: String,
    @SerializedName("state_id")
    @Expose
    val stateId: Int,
    @SerializedName("state_name")
    @Expose
    val stateName: String,
    @SerializedName("street")
    @Expose
    val street: String,
    @SerializedName("street2")
    @Expose
    val street2: String,
    @SerializedName("sub_district_id")
    @Expose
    val subDistrictId: Int,
    @SerializedName("sub_district_name")
    @Expose
    val subDistrictName: String,
    @SerializedName("village_id")
    @Expose
    val villageId: Int,
    @SerializedName("village_name")
    @Expose
    val villageName: String,
    @SerializedName("zip")
    @Expose
    val zip: String
) : Parcelable {

    fun getCompleteAddressDescription(): String {
        if (street.isEmpty())
            return "$villageName\n$stateName $districtName $subDistrictName"
        else
            return "$street\n$villageName\n$stateName $districtName $subDistrictName"
    }

    fun getIncompleteAddressDescription(): String {
        return stateName
    }

}