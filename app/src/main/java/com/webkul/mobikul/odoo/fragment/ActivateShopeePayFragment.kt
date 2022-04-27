package com.webkul.mobikul.odoo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.databinding.FragmentActivateShopeePayBinding

class ActivateShopeePayFragment : Fragment() {

    val TAG = "ActivateShopeePayFragment"
    private lateinit var binding : FragmentActivateShopeePayBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_activate_shopee_pay, container, false)
        return binding.root
    }


}