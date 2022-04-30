package com.webkul.mobikul.odoo.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.FragmentContainerActivity
import com.webkul.mobikul.odoo.connection.ApiConnection
import com.webkul.mobikul.odoo.connection.CustomObserver
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.constant.Status
import com.webkul.mobikul.odoo.databinding.FragmentPaymentProcessingBinding
import com.webkul.mobikul.odoo.helper.AlertDialogHelper
import com.webkul.mobikul.odoo.model.payments.CreateVirtualAccountPaymentRequest
import com.webkul.mobikul.odoo.model.payments.PaymentTransactionResponse
import com.webkul.mobikul.odoo.model.payments.PendingPaymentData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PaymentProcessingFragment : Fragment() {

    private lateinit var binding: FragmentPaymentProcessingBinding
    private lateinit var dialog: SweetAlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_payment_processing,
            container,
            false
        )
        (requireActivity() as FragmentContainerActivity).setToolbarText(getString(R.string.virtual_account))
        hideBackButton()
        getArgs()
        return binding.root
    }

    private fun hideBackButton() {
        (requireActivity() as FragmentContainerActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (requireActivity() as FragmentContainerActivity).supportActionBar?.setHomeButtonEnabled(false)
    }

    private fun getArgs() {
        val createVAPaymentRequest =
            arguments?.get(BundleConstant.BUNDLE_ORDER_COST_DATA) as CreateVirtualAccountPaymentRequest
        val orderId = arguments?.getString(BundleConstant.BUNDLE_KEY_ORDER_ID)
        if (arguments?.containsKey(BundleConstant.BUNDLE_ORDER_COST_DATA) == true) {
            orderId?.let {
                callApi(createVAPaymentRequest, it)
            }
        } else {
            (requireActivity()).finish()
        }
    }


    private fun callApi(
        createVirtualAccountPaymentRequest: CreateVirtualAccountPaymentRequest,
        orderId: String
    ) {

        ApiConnection.createPayments(
            requireContext(),
            createVirtualAccountPaymentRequest.toString()
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<PaymentTransactionResponse?>(requireContext()) {
                override fun onNext(paymentTransactionResponse: PaymentTransactionResponse) {
                    super.onNext(paymentTransactionResponse)
                    handlePaymentTransactionResponse(paymentTransactionResponse, orderId)
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                    showErrorDialog(
                        getString(R.string.error_something_went_wrong),
                        getString(R.string.error_request_failed)
                    )
                }
            })
    }

    private fun showErrorDialog(title: String?, contentText: String) {
        dialog = AlertDialogHelper.getAlertDialog(
            requireContext(), SweetAlertDialog.ERROR_TYPE, title,
            contentText, false, false
        )
        dialog.setConfirmText(getString(R.string.ok))
            .setConfirmClickListener {
                dialog.hide()
                requireActivity().finish()
            }
        dialog.show()
    }

    private fun handlePaymentTransactionResponse(
        paymentTransactionResponse: PaymentTransactionResponse,
        orderId: String
    ) {
        if (paymentTransactionResponse.status.equals(getString(R.string.fail_2), true)) {
            showErrorDialog(
                getString(R.string.error_something_went_wrong),
                getString(R.string.error_order_place)
            )
        } else if (paymentTransactionResponse.statusCode == Status.SERVER_ERROR) {
            showErrorDialog(
                getString(R.string.error_something_went_wrong),
                getString(R.string.error_order_place)
            )
        } else {
            val result = paymentTransactionResponse.result
            val pendingPaymentData: PendingPaymentData
            result.apply {
                pendingPaymentData = PendingPaymentData(
                    expireDate, expireTime, paymentAmount,
                    bank, orderId.toInt(), true
                )
            }
            requireActivity().startActivity(
                Intent(requireActivity(), FragmentContainerActivity::class.java)
                    .putExtra(
                        BundleConstant.BUNDLE_KEY_FRAGMENT_TYPE,
                        BundleConstant.BUNDLE_PAYMENT_PENDING_SCREEN
                    )
                    .putExtra(BundleConstant.BUNDLE_PAYMENT_PENDING_SCREEN, pendingPaymentData)
            )
            requireActivity().finish()
        }

    }


}