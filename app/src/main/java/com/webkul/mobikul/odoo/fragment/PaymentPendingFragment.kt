package com.webkul.mobikul.odoo.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.snackbar.Snackbar
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.FragmentContainerActivity
import com.webkul.mobikul.odoo.connection.ApiConnection
import com.webkul.mobikul.odoo.connection.CustomObserver
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.databinding.FragmentPendingPaymentBinding
import com.webkul.mobikul.odoo.helper.AlertDialogHelper
import com.webkul.mobikul.odoo.helper.CalendarUtil
import com.webkul.mobikul.odoo.helper.SnackbarHelper
import com.webkul.mobikul.odoo.model.checkout.TransferInstruction
import com.webkul.mobikul.odoo.model.payments.PaymentStatusResponse
import com.webkul.mobikul.odoo.model.payments.PaymentTransactionResponse
import com.webkul.mobikul.odoo.model.payments.PendingPaymentData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PaymentPendingFragment : Fragment() {

    private lateinit var binding: FragmentPendingPaymentBinding
    private var TAG = "PendingPaymentFragment"
    private lateinit var pendingPaymentData: PendingPaymentData
    private lateinit var orderId: String
    private lateinit var dialog: SweetAlertDialog

    companion object {
        private val PENDING = 1
        private val COMPLETED = 2
        private val FAILED = 3
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_pending_payment, container, false)
        (requireActivity() as FragmentContainerActivity).setToolbarText(getString(R.string.order_status))
        getArgs()
        buildDialog()
        setClickListeners()
        return binding.root
    }

    private fun buildDialog() {
        dialog = AlertDialogHelper.getAlertDialog(
            requireContext(), SweetAlertDialog.PROGRESS_TYPE,
            getString(R.string.checking_payment_status), "", false, false
        )
    }

    private fun getArgs() {
        if (arguments?.containsKey(BundleConstant.BUNDLE_PAYMENT_PENDING_SCREEN) == true) {
            pendingPaymentData =
                arguments?.getParcelable(BundleConstant.BUNDLE_PAYMENT_PENDING_SCREEN)!!
            binding.data = pendingPaymentData
            orderId = pendingPaymentData.orderId.toString()
        }
    }

    private fun setClickListeners() {
        binding.tvAccountNumber.setOnClickListener {
            copyTextToClipboard()
        }

        binding.btnOrderDetails.setOnClickListener {
            startActivity(
                Intent(requireActivity(), FragmentContainerActivity::class.java)
                    .putExtra(
                        BundleConstant.BUNDLE_KEY_FRAGMENT_TYPE,
                        BundleConstant.BUNDLE_ORDER_DETAIL_SCREEN
                    )
                    .putExtra(BundleConstant.BUNDLE_ORDER_DETAIL_SCREEN, orderId)
            )
        }

        binding.btnPaymentStatus.setOnClickListener {
            callAPIToCheckStatus()
        }
        binding.tvTransferInstruction.setOnClickListener {
            startActivity(
                Intent(requireActivity(), FragmentContainerActivity::class.java)
                    .putExtra(
                        BundleConstant.BUNDLE_KEY_FRAGMENT_TYPE,
                        BundleConstant.BUNDLE_KEY_TRANSFER_INSTRUCTION
                    )
                    .putExtra(
                        BundleConstant.BUNDLE_KEY_TRANSFER_INSTRUCTION,
                        TransferInstruction(
                            pendingPaymentData.bank.name,
                            "",
                            pendingPaymentData.bank.providerId
                        )
                    )
            )
        }
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

    private fun callAPIToCheckStatus() {
        ApiConnection.getPaymentTransactionStatus(requireContext(), orderId.toInt())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<PaymentStatusResponse?>(requireContext()) {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    dialog.show()
                }

                override fun onNext(paymentStatusResponse: PaymentStatusResponse) {
                    super.onNext(paymentStatusResponse)
                    redirectToPaymentStatusScreen(paymentStatusResponse)
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                    dialog.hide()
                    SnackbarHelper.getSnackbar(
                        requireActivity(), getString(R.string.error_something_went_wrong),
                        Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING
                    )
                }

                override fun onComplete() {
                    super.onComplete()
                    dialog.hide()
                }
            })
    }

    private fun redirectToPaymentStatusScreen(paymentStatusResponse: PaymentStatusResponse) {
        val paymentStatus = paymentStatusResponse.result.paymentStatusCode
        if (paymentStatus == COMPLETED) {
            startActivity(
                Intent(requireActivity(), FragmentContainerActivity::class.java)
                    .putExtra(
                        BundleConstant.BUNDLE_KEY_FRAGMENT_TYPE,
                        BundleConstant.BUNDLE_PAYMENT_STATUS_SCREEN
                    )
                    .putExtra(
                        BundleConstant.BUNDLE_PAYMENT_STATUS_SCREEN,
                        PaymentStatusFragment.SHOW_PAYMENT_COMPLETE_MESSAGE
                    )
            )
        } else if (paymentStatus == FAILED)
            startActivity(
                Intent(requireActivity(), FragmentContainerActivity::class.java)
                    .putExtra(
                        BundleConstant.BUNDLE_KEY_FRAGMENT_TYPE,
                        BundleConstant.BUNDLE_PAYMENT_STATUS_SCREEN
                    )
                    .putExtra(
                        BundleConstant.BUNDLE_PAYMENT_STATUS_SCREEN,
                        PaymentStatusFragment.SHOW_PAYMENT_FAILURE_MESSAGE
                    )
            )
    }


}