package com.webkul.mobikul.odoo.ui.checkout.paymentprocessing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.extension.close
import com.webkul.mobikul.odoo.core.mvicore.IEffect
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BaseActivity
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.data.entity.PaymentDetailsEntity
import com.webkul.mobikul.odoo.data.entity.PendingPaymentEntity
import com.webkul.mobikul.odoo.databinding.FragmentPaymentAcquireV1Binding
import com.webkul.mobikul.odoo.databinding.FragmentPaymentProcessingV1Binding
import com.webkul.mobikul.odoo.helper.AlertDialogHelper
import com.webkul.mobikul.odoo.helper.SnackbarHelper
import com.webkul.mobikul.odoo.model.payments.CreateVirtualAccountPaymentRequest
import com.webkul.mobikul.odoo.model.payments.PaymentDetails
import com.webkul.mobikul.odoo.model.payments.PendingPaymentData
import com.webkul.mobikul.odoo.ui.checkout.dashboard.CheckoutDashboardFragmentV1
import com.webkul.mobikul.odoo.ui.checkout.paymentacquire.PaymentAcquireIntent
import com.webkul.mobikul.odoo.ui.checkout.paymentacquire.PaymentAcquireState
import com.webkul.mobikul.odoo.ui.checkout.paymentacquire.PaymentAcquireViewModel
import com.webkul.mobikul.odoo.ui.checkout.paymentpending.PaymentPendingFragmentV1
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PaymentProcessingFragmentV1 @Inject constructor() : BindingBaseFragment<FragmentPaymentProcessingV1Binding>(),
    IView<PaymentProcessingIntent, PaymentProcessingState, PaymentProcessingEffect> {

    override val layoutId = R.layout.fragment_payment_processing_v1
    private val viewModel: PaymentProcessingViewModel by viewModels()

    private var dialog: SweetAlertDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        triggerIntent(PaymentProcessingIntent.SetBundleData(arguments))
    }

    private fun disableBackButton() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {

                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun setToolbar() {
        binding.tbPaymentContainer.tvToolBar.text = getString(R.string.virtual_account)
        binding.tbPaymentContainer.toolbar.navigationIcon = null
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

    override fun triggerIntent(intent: PaymentProcessingIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }


    override fun render(state: PaymentProcessingState) {
        when (state) {
            is PaymentProcessingState.Error -> showErrorState(state.failureStatus, state.message.toString())
            is PaymentProcessingState.ErrorDialog -> showErrorDialog(state.title.toString(), state.message.toString())
            is PaymentProcessingState.Idle -> {}
            is PaymentProcessingState.PaymentProcessingResult -> onPaymentProcessed(state.paymentDetails, state.orderId)
            is PaymentProcessingState.SetInitialLayout -> {
                setToolbar()
                disableBackButton()
            }
        }
    }

    override fun render(effect: PaymentProcessingEffect) {

    }


    private fun onPaymentProcessed(paymentDetails: PaymentDetailsEntity, orderId: Int) {

        val pendingPaymentData: PendingPaymentEntity = PendingPaymentEntity(
                expireDate = paymentDetails.expireDate,
                expireTime = paymentDetails.expireTime,
                paymentAmount = paymentDetails.paymentAmount,
                bank = paymentDetails.bank,
                orderId = orderId,
                isCheckout = true
        )

        (requireActivity() as BaseActivity).replaceFragmentWithoutBackStack(
            R.id.fcv_container,
            PaymentPendingFragmentV1.newInstance(pendingPaymentData)
        )
    }


    private fun showErrorDialog(title: String, message: String) {
        dialog?.hide()

        dialog = AlertDialogHelper.getAlertDialog(
            requireContext(), SweetAlertDialog.ERROR_TYPE, title,
            message, false, false
        )

        dialog
            ?.setConfirmText(getString(R.string.ok))
            ?.setConfirmClickListener {
                dialog?.hide()
                requireActivity().finish()
            }

        dialog?.show()
    }

    companion object {

        const val CREATE_VIRTUAL_ACC_REQUEST = "CREATE_VIRTUAL_ACC_REQUEST"
        const val ORDER_ID = "ORDER_ID"

        @JvmStatic
        fun newInstance(createVirtualAccountPaymentRequest: CreateVirtualAccountPaymentRequest,
                           orderId: Int) =
            PaymentProcessingFragmentV1().apply {
                arguments = Bundle().apply {
                    putInt(ORDER_ID, orderId)
                    putParcelable(CREATE_VIRTUAL_ACC_REQUEST, createVirtualAccountPaymentRequest)
                }
            }
    }
}