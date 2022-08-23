package com.webkul.mobikul.odoo.ui.signUpAuth.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.databinding.FragmentSignUpOptionsBinding
import com.webkul.mobikul.odoo.ui.signUpAuth.effect.SignUpOptionsEffect
import com.webkul.mobikul.odoo.ui.signUpAuth.intent.SignUpOptionsIntent
import com.webkul.mobikul.odoo.ui.signUpAuth.state.SignUpOptionsState
import com.webkul.mobikul.odoo.ui.signUpAuth.viewModel.SignUpOptionsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SignUpOptionsFragment @Inject constructor() :
    BindingBaseFragment<FragmentSignUpOptionsBinding>(),
    IView<SignUpOptionsIntent, SignUpOptionsState, SignUpOptionsEffect> {

    override val layoutId: Int = R.layout.fragment_sign_up_options
    private val viewModel: SignUpOptionsViewModel by viewModels()
    private var phoneNumber: String = ""

    companion object {
        fun newInstance(phoneNumber: String) = SignUpOptionsFragment().also { signUpOptionsFragment ->
            val bundle = Bundle()
            bundle.putString(BundleConstant.BUNDLE_KEY_PHONE_NUMBER, phoneNumber)
            signUpOptionsFragment.arguments = bundle
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
        binding.number = phoneNumber
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
            llShareOtpViaSms.setOnClickListener { triggerIntent(SignUpOptionsIntent.OtpSignUp(phoneNumber)) }
            tvChangePhoneNumber.setOnClickListener { triggerIntent(SignUpOptionsIntent.ChangePhoneNumber) }
        }
    }

    override fun render(state: SignUpOptionsState) {

    }

    override fun render(effect: SignUpOptionsEffect) {
        when(effect) {
            is SignUpOptionsEffect.NavigateToOTPSignUp -> navigateToSignUpOtpScreen(phoneNumber)
            is SignUpOptionsEffect.NavigateToPhoneNumberValidation -> navigateToAuthenticationScreen()
        }
    }

    override fun triggerIntent(intent: SignUpOptionsIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }

    private fun navigateToSignUpOtpScreen(phoneNumber: String) {
        requireActivity().supportFragmentManager.beginTransaction()
            .add(
                R.id.fl_auth_container,
                SignUpOtpFragment.newInstance(phoneNumber),
                SignUpOtpFragment::class.java.simpleName
            ).addToBackStack(SignUpOtpFragment::class.java.simpleName).commit()
        triggerIntent(SignUpOptionsIntent.SetIdle)
    }

    private fun navigateToAuthenticationScreen() {
        requireActivity().supportFragmentManager.popBackStack()
    }
}