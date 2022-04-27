package com.webkul.mobikul.odoo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.databinding.FragmentEwalletInfoBinding

class EWalletInfoFragment : Fragment() {

    private val TAG = "EWalletInfoFragment"
    private lateinit var binding: FragmentEwalletInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_ewallet_info, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Glide.with(binding.ivEwalletLogo)
            .load(R.drawable.ic_shopeepay_logo)
            .into(binding.ivEwalletLogo)

        binding.tvEwalletHeading.text = getString(R.string.what_is_shoppe_pay)
        binding.tvEwalletDesc.text = getString(R.string.shopeepay_description_text)

    }


}