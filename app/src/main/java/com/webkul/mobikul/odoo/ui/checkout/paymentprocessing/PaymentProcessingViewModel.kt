package com.webkul.mobikul.odoo.ui.checkout.paymentprocessing

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.constant.Status
import com.webkul.mobikul.odoo.core.mvicore.IEffect
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import com.webkul.mobikul.odoo.domain.usecase.checkout.CreatePaymentUseCase
import com.webkul.mobikul.odoo.model.payments.CreateVirtualAccountPaymentRequest
import com.webkul.mobikul.odoo.ui.checkout.paymentprocessing.PaymentProcessingFragmentV1.Companion.CREATE_VIRTUAL_ACC_REQUEST
import com.webkul.mobikul.odoo.ui.checkout.paymentprocessing.PaymentProcessingFragmentV1.Companion.ORDER_ID
import com.webkul.mobikul.odoo.ui.checkout.shippingmethod.ShippingMethodFragmentV1
import com.webkul.mobikul.odoo.ui.checkout.shippingmethod.ShippingMethodState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentProcessingViewModel @Inject constructor(
    private val resourcesProvider: ResourcesProvider,
    private val createPaymentUseCase: CreatePaymentUseCase
) : BaseViewModel() , IModel<PaymentProcessingState, PaymentProcessingIntent, PaymentProcessingEffect> {

    override val intents: Channel<PaymentProcessingIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableStateFlow<PaymentProcessingState>(PaymentProcessingState.Idle)
    override val state: StateFlow<PaymentProcessingState>
        get() = _state

    private val _effect = Channel<PaymentProcessingEffect>()
    override val effect: Flow<PaymentProcessingEffect>
        get() = _effect.receiveAsFlow()

    private var orderId : Int = 0
    private var createVirtualAccountPaymentRequest : CreateVirtualAccountPaymentRequest? = null

    init {
        handlerIntent()
    }

    override fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect {
                when(it) {
                    is PaymentProcessingIntent.SetBundleData -> setBundleData(it.bundle)
                    is PaymentProcessingIntent.ShowErrorDialog -> showErrorDialog(it.title, it.message)
                }
            }
        }
    }

    private fun createPayment( createVirtualAccountPaymentRequest: CreateVirtualAccountPaymentRequest) {
        viewModelScope.launch {
            _state.value = try {

                val response =  createPaymentUseCase(createVirtualAccountPaymentRequest)

                var state: PaymentProcessingState = PaymentProcessingState.Idle

                response
                    .collect {
                        when(it) {
                            is Resource.Default -> {}
                            is Resource.Failure ->
                                state = PaymentProcessingState.ErrorDialog(
                                    resourcesProvider.getString(R.string.error_something_went_wrong),
                                    resourcesProvider.getString(R.string.error_order_place)
                                )
                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                state = PaymentProcessingState.PaymentProcessingResult(
                                    it.value.paymentDetails,
                                    orderId
                                )
                            }

                        }
                    }
                state
            } catch (e: Exception) {
                PaymentProcessingState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun showErrorDialog(title: String?, message: String?) {
        _state.value = PaymentProcessingState.ErrorDialog(title, message)
    }

    private fun setBundleData(bundle: Bundle?) {

        bundle?.let {
            orderId = bundle.getInt(ORDER_ID)
            createVirtualAccountPaymentRequest = bundle.getParcelable<CreateVirtualAccountPaymentRequest>(CREATE_VIRTUAL_ACC_REQUEST)
        }

        _state.value = PaymentProcessingState.SetInitialLayout

        createVirtualAccountPaymentRequest?.let {
            createPayment(it)
        }
    }


}