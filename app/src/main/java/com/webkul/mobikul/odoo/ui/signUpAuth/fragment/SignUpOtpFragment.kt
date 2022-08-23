package com.webkul.mobikul.odoo.ui.signUpAuth.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.constant.ApplicationConstant
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.extension.getProgressDialogWithText
import com.webkul.mobikul.odoo.core.extension.makeInvisible
import com.webkul.mobikul.odoo.core.extension.makeVisible
import com.webkul.mobikul.odoo.core.extension.showKeyboard
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.databinding.FragmentSignUpOtpBinding
import com.webkul.mobikul.odoo.helper.GenericKeyEvent
import com.webkul.mobikul.odoo.helper.GenericTextWatcher
import com.webkul.mobikul.odoo.ui.signUpAuth.effect.SignUpOtpEffect
import com.webkul.mobikul.odoo.ui.signUpAuth.intent.SignUpOtpIntent
import com.webkul.mobikul.odoo.ui.signUpAuth.state.SignUpOtpState
import com.webkul.mobikul.odoo.ui.signUpAuth.viewModel.SignUpOtpViewModel
import com.webkul.mobikul.odoo.ui.signUpOnboarding.UserOnboardingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SignUpOtpFragment @Inject constructor() : BindingBaseFragment<FragmentSignUpOtpBinding>(),
    IView<SignUpOtpIntent, SignUpOtpState, SignUpOtpEffect> {

    override val layoutId: Int = R.layout.fragment_sign_up_otp

    private val viewModel: SignUpOtpViewModel by viewModels()
    private var phoneNumber: String = ""
    private lateinit var progressDialog: SweetAlertDialog

    companion object {
        fun newInstance(phoneNumber: String) = SignUpOtpFragment().also { signUpOtpFragment ->
            signUpOtpFragment.arguments = Bundle().apply {
                putString(BundleConstant.BUNDLE_KEY_PHONE_NUMBER, phoneNumber)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = requireActivity().getProgressDialogWithText(
            getString(R.string.please_wait_a_moment),
            ""
        )

        getArgs()

        setObservers()
        setListeners()
        clearOTP(true)

        getOTP()
        requireActivity().onBackPressedDispatcher.addCallback(this, true) {
            showBackPressedAlertDialog()
        }
    }

    private fun clearOTP(isFirstTimeLaunched: Boolean) {
        triggerIntent(SignUpOtpIntent.ClearOTP(isFirstTimeLaunched))
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

    private fun getOTP() {
        triggerIntent(SignUpOtpIntent.GenerateOtp(phoneNumber))
    }

    private fun getArgs() {
        phoneNumber = arguments?.getString(BundleConstant.BUNDLE_KEY_PHONE_NUMBER).toString()
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
                    if (text?.length == 0)
                        etOtp3.requestFocus()
                    else {
                        triggerIntent(
                            SignUpOtpIntent.GetOTPFromInput(
                                phoneNumber,
                                etOtp1.text.toString() + etOtp2.text.toString() + etOtp3.text.toString() + text.toString()
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

    override fun render(state: SignUpOtpState) {
        when (state) {
            is SignUpOtpState.Error -> {
                showErrorState(state.failureStatus, state.message)
                clearOTP(false)
                progressDialog.dismiss()
            }
            is SignUpOtpState.OTPSent -> {
                progressDialog.dismiss()
                binding.tvInvalidOtp.makeInvisible()
                clearOTP(false)
                setResendText(false)
                triggerIntent(SignUpOtpIntent.StartTimer(ApplicationConstant.SECONDS_IN_A_MINUTE))
            }
            is SignUpOtpState.Idle -> {
                progressDialog.dismiss()
            }
            is SignUpOtpState.Loading -> {
                progressDialog.show()
            }
            is SignUpOtpState.OTPExpired -> {
                setResendText(true)
            }
            is SignUpOtpState.CountDownTimer -> {
                updateTimerText(state.timeRemaining)
            }
            is SignUpOtpState.InvalidOTP -> {
                binding.tvInvalidOtp.makeVisible()
                clearOTP(false)
                progressDialog.dismiss()
            }
            is SignUpOtpState.OTPCleared -> {
                clearOTPFields(state.isFirstTimeLaunched)
            }
        }
    }

    override fun render(effect: SignUpOtpEffect) {
        when (effect) {
            is SignUpOtpEffect.NavigateToUserOnboarding -> redirectToUserOnboardingActivity()
        }
    }

    override fun triggerIntent(intent: SignUpOtpIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        triggerIntent(SignUpOtpIntent.StopTimer)
        progressDialog.dismiss()
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

    private fun redirectToUserOnboardingActivity() {
        startActivity(
            Intent(
                requireActivity(),
                UserOnboardingActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        )
        requireActivity().finish()
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
        val minutesRemaining = (seconds / (ApplicationConstant.SECONDS_IN_A_MINUTE))
        val secondsRemaining = (seconds % (ApplicationConstant.SECONDS_IN_A_MINUTE))
        var timeRemaining =
            if (minutesRemaining >= 10) "$minutesRemaining:" else "0$minutesRemaining:"
        timeRemaining += (if (secondsRemaining >= 10) "$secondsRemaining" else "0$secondsRemaining")
        binding.tvTimer.text = timeRemaining
    }
}