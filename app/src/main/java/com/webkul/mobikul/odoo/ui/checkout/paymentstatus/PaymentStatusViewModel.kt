package com.webkul.mobikul.odoo.ui.checkout.paymentstatus

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.core.mvicore.IEffect
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.ui.checkout.shippingmethod.ShippingMethodFragmentV1
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentStatusViewModel @Inject constructor(
) : BaseViewModel(), IModel<PaymentStatusState, PaymentStatusIntent, PaymentStatusEffect> {

    var paymentStat: Int = 0
    override val intents: Channel<PaymentStatusIntent> = Channel<PaymentStatusIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<PaymentStatusState>(PaymentStatusState.Idle)
    override val state: StateFlow<PaymentStatusState>
        get() = _state

    private val _effect = Channel<PaymentStatusEffect>()
    override val effect: Flow<PaymentStatusEffect>
        get() = _effect.receiveAsFlow()

    init {
        handlerIntent()
    }

    override fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect {
                when (it) {
                    is PaymentStatusIntent.ContinueShopping -> navigateToContinueShopping()
                    is PaymentStatusIntent.SetBundleData -> setBundleData(it.bundle)
                    is PaymentStatusIntent.SetInitialLayout -> setInitialLayout()
                }
            }
        }
    }

    private fun setBundleData(bundle: Bundle?) {
        bundle?.let {  paymentStat = bundle.getInt(PaymentStatusFragmentV1.BUNDLE_PAYMENT_MESSAGE) }
        setPaymentStatus(paymentStat = paymentStat)
    }

    private fun setInitialLayout() {
        _state.value = PaymentStatusState.SetInitialLayout
    }

    private fun navigateToContinueShopping(){
        _state.value = PaymentStatusState.ContinueShopping
    }

    private fun setPaymentStatus(paymentStat: Int){
            when(paymentStat){
                PaymentStatusFragmentV1.SHOW_PAYMENT_COMPLETE_MESSAGE -> {
                    _state.value = PaymentStatusState.ShowCompletePayment
                }
                PaymentStatusFragmentV1.SHOW_PAYMENT_FAILURE_MESSAGE ->{
                    _state.value = PaymentStatusState.ShowFailurePayment

                }
                PaymentStatusFragmentV1.SHOW_PAYMENT_COD_MESSAGE ->{
                    _state.value = PaymentStatusState.ShowCODPayment
                }
                PaymentStatusFragmentV1.SHOPEE_PAY_ACTIVATED_MESSAGE -> {
                    _state.value = PaymentStatusState.ShowPaymentShopeeActivated
                }
            }
    }
}