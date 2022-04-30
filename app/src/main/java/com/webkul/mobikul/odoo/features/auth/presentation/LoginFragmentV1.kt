package com.webkul.mobikul.odoo.features.auth.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.webkul.mobikul.odoo.BuildConfig
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.mvicore.IIntent
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.databinding.FragmentLoginV1Binding
import com.webkul.mobikul.odoo.helper.AlertDialogHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragmentV1 @Inject constructor() : BindingBaseFragment<FragmentLoginV1Binding>() , IView<LoginIntent,LoginState> {

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
    }

    private fun onLoginBtnClicked() {

        val username = binding.usernameEt.text.toString()
        val password = binding.passwordEt.text.toString()
        triggerIntent(LoginIntent.Login(username, password))
    }

    override fun render(state: LoginState) {
        when (state) {
            is LoginState.Loading -> {
               // AlertDialogHelper.showDefaultProgressDialog(context)
            }
            is LoginState.Login -> {
               // val loginResponse = state.data
               // startActivity(Intent())
            }
            is LoginState.Error -> {
              //  val error = state.error

            }
        }
    }

    override fun triggerIntent(intent: LoginIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }

}