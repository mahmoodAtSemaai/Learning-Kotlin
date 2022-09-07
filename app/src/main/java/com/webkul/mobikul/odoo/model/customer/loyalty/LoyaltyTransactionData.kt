package com.webkul.mobikul.odoo.model.customer.loyalty

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.webkul.mobikul.odoo.helper.changeFormat
import com.webkul.mobikul.odoo.constant.ApplicationConstant

class LoyaltyTransactionData {
    @SerializedName("points")
    @Expose
    val points: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return if (isCredit) "+$field" else "-$field"
        }

    @SerializedName("date")
    @Expose
    val date: String? = null
        get() = field?.let { changeFormat(langCode, it) } ?: ""

    @SerializedName("time")
    @Expose
    val time: String? = null
        get() = field ?: ""

    @SerializedName("point_type")
    @Expose
    private val pointType: String? = null

    @SerializedName("lang_code")
    @Expose
    private val langCode: String? = ""

    @SerializedName("reference")
    @Expose
    private val reference: String? = null
    fun getPointType(): String {
        return if (pointType == null) {
            ""
        } else pointTypeText
    }

    val pointTypeText: String
        get() = if (reference!!.isEmpty()) {
            ""
        } else reference

    fun getReference(): String {
        return reference ?: ""
    }

    val isCredit: Boolean
        get() = (pointType.equals(ApplicationConstant.CREDIT, true) || pointType.equals(ApplicationConstant.INDO_CREDIT, true))
}