package com.webkul.mobikul.odoo.ui.checkout.paymentpending

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import com.webkul.mobikul.odoo.data.entity.PendingPaymentEntity
import com.webkul.mobikul.odoo.domain.usecase.checkout.FetchTransactionStatusUseCase
import com.webkul.mobikul.odoo.ui.checkout.paymentpending.PaymentPendingFragmentV1.Companion.BUNDLE_PAYMENT_PENDING_SCREEN
import com.webkul.mobikul.odoo.ui.checkout.paymentstatus.PaymentStatusFragmentV1
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentPendingViewModel @Inject constructor(
        private val transactionStatusUseCase: FetchTransactionStatusUseCase,
        private val resourcesProvider: ResourcesProvider
) : BaseViewModel(), IModel<PaymentPendingState, PaymentPendingIntent, PaymentPendingEffect> {

    override val intents: Channel<PaymentPendingIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableStateFlow<PaymentPendingState>(PaymentPendingState.Idle)
    override val state: StateFlow<PaymentPendingState>
        get() = _state

    private val _effect = Channel<PaymentPendingEffect>()
    override val effect: Flow<PaymentPendingEffect>
        get() = _effect.receiveAsFlow()

    var orderId: Int = -1
    var pendingPaymentData: PendingPaymentEntity? = null

    init {
        handlerIntent()
    }

    override fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect {
                when (it) {
                    is PaymentPendingIntent.SetBundleData -> setBundleData(it.bundle)
                    is PaymentPendingIntent.DisplayOrderDetails -> {
                        launch {  _effect.send(PaymentPendingEffect.OrderDetails(it.orderId))}
                    }
                    is PaymentPendingIntent.CheckPaymentStatus -> fetchPaymentStatus(it.orderId) //do api call
                    is PaymentPendingIntent.DisplayTransferInstruction -> {
                        launch {
                            _effect.send(PaymentPendingEffect.TransferInstructionFragment(it.pendingPaymentData.bank))
                        }
                    }
                    is PaymentPendingIntent.CopyToClipBoard -> {
                        launch {
                            _effect.send(PaymentPendingEffect.DisplayCopyToClipBoardToast)
                        }
                    }
                }
            }
        }
    }

    private fun fetchPaymentStatus(orderId: Int) {
        viewModelScope.launch {
            _state.value = PaymentPendingState.Loading(
                    resourcesProvider.getString(R.string.checking_payment_status)
            )
            _state.value = try {

                val response = transactionStatusUseCase(orderId)

                var state: PaymentPendingState = PaymentPendingState.Idle

                response
                        .collect {
                            when (it) {
                                is Resource.Default -> {}
                                is Resource.Failure ->
                                    _effect.send(PaymentPendingEffect.ErrorSnackBar(
                                            resourcesProvider.getString(R.string.error_something_went_wrong)))
                                is Resource.Loading -> state = PaymentPendingState.Loading(
                                        resourcesProvider.getString(R.string.checking_payment_status)
                                )
                                is Resource.Success -> {
                                    val paymentStatusMessageConst = getPaymentStatusMessageConst(it.value.paymentStatusResult.paymentStatusCode)
                                    state = PaymentPendingState.Idle
                                    if (paymentStatusMessageConst != -1) {
                                        _effect.send(PaymentPendingEffect.DisplayPaymentStatusScreen(paymentStatusMsgConst = paymentStatusMessageConst))
                                    }else{
                                        state = PaymentPendingState.StopProgressDialog
                                    }
                                }
                            }
                        }
                state
            } catch (e: Exception) {
                PaymentPendingState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun setBundleData(bundle: Bundle?) {
        if (bundle?.containsKey(BUNDLE_PAYMENT_PENDING_SCREEN) == true) {
            pendingPaymentData =
                    bundle.getParcelable(BUNDLE_PAYMENT_PENDING_SCREEN)!!

            //set up UI with bundle
            _state.value = PaymentPendingState.SetUpInitialUI(pendingPaymentData!!)

            pendingPaymentData?.orderId.also {
                if (it != null) {
                    orderId = it
                }
            }
        }
    }

    private fun getPaymentStatusMessageConst(paymentStatusCode: Int): Int {
        return when (paymentStatusCode) {
            PaymentPendingFragmentV1.COMPLETED -> PaymentStatusFragmentV1.SHOW_PAYMENT_COMPLETE_MESSAGE
            PaymentPendingFragmentV1.FAILED -> PaymentStatusFragmentV1.SHOW_PAYMENT_FAILURE_MESSAGE
            else -> -1
        }
    }
}