package com.webkul.mobikul.odoo.ui.checkout.dashboard

import android.os.Bundle
import androidx.lifecycle.*
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.mvicore.IEffect
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import com.webkul.mobikul.odoo.data.entity.DeliveryEntity
import com.webkul.mobikul.odoo.data.entity.OrderEntity
import com.webkul.mobikul.odoo.data.entity.OrderProductEntity
import com.webkul.mobikul.odoo.domain.enums.CheckoutOrderValidation
import com.webkul.mobikul.odoo.domain.usecase.checkout.*
import com.webkul.mobikul.odoo.helper.AppSharedPref
import com.webkul.mobikul.odoo.model.payments.SelectedPaymentMethod
import com.webkul.mobikul.odoo.model.request.OrderReviewRequest
import com.webkul.mobikul.odoo.model.request.PlaceOrderRequest
import com.webkul.mobikul.odoo.ui.checkout.dashboard.CheckoutDashboardFragmentV1.Companion.CART_ID
import com.webkul.mobikul.odoo.ui.checkout.dashboard.CheckoutDashboardFragmentV1.Companion.LINE_IDS
import com.webkul.mobikul.odoo.ui.checkout.dashboard.CheckoutDashboardFragmentV1.Companion.ORDER_ID
import com.webkul.mobikul.odoo.ui.checkout.dashboard.CheckoutDashboardFragmentV1.Companion.PAYMENT_METHOD_RESULT
import com.webkul.mobikul.odoo.ui.checkout.dashboard.CheckoutDashboardFragmentV1.Companion.POINTS_REDEEMED
import com.webkul.mobikul.odoo.ui.checkout.dashboard.CheckoutDashboardFragmentV1.Companion.RESULT
import com.webkul.mobikul.odoo.ui.checkout.dashboard.CheckoutDashboardFragmentV1.Companion.SELECTED_PAYMENT_METHOD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val resourcesProvider: ResourcesProvider,
    private val fetchOrderDetailsUseCase: FetchOrderDetailsUseCase,
    private val fetchOrderReviewDataUseCase: FetchOrderReviewDataUseCase,
    private val placeOrderUseCase: PlaceOrderUseCase,
    private val validateOrderForCheckOutUseCase: ValidateOrderForCheckOutUseCase,
    private val appPreferences: AppPreferences
) : BaseViewModel() , IModel<CheckoutState, CheckoutIntent, CheckoutEffect> {

    override val intents: Channel<CheckoutIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableStateFlow<CheckoutState>(CheckoutState.Idle)
    override val state: StateFlow<CheckoutState>
        get() = _state

    private val _effect = Channel<CheckoutEffect>()
    override val effect: Flow<CheckoutEffect>
        get() = _effect.receiveAsFlow()

    var orderEntity: OrderEntity? = null
    var selectedPaymentMethod: SelectedPaymentMethod? = null
    var isUserWantToRedeemPoints : Boolean = false
    var orderId: Int = -1
    var cartId: Int = -1
    var lineIds: ArrayList<Int> = ArrayList()

    init {
        handlerIntent()
    }

    override fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect {
                when(it) {
                    is CheckoutIntent.GetOrderData -> fetchOrder(it.orderId, it.refreshRecyclerView, it.refreshPaymentDetails, it.isUserWantToRedeemPoints, it.lineIds)
                    is CheckoutIntent.GetOrderReview -> getOrderReview(it.orderEntity, it.selectedPaymentMethod, it.isUserWantToRedeemPoints)
                    is CheckoutIntent.NavigateToPaymentMethod -> onNavigateToPaymentMethod()
                    is CheckoutIntent.NavigateToShippingAddress -> onNavigateToShippingAddress(it.orderId)
                    is CheckoutIntent.NavigateToShippingMethod -> onNavigateToShippingMethod(it.orderId)
                    is CheckoutIntent.PlaceOrder -> placeOrder(it.transactionId, it.isCOD, it.isUserWantToRedeemPoints)
                    is CheckoutIntent.SetBundleAndPreferenceData -> setBundleAndPreferenceData(it.bundle)
                    is CheckoutIntent.SetInitialLayout -> setInitialLayout()
                    is CheckoutIntent.SetIsCustomerWantToRedeemPoints -> setIsCustomerWantToRedeemPoints(it.value)
                    is CheckoutIntent.StartValidationToPlaceOrder -> onStartValidationToPlaceOrder(it.orderEntity, it.selectedPaymentMethod)
                    is CheckoutIntent.ValidateCheckOutOrderDetails -> validateCheckoutOrderDetails(it.orderEntity, it.selectedPaymentMethod)
                    is CheckoutIntent.ResultFromPaymentMethod -> onResultFromPaymentMethod(it.bundle)
                    is CheckoutIntent.ResultFromShippingAddress -> onResultFromShippingAddress(it.bundle)
                    is CheckoutIntent.ResultFromShippingMethod -> onResultFromShippingMethod(it.bundle)
                }
                 }
            }
    }

    private fun onResultFromShippingMethod(bundle: Bundle?) {
            bundle?.let {
                if (it.getBoolean(RESULT, false)) {
                    _state.value = CheckoutState.ShippingMethodSelection
                }
            }
    }

    private fun onResultFromShippingAddress(bundle: Bundle?) {
            bundle?.let {
                if (it.getBoolean(RESULT, false)) {
                    _state.value = CheckoutState.ShippingAddressSelection
                }
            }
    }

    private fun onResultFromPaymentMethod(bundle: Bundle?) {
             bundle?.let {
                 if (it.containsKey(SELECTED_PAYMENT_METHOD)) {
                     selectedPaymentMethod = bundle.getParcelable(SELECTED_PAYMENT_METHOD)
                     _state.value = CheckoutState.PaymentMethodSelection(selectedPaymentMethod)
                 }
             }
    }

    private fun setIsCustomerWantToRedeemPoints(value: Boolean) {
        appPreferences.isCustomerWantToRedeemPoints = value
    }

    private fun placeOrder(transactionId: Int, cod: Boolean, userWantToRedeemPoints: Boolean) {
            if (cod) {
                placeCODOrder(transactionId, userWantToRedeemPoints)
            } else {
                placeVirtualOrder(transactionId, userWantToRedeemPoints)
            }
    }

    private fun placeCODOrder(transactionId: Int, userWantToRedeemPoints: Boolean) {

        viewModelScope.launch {
            _state.value = try {

                val response =  placeOrderUseCase(
                    PlaceOrderRequest(transactionId, userWantToRedeemPoints)
                )

                var state: CheckoutState = CheckoutState.Idle

                response
                    .collect {
                        when(it) {
                            is Resource.Default -> {}
                            is Resource.Failure -> {
                                _effect.send(
                                    CheckoutEffect.ErrorSnackBar(
                                        resourcesProvider.getString(
                                            R.string.error_request_failed
                                        )
                                    )
                                )
                                state = CheckoutState.Idle
                            }
                            is Resource.Loading -> {}
                            is Resource.Success ->
                                state = CheckoutState.OnPlacedCODOrder

                        }
                    }
                state
            } catch (e: Exception) {
                CheckoutState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun placeVirtualOrder(transactionId: Int, userWantToRedeemPoints: Boolean) {
        viewModelScope.launch {
            _state.value = try {

                val response =  placeOrderUseCase(
                    PlaceOrderRequest(transactionId, userWantToRedeemPoints)
                )

                var state: CheckoutState = CheckoutState.Idle

                response
                    .collect {
                        when(it) {
                            is Resource.Default -> {}
                            is Resource.Failure -> {
                                _effect.send(
                                    CheckoutEffect.ErrorSnackBar(
                                        resourcesProvider.getString(
                                            R.string.error_request_failed
                                        )
                                    )
                                )
                                state = CheckoutState.Idle
                            }
                            is Resource.Loading -> {
                                state = CheckoutState.Loading
                            }
                            is Resource.Success ->
                                state = CheckoutState.OnPlacedVirtualOrder(
                                    transactionId.toString(),
                                    orderId,
                                    selectedPaymentMethod?.paymentAcquirerProviderId.toString())

                        }
                    }
                state
            } catch (e: Exception) {
                CheckoutState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }
    private fun getOrderReview(orderEntity: OrderEntity?, selectedPaymentMethod: SelectedPaymentMethod?, userWantToRedeemPoints: Boolean) {
        viewModelScope.launch {
            _state.value = CheckoutState.ProgressDialog(resourcesProvider.getString(R.string.placing_your_order))
            _state.value = try {
                val response =  fetchOrderReviewDataUseCase(
                    OrderReviewRequest(
                        orderEntity?.shippingAddressId?.id.toString(),
                        orderEntity?.delivery?.shippingId.toString(),
                        selectedPaymentMethod?.paymentAcquirerId,
                        userWantToRedeemPoints,
                        lineIds
                    )
                )

                var state: CheckoutState = CheckoutState.Idle

                response
                    .collect {
                        when(it) {
                            is Resource.Default -> {}
                            is Resource.Failure -> {
                                if (it.message.isNullOrEmpty()) {
                                    _effect.send(
                                        CheckoutEffect.ErrorSnackBar(
                                            resourcesProvider.getString(
                                                R.string.error_request_failed
                                            )
                                        )
                                    )
                                } else {
                                    _effect.send(
                                        CheckoutEffect.WarningDialog(
                                            it.message.toString()
                                        )
                                    )
                                }
                                state = CheckoutState.Idle
                            }
                            is Resource.Loading ->
                                state = CheckoutState.ProgressDialog(resourcesProvider.getString(R.string.placing_your_order))
                            is Resource.Success -> {
                                orderId = it.value.saleOrderId
                                //TODO: TO BE PICKED UP WITH REPO REVAMP BY AASHISH
                                appPreferences.newCartCount = it.value.cartCount
                                state = CheckoutState.GetOrderReviewResponse(it.value)
                            }

                        }
                    }
                state
            } catch (e: Exception) {
                CheckoutState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun validateCheckoutOrderDetails(orderEntity: OrderEntity?, selectedPaymentMethod: SelectedPaymentMethod?) {
        viewModelScope.launch {
            try {
                val response =  validateOrderForCheckOutUseCase(orderEntity, selectedPaymentMethod)

                response
                    .catch {
                        _effect.send(CheckoutEffect.ValidateCheckOutOrderDetails(false))
                    }
                    .collect {
                        _effect.send(CheckoutEffect.ValidateCheckOutOrderDetails(it))
                    }
                state
            } catch (e: Exception) {
                CheckoutState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun onStartValidationToPlaceOrder(orderEntity: OrderEntity?, selectedPaymentMethod: SelectedPaymentMethod?) {
        viewModelScope.launch {
            try {
                val response =  validateOrderForCheckOutUseCase(orderEntity!!, selectedPaymentMethod)


                response
                    .catch {

                        when (it.message?.toInt()) {

                            CheckoutOrderValidation.INVALID_ORDER_DETAILS.value ->
                                _effect.send(CheckoutEffect.ErrorDialog(resourcesProvider.getString(R.string.error_request_failed)))

                            CheckoutOrderValidation.INVALID_SHIPPING_ADDRESS.value ->
                                _effect.send(CheckoutEffect.OnValidateShippingAddressFailure)

                            CheckoutOrderValidation.INVALID_SHIPPING_METHOD.value ->
                                _effect.send(CheckoutEffect.OnValidateShippingMethodFailure)

                            CheckoutOrderValidation.INVALID_PAYMENT_METHOD.value ->
                                _effect.send(CheckoutEffect.OnValidatePaymentMethodFailure)

                        }
                    }
                    .collect {
                        if (it) {
                            _effect.send(CheckoutEffect.OnSuccessfulValidationForPlaceOrder)
                        } else {
                            _effect.send(CheckoutEffect.ErrorDialog(resourcesProvider.getString(R.string.error_request_failed)))
                        }
                    }
            } catch (e: Exception) {
                CheckoutState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private suspend fun setInitialLayout() {
        _effect.send(CheckoutEffect.SetInitialLayout)
    }

    private suspend fun onNavigateToShippingAddress(orderId: Int) {
        _effect.send(CheckoutEffect.NavigateToShippingAddress(orderId))
    }

    private fun onNavigateToShippingMethod(orderId: Int) {
        viewModelScope.launch {
            try {
                val response =  validateOrderForCheckOutUseCase(orderEntity!!, selectedPaymentMethod)

                response
                    .catch {

                        if (CheckoutOrderValidation.INVALID_SHIPPING_ADDRESS.value == it.message?.toInt()) {
                            _effect.send(CheckoutEffect.OnValidateShippingAddressFailure)
                        } else {
                            _effect.send(CheckoutEffect.NavigateToShippingMethod(orderId))
                        }
                    }
                    .collect {
                        if (it) {
                            _effect.send(CheckoutEffect.NavigateToShippingMethod(orderId))
                        } else {
                            _effect.send(CheckoutEffect.OnValidateShippingAddressFailure)
                        }
                    }
            } catch (e: Exception) {
                CheckoutState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private suspend fun onNavigateToPaymentMethod() {
            _effect.send(CheckoutEffect.NavigateToPaymentMethod)
    }

    private fun fetchOrder(cartId: Int, refreshRecyclerView: Boolean, refreshPaymentDetails: Boolean, userWantToRedeemPoints: Boolean, lineIds: ArrayList<Int>) {
        viewModelScope.launch {
            _state.value = CheckoutState.ProgressDialog(resourcesProvider.getString(R.string.fetching_order_details))
            _state.value = try {
                val response =  fetchOrderDetailsUseCase(cartId, userWantToRedeemPoints, lineIds)

                var state: CheckoutState = CheckoutState.Idle

                response
                    .collect {
                        when (it) {
                            is Resource.Default -> {}
                            is Resource.Failure ->
                                _effect.send(CheckoutEffect.ErrorDialog(resourcesProvider.getString(R.string.error_request_failed)))
                            is Resource.Loading -> state = CheckoutState.ProgressDialog(resourcesProvider.getString(R.string.fetching_order_details))
                            is Resource.Success -> {
                                orderEntity = it.value
                                state = CheckoutState.FetchOrderDetails(it.value, refreshRecyclerView, refreshPaymentDetails)
                            }

                        }
                    }
                state
            } catch (e: Exception) {
                CheckoutState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }


    private fun setBundleAndPreferenceData(bundle: Bundle?) {
            bundle?.let {
                cartId = bundle.getInt(CART_ID)
                isUserWantToRedeemPoints = bundle.getBoolean(POINTS_REDEEMED)
                lineIds = bundle.getIntegerArrayList(LINE_IDS)!!
            }
            fetchOrder(cartId, true, true, isUserWantToRedeemPoints, lineIds)
    }

}