package com.webkul.mobikul.odoo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.FragmentContainerActivity
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.databinding.FragmentTransferInstructionBinding
import com.webkul.mobikul.odoo.model.checkout.TransferInstruction

class TransferInstructionFragment : Fragment() {

    private lateinit var binding: FragmentTransferInstructionBinding

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
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as FragmentContainerActivity).setToolbarText(getString(R.string.how_to_transfer))
        getArgument()
    }

    private fun getArgument() {
        if (arguments?.containsKey(BundleConstant.BUNDLE_KEY_TRANSFER_INSTRUCTION) == true) {
            val transferInstruction: TransferInstruction =
                arguments?.getParcelable(BundleConstant.BUNDLE_KEY_TRANSFER_INSTRUCTION)!!
            binding.data = transferInstruction
        }
    }

}