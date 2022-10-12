package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName
import com.webkul.mobikul.odoo.helper.CalendarUtil

data class OrderEntity(

    @SerializedName("amount_tax")
    val amountTax: String,

    @SerializedName("amount_total")
    var amountTotal: String,

    @SerializedName("points_redeemed")
    var pointsRedeemed: String,

    @SerializedName("grand_total")
    var grandTotal: String,

    @SerializedName("amount_untaxed")
    val amountUntaxed: String,

    @SerializedName("billAdd_url")
    val billingAddressUrl: String,

    @SerializedName("billing_address")
    val billingAddress: String,

    @SerializedName("billing_address_id")
    val billingAddressId: ShippingAddressEntity,

    @SerializedName("create_date")
    val createDate: String,

    @SerializedName("WishlistCount")
    val customerId: Int,

    @SerializedName("is_approved")
    val isApproved: Boolean,

    @SerializedName("is_seller")
    val isSeller: Boolean,

    val items: List<OrderProductEntity>,

    val itemsPerPage: Int,

    val message: String,

    val name: String,

    val responseCode: Int,

    @SerializedName("shipAdd_url")
    val shippingAddressUrl: String,

    @SerializedName("shipping_address")
    val shippingAddress: String,

    @SerializedName("shipping_address_id")
    val shippingAddressId: ShippingAddressEntity,

    val status: String = "",

    val success: Boolean,

    val userId: Int,

    val delivery: DeliveryEntity?,

    var paymentAcquirerId: String = "",

    var errorMessage: String = "",

    @SerializedName("order_id")
    var orderId: Int,

    @SerializedName("payment_mode")
    var paymentMode: String,

    @SerializedName("bank_name")
    var bankName: String = "",

    @SerializedName("account_number")
    var accountNumber: String?,

    @SerializedName("payment_status")
    var paymentStatus: String,

    val bank: BankEntity,

    @SerializedName("expire_date")
    val expireDate: String?,

    @SerializedName("expire_time")
    val expireTime: String?,

    @SerializedName("expire_delta")
    val expireDelta: Int?,

    @SerializedName("display_order_status")
    var mobileOrderStatus: String = ""
) {

    fun getPaymentExpiryDate(): String {
        if (expireDate == null)
            return ""
        return CalendarUtil.getDateInBahasa(expireDate)
    }
}