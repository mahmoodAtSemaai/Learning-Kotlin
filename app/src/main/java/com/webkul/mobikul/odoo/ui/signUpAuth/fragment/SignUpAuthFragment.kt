package com.webkul.mobikul.odoo.ui.signUpAuth.fragment

import android.content.Intent
import android.os.Bundle
import android.text.*
import android.text.style.ForegroundColorSpan
import android.view.View
import android.webkit.WebView
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity
import com.webkul.mobikul.odoo.constant.ApplicationConstant
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.extension.getDefaultProgressDialog
import com.webkul.mobikul.odoo.core.extension.makeGone
import com.webkul.mobikul.odoo.core.extension.makeVisible
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.TermsAndConditionsEntity
import com.webkul.mobikul.odoo.databinding.FragmentSignUpAuthBinding
import com.webkul.mobikul.odoo.features.authentication.domain.enums.VerifyPhoneNumberValidation
import com.webkul.mobikul.odoo.helper.Helper
import com.webkul.mobikul.odoo.ui.signUpAuth.effect.SignUpAuthEffect
import com.webkul.mobikul.odoo.ui.signUpAuth.intent.SignUpAuthIntent
import com.webkul.mobikul.odoo.ui.signUpAuth.state.SignUpAuthState
import com.webkul.mobikul.odoo.ui.signUpAuth.viewModel.SignUpAuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SignUpAuthFragment @Inject constructor() :
    BindingBaseFragment<FragmentSignUpAuthBinding>(),
    IView<SignUpAuthIntent, SignUpAuthState, SignUpAuthEffect> {

    override val layoutId: Int = R.layout.fragment_sign_up_auth
    private val viewModel: SignUpAuthViewModel by viewModels()
    private lateinit var progressDialog: SweetAlertDialog

    companion object {
        fun newInstance() = SignUpAuthFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = requireActivity().getDefaultProgressDialog()
        setObservers()
        setListeners()
        setViews()
    }

    private fun setViews() {
        val spannable = SpannableString(requireActivity().applicationContext.getString(R.string.terms_and_conditions_of_semaai))
        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.background_orange)),
            47, // start
            67, // end
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        binding.tvTermsAndConditions.text = spannable
        binding.etPhoneNumber.requestFocus()
    }

    private fun setListeners() {
        binding.etPhoneNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                triggerIntent(SignUpAuthIntent.Verify(s.toString()))
            }
        })

        binding.btnContinue.setOnClickListener {
            triggerIntent(SignUpAuthIntent.Continue(binding.etPhoneNumber.text.toString()))
        }

        binding.tvLogin.setOnClickListener { triggerIntent(SignUpAuthIntent.Login) }

        binding.tvTermsAndConditions.setOnClickListener {
            triggerIntent(SignUpAuthIntent.ViewMarketPlaceTnC)
        }
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

    override fun render(state: SignUpAuthState) {
        when (state) {
            is SignUpAuthState.Loading -> progressDialog.show()
            is SignUpAuthState.Idle -> {
            }
            is SignUpAuthState.Error -> {
                progressDialog.dismiss()
                if(state.failureStatus == FailureStatus.NO_INTERNET){
                    showSnackbarMessage(getString(R.string.no_internet))
                }else if(state.failureStatus == FailureStatus.API_FAIL){
                    showSnackbarMessage(getString(R.string.error_something_went_wrong))
                }else{
                    triggerIntent(SignUpAuthIntent.NewUser(binding.etPhoneNumber.text.toString()))
                }
            }
            is SignUpAuthState.IncorrectPhoneNumberFormat -> setError(VerifyPhoneNumberValidation.INCORRECT_PHONE_NUMBER_FORMAT)
            is SignUpAuthState.IncorrectPhoneNumber -> setError(VerifyPhoneNumberValidation.INCORRECT_PHONE_NUMBER)
            is SignUpAuthState.EmptyPhoneNumber -> setEmptyPhoneNumber()
            is SignUpAuthState.ValidPhoneNumber -> validatedNumber()
            is SignUpAuthState.ExistingUser -> {
                progressDialog.dismiss()
                setResponseError(getString(R.string.existing_user_please_login))
            }
            is SignUpAuthState.MarketPlaceTnCSuccess -> {
                progressDialog.dismiss()
                showMarketPlaceTnC(state.termAndConditionResponse)
            }
        }
    }

    override fun render(effect: SignUpAuthEffect) {
        when (effect) {
            is SignUpAuthEffect.NavigateToLogin -> redirectToSignInSignUpFragment()
            is SignUpAuthEffect.NavigateToSignUpAuthOptions -> navigateToSignUpOptionsScreen(effect.phoneNumber)
        }
    }

    override fun triggerIntent(intent: SignUpAuthIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        setCursorVisibility(binding.etPhoneNumber, true)
    }

    override fun onPause() {
        super.onPause()
        //TODO handle with MVI
        setCursorVisibility(binding.etPhoneNumber, false)
    }

    private fun setCursorVisibility(editText: EditText, isCursorVisible: Boolean) {
        editText.isCursorVisible = isCursorVisible
    }

    private fun setError(verifyPhoneNumberValidation: VerifyPhoneNumberValidation) {
        when (verifyPhoneNumberValidation) {
            VerifyPhoneNumberValidation.INCORRECT_PHONE_NUMBER_FORMAT -> binding.tvError.text =
                requireActivity().applicationContext.getString(R.string.incorrect_mobile_number_format)
            VerifyPhoneNumberValidation.INCORRECT_PHONE_NUMBER -> binding.tvError.text =
                requireActivity().applicationContext.getString(R.string.incorrect_mobile_number)
        }
        binding.tvError.makeVisible()
        binding.btnContinue.isEnabled = false
    }

    private fun setResponseError(error: String) {
        binding.tvError.text = error
        binding.tvError.visibility = View.VISIBLE
    }

    private fun setEmptyPhoneNumber() {
        binding.tvError.text = ""
        binding.btnContinue.isEnabled = false
        binding.tvError.makeGone()
    }

    private fun validatedNumber() {
        binding.tvError.text = ""
        binding.btnContinue.isEnabled = true
        binding.tvError.makeGone()
    }

    private fun navigateToSignUpOptionsScreen(phoneNumber: String) {
        requireActivity().supportFragmentManager.beginTransaction()
            .add(
                R.id.fl_auth_container,
                SignUpOptionsFragment.newInstance(phoneNumber),
                SignUpOptionsFragment::class.java.simpleName
            ).addToBackStack(SignUpOptionsFragment::class.java.simpleName).commit()
        Helper.hideKeyboard(requireActivity())
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

    private fun showMarketPlaceTnC(termAndConditionResponse: TermsAndConditionsEntity) {
        val addedLayout = LinearLayout(requireContext())
        addedLayout.orientation = LinearLayout.VERTICAL
        val myWebView = WebView(requireContext())

        Helper.enableDarkModeInWebView(requireContext(), myWebView)
        myWebView.loadDataWithBaseURL(
            "",
            if (TextUtils.isEmpty(termAndConditionResponse.termsAndConditions)) requireActivity().getString(
                R.string.no_terms_and_conditions_to_display
            ) else termAndConditionResponse.termsAndConditions,
            ApplicationConstant.MIME,
            ApplicationConstant.ENCODING,
            ""
        )
        addedLayout.addView(myWebView)
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setView(addedLayout)
        dialog.setCancelable(true)
        dialog.show()
    }
}