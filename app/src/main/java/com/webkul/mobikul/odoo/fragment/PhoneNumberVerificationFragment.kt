package com.webkul.mobikul.odoo.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.databinding.FragmentPhoneNumberVerificationBinding


class PhoneNumberVerificationFragment : Fragment() {

    private val TAG = "PhoneNumberVerificationFragment"
    private lateinit var mBinding: FragmentPhoneNumberVerificationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_phone_number_verification,
            container,
            false
        )
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var count = 0
        mBinding.apply {
            etNumber.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    btnSendOtp.isEnabled = !s.isNullOrEmpty()
                    etNumber.isCursorVisible = !s.isNullOrEmpty()
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

}