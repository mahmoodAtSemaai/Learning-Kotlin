package com.webkul.mobikul.odoo.model.checkout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.webkul.mobikul.odoo.helper.CalendarUtil
import com.webkul.mobikul.odoo.model.BaseResponse
import com.webkul.mobikul.odoo.helper.StringUtil
import com.webkul.mobikul.odoo.model.customer.order.OrderItem

data class OrderDataResponse(
    @SerializedName("amount_tax")
    @Expose
    val amountTax: String,
    @SerializedName("amount_total")
    @Expose
    val amountTotal: String,
    @SerializedName("points_redeemed")
    @Expose
    var pointsRedeemed: String?,
    @SerializedName("grand_total")
    @Expose
    var grandTotal: String,
    @SerializedName("amount_untaxed")
    @Expose
    val amountUntaxed: String,
    @SerializedName("billAdd_url")
    @Expose
    val billingAddressUrl: String,
    @SerializedName("billing_address")
    @Expose
    val billingAddress: String,
    @SerializedName("billing_address_id")
    @Expose
    val billingAddressId: ShippingAddressId,
    @SerializedName("create_date")
    @Expose
    val createDate: String,
    @SerializedName("WishlistCount")
    @Expose
    val customerId: Int,
    @SerializedName("is_seller")
    @Expose
    val isSeller: Boolean,
    @SerializedName("items")
    @Expose
    val items: List<OrderItem>,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("shipAdd_url")
    @Expose
    val shippingAddressUrl: String,
    @SerializedName("shipping_address")
    @Expose
    val shippingAddress: String,
    @SerializedName("shipping_address_id")
    @Expose
    val shippingAddressId: ShippingAddressId,
    @SerializedName("delivery")
    @Expose
    val delivery: Delivery?,
    @SerializedName("paymentAcquirerId")
    @Expose
    var paymentAcquirerId: String = "",
    @SerializedName("errorMessage")
    @Expose
    var errorMessage: String = "",
    @SerializedName("order_id")
    @Expose
    var orderId: Int,
    @SerializedName("payment_mode")
    @Expose
    var paymentMode: String,
    @SerializedName("bank_name")
    @Expose
    var bankName: String = "",
    @SerializedName("account_number")
    @Expose
    var accountNumber: String?,
    @SerializedName("payment_status")
    @Expose
    var paymentStatus: String,
    @SerializedName("bank")
    @Expose
    val bank: Bank,
    @SerializedName("expire_date")
    @Expose
    val expireDate: String?,
    @SerializedName("expire_time")
    @Expose
    val expireTime: String?,
    @SerializedName("expire_delta")
    @Expose
    val expireDelta: Int?,
    @SerializedName("display_order_status")
    @Expose
    var mobileOrderStatus: String = "",
    @SerializedName("status")
    @Expose
    val status: String = ""
) : BaseResponse() {

    @SerializedName("default_acquirer_id")
    @Expose
    var acquirerId : Int = 0



    fun getPaymentExpiryDate(): String {
        if (expireDate == null)
            return ""
        return CalendarUtil.getDateInBahasa(expireDate)
    }

    fun getAmountFromString(): Long {
        return StringUtil.getPaymentAmount(grandTotal)
    }
}