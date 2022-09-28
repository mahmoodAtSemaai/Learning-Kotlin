package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName

data class AddOnsEntity(
        @SerializedName("wishlist") val wishlist: Boolean,
        @SerializedName("review") val review: Boolean,
        @SerializedName("email_verification") val emailVerification: Boolean,
        @SerializedName("odoo_marketplace") val odooMarketplace: Boolean,
        @SerializedName("website_sale_delivery") val websiteSaleDelivery: Boolean,
        @SerializedName("odoo_gdpr") val odooGdpr: Boolean,
        @SerializedName("website_sale_stock") val websiteSaleStock: Boolean,
        @SerializedName("website_customer_group") val websiteCustomerGroup: Boolean
)
