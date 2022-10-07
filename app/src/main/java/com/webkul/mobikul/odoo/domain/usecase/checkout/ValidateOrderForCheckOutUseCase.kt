package com.webkul.mobikul.odoo.domain.usecase.checkout

import com.webkul.mobikul.odoo.data.entity.OrderEntity
import com.webkul.mobikul.odoo.domain.enums.CheckoutOrderValidation
import com.webkul.mobikul.odoo.domain.enums.CheckoutOrderValidationException
import com.webkul.mobikul.odoo.domain.enums.SignUpFieldsValidation
import com.webkul.mobikul.odoo.domain.enums.SignUpValidationException
import com.webkul.mobikul.odoo.model.customer.address.AddressRequestBody
import com.webkul.mobikul.odoo.model.payments.SelectedPaymentMethod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ValidateOrderForCheckOutUseCase @Inject constructor() {

    @Throws(CheckoutOrderValidationException::class)
    operator fun invoke(orderEntity: OrderEntity?, selectedPaymentMethod: SelectedPaymentMethod?): Flow<Boolean> = flow {

        if (isOrderEligibleForCheckOut(orderEntity, selectedPaymentMethod)) {
            emit(true)
        }

    }.flowOn(Dispatchers.Default)

    private fun isOrderEligibleForCheckOut(orderEntity: OrderEntity?,
                                           selectedPaymentMethod: SelectedPaymentMethod?): Boolean {

        when {
            orderEntity == null -> {
                throw CheckoutOrderValidationException(
                    CheckoutOrderValidation.INVALID_ORDER_DETAILS.value.toString())
            }

            orderEntity.shippingAddressId.districtName.isEmpty() -> throw CheckoutOrderValidationException(
                CheckoutOrderValidation.INVALID_SHIPPING_ADDRESS.value.toString())

            orderEntity.delivery == null || orderEntity.delivery.name?.isEmpty() == true -> throw CheckoutOrderValidationException(
                CheckoutOrderValidation.INVALID_SHIPPING_METHOD.value.toString()
            )

            selectedPaymentMethod == null || selectedPaymentMethod.paymentAcquirerId?.isEmpty() == true -> throw CheckoutOrderValidationException(
                CheckoutOrderValidation.INVALID_PAYMENT_METHOD.value.toString()
            )

            else -> return true
        }
    }

}