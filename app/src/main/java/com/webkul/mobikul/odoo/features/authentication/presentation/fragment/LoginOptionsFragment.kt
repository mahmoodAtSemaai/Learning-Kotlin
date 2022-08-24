package com.webkul.mobikul.odoo.features.authentication.presentation.fragment

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
import com.webkul.mobikul.odoo.features.authentication.presentation.effect.LoginOptionsEffect
import com.webkul.mobikul.odoo.features.authentication.presentation.intent.LoginOptionsIntent
import com.webkul.mobikul.odoo.features.authentication.presentation.state.LoginOptionsState
import com.webkul.mobikul.odoo.features.authentication.presentation.viewmodel.LoginOptionsViewModel
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
        fun newInstance(phoneNumber: String,enablePassword:Boolean) = LoginOptionsFragment().also { loginOptionsFragment ->
            val bundle = Bundle()
            bundle.putString(BundleConstant.BUNDLE_KEY_PHONE_NUMBER, phoneNumber)
            bundle.putBoolean(BundleConstant.BUNDLE_KEY_ENABLE_PASSWORD,enablePassword)
            loginOptionsFragment.arguments = bundle
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgs()
        setObservers()
        setListeners()
    }


    private fun getArgs() {
        phoneNumber = arguments?.getString(BundleConstant.BUNDLE_KEY_PHONE_NUMBER) ?: ""
        enablePassword = arguments?.getBoolean(BundleConstant.BUNDLE_KEY_ENABLE_PASSWORD) ?: false
        binding.number = phoneNumber
        if(enablePassword.not()){
            binding.llEnterPassword.makeGone()
        }
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

    private fun setListeners() {
        binding.apply {
            llShareOtpViaSms.setOnClickListener { triggerIntent(LoginOptionsIntent.OtpLogin(phoneNumber)) }
            llEnterPassword.setOnClickListener { triggerIntent(LoginOptionsIntent.PasswordLogin) }
            tvChangePhoneNumber.setOnClickListener { triggerIntent(LoginOptionsIntent.ChangePhoneNumber) }
        }
    }


    override fun render(state: LoginOptionsState) {
        when (state) {
            is LoginOptionsState.Idle -> {
            }
            is LoginOptionsState.OtpLogin -> {
                navigateToLoginOtpScreen()
            }
            is LoginOptionsState.PasswordLogin -> {
                navigateToLoginPasswordScreen()
            }
            is LoginOptionsState.ChangePhoneNumber -> {
                navigateToAuthenticationScreen()
            }
        }
    }

    override fun render(effect: LoginOptionsEffect) {
        //TODO("Not yet implemented")
    }

    private fun navigateToLoginOtpScreen() {
        requireActivity().supportFragmentManager.beginTransaction()
                .add(
                        R.id.fragment_container_authentication,
                        LoginOtpFragment.newInstance(phoneNumber),
                        LoginOtpFragment::class.java.simpleName
                ).addToBackStack(LoginOtpFragment::class.java.simpleName).commit()
        triggerIntent(LoginOptionsIntent.SetIdle)
    }

    private fun navigateToLoginPasswordScreen() {
        requireActivity().supportFragmentManager.beginTransaction()
                .add(
                        R.id.fragment_container_authentication,
                        LoginPasswordFragment.newInstance(phoneNumber),
                        LoginPasswordFragment::class.java.simpleName
                ).addToBackStack(LoginPasswordFragment::class.java.simpleName).commit()
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