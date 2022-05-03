package com.webkul.mobikul.odoo.features.auth.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.webkul.mobikul.odoo.BuildConfig
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.extension.getDefaultProgressDialog
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.databinding.FragmentLoginV1Binding
import com.webkul.mobikul.odoo.features.auth.domain.enums.AuthFieldsValidation
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
    private lateinit var progressDialog: SweetAlertDialog

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
    }


    override fun render(state: LoginState) {
        when (state) {
            is LoginState.Loading -> {
                progressDialog.show()
            }
            is LoginState.Login -> {
                progressDialog.dismiss()
                ApiRequestHelper.callHomePageApi(requireActivity())
            }
            is LoginState.Error -> {
                progressDialog.dismiss()

                val error = state.error
            }
            is LoginState.InvalidLoginDetailsError -> {
                progressDialog.dismiss()
                when (state.uiError.value) {
                    AuthFieldsValidation.EMPTY_EMAIL.value -> setEmptyUsernameError()
                    AuthFieldsValidation.EMPTY_PASSWORD.value -> setEmptyPasswordError()
                    AuthFieldsValidation.INVALID_PASSWORD.value -> setInvalidPasswordError()
                }
            }
            is LoginState.PrivacyPolicy -> {
                progressDialog.dismiss()
                requireContext().startActivity(state.intent)
            }

            is LoginState.Idle -> {}
        }
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

    private fun onPrivacyPolicyClicked() {
        triggerIntent(LoginIntent.PrivacyPolicy)
        Log.d("testing", "Intent trigered")
    }


    private fun setEmptyUsernameError() {
        binding.usernameEt.error = String.format(
            Locale.getDefault(),
            "%s %s",
            getString(R.string.phone_number_or_username),
            getString(R.string.error_is_required)
        )
    }

    private fun setEmptyPasswordError() {
        binding.passwordEt.error = String.format(
            Locale.getDefault(),
            getString(R.string.password) + " " + getString(R.string.error_is_required)
        )
    }

    private fun setInvalidPasswordError() {
        binding.passwordEt.error = String.format(
            "%s %s",
            getString(R.string.password),
            String.format(
                Locale.getDefault(),
                getString(R.string.error_password_length_x),
                BuildConfig.MIN_PASSWORD_LENGTH
            )
        )

    }


}