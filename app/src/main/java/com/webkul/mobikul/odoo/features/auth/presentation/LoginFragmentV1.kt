package com.webkul.mobikul.odoo.features.auth.presentation

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.snackbar.Snackbar
import com.webkul.mobikul.odoo.BuildConfig
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.extension.getDefaultProgressDialog
import com.webkul.mobikul.odoo.core.extension.showDefaultWarningDialog
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.databinding.FragmentLoginV1Binding
import com.webkul.mobikul.odoo.dialog_frag.ForgotPasswordDialogFragment
import com.webkul.mobikul.odoo.features.auth.domain.enums.LoginFieldsValidation
import com.webkul.mobikul.odoo.helper.ApiRequestHelper
import com.webkul.mobikul.odoo.helper.FingerPrintLoginHelper
import com.webkul.mobikul.odoo.helper.SnackbarHelper
import com.webkul.mobikul.odoo.model.customer.signin.LoginRequestData
import com.webkul.mobikul.odoo.model.customer.signin.LoginResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragmentV1 @Inject constructor() : BindingBaseFragment<FragmentLoginV1Binding>(),
    IView<LoginIntent, LoginState> {

    override val layoutId = R.layout.fragment_login_v1
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var progressDialog: SweetAlertDialog
    private lateinit var username: String
    private lateinit var password: String


    companion object {
        fun newInstance() = LoginFragmentV1().also { loginFragment ->
            val bundle = Bundle()
            loginFragment.arguments = bundle
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = requireContext().getDefaultProgressDialog()
        setObservers()
        setOnClickListeners()
    }

    private fun setObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.state.collect {
                render(it)
            }
        }
    }


    private fun setOnClickListeners() {

        binding.login.setOnClickListener {
            onLoginBtnClicked()
        }
        binding.privacyPolicy.setOnClickListener {
            onPrivacyPolicyClicked()
        }

        binding.forgotPassword.setOnClickListener {
            triggerIntent(LoginIntent.ForgotPassword)
        }

        binding.signUpNow.setOnClickListener {
            onSignUpNowClicked()
        }

        binding.toolbar.setNavigationOnClickListener {
            onToolbarBackPressed()
        }
    }

    private fun onToolbarBackPressed() {
        val fragment =
            requireActivity().supportFragmentManager.findFragmentByTag(LoginFragmentV1::class.java.simpleName)
        requireActivity().supportFragmentManager.beginTransaction().remove(fragment!!).commit()
    }

    override fun render(state: LoginState) {
        when (state) {
            is LoginState.Loading -> progressDialog.show()

            is LoginState.Login -> onLoginSuccess(state.data)

            is LoginState.Error -> {
                progressDialog.dismiss()

                when (state.failureStatus) {
                    FailureStatus.API_FAIL -> showInvalidLoginDetailsDialog(state.message)
                    FailureStatus.EMPTY -> TODO()
                    FailureStatus.NO_INTERNET -> showErrorSnackbar("Please connect to Internet")
                    FailureStatus.OTHER -> showErrorSnackbar(state.message)
                }

            }
            is LoginState.InvalidLoginDetailsError -> {
                progressDialog.dismiss()
                when (state.uiError.value) {
                    LoginFieldsValidation.EMPTY_EMAIL.value -> setEmptyUsernameError()
                    LoginFieldsValidation.EMPTY_PASSWORD.value -> setEmptyPasswordError()
                    LoginFieldsValidation.INVALID_PASSWORD.value -> setInvalidPasswordError()
                    //LoginFieldsValidation.INVALID_LOGIN_DETAILS.value -> showInvalidLoginDetailsDialog()
                }
            }
            is LoginState.PrivacyPolicy -> {
                progressDialog.dismiss()
                requireContext().startActivity(state.intent)
            }

            is LoginState.Idle -> {}

            LoginState.ForgotPassword -> showForgotPasswordDialog()
        }
    }

    private fun onLoginSuccess(loginResponse: LoginResponse) {
        progressDialog.dismiss()
        ApiRequestHelper.callHomePageApi(requireActivity())
    }


    private fun onSignUpNowClicked() {
        requireActivity().supportFragmentManager.beginTransaction()
            .add(
                R.id.fragment_container_v1,
                SignUpFragmentV1.newInstance(),
                SignUpFragmentV1::class.java.simpleName
            ).addToBackStack(SignUpFragmentV1::class.java.simpleName).commit()

        onToolbarBackPressed()
    }


    private fun showForgotPasswordDialog() {
        val fragmentManager = (requireActivity() as AppCompatActivity).supportFragmentManager
        val forgotPasswordDialogFragment = ForgotPasswordDialogFragment.newInstance("")
        forgotPasswordDialogFragment.show(
            fragmentManager,
            ForgotPasswordDialogFragment::class.java.simpleName
        )
    }

    private fun showInvalidLoginDetailsDialog(message: String?) {
        requireContext().showDefaultWarningDialog(
            getString(R.string.error_login_failure),
            message
        )
    }


    override fun triggerIntent(intent: LoginIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }

    private fun onLoginBtnClicked() {
        username = binding.usernameEt.text.toString()
        password = binding.passwordEt.text.toString()
        triggerIntent(LoginIntent.Login(username, password))
    }

    private fun onPrivacyPolicyClicked() {
        triggerIntent(LoginIntent.PrivacyPolicy)
    }


    private fun setEmptyUsernameError() {
        binding.usernameLayout.error = String.format(
            Locale.getDefault(),
            "%s %s",
            getString(R.string.phone_number_or_username),
            getString(R.string.error_is_required)
        )
    }

    private fun setEmptyPasswordError() {
        binding.passwordLayout.error = String.format(
            Locale.getDefault(),
            "${getString(R.string.password)} ${getString(R.string.error_is_required)}"
        )
    }

    private fun setInvalidPasswordError() {
        binding.passwordLayout.error = String.format(
            "%s %s",
            getString(R.string.password),
            String.format(
                Locale.getDefault(),
                getString(R.string.error_password_length_x),
                BuildConfig.MIN_PASSWORD_LENGTH
            )
        )

    }

    private fun showErrorSnackbar(message: String?) {
        message?.let {
            SnackbarHelper.getSnackbar(
                requireActivity(),
                message,
                Snackbar.LENGTH_LONG
            ).show()
        }
    }


}