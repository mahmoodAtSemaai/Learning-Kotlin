package com.webkul.mobikul.odoo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.databinding.FragmentOTPVerificationBinding
import com.webkul.mobikul.odoo.helper.GenericTextWatcher

class OTPVerificationFragment : Fragment() {

    val TAG = "OTPVerificationFragment"
    private lateinit var binding: FragmentOTPVerificationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_o_t_p_verification,
            container,
            false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.apply {
            etOtp1.addTextChangedListener(GenericTextWatcher(etOtp2, etOtp1))
            etOtp2.addTextChangedListener(GenericTextWatcher(etOtp3, etOtp1))
            etOtp3.addTextChangedListener(GenericTextWatcher(etOtp4, etOtp2))
            etOtp4.addTextChangedListener(GenericTextWatcher(etOtp5, etOtp3))
            etOtp5.addTextChangedListener(GenericTextWatcher(etOtp6, etOtp4))
            etOtp6.addTextChangedListener(GenericTextWatcher(etOtp6, etOtp5))

            btnVerifyOtp.setOnClickListener {
                val OTP =
                    etOtp1.text.toString() + etOtp2.text.toString() + etOtp3.text.toString() + etOtp4.text.toString() + etOtp5.text.toString() + etOtp6.text.toString()
                if (OTP.length < 6) {
/*
                    showError
*/
                } else {
/*
                    showSuccess/Processing
*/
                }
            }
        }

    }

    fun showShortToast(string: String) {
        Toast.makeText(requireContext(), string, Toast.LENGTH_SHORT).show()
    }

}