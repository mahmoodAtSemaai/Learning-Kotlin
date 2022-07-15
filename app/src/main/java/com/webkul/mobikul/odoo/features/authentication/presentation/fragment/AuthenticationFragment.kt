package com.webkul.mobikul.odoo.features.authentication.presentation.fragment

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.extension.getDefaultProgressDialog
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.core.utils.INTENT_SPLASH_SCREEN
import com.webkul.mobikul.odoo.databinding.FragmentAuthenticationBinding
import com.webkul.mobikul.odoo.features.authentication.domain.enums.VerifyPhoneNumberValidation
import com.webkul.mobikul.odoo.features.authentication.presentation.intent.AuthenticationIntent
import com.webkul.mobikul.odoo.features.authentication.presentation.effect.AuthenticationEffect
import com.webkul.mobikul.odoo.features.authentication.presentation.state.AuthenticationState
import com.webkul.mobikul.odoo.features.authentication.presentation.viewmodel.AuthenticationViewModel
import com.webkul.mobikul.odoo.helper.AppSharedPref
import com.webkul.mobikul.odoo.helper.Helper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AuthenticationFragment @Inject constructor() :
    BindingBaseFragment<FragmentAuthenticationBinding>(),
    IView<AuthenticationIntent, AuthenticationState, AuthenticationEffect> {

    override val layoutId: Int = R.layout.fragment_authentication
    private val viewModel: AuthenticationViewModel by viewModels()
    private lateinit var progressDialog: SweetAlertDialog

    companion object {
        fun newInstance() = AuthenticationFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = requireActivity().getDefaultProgressDialog()
        setObservers()
        setListeners()
        binding.etPhoneNumber.requestFocus()
        savePrivacyPolicy()
    }

    private fun savePrivacyPolicy() {
        triggerIntent(AuthenticationIntent.SavePrivacyPolicy)
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
        binding.etPhoneNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                triggerIntent(AuthenticationIntent.Verify(s.toString()))
            }
        })

        binding.btnConfirmNumber.setOnClickListener {
            triggerIntent(AuthenticationIntent.Continue(binding.etPhoneNumber.text.toString()))
        }

        binding.tvSignUp.setOnClickListener { triggerIntent(AuthenticationIntent.SignUp) }

        binding.tvPrivacyPolicy.setOnClickListener { triggerIntent(AuthenticationIntent.PrivacyPolicy) }
    }

    override fun render(state: AuthenticationState) {
        when (state) {
            is AuthenticationState.Idle -> {

            }
            is AuthenticationState.Loading -> {
                progressDialog.show()
            }
            is AuthenticationState.Error -> {
                progressDialog.dismiss()
                setResponseError(getString(R.string.error_user_does_not_exists))
            }

            is AuthenticationState.IncorrectPhoneNumberFormat -> {
                setError(VerifyPhoneNumberValidation.INCORRECT_PHONE_NUMBER_FORMAT)
            }

            is AuthenticationState.IncorrectPhoneNumber -> {
                setError(VerifyPhoneNumberValidation.INCORRECT_PHONE_NUMBER)
            }

            is AuthenticationState.EmptyPhoneNumber -> {
                setEmptyPhoneNumber()
            }

            is AuthenticationState.VerifiedPhoneNumber -> {
                navigateToLoginOptionsScreen(binding.etPhoneNumber.text.toString())
                progressDialog.dismiss()
            }

            is AuthenticationState.ContinuePhoneNumber -> {
                progressDialog.dismiss()
            }

            is AuthenticationState.SignUp -> {
                redirectToSignInSignUpFragment()
            }

            is AuthenticationState.ValidPhoneNumber -> {
                validatedNumber()
            }
            is AuthenticationState.PrivacyPolicy -> {
                showPrivacyPolicy()
            }
        }
    }

    override fun render(effect: AuthenticationEffect) {
        //TODO("Not yet implemented")
    }

    private fun showPrivacyPolicy() {
        val pm: PackageManager = requireContext().packageManager
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(AppSharedPref.getPrivacyURL(requireContext())))
        val intents: MutableList<Intent> = ArrayList()
        val list = pm.queryIntentActivities(intent, 0)

        for (info in list) {
            val viewIntent = Intent(intent)
            viewIntent.component = ComponentName(info.activityInfo.packageName, info.activityInfo.name)
            viewIntent.setPackage(info.activityInfo.packageName)
            intents.add(viewIntent)
        }

        for (cur in intents) {
            if (cur.component!!.className.equals(INTENT_SPLASH_SCREEN, ignoreCase = true)) {
                intents.remove(cur)
            }
        }

        intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents.toTypedArray())
        requireActivity().startActivity(intent)
    }

    private fun redirectToSignInSignUpFragment() {
        startActivity(
            Intent(requireContext(), SignInSignUpActivity::class.java)
                .putExtra(BundleConstant.BUNDLE_KEY_SIGNUP_SCREEN, BundleConstant.BUNDLE_KEY_SIGNUP_SCREEN)
        )
        requireActivity().finish()
    }

    private fun navigateToLoginOptionsScreen(phoneNumber: String) {
        requireActivity().supportFragmentManager.beginTransaction()
            .add(
                R.id.fragment_container_authentication,
                LoginOptionsFragment.newInstance(phoneNumber),
                LoginOptionsFragment::class.java.simpleName
            ).addToBackStack(LoginOptionsFragment::class.java.simpleName).commit()
        Helper.hideKeyboard(requireActivity())
    }

    private fun setError(verifyPhoneNumberValidation: VerifyPhoneNumberValidation) {
        when (verifyPhoneNumberValidation) {
            VerifyPhoneNumberValidation.INCORRECT_PHONE_NUMBER_FORMAT -> binding.tvError.text =
                getString(R.string.incorrect_mobile_number_format)
            VerifyPhoneNumberValidation.INCORRECT_PHONE_NUMBER -> binding.tvError.text =
                getString(R.string.incorrect_mobile_number)
        }
        binding.tvError.visibility = View.VISIBLE
        binding.btnConfirmNumber.isEnabled = false
        binding.btnConfirmNumber.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorDarkGrey))
    }

    private fun setResponseError(error: String) {
        binding.tvError.text = error
        binding.tvError.visibility = View.VISIBLE
    }

    private fun setEmptyPhoneNumber() {
        binding.tvError.text = ""
        binding.btnConfirmNumber.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorDarkGrey))
        binding.btnConfirmNumber.isEnabled = false
        binding.tvError.visibility = View.GONE
    }

    private fun validatedNumber() {
        binding.tvError.text = ""
        binding.btnConfirmNumber.isEnabled = true
        binding.btnConfirmNumber.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorPrimary))
        binding.tvError.visibility = View.GONE
    }

    override fun triggerIntent(intent: AuthenticationIntent) {
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
        setCursorVisibility(binding.etPhoneNumber, false)
    }

    private fun setCursorVisibility(editText: EditText, isCursorVisible: Boolean){
        editText.isCursorVisible = isCursorVisible
    }

}