package com.webkul.mobikul.odoo.data.response

import com.google.gson.annotations.SerializedName

data class SellerResponse(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("email") val email: String,
        @SerializedName("average_rating") val averageRating: Int,
        @SerializedName("total_reviews") val totalReviews: Int,
        @SerializedName("product_count") val productCount: Int,
        @SerializedName("profile_image") val profileImage: String,
        @SerializedName("profile_banner") val profileBanner: String,
        @SerializedName("create_date") val createDate: String,
        @SerializedName("state") val state: String,
        @SerializedName("country") val country: String,
        @SerializedName("phone") val phone: String,
        @SerializedName("mobile") val mobile: String,
        @SerializedName("profile_msg") val profileMsg: String,
        @SerializedName("return_policy") val returnPolicy: String,
        @SerializedName("shipping_policy") val shippingPolicy: String,
        //TODO -> need to check
        @SerializedName("seller_reviews") val seller_reviews: List<String>
)
