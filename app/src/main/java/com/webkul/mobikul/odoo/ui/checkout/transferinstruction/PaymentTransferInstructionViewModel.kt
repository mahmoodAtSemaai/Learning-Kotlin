package com.webkul.mobikul.odoo.ui.checkout.transferinstruction

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import com.webkul.mobikul.odoo.data.entity.PaymentTransferInstructionEntity
import com.webkul.mobikul.odoo.domain.usecase.checkout.FetchTransferInstructionUseCase
import com.webkul.mobikul.odoo.model.checkout.TransferInstruction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentTransferInstructionViewModel @Inject constructor(
        private val resourcesProvider: ResourcesProvider,
        private val fetchTransferInstructionUseCase: FetchTransferInstructionUseCase
) : BaseViewModel(), IModel<PaymentTransferInstructionState, PaymentTransferInstructionIntent, PaymentTransferInstructionEffect> {

    override val intents: Channel<PaymentTransferInstructionIntent> = Channel<PaymentTransferInstructionIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<PaymentTransferInstructionState>(PaymentTransferInstructionState.Idle)
    override val state: StateFlow<PaymentTransferInstructionState>
        get() = _state

    private val _effect = Channel<PaymentTransferInstructionEffect>()
    override val effect: Flow<PaymentTransferInstructionEffect>
        get() = _effect.receiveAsFlow()

    private var transferInstruction: TransferInstruction? = null

    private lateinit var paymentTransferInstructionEntity: PaymentTransferInstructionEntity

    init {
        handlerIntent()
    }

    override fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect {
                when (it) {
                    is PaymentTransferInstructionIntent.SetBundleData -> setBundleData(it.bundle)
                    is PaymentTransferInstructionIntent.ShowOrHideInstruction -> displayOrHideInstruction(it.position)
                    is PaymentTransferInstructionIntent.FetchPaymentTransferInstructions -> fetchPaymentTransferInstruction(transferInstruction?.providerId!!)
                }
            }
        }
    }

    private fun setBundleData(bundle: Bundle) {
        transferInstruction = bundle.getParcelable<TransferInstruction>(PaymentTransferInstructionV1Fragment.BUNDLE_KEY_TRANSFER_INSTRUCTION)
        _state.value = PaymentTransferInstructionState.SetInitialLayout
    }

    private suspend fun displayOrHideInstruction(position: Int){
            paymentTransferInstructionEntity.list[position].isInstructionVisible = !paymentTransferInstructionEntity.list[position].isInstructionVisible
            _effect.send(PaymentTransferInstructionEffect.ShowOrHideInstruction(position))
    }

    private fun fetchPaymentTransferInstruction(providerId: Int){
        viewModelScope.launch {
            _state.value = PaymentTransferInstructionState.Loading
            _state.value = try {

                val response =  fetchTransferInstructionUseCase.invoke(
                        providerId
                )

                var state: PaymentTransferInstructionState = PaymentTransferInstructionState.Idle

                response.collect {
                            when(it) {
                                is Resource.Default -> {}
                                is Resource.Failure -> _effect.send(PaymentTransferInstructionEffect.ErrorSnackBar(resourcesProvider.getString(R.string.try_again_later_text)))
                                is Resource.Loading -> {
                                    state = PaymentTransferInstructionState.Loading
                                }
                                is Resource.Success -> {
                                    paymentTransferInstructionEntity = it.value
                                    if (!it.value.status.equals(resourcesProvider.getString(R.string.fail_2), true)) {
                                        state = PaymentTransferInstructionState.DisplayPaymentTransferInstruction(paymentTransferInstructionEntity)
                                    }else{
                                        _effect.send(PaymentTransferInstructionEffect.ErrorSnackBar(
                                                resourcesProvider.getString(R.string.try_again_later_text))
                                        )
                                    }
                                }
                            }
                        }
                state
            } catch (e: Exception) {
                PaymentTransferInstructionState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }
}