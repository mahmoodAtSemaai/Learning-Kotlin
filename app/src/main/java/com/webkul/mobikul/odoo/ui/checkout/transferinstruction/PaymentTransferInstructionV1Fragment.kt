package com.webkul.mobikul.odoo.ui.checkout.transferinstruction

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.extension.close
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.data.entity.PaymentTransferInstructionEntity
import com.webkul.mobikul.odoo.databinding.FragmentPaymentTransferInstructionV1Binding
import com.webkul.mobikul.odoo.helper.SnackbarHelper
import com.webkul.mobikul.odoo.model.checkout.TransferInstruction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PaymentTransferInstructionV1Fragment @Inject constructor() : BindingBaseFragment<FragmentPaymentTransferInstructionV1Binding>(),
        IView<PaymentTransferInstructionIntent, PaymentTransferInstructionState, PaymentTransferInstructionEffect>, TransferInstructionListeners {

    override val layoutId = R.layout.fragment_payment_transfer_instruction_v1
    private val viewModel: PaymentTransferInstructionViewModel by viewModels()

    private lateinit var adapter: PaymentTransferInstructionAdapterV1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setOnClickListeners()
        triggerIntent(PaymentTransferInstructionIntent.SetBundleData(bundle = requireArguments()))
    }

    private fun setObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.state.collect {
                render(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.effect.collect {
                render(it)
            }
        }
    }

    private fun setOnClickListeners() {
        binding.tbTransferInstruction.toolbar.setNavigationOnClickListener {
            this.close()
        }
    }

    override fun render(effect: PaymentTransferInstructionEffect) {
        when (effect) {
            is PaymentTransferInstructionEffect.ShowOrHideInstruction -> adapter?.notifyItemChanged(effect.position)
            is PaymentTransferInstructionEffect.ErrorSnackBar -> {
                dismissProgressDialog()
                showSnackbarMessage(
                        effect.message.toString(),
                        SnackbarHelper.SnackbarType.TYPE_WARNING
                )
            }
        }
    }

    override fun render(state: PaymentTransferInstructionState) {
        when (state) {
            is PaymentTransferInstructionState.Idle -> {}
            is PaymentTransferInstructionState.Loading -> {
                dismissProgressDialog()
                updateProgressDialog(getString(R.string.please_wait_a_moment))
                showProgressDialog()
            }
            is PaymentTransferInstructionState.DisplayPaymentTransferInstruction -> onTransferInstructionFetched(state.transferInstructions)
            is PaymentTransferInstructionState.Error -> {
                dismissProgressDialog()
                showErrorState(state.failureStatus, state.message.toString())
            }
            is PaymentTransferInstructionState.SetInitialLayout -> setToolbar()
        }
    }

    override fun triggerIntent(intent: PaymentTransferInstructionIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }

    private fun setToolbar() {
        binding.tbTransferInstruction.tvToolBar.text = getString(R.string.how_to_transfer)
        triggerIntent(PaymentTransferInstructionIntent.FetchPaymentTransferInstructions)
    }

    private fun onTransferInstructionFetched(transferInstructions: PaymentTransferInstructionEntity) {
        dismissProgressDialog()
        adapter = PaymentTransferInstructionAdapterV1(
                requireContext(),
                transferInstructions.list,
                this
        )

        binding.apply {
            rvTransferInstruction.adapter = adapter
        }
    }

    companion object {

        const val BUNDLE_KEY_TRANSFER_INSTRUCTION = "TRANSFER_INSTRUCTION"

        @JvmStatic
        fun newInstance(transferInstruction: TransferInstruction) =
                PaymentTransferInstructionV1Fragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(BUNDLE_KEY_TRANSFER_INSTRUCTION, transferInstruction)
                    }
                }
    }

    override fun onChildInstructionTextClick(position: Int) {
        triggerIntent(PaymentTransferInstructionIntent.ShowOrHideInstruction(position = position))
    }

}