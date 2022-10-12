package com.webkul.mobikul.odoo.ui.authentication.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.iid.FirebaseInstanceId
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.NewHomeActivity
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl
import com.webkul.mobikul.odoo.analytics.AnalyticsSourceConstants
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.extension.getCompatColor
import com.webkul.mobikul.odoo.core.extension.getDefaultProgressDialog
import com.webkul.mobikul.odoo.core.extension.makeInvisible
import com.webkul.mobikul.odoo.core.extension.makeVisible
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.databinding.FragmentLoginPasswordBinding
import com.webkul.mobikul.odoo.dialog_frag.ForgotPasswordDialogFragment
import com.webkul.mobikul.odoo.data.request.LoginOtpAuthenticationRequest
import com.webkul.mobikul.odoo.ui.authentication.effect.LoginPasswordEffect
import com.webkul.mobikul.odoo.ui.authentication.intent.LoginPasswordIntent
import com.webkul.mobikul.odoo.ui.authentication.state.LoginPasswordState
import com.webkul.mobikul.odoo.ui.authentication.viewmodel.LoginPasswordViewModel
import com.webkul.mobikul.odoo.helper.AsteriskPasswordTransformationMethod
import com.webkul.mobikul.odoo.model.home.HomePageResponse
import com.webkul.mobikul.odoo.model.request.RegisterDeviceTokenRequest
import com.webkul.mobikul.odoo.ui.signUpOnboarding.UserOnboardingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class LoginPasswordFragment @Inject constructor() :
        BindingBaseFragment<FragmentLoginPasswordBinding>(),
        IView<LoginPasswordIntent, LoginPasswordState, LoginPasswordEffect> {

    override val layoutId: Int = R.layout.fragment_login_password
    private val viewModel: LoginPasswordViewModel by viewModels()
    private var phoneNumber = ""
    private val profileName = "name"

    companion object {
        fun newInstance(phoneNumber: String) =
                LoginPasswordFragment().also { loginPasswordFragment ->
                    val bundle = Bundle()
                    bundle.putString(BundleConstant.BUNDLE_KEY_PHONE_NUMBER, phoneNumber)
                    loginPasswordFragment.arguments = bundle
                }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setupViews()
        getArgs()
        setListeners()
    }


    private fun getArgs() {
        triggerIntent(LoginPasswordIntent.GetArguments(requireArguments()))
    }

    override fun setupViews() {
        triggerIntent(LoginPasswordIntent.SetupViews)
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
        binding.btnContinue.setOnClickListener {
            triggerIntent(LoginPasswordIntent.Login(LoginOtpAuthenticationRequest().apply {
                password = binding.passwordEt.text.toString()
                login = phoneNumber
                isSocialLogin = false
                fcmDeviceId = Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)
                fcmToken = if (FirebaseInstanceId.getInstance().token == null) "" else FirebaseInstanceId.getInstance().token!!
                customerId = ""
                name = profileName
            }))
        }
        binding.passwordEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                triggerIntent(LoginPasswordIntent.PasswordReceived(s.toString()))
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        binding.tvForgotPassword.setOnClickListener {
            triggerIntent(LoginPasswordIntent.ForgetPassword)
        }
    }

    override fun render(state: LoginPasswordState) {
        when (state) {
            is LoginPasswordState.Idle -> {
            }
            is LoginPasswordState.InitialiseViews -> {
                binding.passwordEt.transformationMethod = AsteriskPasswordTransformationMethod()
            }
            is LoginPasswordState.InvalidPasswordError -> {
                showError(state.errorMessage)
                dismissProgressDialog()
                dismissProgressDialog()
            }
            is LoginPasswordState.Error -> {
                dismissProgressDialog()
                showErrorState(state.failureStatus,
                        state.message ?: getString(R.string.error_something_went_wrong))
            }
            is LoginPasswordState.ForgotPassword -> {
                showForgetPasswordFragment(phoneNumber)
            }
            is LoginPasswordState.InvalidLoginDetailsError -> {
                dismissProgressDialog()
            }
            is LoginPasswordState.Loading -> {
                showProgressDialog()
            }
            is LoginPasswordState.LoggedIn -> {
                binding.tvError.makeInvisible()
                triggerIntent(LoginPasswordIntent.RegisterFCMToken(RegisterDeviceTokenRequest(requireActivity())))
            }
            is LoginPasswordState.EnableButton -> {
                binding.apply {
                    btnContinue.isEnabled = true
                    btnContinue.setTextColor(requireContext().getCompatColor(R.color.colorPrimary))
                }
            }
            is LoginPasswordState.DisableButton -> {
                binding.apply {
                    btnContinue.isEnabled = false
                    btnContinue.setTextColor(requireContext().getCompatColor(R.color.colorDarkGrey))
                }
            }
            is LoginPasswordState.RegisterFCMTokenState -> {
                triggerIntent(LoginPasswordIntent.SplashPage)
            }
            is LoginPasswordState.Splash -> {
                triggerIntent(LoginPasswordIntent.GetUser)
            }
            is LoginPasswordState.UserFetched -> {
                triggerIntent(LoginPasswordIntent.CreateChatChannel)
                triggerIntent(LoginPasswordIntent.HomePage)
            }
            is LoginPasswordState.SetPhoneNumber -> phoneNumber = state.phoneNumber
        }
    }

    override fun render(effect: LoginPasswordEffect) {
        when (effect) {
            is LoginPasswordEffect.NavigateToUserHomeActivity -> redirectToHomeActivity()
        }
    }

    private fun redirectToHomeActivity() {
        startActivity(Intent(requireActivity(), NewHomeActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        )
        dismissProgressDialog()
        requireActivity().finish()
    }

    private fun showForgetPasswordFragment(phoneNumber: String) {
        AnalyticsImpl.trackForgotPasswordSelected(AnalyticsSourceConstants.EVENT_SOURCE_LOGIN)
        val fragmentManager = requireActivity().supportFragmentManager
        val forgotPasswordDialogFragment = ForgotPasswordDialogFragment.newInstance(phoneNumber)
        forgotPasswordDialogFragment.show(fragmentManager, ForgotPasswordDialogFragment::class.java.simpleName)
        fragmentManager.executePendingTransactions()
        forgotPasswordDialogFragment.dialog?.setOnDismissListener {
            triggerIntent(LoginPasswordIntent.Default)
            forgotPasswordDialogFragment.dialog?.dismiss()
        }
    }

    private fun showError(errorMessage: String) {
        binding.tvError.apply {
            makeVisible()
            text = errorMessage
        }
    }

    override fun triggerIntent(intent: LoginPasswordIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }

}