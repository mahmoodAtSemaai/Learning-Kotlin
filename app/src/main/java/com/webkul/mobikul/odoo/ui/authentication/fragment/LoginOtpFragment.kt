package com.webkul.mobikul.odoo.ui.authentication.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.iid.FirebaseInstanceId
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.NewHomeActivity
import com.webkul.mobikul.odoo.constant.ApplicationConstant.SECONDS_IN_A_MINUTE
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.extension.makeVisible
import com.webkul.mobikul.odoo.core.extension.showKeyboard
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.data.request.OtpAuthenticationRequest
import com.webkul.mobikul.odoo.databinding.FragmentLoginOtpBinding
import com.webkul.mobikul.odoo.helper.GenericKeyEvent
import com.webkul.mobikul.odoo.helper.GenericTextWatcher
import com.webkul.mobikul.odoo.model.home.HomePageResponse
import com.webkul.mobikul.odoo.model.request.RegisterDeviceTokenRequest
import com.webkul.mobikul.odoo.ui.authentication.effect.LoginOtpEffect
import com.webkul.mobikul.odoo.ui.authentication.intent.LoginOtpIntent
import com.webkul.mobikul.odoo.ui.authentication.state.LoginOtpState
import com.webkul.mobikul.odoo.ui.authentication.viewmodel.LoginOtpViewModel
import com.webkul.mobikul.odoo.ui.signUpOnboarding.UserOnboardingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class LoginOtpFragment @Inject constructor() : BindingBaseFragment<FragmentLoginOtpBinding>(),
    IView<LoginOtpIntent, LoginOtpState, LoginOtpEffect> {

    override val layoutId: Int = R.layout.fragment_login_otp
    private val viewModel: LoginOtpViewModel by viewModels()
    private var phoneNumber: String = ""

    companion object {
        fun newInstance(phoneNumber: String) = LoginOtpFragment().also { loginOtpFragment ->
            loginOtpFragment.arguments = Bundle().apply {
                putString(BundleConstant.BUNDLE_KEY_PHONE_NUMBER, phoneNumber)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        getArgs()
        setupViews()
        setListeners()
        clearOTP(true)
        getOTP()
        setupBackPressedListener()
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
        triggerIntent(LoginOtpIntent.GetArguments(requireArguments()))
    }


    private fun setListeners() {
        binding.apply {
            etOtp1.addTextChangedListener(GenericTextWatcher(etOtp2, etOtp1))
            etOtp2.addTextChangedListener(GenericTextWatcher(etOtp3, etOtp1))
            etOtp3.addTextChangedListener(GenericTextWatcher(etOtp4, etOtp2))
            etOtp4.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(text: Editable?) {
                    if (text?.length == 0) {
                        triggerIntent(LoginOtpIntent.RequestFocus)
                    } else {
                        val otp = getOTPFromInputFields()
                        triggerIntent(
                            LoginOtpIntent.GetOTPFromInput(
                                OtpAuthenticationRequest(
                                    otp = otp,
                                    fcmDeviceId = Settings.Secure.getString(
                                        requireContext().contentResolver,
                                        Settings.Secure.ANDROID_ID
                                    ),
                                    fcmToken = if (FirebaseInstanceId.getInstance().token == null) "" else FirebaseInstanceId.getInstance().token!!,
                                    phoneNumber = phoneNumber
                                )
                            )
                        )
                    }
                }
            })

            etOtp2.setOnKeyListener(GenericKeyEvent(etOtp2, etOtp1))
            etOtp3.setOnKeyListener(GenericKeyEvent(etOtp3, etOtp2))
            etOtp4.setOnKeyListener(GenericKeyEvent(etOtp4, etOtp3))

            tvResend.setOnClickListener {
                getOTP()
            }
        }
    }

    private fun getOTPFromInputFields(): String {
        var otpBuilder = ""
        binding.apply {
            otpBuilder = if (etOtp1.text.isNullOrEmpty()) "" else etOtp1.text.toString()
            otpBuilder += if (etOtp2.text.isNullOrEmpty()) "" else etOtp2.text.toString()
            otpBuilder += if (etOtp3.text.isNullOrEmpty()) "" else etOtp3.text.toString()
            otpBuilder += if (etOtp4.text.isNullOrEmpty()) "" else etOtp4.text.toString()
        }
        return otpBuilder
    }

    private fun clearOTP(isFirstTimeLaunched: Boolean) {
        triggerIntent(LoginOtpIntent.ClearOTP(isFirstTimeLaunched))
    }

    private fun getOTP() {
        triggerIntent(LoginOtpIntent.GenerateOtp(phoneNumber))
    }

    private fun setupBackPressedListener() {
        triggerIntent(LoginOtpIntent.DispatchBackPressed)
    }

    private fun showBackPressedAlertDialog() {
        val alertDialog = SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
        alertDialog.titleText = getString(R.string.otp_already_sent)
        alertDialog.contentText = getString(R.string.are_you_sure_to_go_back)
        alertDialog.confirmText = getString(R.string.no)
        alertDialog.cancelText = getString(R.string.yes)
        alertDialog.setConfirmClickListener {
            alertDialog.dismiss()
        }
        alertDialog.setCancelClickListener {
            requireActivity().supportFragmentManager.popBackStack()
            alertDialog.dismiss()
        }
        alertDialog.show()
    }


    override fun triggerIntent(intent: LoginOtpIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }

    override fun render(state: LoginOtpState) {
        when (state) {
            is LoginOtpState.Idle -> {
            }

            is LoginOtpState.SetupBackPressedListener -> {
                requireActivity().onBackPressedDispatcher.addCallback(this, true) {
                    showBackPressedAlertDialog()
                }
            }

            is LoginOtpState.SetPhoneNumber -> {
                phoneNumber = state.phoneNumber
            }
            is LoginOtpState.Error -> {
                showErrorState(state.failureStatus, state.message)
                clearOTP(false)
                dismissProgressDialog()
            }
            is LoginOtpState.OTPSent -> {
                dismissProgressDialog()
                binding.tvInvalidOtp.visibility = View.INVISIBLE
                clearOTP(false)
                setResendText(false)
                triggerIntent(LoginOtpIntent.StartTimer(SECONDS_IN_A_MINUTE))
            }
            is LoginOtpState.Loading -> {
                showProgressDialog()
            }
            is LoginOtpState.Login -> {
            }
            is LoginOtpState.GetOTPFromInput -> {
            }
            is LoginOtpState.OTPExpired -> {
                setResendText(true)
            }
            is LoginOtpState.OTPVerified -> {
                triggerIntent(LoginOtpIntent.StopTimer)
                triggerIntent(LoginOtpIntent.RegisterFCMToken(RegisterDeviceTokenRequest(requireActivity())))
            }
            is LoginOtpState.RegisterFCMTokenState -> {
                triggerIntent(LoginOtpIntent.GetSplashData)
            }
            is LoginOtpState.Splash -> {
                triggerIntent(LoginOtpIntent.GetUser)
            }
            is LoginOtpState.UserFetched -> {
                triggerIntent(LoginOtpIntent.CreateChatChannel)
                triggerIntent(LoginOtpIntent.GetHomePageData)
            }
            is LoginOtpState.CountDownTimer -> {
                updateTimerText(state.timeRemaining)
            }
            is LoginOtpState.InvalidOTP -> {
                binding.tvInvalidOtp.makeVisible()
                clearOTP(false)
                dismissProgressDialog()
            }
            is LoginOtpState.OTPCleared -> clearOTPFields(state.isFirstTimeLaunched)
            is LoginOtpState.SetFocus -> binding.etOtp3.requestFocus()
        }
    }

    override fun render(effect: LoginOtpEffect) {
        when (effect) {
            is LoginOtpEffect.NavigateToUserHomeActivity -> redirectToHomeActivity()
        }
    }

    private fun clearOTPFields(firstTimeLaunched: Boolean) {
        binding.apply {
            if (!firstTimeLaunched) {
                etOtp1.text.clear()
                etOtp2.text.clear()
                etOtp3.text.clear()
                etOtp4.text.clear()
            }

            etOtp1.apply {
                showKeyboard()
                setText(" ")
                setText("")
            }
        }
    }

    private fun redirectToUserApprovalActivity() {
        startActivity(
            Intent(
                requireActivity(),
                UserOnboardingActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    private fun redirectToHomeActivity() {
        startActivity(
            Intent(requireActivity(), NewHomeActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }


    private fun setResendText(enable: Boolean) {
        binding.tvResend.apply {
            isEnabled = enable
            setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (enable) R.color.colorPrimary else R.color.colorDarkGrey
                )
            )
        }
    }

    private fun updateTimerText(seconds: Int) {
        val minutesRemaining = (seconds / (SECONDS_IN_A_MINUTE))
        val secondsRemaining = (seconds % (SECONDS_IN_A_MINUTE))
        var timeRemaining =
            if (minutesRemaining >= 10) "$minutesRemaining:" else "0$minutesRemaining:"
        timeRemaining += (if (secondsRemaining >= 10) "$secondsRemaining" else "0$secondsRemaining")
        binding.tvTimer.text = timeRemaining
    }


    override fun onDestroy() {
        super.onDestroy()
        triggerIntent(LoginOtpIntent.StopTimer)
        dismissProgressDialog()
    }


}