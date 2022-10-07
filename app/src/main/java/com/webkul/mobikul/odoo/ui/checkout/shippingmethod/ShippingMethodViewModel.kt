package com.webkul.mobikul.odoo.ui.checkout.shippingmethod

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.mvicore.IEffect
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import com.webkul.mobikul.odoo.data.entity.OrderEntity
import com.webkul.mobikul.odoo.data.entity.ShippingMethodEntity
import com.webkul.mobikul.odoo.domain.enums.CheckoutOrderValidation
import com.webkul.mobikul.odoo.domain.enums.UpdateShippingMethodValidation
import com.webkul.mobikul.odoo.domain.usecase.checkout.*
import com.webkul.mobikul.odoo.helper.AppSharedPref
import com.webkul.mobikul.odoo.model.request.PlaceOrderRequest
import com.webkul.mobikul.odoo.ui.checkout.address.CheckoutAddressState
import com.webkul.mobikul.odoo.ui.checkout.dashboard.CheckoutDashboardFragmentV1
import com.webkul.mobikul.odoo.ui.checkout.dashboard.CheckoutIntent
import com.webkul.mobikul.odoo.ui.checkout.dashboard.CheckoutState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShippingMethodViewModel @Inject constructor(
    private val resourcesProvider: ResourcesProvider,
    private val updateShippingMethodForOrderUseCase: UpdateShippingMethodForOrderUseCase,
    private val fetchActiveShippingMethodsUseCase: FetchActiveShippingMethodsUseCase,
    private val appPreferences: AppPreferences
) : BaseViewModel() , IModel<ShippingMethodState, ShippingMethodIntent, ShippingMethodEffect> {

    override val intents: Channel<ShippingMethodIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableStateFlow<ShippingMethodState>(ShippingMethodState.Idle)
    override val state: StateFlow<ShippingMethodState>
        get() = _state

    private val _effect = Channel<ShippingMethodEffect>()
    override val effect: Flow<ShippingMethodEffect>
        get() = _effect.receiveAsFlow()

    var shippingMethodEntity: ShippingMethodEntity? = null
    var cartId: Int = -1

    init {
        handlerIntent()
    }

    override fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect {
                when(it) {
                    is ShippingMethodIntent.FetchShippingMethod -> fetchActiveShippingMethods()
                    is ShippingMethodIntent.SetBundleData -> setBundleData(it.bundle)
                    is ShippingMethodIntent.UpdateShippingMethod -> updateShippingMethodForOrder(
                        it.orderId, it.shippingMethodEntity)
                    is ShippingMethodIntent.SetInitialLayout -> setInitialLayout()
                }
            }
        }
    }

    private fun setInitialLayout() {
        _state.value = ShippingMethodState.SetInitialLayout
    }

    private fun updateShippingMethodForOrder(orderId: Int, shippingMethodEntity: ShippingMethodEntity?) {
        viewModelScope.launch {
            _state.value = ShippingMethodState.ProgressDialog(
                resourcesProvider.getString(R.string.updating_shipping_method_text)
            )
            _state.value = try {

                val response =  updateShippingMethodForOrderUseCase(
                    orderId,
                    shippingMethodEntity
                )

                var state: ShippingMethodState = ShippingMethodState.Idle

                response
                    .catch {
                        when (it.message?.toInt()) {

                            UpdateShippingMethodValidation.NULL_SHIPPING_METHOD.value ->
                                _effect.send(ShippingMethodEffect.ErrorSnackBar(
                                    resourcesProvider.getString(R.string.checkout_select_a_shipping_method)))
                        }
                    }
                    .collect {
                        when(it) {
                            is Resource.Default -> {}
                            is Resource.Failure ->
                                _effect.send(ShippingMethodEffect.ErrorSnackBar(
                                    resourcesProvider.getString(R.string.error_something_went_wrong)))
                            is Resource.Loading ->
                                state = ShippingMethodState.ProgressDialog(
                                    resourcesProvider.getString(R.string.updating_shipping_method_text)
                                )
                            is Resource.Success ->
                                state = ShippingMethodState.UpdateShippingMethodForOrder

                        }
                    }
                state
            } catch (e: Exception) {
                ShippingMethodState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun setBundleData(bundle: Bundle?) {
        bundle?.let {  cartId = bundle.getInt(ShippingMethodFragmentV1.CART_ID) }
        fetchActiveShippingMethods()
    }

    private fun fetchActiveShippingMethods() {
        viewModelScope.launch {
            _state.value = ShippingMethodState.ProgressDialog(
                resourcesProvider.getString(R.string.shipping_options_loading_text)
            )
            _state.value = try {

                val response =  fetchActiveShippingMethodsUseCase(appPreferences.customerId.toString())

                var state: ShippingMethodState = ShippingMethodState.Idle

                response
                    .collect {
                        when(it) {
                            is Resource.Default -> {}
                            is Resource.Failure ->
                                _effect.send(ShippingMethodEffect.ErrorSnackBar(
                                    resourcesProvider.getString(R.string.error_something_went_wrong)))
                            is Resource.Loading -> state = ShippingMethodState.ProgressDialog(
                                resourcesProvider.getString(R.string.shipping_options_loading_text)
                            )
                            is Resource.Success ->
                                state = ShippingMethodState.FetchShippingMethod(it.value)

                        }
                    }
                state
            } catch (e: Exception) {
                ShippingMethodState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

}