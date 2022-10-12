package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName

data class UserEntity(
    var customerLoginToken: String,
    var customerJWTToken: String,
    var customerEmail: String,
    var customerPhoneNumber: String,
    var customerBannerImage: String,
    var userAnalyticsId: String,
    var isSeller: Boolean,
    var isUserApproved: Boolean,
    var isUserLoggedIn: Boolean,
    var isUserOnboarded: Boolean,
    @SerializedName("customer_grp_id") val customerGrpId: Int,
    @SerializedName("name") val customerName: String,
    @SerializedName("group_name") val customerGroupName: String,
    @SerializedName("profile_image") val customerProfileImage: String,
    @SerializedName("partner_id") val customerId: Int,
    var groupName: String,
    var userId: String
)
