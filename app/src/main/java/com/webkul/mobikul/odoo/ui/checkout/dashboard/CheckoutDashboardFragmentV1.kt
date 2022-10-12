package com.webkul.mobikul.odoo.ui.checkout.dashboard

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.extension.makeGone
import com.webkul.mobikul.odoo.core.extension.makeInvisible
import com.webkul.mobikul.odoo.core.extension.makeVisible
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BaseActivity
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.data.entity.DeliveryEntity
import com.webkul.mobikul.odoo.data.entity.OrderEntity
import com.webkul.mobikul.odoo.data.entity.OrderProductEntity
import com.webkul.mobikul.odoo.data.entity.OrderReviewEntity
import com.webkul.mobikul.odoo.databinding.FragmentCheckoutDashboardV1Binding
import com.webkul.mobikul.odoo.helper.AlertDialogHelper
import com.webkul.mobikul.odoo.helper.AppSharedPref
import com.webkul.mobikul.odoo.helper.SnackbarHelper
import com.webkul.mobikul.odoo.model.checkout.ShippingMethod
import com.webkul.mobikul.odoo.model.payments.CreateVirtualAccountPaymentRequest
import com.webkul.mobikul.odoo.model.payments.OrderPaymentData
import com.webkul.mobikul.odoo.model.payments.SelectedPaymentMethod
import com.webkul.mobikul.odoo.ui.checkout.address.CheckoutAddressBookFragmentV1
import com.webkul.mobikul.odoo.ui.checkout.paymentacquire.PaymentAcquireFragmentV1
import com.webkul.mobikul.odoo.ui.checkout.paymentprocessing.PaymentProcessingFragmentV1
import com.webkul.mobikul.odoo.ui.checkout.paymentstatus.PaymentStatusFragmentV1
import com.webkul.mobikul.odoo.ui.checkout.paymentstatus.PaymentStatusFragmentV1.Companion.SHOW_PAYMENT_COD_MESSAGE
import com.webkul.mobikul.odoo.ui.checkout.shippingmethod.ShippingMethodFragmentV1
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

