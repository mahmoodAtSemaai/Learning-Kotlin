package com.webkul.mobikul.odoo.features.authentication.presentation.fragment

import android.content.Intent
import android.content.res.Configuration
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
import com.webkul.mobikul.odoo.activity.NewHomeActivity
import com.webkul.mobikul.odoo.activity.UserApprovalActivity
import com.webkul.mobikul.odoo.constant.ApplicationConstant.SECONDS_IN_A_MINUTE
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.extension.getProgressDialogWithText
import com.webkul.mobikul.odoo.core.extension.showKeyboard
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.databinding.FragmentLoginOtpBinding
import com.webkul.mobikul.odoo.features.authentication.presentation.intent.LoginOtpIntent
import com.webkul.mobikul.odoo.features.authentication.presentation.state.LoginOtpState
import com.webkul.mobikul.odoo.features.authentication.presentation.viewmodel.LoginOtpViewModel
import com.webkul.mobikul.odoo.helper.GenericKeyEvent
import com.webkul.mobikul.odoo.helper.GenericTextWatcher
import com.webkul.mobikul.odoo.model.home.HomePageResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class LoginOtpFragment @Inject constructor() : BindingBaseFragment<FragmentLoginOtpBinding>(),
    IView<LoginOtpIntent, LoginOtpState> {

    override val layoutId: Int = R.layout.fragment_login_otp
    private val viewModel: LoginOtpViewModel by viewModels()
    private var phoneNumber: String = ""
    private lateinit var progressDialog: SweetAlertDialog

    companion object {
        fun newInstance(phoneNumber: String) = LoginOtpFragment().also { loginOtpFragment ->
            loginOtpFragment.arguments = Bundle().apply {
                putString(BundleConstant.BUNDLE_KEY_PHONE_NUMBER, phoneNumber)
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = requireActivity().getProgressDialogWithText(getString(R.string.please_wait_a_moment),"")

        getArgs()

        setObservers()
        setListeners()
        clearOTPFields(true)

        getOTP()
        requireActivity().onBackPressedDispatcher.addCallback(this,true) {
            showBackPressedAlertDialog()
        }
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
        triggerIntent(LoginOtpIntent.GenerateOtp(phoneNumber))
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
    }

    private fun setListeners() {
        binding.apply {
            etOtp1.addTextChangedListener(GenericTextWatcher(etOtp2, etOtp1))
            etOtp2.addTextChangedListener(GenericTextWatcher(etOtp3, etOtp1))
            etOtp3.addTextChangedListener(GenericTextWatcher(etOtp4, etOtp2))
            etOtp4.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(text: Editable?) {
                    if(text?.length == 0 )
                        etOtp3.requestFocus()
                    else {
                        triggerIntent(LoginOtpIntent.GetOTPFromInput(phoneNumber,etOtp1.text.toString() + etOtp2.text.toString() + etOtp3.text.toString() + text.toString()))
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

    override fun triggerIntent(intent: LoginOtpIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }

    override fun render(state: LoginOtpState) {
        when (state) {
            is LoginOtpState.Error -> {
                clearOTPFields(false)
                progressDialog.dismiss()
            }
            is LoginOtpState.OTPSent -> {
                progressDialog.dismiss()
                binding.tvInvalidOtp.visibility = View.INVISIBLE
                clearOTPFields(false)
                setResendText(false)
                triggerIntent(LoginOtpIntent.StartTimer(SECONDS_IN_A_MINUTE))
            }
            is LoginOtpState.Idle -> {
            }
            is LoginOtpState.Loading -> {
                progressDialog.show()
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
                triggerIntent(LoginOtpIntent.GetSplashData)
            }
            is LoginOtpState.Splash -> {
                state.splashScreenResponse.updateSharedPref(requireActivity())
                triggerIntent(LoginOtpIntent.GetHomePageData)
            }
            is LoginOtpState.HomePage -> {
                redirectToHomeActivity(state.homePageResponse)
            }
            is LoginOtpState.UnauthorisedUser -> {
                redirectToUserApprovalActivity()
            }
            is LoginOtpState.CountDownTimer -> {
                updateTimerText(state.timeRemaining)
            }
            is LoginOtpState.InvalidOTP -> {
                binding.tvInvalidOtp.visibility = View.VISIBLE
                clearOTPFields(false)
                progressDialog.dismiss()
            }
        }
    }

    private fun clearOTPFields(firstTimeLaunched : Boolean) {
        binding.apply {
            if(!firstTimeLaunched) {
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
            Intent(requireActivity(), UserApprovalActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
    }

    private fun redirectToHomeActivity(homePageResponse: HomePageResponse) {
        startActivity(Intent(requireActivity(), NewHomeActivity::class.java)
            .putExtra(BundleConstant.BUNDLE_KEY_HOME_PAGE_RESPONSE, homePageResponse)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
    }



    private fun setResendText(enable: Boolean) {
        binding.tvResend.apply {
            isEnabled = enable
            setTextColor(ContextCompat.getColor(requireContext(), if(enable) R.color.colorPrimary else R.color.colorDarkGrey))
        }
    }

    private fun updateTimerText(seconds : Int){
        val minutesRemaining = (seconds / (SECONDS_IN_A_MINUTE))
        val secondsRemaining = (seconds % (SECONDS_IN_A_MINUTE))
        var timeRemaining = if(minutesRemaining >= 10) "$minutesRemaining:" else "0$minutesRemaining:"
        timeRemaining += (if(secondsRemaining >= 10) "$secondsRemaining" else "0$secondsRemaining")
        binding.tvTimer.text = timeRemaining
    }


    override fun onDestroy() {
        super.onDestroy()
        triggerIntent(LoginOtpIntent.StopTimer)
        progressDialog.dismiss()
    }


}