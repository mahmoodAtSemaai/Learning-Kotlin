package com.webkul.mobikul.odoo.ui.checkout.paymentacquire

import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.mvicore.IEffect
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import com.webkul.mobikul.odoo.domain.usecase.checkout.*
import com.webkul.mobikul.odoo.model.payments.SelectedPaymentMethod
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentAcquireViewModel  @Inject constructor(
    private val resourcesProvider: ResourcesProvider,
    private val fetchPaymentAcquirersUseCase: FetchPaymentAcquirersUseCase,
    private val fetchPaymentAcquirerMethodsUseCase: FetchPaymentAcquirerMethodsUseCase,
    private val fetchPaymentAcquirerMethodProvidersUseCase: FetchPaymentAcquirerMethodProvidersUseCase
) : BaseViewModel() , IModel<PaymentAcquireState, PaymentAcquireIntent, PaymentAcquireEffect> {

    override val intents: Channel<PaymentAcquireIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableStateFlow<PaymentAcquireState>(PaymentAcquireState.Idle)
    override val state: StateFlow<PaymentAcquireState>
        get() = _state

    private val _effect = Channel<PaymentAcquireEffect>()
    override val effect: Flow<PaymentAcquireEffect>
        get() = _effect.receiveAsFlow()


    var isCODSelected = false
    var selectedPaymentMethod: SelectedPaymentMethod? = null
    var COD_ID: Int = -1

    init {
        handlerIntent()
    }

    override fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect {
                when(it) {
                    is PaymentAcquireIntent.FetchPaymentAcquirerMethodProvider ->
                        fetchPaymentAcquirerMethodProvider(it.acquirerId, it.paymentMethodId)
                    is PaymentAcquireIntent.FetchPaymentAcquirerMethods ->
                        fetchPaymentAcquirerMethods(it.acquirerId)
                    is PaymentAcquireIntent.FetchPaymentAcquirers ->
                        fetchPaymentAcquirers(it.companyId)
                    is PaymentAcquireIntent.SetInitialLayout ->
                        setInitialLayout()
                    is PaymentAcquireIntent.SetEndResult -> setEndResult()
                }
            }
        }
    }

    private suspend fun setEndResult() {
            if (isCODSelected) {
                _state.value = PaymentAcquireState.EndResult(
                    SelectedPaymentMethod(
                        resourcesProvider.getString(R.string.cod_text),
                        COD_ID.toString(),
                        "",
                        "",
                        true
                    )
                )
            } else {
                if (selectedPaymentMethod == null)
                    _effect.send(PaymentAcquireEffect.ErrorSnackBar(
                        resourcesProvider.getString(R.string.checkout_select_your_payment_method)
                    ))
                else {
                    _state.value = PaymentAcquireState.EndResult(
                        SelectedPaymentMethod(
                            selectedPaymentMethod?.paymentMethodText,
                            selectedPaymentMethod?.paymentAcquirerId,
                            selectedPaymentMethod?.paymentAcquirerProviderId,
                            selectedPaymentMethod?.paymentAcquirerProviderType,
                            false
                        )
                    )
                }
            }
    }

    private fun setInitialLayout() {
        _state.value = PaymentAcquireState.SetInitialLayout
    }

    private fun fetchPaymentAcquirers(companyId: Int) {

        viewModelScope.launch {
            _state.value = PaymentAcquireState.ProgressDialog(
                resourcesProvider.getString(R.string.payment_options_loading_text)
            )
            _state.value = try {

                val response =  fetchPaymentAcquirersUseCase(companyId)

                var state: PaymentAcquireState = PaymentAcquireState.Idle

                response
                    .collect {
                        when(it) {
                            is Resource.Default -> {}
                            is Resource.Failure ->
                                _effect.send(PaymentAcquireEffect.ErrorSnackBar(
                                    resourcesProvider.getString(R.string.error)))
                            is Resource.Loading -> state = PaymentAcquireState.ProgressDialog(
                                resourcesProvider.getString(R.string.payment_options_loading_text)
                            )
                            is Resource.Success ->
                                state = PaymentAcquireState.PaymentAcquirersResult(it.value)

                        }
                    }
                state
            } catch (e: Exception) {
                PaymentAcquireState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }


    private fun fetchPaymentAcquirerMethods(acquirerId: Int) {

        viewModelScope.launch {
            _state.value = try {

                val response =  fetchPaymentAcquirerMethodsUseCase(acquirerId)

                var state: PaymentAcquireState = PaymentAcquireState.Idle

                response
                    .collect {
                        when(it) {
                            is Resource.Default -> {}
                            is Resource.Failure ->
                                _effect.send(PaymentAcquireEffect.ErrorSnackBar(
                                    resourcesProvider.getString(R.string.error)))
                            is Resource.Loading -> {}
                            is Resource.Success ->
                                state = PaymentAcquireState.PaymentAcquirerMethodsResult(it.value, acquirerId)

                        }
                    }
                state
            } catch (e: Exception) {
                PaymentAcquireState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun fetchPaymentAcquirerMethodProvider(acquirerId: Int, paymentMethodId: Int) {


        viewModelScope.launch {
            _state.value = try {

                val response =  fetchPaymentAcquirerMethodProvidersUseCase(acquirerId, paymentMethodId)

                var state: PaymentAcquireState = PaymentAcquireState.Idle

                response
                    .collect {
                        when(it) {
                            is Resource.Default -> {}
                            is Resource.Failure ->
                                _effect.send(PaymentAcquireEffect.ErrorSnackBar(
                                    resourcesProvider.getString(R.string.error)))
                            is Resource.Loading -> {}
                            is Resource.Success ->
                                state = PaymentAcquireState.PaymentAcquirerMethodProviderResult(it.value, paymentMethodId)

                        }
                    }
                state
            } catch (e: Exception) {
                PaymentAcquireState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }



}