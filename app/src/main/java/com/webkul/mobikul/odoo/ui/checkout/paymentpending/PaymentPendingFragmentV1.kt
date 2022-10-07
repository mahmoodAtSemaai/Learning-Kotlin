package com.webkul.mobikul.odoo.ui.checkout.paymentpending

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.FragmentContainerActivity
import com.webkul.mobikul.odoo.core.extension.close
import com.webkul.mobikul.odoo.core.extension.makeGone
import com.webkul.mobikul.odoo.core.extension.makeVisible
import com.webkul.mobikul.odoo.core.mvicore.IEffect
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BaseActivity
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.data.entity.BankEntity
import com.webkul.mobikul.odoo.data.entity.PendingPaymentEntity
import com.webkul.mobikul.odoo.databinding.FragmentPaymentPendingV1Binding
import com.webkul.mobikul.odoo.helper.SnackbarHelper
import com.webkul.mobikul.odoo.model.checkout.Bank
import com.webkul.mobikul.odoo.model.checkout.TransferInstruction
import com.webkul.mobikul.odoo.model.payments.PendingPaymentData
import com.webkul.mobikul.odoo.ui.checkout.order.OrderFragmentV1
import com.webkul.mobikul.odoo.ui.checkout.paymentstatus.PaymentStatusFragmentV1
import com.webkul.mobikul.odoo.ui.checkout.transferinstruction.PaymentTransferInstructionV1Fragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PaymentPendingFragmentV1 @Inject constructor() : BindingBaseFragment<FragmentPaymentPendingV1Binding>(),
        IView<PaymentPendingIntent, PaymentPendingState, IEffect> {

    override val layoutId = R.layout.fragment_payment_pending_v1
    private val viewModel: PaymentPendingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setOnClickListeners()
        triggerIntent(PaymentPendingIntent.SetBundleData(bundle = arguments))
    }

    private fun setToolbar() {
        binding.tbPaymentContainer.tvToolBar.text = getString(R.string.order_status)
    }

    override fun render(state: PaymentPendingState) {
        when (state) {
            is PaymentPendingState.Idle -> {}
            is PaymentPendingState.StopProgressDialog -> dismissProgressDialog()
            is PaymentPendingState.Loading -> initializeProgressDialog(state.message)
            is PaymentPendingState.SetUpInitialUI -> setUpInitialScreen(state.pendingPaymentData)
            is PaymentPendingState.Error -> {
                dismissProgressDialog()
                showErrorState(state.failureStatus, state.message.toString())
            }
        }
    }

    override fun render(effect: IEffect) {
        when(effect) {
            is PaymentPendingEffect.DisplayCopyToClipBoardToast -> copyTextToClipboard()
            is PaymentPendingEffect.OrderDetails -> navigateToOrderDetails(effect.orderId)
            is PaymentPendingEffect.TransferInstructionFragment -> navigateToTransferInstructionFragment(effect.bank)
            is PaymentPendingEffect.DisplayPaymentStatusScreen -> {
                dismissProgressDialog()
                navigateToPaymentStatusScreen(effect.paymentStatusMsgConst)
            }
            is PaymentPendingEffect.ErrorSnackBar -> {
                dismissProgressDialog()
                showSnackbarMessage(
                        effect.message.toString(),
                        SnackbarHelper.SnackbarType.TYPE_WARNING
                )
            }
        }
    }

    override fun triggerIntent(intent: PaymentPendingIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }

    private fun setOnClickListeners() {
        binding.tvAccountNumber.setOnClickListener {
            triggerIntent(PaymentPendingIntent.CopyToClipBoard)
        }

        binding.btnOrderDetails.setOnClickListener {
            triggerIntent(PaymentPendingIntent.DisplayOrderDetails(orderId = viewModel.orderId))
        }

        binding.btnPaymentStatus.setOnClickListener {
            triggerIntent(PaymentPendingIntent.CheckPaymentStatus(viewModel.orderId))
        }

        binding.tvTransferInstruction.setOnClickListener {
            triggerIntent(PaymentPendingIntent.DisplayTransferInstruction(viewModel.pendingPaymentData!!))
        }

        binding.tbPaymentContainer.toolbar.setNavigationOnClickListener {
            this.close()
        }
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

    private fun setUpInitialScreen(paymentData: PendingPaymentEntity) {
        setToolbar()
        binding.apply {
            tvPaymentDueDate.text = paymentData.getPaymentExpiryDate()
            tvPaymentDueDateTime.text = paymentData.expireTime
            Glide.with(requireActivity()).load(paymentData.bank.thumbnail).into(ivVirtualAccount)
            tvVirtualAccountBank.text = String.format("%s (%s)", paymentData.bank.name, paymentData.bank.type)
            tvAccountNumber.text = paymentData.bank.accountNumb
            tvTotalPaymentValue.text = paymentData.getFormattedPaymentAmount()
        }

        if (paymentData.isCheckout) visibleButtons()
        else disappearButtons()
    }

    //remove buttons if checkout is false
    private fun disappearButtons(){
        binding.btnPaymentStatus.makeGone()
        binding.btnOrderDetails.makeGone()
    }

    //remove buttons only if checkout is available
    private fun visibleButtons(){
        binding.btnPaymentStatus.makeVisible()
        binding.btnOrderDetails.makeVisible()
    }

    private fun copyTextToClipboard() {
        val clipboard =
                ContextCompat.getSystemService(requireContext(), ClipboardManager::class.java)
        val clip = ClipData.newPlainText(
                getString(R.string.label),
                binding.tvAccountNumber.text.toString()
        )
        clipboard!!.setPrimaryClip(clip)
        Toast.makeText(requireContext(), R.string.text_copied_message, Toast.LENGTH_SHORT).show()
    }

    private fun initializeProgressDialog(message: String) {
        dismissProgressDialog()
        updateProgressDialog(message)
        showProgressDialog()
    }

    private fun navigateToOrderDetails(orderId: Int) {
        (requireActivity() as BaseActivity).addFragmentWithBackStack(
            R.id.fcv_container,
            OrderFragmentV1.newInstance(orderId),
            null
        )
    }

    private fun navigateToTransferInstructionFragment(bank: BankEntity) {
        (requireActivity() as BaseActivity).addFragmentWithBackStack(
                R.id.fcv_container,
                PaymentTransferInstructionV1Fragment.newInstance(TransferInstruction(heading = bank.name, instruction = "", providerId = bank.providerId)),
                null
        )
    }

    private fun navigateToPaymentStatusScreen(paymentStatusMessageConst: Int) {

        (requireActivity() as BaseActivity).addFragmentWithBackStack(
                R.id.fcv_container,
                PaymentStatusFragmentV1.newInstance(paymentStatusMessageConst),
                null
        )
    }

    companion object {
        const val PENDING = 1
        const val COMPLETED = 2
        const val FAILED = 3
        const val BUNDLE_KEY_FRAGMENT_TYPE = "FRAGMENT_TYPE"
        const val BUNDLE_PAYMENT_PENDING_SCREEN = "PAYMENT_PENDING"
        const val BUNDLE_ORDER_DETAIL_SCREEN = "ORDER_DETAIL"
        const val BUNDLE_KEY_TRANSFER_INSTRUCTION = "TRANSFER_INSTRUCTION"
        const val BUNDLE_PAYMENT_STATUS_SCREEN = "PAYMENT_STATUS"

        @JvmStatic
        fun newInstance(pendingPaymentData: PendingPaymentEntity) =
                PaymentPendingFragmentV1().apply {
                    arguments = Bundle().apply {
                        putParcelable(BUNDLE_PAYMENT_PENDING_SCREEN, pendingPaymentData)
                    }
                }
    }

}