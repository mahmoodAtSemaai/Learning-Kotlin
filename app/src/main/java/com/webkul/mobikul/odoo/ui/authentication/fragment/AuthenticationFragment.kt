package com.webkul.mobikul.odoo.ui.authentication.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.extension.getCompatColor
import com.webkul.mobikul.odoo.core.extension.makeGone
import com.webkul.mobikul.odoo.core.extension.makeVisible
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.databinding.FragmentAuthenticationBinding
import com.webkul.mobikul.odoo.domain.enums.VerifyPhoneNumberValidation
import com.webkul.mobikul.odoo.helper.Helper
import com.webkul.mobikul.odoo.ui.authentication.effect.AuthenticationEffect
import com.webkul.mobikul.odoo.ui.authentication.intent.AuthenticationIntent
import com.webkul.mobikul.odoo.ui.authentication.state.AuthenticationState
import com.webkul.mobikul.odoo.ui.authentication.viewmodel.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AuthenticationFragment @Inject constructor() :
    BindingBaseFragment<FragmentAuthenticationBinding>(),
    IView<AuthenticationIntent, AuthenticationState, AuthenticationEffect> {

    override val layoutId: Int = R.layout.fragment_authentication
    private val viewModel: AuthenticationViewModel by viewModels()

    companion object {
        fun newInstance() = AuthenticationFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setListeners()
        setFocusOnPhoneNumberInputField()
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.state.collect {
                render(it)
            }
        }

        lifecycleScope.launch {
            viewModel.effect.collect() {
                render(it)
            }
        }
    }

    private fun setListeners() {
        binding.apply {
            etPhoneNumber.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    triggerIntent(AuthenticationIntent.Verify(s.toString()))
                }
            })

            btnConfirmNumber.setOnClickListener {
                triggerIntent(AuthenticationIntent.Continue(etPhoneNumber.text.toString()))
            }

            tvSignUp.setOnClickListener { triggerIntent(AuthenticationIntent.SignUp) }

            tvPrivacyPolicy.setOnClickListener {
                triggerIntent(AuthenticationIntent.PrivacyPolicy)
            }
        }
    }

    private fun setFocusOnPhoneNumberInputField() {
        triggerIntent(AuthenticationIntent.GetFocus)
    }

    override fun render(state: AuthenticationState) {
        when (state) {
            is AuthenticationState.Idle -> {}
            is AuthenticationState.SetFocusOnInputField -> binding.etPhoneNumber.requestFocus()
            is AuthenticationState.IncorrectPhoneNumberFormat -> setError(
                VerifyPhoneNumberValidation.INCORRECT_PHONE_NUMBER_FORMAT
            )
            is AuthenticationState.IncorrectPhoneNumber -> setError(VerifyPhoneNumberValidation.INCORRECT_PHONE_NUMBER)
            is AuthenticationState.InvalidNumber -> setError(VerifyPhoneNumberValidation.INVALID_PHONE_NUMBER)
            is AuthenticationState.EmptyPhoneNumber -> setEmptyPhoneNumber()
            is AuthenticationState.ContinuePhoneNumber -> dismissProgressDialog()
            is AuthenticationState.ValidPhoneNumber -> validatedNumber()
            is AuthenticationState.Loading -> showProgressDialog()
            is AuthenticationState.Error -> {
                dismissProgressDialog()
                showErrorState(
                    state.failureStatus,
                    state.message ?: getString(R.string.error_something_went_wrong)
                )
            }
        }
    }

    override fun render(effect: AuthenticationEffect) {
        when (effect) {
            is AuthenticationEffect.NavigateToSignUp -> redirectToSignInSignUpFragment()
            is AuthenticationEffect.ShowPrivacyPolicy -> showPrivacyPolicy(effect.intent)
            is AuthenticationEffect.NavigateToLoginOptionsScreen -> {
                navigateToLoginOptionsScreen(effect.phoneNumber, effect.enablePassword)
                dismissProgressDialog()
            }
        }
    }

    private fun showPrivacyPolicy(intent: Intent) {
        requireContext().startActivity(intent)
    }

    private fun redirectToSignInSignUpFragment() {
        startActivity(
            Intent(requireContext(), SignInSignUpActivity::class.java)
                .putExtra(
                    BundleConstant.BUNDLE_KEY_SIGNUP_SCREEN,
                    BundleConstant.BUNDLE_KEY_SIGNUP_SCREEN
                )
        )
        requireActivity().finish()
    }

    private fun navigateToLoginOptionsScreen(phoneNumber: String, enablePassword: Boolean) {
        requireActivity().supportFragmentManager.beginTransaction()
            .add(
                R.id.fragment_container_authentication,
                LoginOptionsFragment.newInstance(phoneNumber, enablePassword),
                LoginOptionsFragment::class.java.simpleName
            ).addToBackStack(LoginOptionsFragment::class.java.simpleName).commit()
        Helper.hideKeyboard(requireActivity())
        //TODO check behaviour with idle state and without idle state
        triggerIntent(AuthenticationIntent.SetIdle)
    }

    private fun setError(verifyPhoneNumberValidation: VerifyPhoneNumberValidation) {
        when (verifyPhoneNumberValidation) {
            VerifyPhoneNumberValidation.INCORRECT_PHONE_NUMBER_FORMAT -> {
                binding.tvError.text =
                    getString(R.string.incorrect_mobile_number_format)
                setButtonState(false)
            }
            VerifyPhoneNumberValidation.INCORRECT_PHONE_NUMBER -> {
                binding.tvError.text =
                    getString(R.string.incorrect_mobile_number)
                setButtonState(false)
            }
            VerifyPhoneNumberValidation.INVALID_PHONE_NUMBER -> {
                binding.tvError.text =
                    getString(R.string.error_user_does_not_exists)
                dismissProgressDialog()
            }
        }
        binding.tvError.makeVisible()
    }

    private fun setButtonState(enabled: Boolean) {
        binding.apply {
            btnConfirmNumber.isEnabled = enabled
            btnConfirmNumber.setTextColor(requireContext().getCompatColor(if (enabled) R.color.colorPrimary else R.color.colorDarkGrey))
        }
    }

    private fun setEmptyPhoneNumber() {
        binding.apply {
            tvError.text = ""
            btnConfirmNumber.setTextColor(requireContext().getCompatColor(R.color.colorDarkGrey))
            btnConfirmNumber.isEnabled = false
            tvError.makeGone()
        }
    }

    private fun validatedNumber() {
        binding.apply {
            tvError.text = ""
            btnConfirmNumber.isEnabled = true
            btnConfirmNumber.setTextColor(requireContext().getCompatColor(R.color.colorPrimary))
            tvError.makeGone()
        }
    }

    override fun triggerIntent(intent: AuthenticationIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }

}