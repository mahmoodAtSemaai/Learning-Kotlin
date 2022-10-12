package com.webkul.mobikul.odoo.ui.checkout.dashboard

import android.os.Bundle
import com.webkul.mobikul.odoo.core.mvicore.IIntent
import com.webkul.mobikul.odoo.data.entity.DeliveryEntity
import com.webkul.mobikul.odoo.data.entity.OrderEntity
import com.webkul.mobikul.odoo.data.entity.OrderProductEntity
import com.webkul.mobikul.odoo.model.payments.SelectedPaymentMethod
import java.util.ArrayList

sealed class CheckoutIntent : IIntent {
    data class GetOrderData( val orderId: Int,
                             val refreshRecyclerView: Boolean,
                             val refreshPaymentDetails: Boolean,
                             val isUserWantToRedeemPoints: Boolean,
                             val lineIds: ArrayList<Int>
    ) : CheckoutIntent()
    data class NavigateToShippingAddress(val orderId: Int) : CheckoutIntent()
    data class NavigateToShippingMethod(val orderId: Int) : CheckoutIntent()
    object NavigateToPaymentMethod : CheckoutIntent()
    data class ResultFromShippingAddress(val bundle: Bundle?) : CheckoutIntent()
    data class ResultFromShippingMethod(val bundle: Bundle?) : CheckoutIntent()
    data class ResultFromPaymentMethod(val bundle: Bundle?) : CheckoutIntent()
    data class SetIsCustomerWantToRedeemPoints(val value: Boolean) : CheckoutIntent()
    data class StartValidationToPlaceOrder(
        val orderEntity: OrderEntity?,
        val selectedPaymentMethod: SelectedPaymentMethod?
    ) : CheckoutIntent()
    data class PlaceOrder(val transactionId: Int,
                          val isCOD: Boolean,
                          val isUserWantToRedeemPoints: Boolean) : CheckoutIntent()
    data class ValidateCheckOutOrderDetails(val orderEntity: OrderEntity?,
                                            val selectedPaymentMethod: SelectedPaymentMethod?) : CheckoutIntent()
    data class SetBundleAndPreferenceData(val bundle: Bundle?) : CheckoutIntent()
    object SetInitialLayout : CheckoutIntent()
    data class GetOrderReview(val orderEntity: OrderEntity?,
                              val selectedPaymentMethod: SelectedPaymentMethod?,
                              val isUserWantToRedeemPoints: Boolean) : CheckoutIntent()

}
