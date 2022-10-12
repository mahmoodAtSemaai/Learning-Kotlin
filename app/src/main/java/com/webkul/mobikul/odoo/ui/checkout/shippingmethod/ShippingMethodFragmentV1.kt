package com.webkul.mobikul.odoo.ui.checkout.shippingmethod

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.extension.close
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.data.entity.ShippingMethodEntity
import com.webkul.mobikul.odoo.data.entity.ShippingMethodListEntity
import com.webkul.mobikul.odoo.databinding.FragmentShippingMethodV1Binding
import com.webkul.mobikul.odoo.helper.SnackbarHelper
import com.webkul.mobikul.odoo.ui.checkout.dashboard.CheckoutDashboardFragmentV1
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ShippingMethodFragmentV1 @Inject constructor() : BindingBaseFragment<FragmentShippingMethodV1Binding>(),
    IView<ShippingMethodIntent, ShippingMethodState, ShippingMethodEffect> {

    override val layoutId = R.layout.fragment_shipping_method_v1
    private val viewModel: ShippingMethodViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setOnClickListeners()
        triggerIntent(ShippingMethodIntent.SetInitialLayout)
        triggerIntent(ShippingMethodIntent.SetBundleData(arguments))
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

        binding.btnConfirmShippingMethod.setOnClickListener {
            triggerIntent(ShippingMethodIntent.UpdateShippingMethod(
                viewModel.cartId,
                viewModel.shippingMethodEntity)
            )
        }

        binding.tbPaymentContainer.toolbar.setNavigationOnClickListener {
            this.close()
        }
    }

    private fun setToolbar() {
        binding.tbPaymentContainer.tvToolBar.text = getString(R.string.shipping_method)
    }


    override fun render(state: ShippingMethodState) {
        when (state) {
            is ShippingMethodState.Error -> {
                dismissProgressDialog()
                showErrorState(state.failureStatus, state.message.toString())
            }
            is ShippingMethodState.FetchShippingMethod ->
                setShippingMethodList(state.shippingMethodListEntity)
            is ShippingMethodState.Idle -> {}
            is ShippingMethodState.ProgressDialog -> initializeProgressDialog(state.message.toString())
            is ShippingMethodState.UpdateShippingMethodForOrder -> onShippingMethodUpdated()
            is ShippingMethodState.SetInitialLayout -> setToolbar()
        }
    }


    override fun triggerIntent(intent: ShippingMethodIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }

    private fun onShippingMethodUpdated() {
        dismissProgressDialog()

        requireActivity().supportFragmentManager.setFragmentResult(
            CheckoutDashboardFragmentV1.SHIPPING_METHOD_RESULT,
            Bundle().also {
                it.putBoolean(CheckoutDashboardFragmentV1.RESULT, true)
                it.putString(CheckoutDashboardFragmentV1.SHIPPING_METHOD_RESULT_VALUE, viewModel.shippingMethodEntity?.name)
            }
        )

        this.close()
    }

    private fun setShippingMethodList(shippingMethodListEntity: ShippingMethodListEntity) {
        dismissProgressDialog()

        binding.rvShippingMethod.adapter = ShippingMethodAdapterV1(
            shippingMethodListEntity ?: emptyList(),
            object : ShippingMethodAdapterV1.ShippingMethodSelectionListenerV1 {
                override fun updateShippingMethod(shippingMethodEntity: ShippingMethodEntity) {
                    viewModel.shippingMethodEntity = shippingMethodEntity
                }

            }
        )
    }

    private fun initializeProgressDialog(message: String) {
        dismissProgressDialog()
        updateProgressDialog(message)
        showProgressDialog()
    }

    companion object {

        const val CART_ID = "CART_ID"

        @JvmStatic
        fun newInstance(cartId: Int) =
            ShippingMethodFragmentV1().apply {
                arguments = Bundle().apply {
                    putInt(CART_ID, cartId)
                }
            }


    }

    override fun render(effect: ShippingMethodEffect) {
        when(effect) {
            is ShippingMethodEffect.ErrorSnackBar -> {
                dismissProgressDialog()
                showSnackbarMessage(
                    effect.message.toString(),
                    SnackbarHelper.SnackbarType.TYPE_WARNING
                )
            }
        }
    }

}