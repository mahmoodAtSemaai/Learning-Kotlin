package com.webkul.mobikul.odoo.ui.auth

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.webkul.mobikul.odoo.BuildConfig
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.extension.close
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BaseActivity
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.core.utils.ERROR_INTERNET_CONNECTION
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.LoginEntity
import com.webkul.mobikul.odoo.databinding.FragmentLoginV1Binding
import com.webkul.mobikul.odoo.dialog_frag.ForgotPasswordDialogFragment
import com.webkul.mobikul.odoo.domain.enums.LoginFieldsValidation
import com.webkul.mobikul.odoo.helper.ApiRequestHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragmentV1 @Inject constructor() : BindingBaseFragment<FragmentLoginV1Binding>(),
        IView<LoginIntent, LoginState> {

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
                render(it)
            }
        }
    }


    private fun setOnClickListeners() {

        binding.login.setOnClickListener {
            onLoginBtnClicked()
        }
        binding.privacyPolicy.setOnClickListener {
            triggerIntent(LoginIntent.PrivacyPolicy)
        }

        binding.forgotPassword.setOnClickListener {
            triggerIntent(LoginIntent.ForgotPassword)
        }

        binding.signUpNow.setOnClickListener {
            triggerIntent(LoginIntent.NavigateToSignUp)
        }

        binding.toolbar.setNavigationOnClickListener {
            triggerIntent(LoginIntent.CloseLoginScreen)
        }
    }


    override fun render(state: LoginState) {
        when (state) {
            is LoginState.Loading -> {
                setErrorToNull()
                showProgressDialog()
            }

            is LoginState.Login -> onLoginSuccess(state.data)

            is LoginState.Dialog -> {
                dismissProgressDialog()
                showErrorDialogState(getString(R.string.error_login_failure), state.message
                        ?: getString(R.string.error_something_went_wrong))
            }

            is LoginState.Error -> {
                dismissProgressDialog()
                showErrorState(state.failureStatus, state.message)
            }

            is LoginState.InvalidLoginDetailsError -> {
                dismissProgressDialog()
                when (state.uiError.value) {
                    LoginFieldsValidation.EMPTY_EMAIL.value -> setEmptyUsernameError()
                    LoginFieldsValidation.EMPTY_PASSWORD.value -> setEmptyPasswordError()
                    LoginFieldsValidation.INVALID_PASSWORD.value -> setInvalidPasswordError()
                    //LoginFieldsValidation.INVALID_LOGIN_DETAILS.value -> showInvalidLoginDetailsDialog()
                }
            }
            is LoginState.PrivacyPolicy -> {
                dismissProgressDialog()
                requireContext().startActivity(state.intent)
            }

            is LoginState.Idle -> {}

            is LoginState.NavigateToSignUp -> navigateToSignUp()

            is LoginState.CloseLoginScreen -> this.close()

            is LoginState.ForgotPassword -> showForgotPasswordDialog()
        }
    }

    private fun onLoginSuccess(loginResponse: LoginEntity) {
        dismissProgressDialog()

        //todo -> create homePageUseCase ... already addressed in OTP feature
        ApiRequestHelper.callHomePageApi(requireActivity())
    }


    private fun navigateToSignUp() {
        this.close()
        (requireActivity() as BaseActivity).addFragmentWithBackStack(
                R.id.fragment_container_v1,
                SignUpFragmentV1.newInstance(),
                SignUpFragmentV1::class.java.simpleName
        )
    }


    private fun showForgotPasswordDialog() {
        val fragmentManager = (requireActivity() as AppCompatActivity).supportFragmentManager
        val forgotPasswordDialogFragment = ForgotPasswordDialogFragment.newInstance("")
        forgotPasswordDialogFragment.show(
                fragmentManager,
                ForgotPasswordDialogFragment::class.java.simpleName
        )
    }


    override fun triggerIntent(intent: LoginIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }

    private fun onLoginBtnClicked() {
        val username = binding.usernameEt.text.toString()
        val password = binding.passwordEt.text.toString()
        triggerIntent(LoginIntent.Login(username, password))
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


    private fun setErrorToNull() {
        binding.usernameLayout.isErrorEnabled = false
        binding.passwordLayout.isErrorEnabled = false
    }
}