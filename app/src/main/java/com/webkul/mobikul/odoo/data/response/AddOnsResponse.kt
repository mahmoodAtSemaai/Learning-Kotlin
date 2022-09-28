package com.webkul.mobikul.odoo.data.response

import com.google.gson.annotations.SerializedName

data class AddOnsResponse(
        @SerializedName("wishlist") val wishlist: Boolean,
        @SerializedName("review") val review: Boolean,
        @SerializedName("email_verification") val email_verification: Boolean,
        @SerializedName("odoo_marketplace") val odoo_marketplace: Boolean,
        @SerializedName("website_sale_delivery") val website_sale_delivery: Boolean,
        @SerializedName("odoo_gdpr") val odoo_gdpr: Boolean,
        @SerializedName("website_sale_stock") val website_sale_stock: Boolean,
        @SerializedName("website_customer_group") val website_customer_group: Boolean
)
