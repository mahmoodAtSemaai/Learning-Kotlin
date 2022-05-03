package com.webkul.mobikul.odoo.features.auth.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.extension.getDefaultProgressDialog
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.databinding.FragmentSignUpV1Binding
import com.webkul.mobikul.odoo.features.auth.domain.enums.SignUpFieldsValidation
import com.webkul.mobikul.odoo.helper.AppSharedPref
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragmentV1 @Inject constructor() : BindingBaseFragment<FragmentSignUpV1Binding>(),
    IView<SignUpIntent, SignUpState> {

    override val layoutId: Int = R.layout.fragment_sign_up_v1
    private val viewModel: SignUpViewModel by viewModels()
    private lateinit var progressDialog: SweetAlertDialog
    private lateinit var signUpData: SignUpData

    companion object {
        fun newInstance() = SignUpFragmentV1().also { signUpFragment ->
            val bundle = Bundle()
            signUpFragment.arguments = bundle
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = requireActivity().getDefaultProgressDialog()
        signUpData = SignUpData()

        setObservers()
        setOnClickListeners()
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.state.collect {
                render(it)
            }
        }
    }

    override fun render(state: SignUpState) {
        when (state) {
            is SignUpState.Loading -> {
                progressDialog.show()
            }

            is SignUpState.Error -> {
                progressDialog.dismiss()
                val error = state.error
            }

            is SignUpState.SignUp -> {
                progressDialog.dismiss()
                // startActivity(Intent())
            }

            is SignUpState.InvalidSignUpDetailsError -> {
                progressDialog.dismiss()
                when (state.invalidDetails.value) {
                    SignUpFieldsValidation.EMPTY_PHONE_NO.value -> setEmptyUsernameError()
                    SignUpFieldsValidation.EMPTY_PASSWORD.value -> setEmptyPasswordError()
                    SignUpFieldsValidation.INVALID_PASSWORD.value -> setInvalidPasswordError()
                }
            }

            is SignUpState.Idle -> {}

            is SignUpState.IsSeller -> showIsSeller(if (state.isSeller) View.VISIBLE else View.GONE)
        }
    }


    override fun triggerIntent(intent: SignUpIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }


    private fun setOnClickListeners() {

        binding.isSellerCb.setOnCheckedChangeListener { view, isChecked ->
            signUpData.isSeller = isChecked
            triggerIntent(SignUpIntent.IsSeller(isChecked))
        }

        binding.signUpBtn.setOnClickListener {
            onSignUpBtnClicked()
        }

    }

    private fun onSignUpBtnClicked() {
        signUpData.phoneNumber = binding.emailEt.text.toString()
        signUpData.password = binding.passwordEt.text.toString()
        signUpData.confirmPassword = binding.confirmPasswordEt.text.toString()
        signUpData.isSeller = binding.isSellerCb.isChecked
        signUpData.phoneNumber = binding.emailEt.text.toString()
        signUpData.country = binding.countrySpinner.selectedItem
        signUpData.profileURL = binding.profileUrlEt.text.toString()

        triggerIntent(SignUpIntent.SignUp(signUpData))
    }

    private fun showIsSeller(visible: Int) {
        binding.countryLayout.visibility = visible
        binding.urlLayout.visibility = visible
        binding.marketplaceTncLayout.visibility = visible

        if (AppSharedPref.isTermAndCondition(requireContext()))
            binding.marketplaceTncLayout.visibility = View.VISIBLE
        else
            binding.marketplaceTncLayout.visibility = View.GONE

    }


    private fun setEmptyPasswordError() {

    }

    private fun setInvalidPasswordError() {

    }

    private fun setEmptyUsernameError() {
        binding.emailEt.error = "Required"
    }


}