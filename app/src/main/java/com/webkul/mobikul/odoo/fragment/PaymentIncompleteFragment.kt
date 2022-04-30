package com.webkul.mobikul.odoo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.databinding.FragmentPaymentIncompleteBinding

class PaymentIncompleteFragment : Fragment() {

    private val TAG = "PaymentIncompleteFragment"
    private var binding: FragmentPaymentIncompleteBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_payment_incomplete,
            container,
            false
        )
        return binding?.root
    }


}