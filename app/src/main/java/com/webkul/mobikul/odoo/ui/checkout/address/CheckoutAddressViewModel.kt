package com.webkul.mobikul.odoo.ui.checkout.address

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.mvicore.IEffect
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import com.webkul.mobikul.odoo.domain.enums.UpdateAddressValidation
import com.webkul.mobikul.odoo.domain.usecase.checkout.DeleteAddressUseCase
import com.webkul.mobikul.odoo.domain.usecase.checkout.FetchAllAddressUseCase
import com.webkul.mobikul.odoo.domain.usecase.checkout.UpdateAddressForOrderUseCase
import com.webkul.mobikul.odoo.model.customer.address.AddressData
import com.webkul.mobikul.odoo.ui.auth.SignUpEffect
import com.webkul.mobikul.odoo.ui.checkout.address.CheckoutAddressBookFragmentV1.Companion.CART_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutAddressViewModel @Inject constructor(
    private val resourcesProvider: ResourcesProvider,
    private val fetchAllAddressUseCase: FetchAllAddressUseCase,
    private val updateAddressForOrderUseCase: UpdateAddressForOrderUseCase,
    private val deleteAddressUseCase: DeleteAddressUseCase
) : BaseViewModel() , IModel<CheckoutAddressState, CheckoutAddressIntent, CheckoutAddressEffect> {

    override val intents: Channel<CheckoutAddressIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableStateFlow<CheckoutAddressState>(CheckoutAddressState.Idle)
    override val state: StateFlow<CheckoutAddressState>
        get() = _state

    private val _effect = Channel<CheckoutAddressEffect>()
    override val effect: Flow<CheckoutAddressEffect>
        get() = _effect.receiveAsFlow()

    var cartId: Int = 0
    var selectedAddress: AddressData? = null

    init {
        handlerIntent()
    }

    override fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect {
                when (it) {
                    is CheckoutAddressIntent.UpdateAddress -> updateAddressForOrder(it.orderId, it.address)
                    is CheckoutAddressIntent.FetchAddress ->  fetchAddress()
                    is CheckoutAddressIntent.DeleteAddress -> deleteAddress(it.address)
                    is CheckoutAddressIntent.NavigateToAddAddress -> navigateToAddAddress()
                    is CheckoutAddressIntent.NavigateToEditAddress -> navigateToEditAddress(it.address)
                    is CheckoutAddressIntent.SetBundleData -> setBundleData(it.bundle)
                    is CheckoutAddressIntent.SetInitialLayout -> setInitialLayout()
                }
            }
        }

    }

    private fun setBundleData(bundle: Bundle?) {
            bundle?.let { cartId = bundle.getInt(CART_ID) }
            fetchAddress()
    }


    private fun setInitialLayout() {
        _state.value = CheckoutAddressState.SetInitialLayout
    }

    private fun fetchAddress() {
        viewModelScope.launch {
            _state.value = CheckoutAddressState.Loading
            _state.value = try {
                val addressData = fetchAllAddressUseCase.invoke()
                var checkoutAddressState: CheckoutAddressState = CheckoutAddressState.Idle

                addressData
                    .collect {
                        when (it) {
                            is Resource.Default -> {}
                            is Resource.Failure -> checkoutAddressState =
                                CheckoutAddressState.Error(it.message, it.failureStatus)
                            is Resource.Loading -> checkoutAddressState =
                                CheckoutAddressState.Loading
                            is Resource.Success -> checkoutAddressState =
                                CheckoutAddressState.FetchAddress(it.value)
                        }
                }
                checkoutAddressState
            } catch (e: Exception) {
                CheckoutAddressState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun updateAddressForOrder(orderId: Int, address: AddressData?) {

        viewModelScope.launch {
            _state.value = CheckoutAddressState.ProgressDialog(resourcesProvider.getString(R.string.updating_shipping_address_text))
            _state.value = try {
                val response = updateAddressForOrderUseCase.invoke(orderId, address)
                var checkoutAddressState: CheckoutAddressState = CheckoutAddressState.Idle

                response
                    .catch {
                        when (it.message?.toInt()) {

                            UpdateAddressValidation.EMPTY_DISPLAY_NAME.value -> checkoutAddressState =
                                CheckoutAddressState.AlertDialog(
                                    resourcesProvider.getString(R.string.service_unavailable),
                                    resourcesProvider.getString(R.string.service_not_availabe_text)
                                )

                            UpdateAddressValidation.NULL_ADDRESS.value -> checkoutAddressState =
                                CheckoutAddressState.Error(
                                    resourcesProvider.getString(R.string.select_your_shipping_address),
                                    FailureStatus.OTHER
                                )

                        }
                    }
                    .collect {
                        when (it) {
                            is Resource.Default -> {}
                            is Resource.Failure -> checkoutAddressState =
                                CheckoutAddressState.Error(it.message, it.failureStatus)
                            is Resource.Loading -> checkoutAddressState =
                                CheckoutAddressState.ProgressDialog(resourcesProvider.getString(R.string.updating_shipping_address_text))
                            is Resource.Success -> checkoutAddressState =
                                CheckoutAddressState.UpdateAddress
                        }
                    }
                checkoutAddressState
            } catch (e: Exception) {
                CheckoutAddressState.Error(e.localizedMessage, FailureStatus.OTHER)
            }

        }

    }

    private fun deleteAddress(address: AddressData) {

        viewModelScope.launch {
            _state.value = CheckoutAddressState.Loading
            _state.value = try {
                val response = deleteAddressUseCase.invoke(address)
                var checkoutAddressState: CheckoutAddressState = CheckoutAddressState.Idle

                response
                    .collect {
                        when (it) {
                            is Resource.Default -> {}
                            is Resource.Failure -> checkoutAddressState =
                                CheckoutAddressState.Error(it.message, it.failureStatus)
                            is Resource.Loading -> checkoutAddressState =
                                CheckoutAddressState.Loading
                            is Resource.Success -> {
                                checkoutAddressState = if (it.value.success) {
                                    CheckoutAddressState.DeleteAddress(it.value)
                                } else {
                                    CheckoutAddressState.WarningDialog(
                                        resourcesProvider.getString(R.string.error_something_went_wrong),
                                        it.value.message
                                    )
                                }
                            }
                        }
                    }
                checkoutAddressState
            } catch (e: Exception) {
                CheckoutAddressState.Error(e.localizedMessage, FailureStatus.OTHER)
            }

        }

    }

    private suspend fun navigateToAddAddress() {
        _effect.send(CheckoutAddressEffect.NavigateToAddAddress)
    }

    private suspend fun navigateToEditAddress(address: AddressData) {
        _effect.send(CheckoutAddressEffect.NavigateToEditAddress(address))
    }


}