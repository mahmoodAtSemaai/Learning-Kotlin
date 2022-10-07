package com.webkul.mobikul.odoo.ui.checkout.order

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import com.webkul.mobikul.odoo.domain.usecase.checkout.*
import com.webkul.mobikul.odoo.model.payments.PendingPaymentData
import com.webkul.mobikul.odoo.ui.checkout.dashboard.CheckoutDashboardFragmentV1
import com.webkul.mobikul.odoo.ui.checkout.dashboard.CheckoutEffect
import com.webkul.mobikul.odoo.ui.checkout.dashboard.CheckoutState
import com.webkul.mobikul.odoo.ui.checkout.paymentpending.PaymentPendingEffect
import com.webkul.mobikul.odoo.ui.checkout.paymentpending.PaymentPendingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val resourcesProvider: ResourcesProvider,
    private val fetchOrderDetailsUseCase: FetchOrderDetailsAfterCheckoutUseCase,
    private val fetchTransactionStatusUseCase: FetchTransactionStatusUseCase
) : BaseViewModel() , IModel<OrderState, OrderIntent, OrderEffect> {

    override val intents: Channel<OrderIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableStateFlow<OrderState>(OrderState.Idle)
    override val state: StateFlow<OrderState>
        get() = _state

    private val _effect = Channel<OrderEffect>()
    override val effect: Flow<OrderEffect>
        get() = _effect.receiveAsFlow()

    var orderId: Int = -1

    init {
        handlerIntent()
    }

    override fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect {
                when(it) {
                    is OrderIntent.CopyToClipBoard -> copyToClipBoard()
                    is OrderIntent.FetchOrderDetails -> fetchOrderDetails(it.orderID, it.pointsRedeem)
                    is OrderIntent.FetchTransactionStatus -> fetchTransactionStatus(it.orderID)
                    is OrderIntent.SetBundleDataAndInitialLayout -> setBundleDataAndInitialLayout(it.bundle)
                }
            }
        }
    }

    private fun setBundleDataAndInitialLayout(bundle: Bundle?) {
        bundle?.let {  orderId = bundle.getInt(OrderFragmentV1.ORDER_ID) }
        _state.value = OrderState.SetInitialLayout
    }

    private fun fetchTransactionStatus(orderID: Int) {
        viewModelScope.launch {
            _state.value = OrderState.ShowProgressDialog(
                resourcesProvider.getString(R.string.checking_payment_status)
            )
            _state.value = try {

                val response = fetchTransactionStatusUseCase.invoke(orderId)

                var state: OrderState =  OrderState.Idle

                response
                    .collect {
                        when (it) {
                            is Resource.Default -> {}
                            is Resource.Failure ->
                                state = OrderState.ShowWarningDialog(
                                    resourcesProvider.getString(R.string.error_payment_status)
                                )
                            is Resource.Loading ->
                                state = OrderState.ShowProgressDialog(
                                resourcesProvider.getString(R.string.checking_payment_status)
                            )
                            is Resource.Success -> {
                                when (it.value.paymentStatusResult.paymentStatusCode) {
                                    OrderFragmentV1.COMPLETED -> state = OrderState.OnCompletedTransactionStatus
                                    OrderFragmentV1.FAILED -> state = OrderState.OnCancelledTransactionStatus
                                    OrderFragmentV1.PENDING -> state = OrderState.OnPendingTransactionStatus(it.value, orderID)
                                }

                            }
                        }
                    }
                state
            } catch (e: Exception) {
                OrderState.Error(e.localizedMessage, FailureStatus.OTHER)
            }

        }
    }

    private fun fetchOrderDetails(orderID: Int, pointsRedeem: Boolean) {
        viewModelScope.launch {
            _state.value = OrderState.ShowProgressDialog(resourcesProvider.getString(R.string.fetching_order_details))
            _state.value = try {
                val response =  fetchOrderDetailsUseCase(orderId, pointsRedeem)

                var state: OrderState = OrderState.Idle

                response
                    .collect {
                        when (it) {
                            is Resource.Default -> {}
                            is Resource.Failure -> state = OrderState.ShowErrorDialog(
                                resourcesProvider.getString(R.string.error_something_went_wrong),
                                resourcesProvider.getString(R.string.error_order_details))
                            is Resource.Loading -> state = OrderState.ShowProgressDialog(resourcesProvider.getString(R.string.fetching_order_details))
                            is Resource.Success -> {
                                state = OrderState.OnOrderDetailsReceived(it.value)
                            }

                        }
                    }
                state
            } catch (e: Exception) {
                OrderState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private suspend fun copyToClipBoard() {
            _effect.send(OrderEffect.CopyToClipBoard)
    }
}