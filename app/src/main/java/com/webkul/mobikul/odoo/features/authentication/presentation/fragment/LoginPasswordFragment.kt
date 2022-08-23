package com.webkul.mobikul.odoo.features.authentication.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.iid.FirebaseInstanceId
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.NewHomeActivity
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl
import com.webkul.mobikul.odoo.analytics.AnalyticsSourceConstants
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.extension.getDefaultProgressDialog
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.databinding.FragmentLoginPasswordBinding
import com.webkul.mobikul.odoo.dialog_frag.ForgotPasswordDialogFragment
import com.webkul.mobikul.odoo.features.authentication.data.models.LoginOtpAuthenticationRequest
import com.webkul.mobikul.odoo.features.authentication.presentation.effect.LoginPasswordEffect
import com.webkul.mobikul.odoo.features.authentication.presentation.intent.LoginPasswordIntent
import com.webkul.mobikul.odoo.features.authentication.presentation.state.LoginPasswordState
import com.webkul.mobikul.odoo.features.authentication.presentation.viewmodel.LoginPasswordViewModel
import com.webkul.mobikul.odoo.helper.AsteriskPasswordTransformationMethod
import com.webkul.mobikul.odoo.model.home.HomePageResponse
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
    private lateinit var progressDialog: SweetAlertDialog
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
        binding.passwordEt.transformationMethod = AsteriskPasswordTransformationMethod()
        progressDialog = requireActivity().getDefaultProgressDialog()
        getArgs()
        setObservers()
        setListeners()
    }


    private fun getArgs() {
        phoneNumber = arguments?.getString(BundleConstant.BUNDLE_KEY_PHONE_NUMBER) ?: ""
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
                progressDialog.dismiss()
            }
            is LoginPasswordState.Error -> {
                progressDialog.dismiss()
                //TODO-> Handle elegantly in v2 login arch rvamp
                when (state.failureStatus) {
                    FailureStatus.INCOMPLETE_ONBOARDING -> showErrorState(
                        state.failureStatus, state.message ?: getString(R.string.error_something_went_wrong)
                    )
                    FailureStatus.NO_INTERNET -> showErrorState(state.failureStatus,
                            state.message ?: getString(R.string.error_something_went_wrong))
                    FailureStatus.ACCESS_DENIED -> showErrorState(state.failureStatus,
                            state.message ?: getString(R.string.error_something_went_wrong))
                    FailureStatus.USER_UNAPPROVED -> showErrorState(state.failureStatus,
                            state.message ?: getString(R.string.error_something_went_wrong))
                    else -> showError(getString(R.string.error_incorrect_password))
                }
            }
            is LoginPasswordState.ForgotPassword -> {
                showForgetPasswordFragment(phoneNumber)
            }
            is LoginPasswordState.InvalidLoginDetailsError -> {
                progressDialog.dismiss()
            }
            is LoginPasswordState.Loading -> {
                progressDialog.show()
            }
            is LoginPasswordState.LoggedIn -> {
                binding.tvError.visibility = View.INVISIBLE
                triggerIntent(LoginPasswordIntent.RegisterFCMToken)
            }
            is LoginPasswordState.EnableButton -> {
                binding.btnContinue.isEnabled = true
                binding.btnContinue.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            }
            is LoginPasswordState.DisableButton -> {
                binding.btnContinue.isEnabled = false
                binding.btnContinue.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorDarkGrey))
            }
            is LoginPasswordState.RegisterFCMTokenState -> {
                triggerIntent(LoginPasswordIntent.SplashPage)
            }
            is LoginPasswordState.Splash -> {
                state.splashScreenResponse.updateSharedPref(requireActivity())
                triggerIntent(LoginPasswordIntent.HomePage)
            }
            is LoginPasswordState.UnauthorisedUser -> {
                redirectToUserOnboardingActivity()
            }
            is LoginPasswordState.HomePage -> {
                redirectToHomeActivity(state.homePageResponse)
            }
        }
    }

    override fun render(effect: LoginPasswordEffect) {
        //TODO("Not yet implemented")
    }

    private fun redirectToUserOnboardingActivity() {
        startActivity(Intent(requireActivity(), UserOnboardingActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
        progressDialog.dismiss()
    }

    private fun redirectToHomeActivity(homePageResponse: HomePageResponse) {
        startActivity(Intent(requireActivity(), NewHomeActivity::class.java)
                .putExtra(BundleConstant.BUNDLE_KEY_HOME_PAGE_RESPONSE, homePageResponse)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        )
        progressDialog.dismiss()
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
            visibility = View.VISIBLE
            text = errorMessage
        }
    }

    override fun triggerIntent(intent: LoginPasswordIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }

}