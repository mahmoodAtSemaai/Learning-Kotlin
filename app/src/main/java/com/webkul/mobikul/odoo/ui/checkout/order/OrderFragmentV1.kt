package com.webkul.mobikul.odoo.ui.checkout.order

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.extension.close
import com.webkul.mobikul.odoo.core.extension.makeGone
import com.webkul.mobikul.odoo.core.extension.makeVisible
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BaseActivity
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.data.entity.OrderEntity
import com.webkul.mobikul.odoo.data.entity.PaymentStatusEntity
import com.webkul.mobikul.odoo.data.entity.PendingPaymentEntity
import com.webkul.mobikul.odoo.databinding.FragmentOrderFragmentv1Binding
import com.webkul.mobikul.odoo.helper.AlertDialogHelper
import com.webkul.mobikul.odoo.helper.ImageHelper
import com.webkul.mobikul.odoo.model.payments.OrderPaymentData
import com.webkul.mobikul.odoo.ui.checkout.paymentpending.PaymentPendingFragmentV1
import com.webkul.mobikul.odoo.ui.checkout.paymentstatus.PaymentStatusFragmentV1
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OrderFragmentV1 @Inject constructor() : BindingBaseFragment<FragmentOrderFragmentv1Binding>(),
    IView<OrderIntent, OrderState, OrderEffect> {

    override val layoutId = R.layout.fragment_order_fragmentv1
    private val viewModel: OrderViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setOnClickListeners()
        triggerIntent(OrderIntent.SetBundleDataAndInitialLayout(arguments))
    }

    override fun onResume() {
        super.onResume()
        triggerIntent(OrderIntent.FetchOrderDetails(viewModel.orderId, false))
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
        binding.tbOrder.toolbar.setNavigationOnClickListener {
            this.close()
        }

        binding.btnPaymentStatus.setOnClickListener {
            triggerIntent(OrderIntent.FetchTransactionStatus(viewModel.orderId))
        }

        binding.tvAccountNumber.setOnClickListener{
            triggerIntent(OrderIntent.CopyToClipBoard)
        }
    }

    private fun setToolbar() {
        binding.tbOrder.tvToolBar.text = getString(R.string.mobikul)
    }

    override fun triggerIntent(intent: OrderIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }

    override fun render(state: OrderState) {
        when(state) {
            is OrderState.Error -> {
                dismissProgressDialog()
                showErrorState(state.failureStatus, state.message.toString())
            }
            is OrderState.Idle -> {}
            is OrderState.OnCancelledTransactionStatus -> onCancelledTransactionStatus()
            is OrderState.OnCompletedTransactionStatus -> onCompletedTransactionStatus()
            is OrderState.OnOrderDetailsReceived -> onOrderDetailsReceived(state.orderEntity)
            is OrderState.OnPendingTransactionStatus -> onPendingTransactionStatus(state.paymentStatusEntity, state.orderId)
            is OrderState.SetInitialLayout -> setInitialLayout()
            is OrderState.ShowErrorDialog -> showErrorDialog(state.title.toString(), state.message.toString())
            is OrderState.ShowProgressDialog -> {
                updateProgressDialog(state.message.toString())
                showProgressDialog()
            }
            is OrderState.ShowWarningDialog -> showWarningDialog(state.message)
        }
    }

    override fun render(effect: OrderEffect) {
        when(effect) {
            OrderEffect.CopyToClipBoard -> copyToClipBoard()
        }
    }

    private fun copyToClipBoard() {
        val clipboard = ContextCompat.getSystemService(
            requireContext(),
            ClipboardManager::class.java
        )
        val clip = ClipData.newPlainText(getString(R.string.label), binding.tvAccountNumber.text.toString())
        clipboard?.setPrimaryClip(clip)
        showToast(getString(R.string.text_copied_message))
    }

    private fun setInitialLayout() {
        setToolbar()
    }


    private fun showWarningDialog(message: String?) {
            dismissProgressDialog()

        AlertDialogHelper.getAlertDialog(
            requireContext(),
            SweetAlertDialog.WARNING_TYPE,
            message,
            "",
            false,
            true
        ).also { dialog ->
            dialog.setConfirmText(getString(R.string.ok))
                .setConfirmClickListener {
                    dialog.hide()
                }
            dialog.show()
        }
    }


    private fun onPendingTransactionStatus(paymentStatusEntity: PaymentStatusEntity, orderId: Int) {
            dismissProgressDialog()
            val pendingData = PendingPaymentEntity(
                expireDate = paymentStatusEntity.paymentStatusResult.expireDate.toString(),
                expireTime = paymentStatusEntity.paymentStatusResult.expireTime.toString(),
                paymentAmount = binding.paymentDetails.tvTotalPayment.text.toString(),
                bank = paymentStatusEntity.paymentStatusResult.bank,
                orderId =  orderId,
                isCheckout = false
            )

        (requireActivity() as BaseActivity).addFragmentWithBackStack(
            R.id.fcv_container,
            PaymentPendingFragmentV1.newInstance(pendingData)
        ,null)

    }

    private fun onCompletedTransactionStatus() {
        dismissProgressDialog()

        (requireActivity() as BaseActivity).addFragmentWithBackStack(
            R.id.fcv_container,
            PaymentStatusFragmentV1.newInstance(PaymentStatusFragmentV1.SHOW_PAYMENT_COMPLETE_MESSAGE),
            null
        )
    }

    private fun onCancelledTransactionStatus() {
        dismissProgressDialog()

        (requireActivity() as BaseActivity).addFragmentWithBackStack(
            R.id.fcv_container,
            PaymentStatusFragmentV1.newInstance(PaymentStatusFragmentV1.SHOW_PAYMENT_FAILURE_MESSAGE),
            null
        )
    }

    private fun showErrorDialog(title: String, message: String) {
        dismissProgressDialog()

        AlertDialogHelper.getAlertDialog(
            requireContext(), SweetAlertDialog.ERROR_TYPE,
            title, message, false, false
        ).also { dialog ->
            dialog?.confirmText = getString(R.string.ok)
            dialog?.setConfirmClickListener {
                dialog.hide()
                this.close()
            }
            dialog?.show()
        }

        binding.root.visibility = View.GONE
    }


    private fun onOrderDetailsReceived(orderEntity: OrderEntity) {
        dismissProgressDialog()
        setTextOnViews(orderEntity)
        binding.orderDetailsContainer.makeVisible()
        binding.executePendingBindings()
        setupRecyclerView(orderEntity);
        setViewGroupVisibility(orderEntity)
    }

    //TODO: CAN BE OPTIMIZED FURTHER
    private fun setTextOnViews(orderEntity: OrderEntity) {

        val codText = getString(R.string.cod_text)
        val paymentMode = if (orderEntity.paymentMode == null) ""
        else {
            if (codText.equals(orderEntity.paymentMode, ignoreCase = true))
                orderEntity.paymentMode
            else orderEntity.bank.name + " " + orderEntity.paymentMode
        }
        val orderPaymentData = OrderPaymentData(
            paymentMode,
            orderEntity.amountTotal,
            "(" + orderEntity.items.size + " " + getString(R.string.product) + ")",
            orderEntity.pointsRedeemed,
            orderEntity.delivery?.total.toString(),
            "",
            orderEntity.grandTotal
        )
        binding.paymentDetails.data = orderPaymentData
        binding.tvOrderName.text = getString(R.string.order_noX, orderEntity.name)
        binding.tvPurchaseDate.text = orderEntity.createDate
        binding.tvDueDateDay.text = orderEntity.getPaymentExpiryDate()
        binding.tvRemainingTime.text =
            if (orderEntity.paymentStatus.equals(getString(R.string.failed), true)
                || orderEntity.paymentStatus.equals(getString(R.string.time_expired), false))
            getString(R.string.time_expired)
            else orderEntity.expireTime
        binding.tvShippingMethodName.text = orderEntity.delivery?.name + orderEntity.delivery?.total
        binding.tvCustomerName.text = orderEntity.shippingAddressId.name
        binding.tvPhoneNumber.text = "(" + orderEntity.shippingAddressId.phone + ")"
        if (orderEntity.shippingAddressId.phone.isNullOrEmpty())  binding.tvPhoneNumber.makeGone() else binding.tvPhoneNumber.makeVisible()
        binding.tvAddressDescription.text = orderEntity.shippingAddressId.getCompleteAddressDescription()
        ImageHelper.load(binding.ivBankIcon, orderEntity.bank.thumbnail, null, DiskCacheStrategy.NONE, true, ImageHelper.ImageType.PRODUCT_TINY)
        binding.tvMethodName.text = orderEntity.bank.name +  "(" + orderEntity.bank.type +  ")"
        binding.tvAccountNumber.text = orderEntity.bank.accountNumb
        binding.tvStatus.text = orderEntity.paymentStatus
        binding.pbLoader.makeGone()
    }

    private fun setupRecyclerView(orderEntity: OrderEntity) {
        val dividerItemDecorationHorizontal =
            DividerItemDecoration(requireContext(), LinearLayout.VERTICAL)
        binding.rvProductsInfo.addItemDecoration(dividerItemDecorationHorizontal)
        binding.rvProductsInfo.adapter =
            OrderItemDetailAdapterV1(requireContext(), orderEntity.items)
    }

    private fun setViewGroupVisibility(orderEntity: OrderEntity) {
        if (orderEntity.paymentMode.equals(getString(R.string.cod_text), ignoreCase = true)) {
           setViewGroupVisibilityForCOD()
        } else {
            setViewGroupVisibilityForVirtualPayment(orderEntity)
        }
    }

    private fun setViewGroupVisibilityForCOD() {
        binding.grDueDateDetail.makeGone()
        binding.grBankPaymentMethodDetail.makeGone()
    }

    //TODO: CAN BE OPTIMIZED FURTHER
    private fun setViewGroupVisibilityForVirtualPayment(orderEntity: OrderEntity) {
        val isPaymentDone =
            orderEntity.paymentStatus.equals(getString(R.string.done), ignoreCase = true)
                    || orderEntity.paymentStatus.equals(getString(R.string.completed), ignoreCase = true)
                    || orderEntity.paymentStatus.equals(getString(R.string.paid_2), ignoreCase = true)
        if (isPaymentDone) {
            binding.grDueDateDetail.makeGone()
            binding.tvStatus.text = getString(R.string.completed)
            binding.ivBankIcon.makeGone()
            binding.grPurchaseDateDetail.makeVisible()

        } else if (orderEntity.paymentStatus.equals(getString(R.string.time_expired_2), ignoreCase = true)
            || orderEntity.paymentStatus.equals(getString(R.string.failed_2), ignoreCase = true)
            || orderEntity.paymentStatus.equals(getString(R.string.error), ignoreCase = true)
            || orderEntity.status.equals(getString(R.string.cancelled), ignoreCase = true)
        ) {
            binding.grDueDateDetail.makeGone()
            binding.tvStatus.text =
                if (orderEntity.status.equals(getString(R.string.cancelled), ignoreCase = true))
                    getString(R.string.cancelled)
                else getString(R.string.time_expired)
            binding.grBankPaymentMethodDetail.makeGone()
            binding.grPurchaseDateDetail.makeVisible()

        } else if (orderEntity.mobileOrderStatus.equals(getString(R.string.backend_cancelled_state), ignoreCase = true)
        ) {
            binding.grDueDateDetail.makeGone()
            binding.tvStatus.text =  getString(R.string.pending)
            binding.grBankPaymentMethodDetail.makeGone()
            binding.grPurchaseDateDetail.makeVisible()
        } else {
            binding.grPurchaseDateDetail.makeGone()
            binding.tvStatus.text =  getString(R.string.pending)
        }
    }

    companion object {

        const val ORDER_ID = "ORDER_ID"
        const val PENDING = 1
        const val COMPLETED = 2
        const val FAILED = 3

        @JvmStatic
        fun newInstance(orderId: Int) =
            OrderFragmentV1().apply {
                arguments = Bundle().apply {
                    putInt(ORDER_ID, orderId)
                }
            }
    }

}