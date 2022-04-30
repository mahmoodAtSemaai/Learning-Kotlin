package com.webkul.mobikul.odoo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.SimpleItemAnimator
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.adapter.checkout.CouponsAdapter
import com.webkul.mobikul.odoo.databinding.FragmentCouponsListBinding

class CouponsListfragment : Fragment() {

    lateinit var binding: FragmentCouponsListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_coupons_list, container, false)

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.apply {
            rvCoupons.adapter =
                CouponsAdapter(
                    requireContext()
                )
            (rvCoupons.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }

}