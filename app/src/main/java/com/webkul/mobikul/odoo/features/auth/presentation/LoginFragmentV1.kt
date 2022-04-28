package com.webkul.mobikul.odoo.features.auth.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.webkul.mobikul.odoo.BuildConfig
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.databinding.FragmentLoginV1Binding
import com.webkul.mobikul.odoo.helper.AlertDialogHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragmentV1 @Inject constructor() : BindingBaseFragment<FragmentLoginV1Binding>() {

    override val layoutId = R.layout.fragment_login_v1
    private val viewModel: LoginViewModel by viewModels()

    companion object {
        fun newInstance() = LoginFragmentV1().also { loginFragment ->
            val bundle = Bundle()
            loginFragment.arguments = bundle
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()
        setOnClickListeners()
    }

    private fun setObservers() {

        lifecycleScope.launchWhenCreated {
            viewModel.state.collect {
                when (it) {
                    is LoginState.Loading -> {
                        AlertDialogHelper.showDefaultProgressDialog(context)
                    }
                    is LoginState.Login -> {
                        val loginResponse = it.data
                        startActivity(Intent())
                    }
                    is LoginState.Error -> {
                        val error = it.error

                    }
                }
            }
        }
    }

    private fun setOnClickListeners() {

        binding.login.setOnClickListener {
            onLoginBtnClicked()
        }
    }

    private fun onLoginBtnClicked() {

        val username = binding.usernameEt.text.toString()
        val password = binding.passwordEt.text.toString()

        if (username.isEmpty()) binding.usernameEt.error =
            String.format(
                "%s %s",
                getString(R.string.phone_number_or_username),
                getString(R.string.error_is_required)
            )

        if (password.isEmpty()) binding.passwordEt.error =
            getString(R.string.password) + " " + getString(R.string.error_is_required)

        if (password.length < BuildConfig.MIN_PASSWORD_LENGTH)
            binding.passwordEt.error = String.format(
                "%s %s",
                getString(R.string.password),
                String.format(
                    Locale.getDefault(),
                    getString(R.string.error_password_length_x),
                    BuildConfig.MIN_PASSWORD_LENGTH
                )
            )

        if (username.isNotEmpty() && password.isNotEmpty() && password.length > BuildConfig.MIN_PASSWORD_LENGTH)
            lifecycleScope.launch {
                viewModel.loginIntent.send(LoginIntent.Login(username, password))
            }
    }

}