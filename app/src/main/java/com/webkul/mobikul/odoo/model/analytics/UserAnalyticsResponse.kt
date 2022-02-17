package com.webkul.mobikul.odoo.model.analytics

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class UserAnalyticsResponse(
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("is_seller")
    val isSeller: Boolean,
    @SerializedName("analytics_id")
    val analyticsId: String
) : Serializable