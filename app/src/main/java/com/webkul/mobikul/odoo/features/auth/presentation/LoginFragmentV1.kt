package com.webkul.mobikul.odoo.features.auth.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.webkul.mobikul.odoo.BuildConfig
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.databinding.FragmentLoginV1Binding
import com.webkul.mobikul.odoo.helper.AlertDialogHelper
import dagger.hilt.android.AndroidEntryPoint
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
            viewModel.logInResponse.collect {
                when (it) {
                    is Resource.Success -> {

                    }
                    is Resource.Failure -> {
                        val error = it
                        when (it.failureStatus) {

                            FailureStatus.API_FAIL -> {
                                var message = error.message
                                if (message == null) message = "Could not connect to server"
                                AlertDialogHelper.showDefaultWarningDialog(
                                    context,
                                    getString(R.string.error_login_failure),
                                    message
                                )
                            }

                            FailureStatus.NO_INTERNET -> {
                                var message = error.message
                                if (message == null) message =
                                    "Please check your internet connection"

                                AlertDialogHelper.showDefaultWarningDialog(
                                    context,
                                    getString(R.string.error_login_failure),
                                    message
                                )

                            }

                            FailureStatus.OTHER -> {
                                var message = error.message
                                if (message == null) message = "Something went wrong"
                                AlertDialogHelper.showDefaultWarningDialog(
                                    context,
                                    getString(R.string.error_login_failure),
                                    message
                                )

                            }

                        }
                    }
                    is Resource.Loading -> {
                        AlertDialogHelper.showDefaultProgressDialog(
                            context
                        )
                    }
                    else -> {}
                }
            }
        }
    }

    private fun setOnClickListeners() {

        binding.login.setOnClickListener {
            onLoginBtnClicked()
        }

        binding.privacy.setOnClickListener {
            onPrivacyPolicyClicked()
        }

        binding.signUpNow.setOnClickListener {
            onSignUpNowClicked()
        }

        binding.forgotPassword.setOnClickListener {
            onForgotPasswordClicked()
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
            Toast.makeText(requireContext(), "Login", Toast.LENGTH_SHORT).show()
//           viewModel.onLogInClicked(username, password)
    }

    private fun onPrivacyPolicyClicked() {
        TODO("Not yet implemented")
    }

    private fun onForgotPasswordClicked() {
        /*  val fragmentManager = requireActivity().supportFragmentManager
            val forgotPasswordDialogFragment = ForgotPasswordDialogFragment.newInstance(binding.usernameEt.text.toString())
            forgotPasswordDialogFragment.show(fragmentManager, ForgotPasswordDialogFragment::class.java.simpleName)
        */
    }

    private fun onSignUpNowClicked() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container_v1,
                SignUpFragmentV1.newInstance(),
                SignUpFragmentV1::class.java.simpleName
            ).commit()
    }

}