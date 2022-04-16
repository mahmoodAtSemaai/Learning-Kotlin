package com.webkul.mobikul.odoo.features.auth.presentation

import android.os.Bundle
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.databinding.FragmentLoginBinding

class LoginFragment : BindingBaseFragment<FragmentLoginBinding>() {

    override val layoutId = R.layout.fragment_login

    companion object {
        fun newInstance() = LoginFragment().also { loginFragment ->
            val bundle = Bundle()
            loginFragment.arguments = bundle
        }
    }
}