@AndroidEntryPoint
class CheckoutDashboardFragmentV1 @Inject constructor() : BindingBaseFragment<FragmentCheckoutDashboardV1Binding>(),
    IView<CheckoutIntent, CheckoutState, CheckoutEffect> {

    override val layoutId = R.layout.fragment_checkout_dashboard_v1
    private val viewModel: CheckoutViewModel by viewModels()
    private var dialog: SweetAlertDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setOnClickListeners()
        setFragmentResultListeners()
        triggerIntent(CheckoutIntent.SetInitialLayout)
    }

    override fun onDestroy() {
        triggerIntent(CheckoutIntent.SetIsCustomerWantToRedeemPoints(false))
        super.onDestroy()
    }

    override fun render(state: CheckoutState) {

        when (state) {
            is CheckoutState.Error -> {
                hideCurrentDialog()
                showErrorState(state.failureStatus, state.message.toString())
            }
            is CheckoutState.FetchOrderDetails -> onOrderDetailsReceived(state.orderEntity, state.refreshRecyclerView, state.refreshPaymentDetails)
            is CheckoutState.GetOrderReviewResponse -> getOrderReviewResponse(state.orderReviewEntity)
            is CheckoutState.Idle -> {}
            is CheckoutState.Loading -> {}
            is CheckoutState.OnPlacedCODOrder -> onPlacedCODOrder()
            is CheckoutState.OnPlacedVirtualOrder -> onPlacedVirtualOrder(state.transactionId, state.orderId, state.paymentAcquirerProviderId)
            is CheckoutState.PaymentMethodSelection -> onPaymentMethodSelected(state.selectedPaymentMethod)
            is CheckoutState.ProgressDialog -> showProgressDialog(state.message.toString())
            is CheckoutState.ShippingAddressSelection -> onShippingAddressSelected()
            is CheckoutState.ShippingMethodSelection -> onShippingMethodSelected()
            is CheckoutState.ShowError -> showError(state.view, state.message)
        }
    }

    private fun navigateToShippingAddress(cartId: Int) {

        (requireActivity() as BaseActivity).addFragmentWithBackStack(
            R.id.fcv_container,
            CheckoutAddressBookFragmentV1.newInstance(cartId),
            null
        )
    }

    private fun navigateToPaymentMethod() {
        (requireActivity() as BaseActivity).addFragmentWithBackStack(
            R.id.fcv_container,
            PaymentAcquireFragmentV1.newInstance(),
            null
        )
    }

    private fun navigateToShippingMethod(cartId: Int) {

        (requireActivity() as BaseActivity).addFragmentWithBackStack(
            R.id.fcv_container,
            ShippingMethodFragmentV1.
            newInstance(cartId),
            null
        )
    }

    private fun setFragmentResultListeners() {
        requireActivity().supportFragmentManager.setFragmentResultListener(
            SHIPPING_ADDRESS_RESULT,
            this
        ) { requestKey, bundle ->
            triggerIntent(CheckoutIntent.ResultFromShippingAddress(bundle))
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            SHIPPING_METHOD_RESULT,
            this
        ) { requestKey, bundle ->
            triggerIntent(CheckoutIntent.ResultFromShippingMethod(bundle))
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            PAYMENT_METHOD_RESULT,
            this
        ) { requestKey, bundle ->
            triggerIntent(CheckoutIntent.ResultFromPaymentMethod(bundle))
        }
    }

    private fun setObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.state.collect {
                render(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.effect.collect{
                render(it)
            }
        }
    }

    private fun setOnClickListeners() {

        binding.addressHeading.root.setOnClickListener {
            triggerIntent(CheckoutIntent.NavigateToShippingAddress(viewModel.cartId))
        }

        binding.layoutPaymentMethod.root.setOnClickListener {
            triggerIntent(CheckoutIntent.NavigateToPaymentMethod)
        }

        binding.layoutShippingMethod.root.setOnClickListener {
            triggerIntent(CheckoutIntent.NavigateToShippingMethod(viewModel.cartId))
        }

        binding.tbCheckout.toolbar.setNavigationOnClickListener {
            requireActivity().finish()
        }

        binding.btnConfirmOrder.setOnClickListener {
            triggerIntent(CheckoutIntent.StartValidationToPlaceOrder(
                viewModel.orderEntity,
                viewModel.selectedPaymentMethod))
        }
    }

    private fun onSuccessfulValidationForPlaceOrder() {
        binding.tvErrorMessage.makeGone()
        triggerIntent(CheckoutIntent.GetOrderReview(
            viewModel.orderEntity,
            viewModel.selectedPaymentMethod,
            viewModel.isUserWantToRedeemPoints
        ))

    }

    private fun setInitialLayout() {
        binding.root.makeInvisible()
        binding.tbCheckout.tvToolBar.text = getString(R.string.checkout)

        binding.addressHeading.apply {
            ivMethodIcon.setImageResource(R.drawable.ic_location_active)
            data = ShippingMethod(getString(R.string.address), "")
        }

        binding.layoutShippingMethod.apply {
            data = ShippingMethod(getString(R.string.shipping_method), getString(R.string.checkout_select_a_shipping_method))
            ivMethodIcon.setImageResource(R.drawable.ic_shipping_active)
        }

        binding.layoutPaymentMethod.apply {
            data = ShippingMethod(getString(R.string.payment_method), getString(R.string.checkout_select_your_payment_method))
            ivMethodIcon.setImageResource(R.drawable.ic_payment_active)
        }

        triggerIntent(CheckoutIntent.SetBundleAndPreferenceData(arguments))
    }



    private fun updateAddress(orderEntity: OrderEntity) {
        binding.layoutAddressInfo.addressData = orderEntity.shippingAddressId
    }

    private fun onValidatedShippingMethodFailure() {
            showError(
                binding.layoutShippingMethod.clCheckoutOptions,
                getString(R.string.select_your_shipping_method)
            )
            binding.nsvCheckout.smoothScrollTo(
                0,
                binding.layoutShippingMethod.clCheckoutOptions.top
            )
    }

    private fun onValidatedPaymentMethodFailure() {
            showError(
                binding.layoutPaymentMethod.clCheckoutOptions,
                getString(R.string.error_no_payment_method_chosen)
            )
            binding.nsvCheckout.smoothScrollTo(0,
                binding.paymentDetailContainer.root.bottom)
    }

    private fun onValidatedShippingAddressFailure() {
                showError(
                    binding.layoutAddressInfo.root,
                    getString(R.string.unserviceable_address_text)
                )
                binding.nsvCheckout.smoothScrollTo(0, 0)
    }



    private fun onValidatedCheckoutOrderDetails(result: Boolean) {
        if (result) {
            binding.btnConfirmOrder.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorAccent
                )
            )
        } else {
            binding.btnConfirmOrder.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.grey_600
                )
            )
        }
    }

    private fun updateShippingMethod(delivery: DeliveryEntity?) {
        binding.layoutShippingMethod.tvMethodDescription.text =
            if (delivery != null) delivery.name else getString(R.string.checkout_select_a_shipping_method)

    }

    private fun updateProductDetail(productsList: List<OrderProductEntity>?) {
        if (productsList != null) {
            binding.rvCheckout.adapter =
                CheckoutProductsAdapterV1(productsList)
        }
    }

    private fun updatePaymentDetail(orderEntity: OrderEntity) {
        val orderPaymentData: OrderPaymentData
        if (orderEntity.delivery == null)
            orderPaymentData = OrderPaymentData(
                getString(R.string.cod_text),
                orderEntity.amountTotal,
                "(${orderEntity.items.size} ${getString(R.string.product)})",
                orderEntity.pointsRedeemed,
                "",
                "",
                orderEntity.grandTotal
            )
        else {
            val paymentMethodText = binding.layoutPaymentMethod.tvMethodDescription.text.toString()
            if (!paymentMethodText.equals(
                    getString(R.string.checkout_select_your_payment_method),
                    true
                )
            ) {
                orderPaymentData = OrderPaymentData(
                    paymentMethodText,
                    orderEntity.amountTotal,
                    "(${orderEntity.items.size} ${getString(R.string.product)})",
                    orderEntity.pointsRedeemed,
                    orderEntity.delivery.total.toString(),
                    "",
                    orderEntity.grandTotal
                )
            } else
                orderPaymentData = OrderPaymentData(
                    getString(R.string.cod_text),
                    orderEntity.amountTotal,
                    "(${orderEntity.items.size} ${getString(R.string.product)})",
                    orderEntity.pointsRedeemed,
                    orderEntity.delivery.total.toString(),
                    "",
                    orderEntity.grandTotal
                )
        }
        binding.paymentDetailContainer.data = orderPaymentData
    }

    private fun onShippingMethodSelected() {
        hideError(binding.layoutShippingMethod.clCheckoutOptions, true)
        triggerIntent(CheckoutIntent.GetOrderData(viewModel.cartId, false, true, viewModel.isUserWantToRedeemPoints, viewModel.lineIds))
    }

    private fun onShippingAddressSelected() {
        hideError(binding.layoutAddressInfo.root, false)
        triggerIntent(CheckoutIntent.GetOrderData(viewModel.cartId, false, false, viewModel.isUserWantToRedeemPoints, viewModel.lineIds))
    }

    private fun showProgressDialog(message: String) {

        hideCurrentDialog()

        dialog = AlertDialogHelper.getAlertDialog(
            requireContext(), SweetAlertDialog.PROGRESS_TYPE,
            message, "", false, false
        )

        dialog?.show()
    }

    private fun onPaymentMethodSelected(selectedPaymentMethod: SelectedPaymentMethod?) {
        binding.layoutPaymentMethod.tvMethodDescription.text =
            selectedPaymentMethod?.paymentMethodText
        binding.paymentDetailContainer.tvPaymentMethod.text =
            selectedPaymentMethod?.paymentMethodText
        hideError(binding.layoutPaymentMethod.clCheckoutOptions, true)
    }

    private fun onPlacedCODOrder() {
        hideCurrentDialog()
        triggerIntent(CheckoutIntent.SetIsCustomerWantToRedeemPoints(false))
        (requireActivity() as BaseActivity).replaceFragmentWithoutBackStack(
            R.id.fcv_container,
            PaymentStatusFragmentV1.newInstance(SHOW_PAYMENT_COD_MESSAGE)
        )
    }

    private fun onPlacedVirtualOrder(
        transactionId: String,
        orderId: Int,
        paymentAcquirerProviderId: String
    ) {
        hideCurrentDialog()
        triggerIntent(CheckoutIntent.SetIsCustomerWantToRedeemPoints(false))
        (requireActivity() as BaseActivity).replaceFragmentWithoutBackStack(
            R.id.fcv_container,
            PaymentProcessingFragmentV1.newInstance(
                CreateVirtualAccountPaymentRequest(
                    getString(R.string.currency_idr),
                    paymentAcquirerProviderId,
                    orderId.toString(),
                    transactionId
                ),
                orderId
            )
        )
    }

    private fun showError(view: View, message: String?) {
        view.background =
            ResourcesCompat.getDrawable(resources, R.drawable.rectangle_red_border, null)
        binding.tvErrorMessage.text = message
        binding.tvErrorMessage.visibility = if (message.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    private fun hideError(view: View, errorPresent: Boolean) {

        binding.tvErrorMessage.makeGone()

        if (errorPresent)
            view.background = ResourcesCompat.getDrawable(
                resources,
                R.drawable.shape_rectangular_white_bg_gray_border_1dp,
                null
            )
        else
            view.setBackgroundResource(R.color.white)

        triggerIntent(CheckoutIntent.ValidateCheckOutOrderDetails(viewModel.orderEntity!!,viewModel.selectedPaymentMethod))
    }

    private fun getOrderReviewResponse(orderReviewEntity: OrderReviewEntity) {

        triggerIntent(CheckoutIntent.PlaceOrder(orderReviewEntity.transactionId,
                viewModel.selectedPaymentMethod?.isCOD == true,
                        viewModel.isUserWantToRedeemPoints))
    }

    override fun triggerIntent(intent: CheckoutIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }

    private fun showErrorDialogAndFinish(message: String) {

        binding.root.makeGone()

        hideCurrentDialog()

        dialog = AlertDialogHelper.getAlertDialog(
            requireContext(), SweetAlertDialog.ERROR_TYPE,
            message, "", false, false
        )
        dialog?.confirmText = getString(R.string.ok)
        dialog?.setConfirmClickListener {
            dialog?.hide()
            triggerIntent(CheckoutIntent.SetIsCustomerWantToRedeemPoints(false))
            requireActivity().finish()
        }

        dialog?.show()
    }

    private fun showErrorSnackBar(message: String) {
        hideCurrentDialog()
        showSnackbarMessage(message, SnackbarHelper.SnackbarType.TYPE_WARNING)
    }

    private fun onOrderDetailsReceived(
        orderEntity: OrderEntity,
        refreshRecyclerView: Boolean,
        refreshPaymentDetails: Boolean
    ) {
            binding.tvTotalPrice.text = orderEntity.grandTotal
            updateAddress(orderEntity)
            updateShippingMethod(orderEntity.delivery)
            if (refreshRecyclerView)  updateProductDetail(orderEntity.items)
            if (refreshPaymentDetails) updatePaymentDetail(orderEntity)
            hideCurrentDialog()
            binding.root.makeVisible()
           triggerIntent(CheckoutIntent.ValidateCheckOutOrderDetails(viewModel.orderEntity!!,viewModel.selectedPaymentMethod))

    }


    private fun hideCurrentDialog() {
        dialog?.hide()
    }

    companion object {

        const val ORDER_ID = "ORDER_ID"
        const val CART_ID = "CART_ID"
        const val POINTS_REDEEMED = "POINTS_REDEEMED"
        const val LINE_IDS = "LINE_IDS"
        const val SHIPPING_ADDRESS_RESULT = "SHIPPING_ADDRESS_RESULT"
        const val SHIPPING_METHOD_RESULT = "SHIPPING_METHOD_RESULT"
        const val SHIPPING_METHOD_RESULT_VALUE = "SHIPPING_METHOD_RESULT_VALUE"
        const val PAYMENT_METHOD_RESULT = "PAYMENT_METHOD_RESULT"
        const val SELECTED_PAYMENT_METHOD = "SELECTED_PAYMENT_METHOD"
        const val RESULT = "RESULT"


        @JvmStatic
        fun newInstance(cartId: Int, pointsRedeemed: Boolean, lineIds: ArrayList<Int>) =
            CheckoutDashboardFragmentV1().apply {
                arguments = Bundle().apply {
                    putInt(CART_ID, cartId)
                    putBoolean(POINTS_REDEEMED, pointsRedeemed)
                    putIntegerArrayList(LINE_IDS, lineIds)
                }
            }


    }

    override fun render(effect: CheckoutEffect) {
        when (effect) {
            is CheckoutEffect.ErrorDialog -> showErrorDialogAndFinish(effect.message.toString())
            is CheckoutEffect.ErrorSnackBar -> showErrorSnackBar(effect.message.toString())
            is CheckoutEffect.NavigateToPaymentMethod -> navigateToPaymentMethod()
            is CheckoutEffect.NavigateToShippingAddress -> navigateToShippingAddress(effect.cartId)
            is CheckoutEffect.NavigateToShippingMethod -> navigateToShippingMethod(effect.cartId)
            is CheckoutEffect.OnSuccessfulValidationForPlaceOrder -> onSuccessfulValidationForPlaceOrder()
            is CheckoutEffect.SetInitialLayout -> setInitialLayout()
            is CheckoutEffect.Toast -> showToast(effect.message.toString())
            is CheckoutEffect.ValidateCheckOutOrderDetails ->  onValidatedCheckoutOrderDetails(effect.result)
            is CheckoutEffect.OnValidatePaymentMethodFailure -> onValidatedPaymentMethodFailure()
            is CheckoutEffect.OnValidateShippingAddressFailure -> onValidatedShippingAddressFailure()
            is CheckoutEffect.OnValidateShippingMethodFailure -> onValidatedShippingMethodFailure()
            is CheckoutEffect.WarningDialog -> {
                hideCurrentDialog()
                showErrorDialogState("", effect.message.toString())
            }
        }


    }

}
