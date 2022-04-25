package com.webkul.mobikul.odoo.features.auth.presentation

import android.os.Bundle
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.platform.BindingBaseActivity
import com.webkul.mobikul.odoo.databinding.ActivitySignInSignUpV1Binding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInSignUpActivityV1 @Inject constructor(): BindingBaseActivity<ActivitySignInSignUpV1Binding>() {

    override val contentViewId: Int
        get() = R.layout.activity_sign_in_sign_up_v1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setOnClickListeners()

    }

    private fun setOnClickListeners() {

        binding.loginButton.setOnClickListener { openLogin() }

        binding.signUpButton.setOnClickListener { openSignUp() }

        binding.fingerprintButton.setOnClickListener { }
    }


    private fun openLogin() {
        supportFragmentManager.beginTransaction()
            .add(
                R.id.fragment_container_v1,
                LoginFragmentV1.newInstance(),
                LoginFragmentV1::class.java.simpleName
            ).commit()
    }

    private fun openSignUp() {
        supportFragmentManager.beginTransaction()
            .add(
                R.id.fragment_container_v1,
                SignUpFragmentV1.newInstance(),
                SignUpFragmentV1::class.java.simpleName
            ).commit()
    }


}