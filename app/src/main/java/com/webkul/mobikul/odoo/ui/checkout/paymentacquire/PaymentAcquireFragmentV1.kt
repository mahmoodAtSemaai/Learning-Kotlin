package com.webkul.mobikul.odoo.ui.checkout.paymentacquire

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import cn.pedant.SweetAlert.SweetAlertDialog
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.adapter.checkout.VirtualAccountAdapter
import com.webkul.mobikul.odoo.core.extension.close
import com.webkul.mobikul.odoo.core.extension.makeGone
import com.webkul.mobikul.odoo.core.extension.makeVisible
import com.webkul.mobikul.odoo.core.mvicore.IEffect
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.data.entity.PaymentAcquireCheckoutEntity
import com.webkul.mobikul.odoo.data.entity.PaymentAcquirerMethodProviderCheckoutEntity
import com.webkul.mobikul.odoo.data.entity.PaymentAcquirerMethodCheckoutEntity
import com.webkul.mobikul.odoo.databinding.FragmentPaymentAcquireV1Binding
import com.webkul.mobikul.odoo.helper.SnackbarHelper
import com.webkul.mobikul.odoo.model.payments.PaymentAcquirerMethod
import com.webkul.mobikul.odoo.model.payments.SelectedPaymentMethod
import com.webkul.mobikul.odoo.ui.checkout.dashboard.CheckoutDashboardFragmentV1
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PaymentAcquireFragmentV1 @Inject constructor() : BindingBaseFragment<FragmentPaymentAcquireV1Binding>(),
    IView<PaymentAcquireIntent, PaymentAcquireState, PaymentAcquireEffect>,
    VirtualAccountAdapter.onVirtualAccountOptionSelected {

    override val layoutId = R.layout.fragment_payment_acquire_v1
    private val viewModel: PaymentAcquireViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setOnClickListeners()
        triggerIntent(PaymentAcquireIntent.SetInitialLayout)
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

        binding.btnConfirmPaymentMethod.setOnClickListener { v ->
            triggerIntent(PaymentAcquireIntent.SetEndResult)
        }

        binding.tbPaymentContainer.toolbar.setNavigationOnClickListener {
            this.close()
        }
    }

    private fun setToolbar() {
        binding.tbPaymentContainer.tvToolBar.text = getString(R.string.payment_method)
    }

    override fun triggerIntent(intent: PaymentAcquireIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }

    override fun updatePaymentOption(paymentAcquirerMethod: PaymentAcquirerMethod) {
        binding.layoutCod.ivOption.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.circular_radio_button_unselected_state,
                null
            )
        )
        viewModel.isCODSelected = false
        viewModel.selectedPaymentMethod = SelectedPaymentMethod(
            paymentAcquirerMethod.name,
            paymentAcquirerMethod.vendorID.toString(),
            paymentAcquirerMethod.id.toString(),
            paymentAcquirerMethod.type,
            false
        )
    }

    override fun render(state: PaymentAcquireState) {
        when (state) {
            is PaymentAcquireState.Error -> {
                dismissProgressDialog()
                showErrorState(state.failureStatus, state.message.toString())
            }
            is PaymentAcquireState.Idle -> {}
            is PaymentAcquireState.PaymentAcquirerMethodProviderResult -> onPaymentAcquirerMethodProviderResult(state.paymentAcquirerMethodProviderCheckoutEntity, state.paymentVendorId)
            is PaymentAcquireState.PaymentAcquirerMethodsResult -> onPaymentAcquirerMethodResult(state.paymentAcquirerMethodCheckoutEntity, state.paymentVendorId)
            is PaymentAcquireState.PaymentAcquirersResult -> onPaymentAcquirerResult(state.paymentAcquireCheckoutEntity)
            is PaymentAcquireState.ProgressDialog -> initializeProgressDialog(state.message.toString())
            is PaymentAcquireState.SetInitialLayout -> setInitialLayout()
            is PaymentAcquireState.EndResult -> onGetEndResult(state.selectedPaymentMethod)
        }
    }

    private fun onGetEndResult(selectedPaymentMethod: SelectedPaymentMethod) {
        dismissProgressDialog()

        requireActivity().supportFragmentManager.setFragmentResult(
            CheckoutDashboardFragmentV1.PAYMENT_METHOD_RESULT,
            Bundle().also {
                it.putParcelable(SELECTED_PAYMENT_METHOD, selectedPaymentMethod)
            }
        )

        this.close()
    }

    private fun onPaymentAcquirerMethodProviderResult(paymentAcquirerMethodProviderCheckoutEntity: PaymentAcquirerMethodProviderCheckoutEntity, paymentVendorId: Int) {
        for (item in paymentAcquirerMethodProviderCheckoutEntity.paymentAcquirerMethodProvider.paymentMethodProvider) {
            item.vendorID = paymentVendorId
        }
        setUpRecyclerView(paymentAcquirerMethodProviderCheckoutEntity.paymentAcquirerMethodProvider.paymentMethodProvider)
        binding.grLayout.makeVisible()
        dismissProgressDialog()
    }

    private fun onPaymentAcquirerMethodResult(
        paymentAcquirerMethodCheckoutEntity: PaymentAcquirerMethodCheckoutEntity, paymentVendorId: Int) {
        for (item in paymentAcquirerMethodCheckoutEntity.paymentAcquirerMethods.paymentAcquirerMethods) {
            if (item.type.equals(getString(R.string.va_text), true))
                triggerIntent(PaymentAcquireIntent.FetchPaymentAcquirerMethodProvider(item.id, paymentVendorId))
        }
    }

    private fun onPaymentAcquirerResult(paymentAcquireCheckoutEntity: PaymentAcquireCheckoutEntity) {
        for (item in paymentAcquireCheckoutEntity.paymentAcquirers.acquirers) {
            if (!item.isMobikulAvailable) {
                triggerIntent(PaymentAcquireIntent.FetchPaymentAcquirerMethods(item.id))
            } else {
                viewModel.COD_ID = item.id
            }
        }
    }

    private fun setInitialLayout() {
        setToolbar()
        setUpCODInitialLayout()
        setUpBankTransferHeadingLayout()
        triggerIntent(PaymentAcquireIntent.FetchPaymentAcquirers(COMPANY_ID))
    }

    private fun setUpCODInitialLayout() {
        binding.layoutCod.tvPaymentMethod.text = getString(R.string.cod_text)
        binding.layoutCod.root.setOnClickListener { v ->
            if (!viewModel.isCODSelected) {
                binding.layoutCod.ivOption.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.circular_radio_button_selected_state,
                        null
                    )
                )
                if (viewModel.selectedPaymentMethod == null) {
                    viewModel.selectedPaymentMethod =
                        SelectedPaymentMethod("", "", "", "", true)
                }
                viewModel.selectedPaymentMethod?.apply {
                    paymentMethodText = getString(R.string.cod_text)
                    paymentAcquirerId = viewModel.COD_ID.toString()
                    paymentAcquirerProviderType = ""
                    paymentAcquirerProviderId = ""
                    isCOD = true
                }
                viewModel.isCODSelected = true
                if (binding.rvPaymentMethodVaOptions.adapter != null) (binding.rvPaymentMethodVaOptions.adapter as VirtualAccountAdapter).resetAdapter()
            }
        }
    }

    private fun setUpRecyclerView(paymentMethodProvider: MutableList<PaymentAcquirerMethod>) {
        binding.rvPaymentMethodVaOptions.adapter = VirtualAccountAdapter(
            requireContext(),
            paymentMethodProvider,
            this
        )

        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.rvPaymentMethodVaOptions.addItemDecoration(dividerItemDecoration)
    }

    private fun setUpBankTransferHeadingLayout() {
        binding.layoutBankTransfer.ivPaymentMethod.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_bank_transfer,
                null
            )
        )
        binding.layoutBankTransfer.tvPaymentMethod.text = getString(R.string.bank_transfer_va)
        binding.layoutBankTransfer.ivOption.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_up_arrow_grey,
                null
            )
        )
        binding.layoutBankTransfer.root.setOnClickListener { v ->
            if (binding.rvPaymentMethodVaOptions.visibility == View.VISIBLE) {
                binding.rvPaymentMethodVaOptions.makeGone()
                binding.layoutBankTransfer.ivOption.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_down_arrow_grey,
                        null
                    )
                )
            } else {
                binding.rvPaymentMethodVaOptions.makeVisible()
                binding.layoutBankTransfer.ivOption.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_up_arrow_grey,
                        null
                    )
                )
            }
        }
    }

    private fun initializeProgressDialog(message: String) {
        dismissProgressDialog()
        updateProgressDialog(message)
        showProgressDialog()
    }

    companion object {

        const val COMPANY_ID = 1
        const val SELECTED_PAYMENT_METHOD = "SELECTED_PAYMENT_METHOD"

        @JvmStatic
        fun newInstance() =
            PaymentAcquireFragmentV1()
    }

    override fun render(effect: PaymentAcquireEffect) {
        when(effect) {
            is PaymentAcquireEffect.ErrorSnackBar -> {
                dismissProgressDialog()
                showSnackbarMessage(
                    effect.message.toString(),
                    SnackbarHelper.SnackbarType.TYPE_WARNING
                )
            }
        }
    }

}