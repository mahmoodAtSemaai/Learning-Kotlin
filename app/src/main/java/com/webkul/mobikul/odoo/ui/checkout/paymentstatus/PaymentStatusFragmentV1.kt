package com.webkul.mobikul.odoo.ui.checkout.paymentstatus

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.extension.close
import com.webkul.mobikul.odoo.core.extension.makeGone
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.databinding.FragmentPaymentStatusV1Binding
import com.webkul.mobikul.odoo.helper.IntentHelper
import com.webkul.mobikul.odoo.model.payments.PaymentStatusDescription
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class PaymentStatusFragmentV1 @Inject constructor() : BindingBaseFragment<FragmentPaymentStatusV1Binding>(),
        IView<PaymentStatusIntent, PaymentStatusState, PaymentStatusEffect> {

    override val layoutId = R.layout.fragment_payment_status_v1
    private val viewModel: PaymentStatusViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setOnClickListeners()
        triggerIntent(PaymentStatusIntent.SetInitialLayout)
        triggerIntent(PaymentStatusIntent.SetBundleData(bundle = arguments))
    }

    private fun setToolbar() {
        binding.tbPaymentStatus.tvToolBar.makeGone()
        binding.tbPaymentStatus.toolbar.title = getString(R.string.payment_status)
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

    override fun render(effect: PaymentStatusEffect) {

    }

    override fun render(state: PaymentStatusState) {
        when(state){
            is PaymentStatusState.ContinueShopping ->{
                IntentHelper.continueShopping(requireContext())
            }
            is PaymentStatusState.ShowCompletePayment -> {
                displayCompletePayment()
            }
            is PaymentStatusState.ShowFailurePayment -> {
                displayFailurePayment()
            }
            is PaymentStatusState.ShowCODPayment -> {
                displayCODPayment()
            }
            is PaymentStatusState.ShowPaymentShopeeActivated -> {
                displayPaymentShopeeActivated()
            }
            is PaymentStatusState.Idle -> {}
            is PaymentStatusState.SetInitialLayout -> setToolbar()
        }
    }

    override fun triggerIntent(intent: PaymentStatusIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }

    private fun setOnClickListeners() {
        binding.btnContinue.setOnClickListener {
            onContinueButtonClicked()
        }

        binding.tbPaymentStatus.toolbar.setNavigationOnClickListener {
            this.close()
        }
    }

    private fun displayCompletePayment(){
        setUpPaymentStatusUI(
                PaymentStatusDescription(
                        getString(R.string.payment_n_status),
                        getString(R.string.continue_shopping),
                        getString(R.string.paid),
                        getString(R.string.payment_success_text),
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_green_bg, null),
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_green_tick, null)
                )
        )
    }

    private fun displayFailurePayment(){
        setUpPaymentStatusUI(
                PaymentStatusDescription(
                        getString(R.string.payment_n_status),
                        getString(R.string.continue_shopping),
                        getString(R.string.payment_failure),
                        getString(R.string.payment_failure_text),
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_grey_bg, null),
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_cross, null)
                )
        )
    }

    private fun displayCODPayment(){
        setUpPaymentStatusUI(
                PaymentStatusDescription(
                        getString(R.string.payment_n_status),
                        getString(R.string.continue_shopping),
                        getString(R.string.your_order_will_be_delivered_soon),
                        "",
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_green_bg, null),
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_cod, null)
                )
        )
    }

    private fun displayPaymentShopeeActivated(){
        setUpPaymentStatusUI(
                PaymentStatusDescription(
                        getString(R.string.activate_shopee_pay_text),
                        getString(R.string.continue_),
                        getString(R.string.shopee_activated_text),
                        "",
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_orange_bg, null),
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_orange_tick, null)
                )
        )
    }


    private fun setUpPaymentStatusUI(paymentStatusDescription: PaymentStatusDescription) {
        binding.apply {
            tbPaymentStatus.tvToolBar.text = paymentStatusDescription.activityToolbarText
            btnContinue.text = paymentStatusDescription.buttonText
            tvPaymentStatusHeading.text = paymentStatusDescription.headingText
            tvPaymentStatusDesc.text = paymentStatusDescription.descriptionText
            ivPaymentStatusBg.background = paymentStatusDescription.drawable
            ivPaymentStatusIcon.setImageDrawable(paymentStatusDescription.icon)
        }
    }

    private fun onContinueButtonClicked(){
        triggerIntent(PaymentStatusIntent.ContinueShopping)
    }

    companion object {

        const val BUNDLE_PAYMENT_MESSAGE = "paymentStatus"
        const val SHOW_PAYMENT_COMPLETE_MESSAGE = 1
        const val SHOW_PAYMENT_FAILURE_MESSAGE = 2
        const val SHOW_PAYMENT_COD_MESSAGE = 3
        const val SHOPEE_PAY_ACTIVATED_MESSAGE = 4

        @JvmStatic
        fun newInstance(paymentStatus: Int) =
                PaymentStatusFragmentV1().apply {
                    arguments = Bundle().apply {
                        putInt(BUNDLE_PAYMENT_MESSAGE, paymentStatus)
                    }
                }
    }

}