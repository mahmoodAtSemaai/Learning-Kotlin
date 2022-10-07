package com.webkul.mobikul.odoo.ui.authentication.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.extension.makeGone
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.databinding.FragmentLoginOptionsBinding
import com.webkul.mobikul.odoo.ui.authentication.effect.LoginOptionsEffect
import com.webkul.mobikul.odoo.ui.authentication.intent.LoginOptionsIntent
import com.webkul.mobikul.odoo.ui.authentication.state.LoginOptionsState
import com.webkul.mobikul.odoo.ui.authentication.viewmodel.LoginOptionsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginOptionsFragment @Inject constructor() :
    BindingBaseFragment<FragmentLoginOptionsBinding>(),
    IView<LoginOptionsIntent, LoginOptionsState, LoginOptionsEffect> {

    override val layoutId: Int = R.layout.fragment_login_options
    private val viewModel: LoginOptionsViewModel by viewModels()
    private var phoneNumber: String = ""
    private var enablePassword = true


    companion object {
        fun newInstance(phoneNumber: String, enablePassword: Boolean) =
            LoginOptionsFragment().also { loginOptionsFragment ->
                val bundle = Bundle()
                bundle.putString(BundleConstant.BUNDLE_KEY_PHONE_NUMBER, phoneNumber)
                bundle.putBoolean(BundleConstant.BUNDLE_KEY_ENABLE_PASSWORD, enablePassword)
                loginOptionsFragment.arguments = bundle
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        getArgs()
        setListeners()
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.state.collect {
                render(it)
            }
        }

        lifecycleScope.launch {
            viewModel.effect.collect {
                render(it)
            }
        }
    }

    private fun getArgs() {
        triggerIntent(LoginOptionsIntent.GetArguments(requireArguments()))
    }

    private fun setListeners() {
        binding.apply {
            llShareOtpViaSms.setOnClickListener {
                triggerIntent(
                    LoginOptionsIntent.OtpLogin(
                        phoneNumber
                    )
                )
            }
            llEnterPassword.setOnClickListener {
                triggerIntent(
                    LoginOptionsIntent.PasswordLogin(
                        phoneNumber
                    )
                )
            }
            tvChangePhoneNumber.setOnClickListener { triggerIntent(LoginOptionsIntent.ChangePhoneNumber) }
        }
    }


    override fun render(state: LoginOptionsState) {
        when (state) {
            is LoginOptionsState.Idle -> {}
            is LoginOptionsState.SetPhoneNumber -> {
                phoneNumber = state.phoneNumber
                binding.number = state.phoneNumber
                enablePassword = state.enablePassword
                if (enablePassword.not()) {
                    binding.llEnterPassword.makeGone()
                }
            }
        }
    }

    override fun render(effect: LoginOptionsEffect) {
        when (effect) {
            is LoginOptionsEffect.ChangePhoneNumber -> navigateToAuthenticationScreen()
            is LoginOptionsEffect.OtpLogin -> navigateToLoginOtpScreen(effect.phoneNumber)
            is LoginOptionsEffect.PasswordLogin -> navigateToLoginPasswordScreen(effect.phoneNumber)
        }
    }

    private fun navigateToLoginOtpScreen(phoneNumber: String) {
        requireActivity().supportFragmentManager.beginTransaction()
            .add(
                R.id.fragment_container_authentication,
                LoginOtpFragment.newInstance(phoneNumber),
                LoginOtpFragment::class.java.simpleName
            ).addToBackStack(LoginOtpFragment::class.java.simpleName).commit()
        //TODO check behaviour with idle state and without idle state
        triggerIntent(LoginOptionsIntent.SetIdle)
    }

    private fun navigateToLoginPasswordScreen(phoneNumber: String) {
        requireActivity().supportFragmentManager.beginTransaction()
            .add(
                R.id.fragment_container_authentication,
                LoginPasswordFragment.newInstance(phoneNumber),
                LoginPasswordFragment::class.java.simpleName
            ).addToBackStack(LoginPasswordFragment::class.java.simpleName).commit()
        //TODO check behaviour with idle state and without idle state
        triggerIntent(LoginOptionsIntent.SetIdle)
    }


    private fun navigateToAuthenticationScreen() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    override fun triggerIntent(intent: LoginOptionsIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }

}