package com.webkul.mobikul.odoo.data.response

import com.google.gson.annotations.SerializedName

data class ProductResponse(
        @SerializedName("template_id") val templateId: Int,
        @SerializedName("name") val name: String,
        @SerializedName("price_unit") val priceUnit: String,
        @SerializedName("price_reduce") val priceReduce: String,
        @SerializedName("product_id") val productId: String,
        @SerializedName("description") val description: String,
        @SerializedName("thumbnail") val thumbnail: String,
        @SerializedName("sale_delay") val saleDelay: Float,
        @SerializedName("brand_name") val brandName: String,
        @SerializedName("available_quantity") val availableQuantity: Int,
        @SerializedName("inventory_availability") val inventoryAvailability: String,
        @SerializedName("global_product_id") val globalProductId: String,
        @SerializedName("images") val images: ArrayList<String>,
        @SerializedName("available_threshold") val availableThreshold: Float,
        @SerializedName("added_to_wishlist") val addedToWishlist: Boolean,
        @SerializedName("seller_info") val sellerInfo: SellerResponse,
        @SerializedName("add_to_cart") val addToCart: Boolean,
        @SerializedName("stock_display_msg") val stockDisplayMsg: String,
        @SerializedName("ar_ios") val arIos: String,
        @SerializedName("ar_android") val arAndroid: String,
        @SerializedName("absolute_url") val absoluteUrl: String,
        @SerializedName("mobikul_category_details") val mobikulCategoryDetails: MobikulCategoryDetailResponse,
        @SerializedName("product_type") val productType: String
)
