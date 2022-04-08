package com.webkul.mobikul.odoo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.snackbar.Snackbar
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.FragmentContainerActivity
import com.webkul.mobikul.odoo.adapter.checkout.TransferInstructionAdapter
import com.webkul.mobikul.odoo.connection.ApiConnection
import com.webkul.mobikul.odoo.connection.CustomObserver
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.databinding.FragmentTransferInstructionBinding
import com.webkul.mobikul.odoo.helper.AlertDialogHelper
import com.webkul.mobikul.odoo.helper.SnackbarHelper
import com.webkul.mobikul.odoo.model.checkout.TransferInstruction
import com.webkul.mobikul.odoo.model.payments.TransferInstructionResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class TransferInstructionFragment : Fragment() {

    private lateinit var binding: FragmentTransferInstructionBinding
    private lateinit var dialog: SweetAlertDialog
    private lateinit var transferInstruction: TransferInstruction

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_transfer_instruction,
            container,
            false
        )
        dialog = AlertDialogHelper.getAlertDialog(
            requireContext(),
            SweetAlertDialog.PROGRESS_TYPE,
            getString(R.string.please_wait_a_moment),
            "",
            false,
            false
        )
        (requireActivity() as FragmentContainerActivity).setToolbarText(getString(R.string.how_to_transfer))
        getArgument()
        return binding.root
    }

    private fun getArgument() {
        if (arguments?.containsKey(BundleConstant.BUNDLE_KEY_TRANSFER_INSTRUCTION) == true) {
            val transferInstruction: TransferInstruction =
                arguments?.getParcelable(BundleConstant.BUNDLE_KEY_TRANSFER_INSTRUCTION)!!
            getTransferInstruction(transferInstruction.providerId)
        }
    }

    private fun getTransferInstruction(bankId: Int) {
        ApiConnection.getTransferInstruction(requireContext(), bankId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<TransferInstructionResponse?>(requireContext()) {
                override fun onSubscribe(d: Disposable) {
                    dialog.show()
                }

                override fun onNext(transferInstructionResponse: TransferInstructionResponse) {
                    super.onNext(transferInstructionResponse)
                    dialog.hide()
                    handleResponse(transferInstructionResponse)
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                    dialog.hide()
                    SnackbarHelper.getSnackbar(
                        requireActivity(), getString(R.string.try_again_later_text),
                        Snackbar.LENGTH_LONG, SnackbarHelper.SnackbarType.TYPE_WARNING
                    ).show()
                }
            })
    }

    private fun handleResponse(transferInstructionResponse: TransferInstructionResponse) {
        if (!transferInstructionResponse.status.equals(getString(R.string.fail_2), true)) {
            val transferInstructionAdapter =
                TransferInstructionAdapter(requireContext(), transferInstructionResponse.list)
            binding.rvTransferInstruction.adapter = transferInstructionAdapter
        } else {
            SnackbarHelper.getSnackbar(
                requireActivity(), getString(R.string.try_again_later_text),
                Snackbar.LENGTH_LONG, SnackbarHelper.SnackbarType.TYPE_WARNING
            ).show()
        }
    }
}