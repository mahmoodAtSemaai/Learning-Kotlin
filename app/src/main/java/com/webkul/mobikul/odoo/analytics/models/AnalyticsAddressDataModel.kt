package com.webkul.mobikul.odoo.analytics.models

data class AnalyticsAddressDataModel(
        var province: String = "",
        var district: String = "",
        var subDistrict: String = "",
        var village: String = "",
        val postalCode: String = "",
        val street: String = "",
        var isServicable: Boolean = false
    )