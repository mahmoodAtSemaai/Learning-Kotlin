package com.webkul.mobikul.odoo.features.auth.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.databinding.FragmentSignUpBindingImpl
import com.webkul.mobikul.odoo.databinding.FragmentSignUpV1Binding
import com.webkul.mobikul.odoo.generated.callback.OnCheckedChangeListener

class SignUpFragmentV1 : BindingBaseFragment<FragmentSignUpV1Binding>() {

    override val layoutId: Int
        get() = R.layout.fragment_sign_up_v1

    companion object {
        fun newInstance() = SignUpFragmentV1().also { signUpFragment ->
            val bundle = Bundle()
            signUpFragment.arguments = bundle
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()

    }

    private fun setOnClickListeners(){
        binding.isSellerCb.setOnCheckedChangeListener{ view ,isChecked ->
            if (isChecked) Toast.makeText(requireContext(),"checked",Toast.LENGTH_SHORT).show()
            else Toast.makeText(requireContext(),"not checked",Toast.LENGTH_SHORT).show()
        }
    }


}