package com.webkul.mobikul.odoo.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.FragmentContainerActivity
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.databinding.FragmentPaymentVirtualAccountBinding
import com.webkul.mobikul.odoo.helper.IntentHelper
import com.webkul.mobikul.odoo.model.checkout.TransferInstruction
import com.webkul.mobikul.odoo.model.payments.Result


class PaymentVirtualAccountFragment : Fragment() {

    lateinit var binding: FragmentPaymentVirtualAccountBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_payment_virtual_account,
            container,
            false
        )
        getArgs()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setListeners()
    }

    private fun getArgs() {
        if (arguments?.containsKey(BundleConstant.BUNDLE_PAYMENT_PENDING_SCREEN_VA) == true) {
            val result = arguments?.get(BundleConstant.BUNDLE_PAYMENT_PENDING_SCREEN_VA) as Result
            val orderAmount = arguments?.getString(BundleConstant.BUNDLE_KEY_ORDER_AMOUNT)
            result.meta.paidAmount = orderAmount
            binding.data = result
            binding.bankingInstruction.data =
                TransferInstruction(result.bank.name, result.bank.instructions)
        }
        (requireActivity() as FragmentContainerActivity).setToolbarText(getString(R.string.payment_n_status))
    }

    private fun setListeners() {
        binding.btnPaymentStatus.setOnClickListener {
            (requireActivity().finish())
        }

        binding.btnOrderDetails.setOnClickListener {
            IntentHelper.continueShopping(requireContext())
        }

        binding.tvAccountNumber.setOnClickListener {
            copyTextToClipBoard(binding.tvAccountNumber.text.toString())
        }
    }

    private fun copyTextToClipBoard(textTobeCopied: String) {
        val clipboard =
            ContextCompat.getSystemService(requireContext(), ClipboardManager::class.java)
        val clip = ClipData.newPlainText(getString(R.string.label), textTobeCopied)
        clipboard!!.setPrimaryClip(clip)
        Toast.makeText(requireContext(), R.string.text_copied_message, Toast.LENGTH_SHORT).show()
    }


}