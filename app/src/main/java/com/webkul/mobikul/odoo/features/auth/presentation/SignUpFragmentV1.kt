package com.webkul.mobikul.odoo.features.auth.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.snackbar.Snackbar
import com.webkul.mobikul.odoo.BuildConfig
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.extension.getDefaultProgressDialog
import com.webkul.mobikul.odoo.core.extension.showDefaultWarningDialogWithDismissListener
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.databinding.FragmentSignUpV1Binding
import com.webkul.mobikul.odoo.features.auth.data.models.SignUpData
import com.webkul.mobikul.odoo.features.auth.domain.enums.SignUpFieldsValidation
import com.webkul.mobikul.odoo.helper.AppSharedPref
import com.webkul.mobikul.odoo.helper.SnackbarHelper
import com.webkul.mobikul.odoo.model.generic.CountryStateData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
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
        getCountryStateSpinnerData()
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
                setErrorToNull()
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
                    SignUpFieldsValidation.EMPTY_NAME.value -> setEmptyNameError()
                    SignUpFieldsValidation.EMPTY_PASSWORD.value -> setEmptyPasswordError()
                    SignUpFieldsValidation.INVALID_PASSWORD.value -> setInvalidPasswordError()
                    SignUpFieldsValidation.UNEQUAL_PASS_AND_CONFIRM_PASS.value -> setInvalidConfirmPasswordError()
                    SignUpFieldsValidation.EMPTY_TERMS_CONDITIONS.value -> setTermsAndConditionsError()
                    SignUpFieldsValidation.EMPTY_PROFILE_URL.value -> setEmptyProfileUrlError()
                    SignUpFieldsValidation.EMPTY_COUNTRY.value -> setEmptyCountryError()

                }
            }

            is SignUpState.CountryStateDataSuccess -> setCountrySpinnerAdapter(state.countryStateData)


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

    private fun setCountrySpinnerAdapter(countryStateData: CountryStateData) {
        if (countryStateData.isAccessDenied) {

            requireContext().showDefaultWarningDialogWithDismissListener(getString(R.string.error_login_failure), getString(R.string.access_denied_message)) { sweetAlertDialog: SweetAlertDialog ->

                sweetAlertDialog.dismiss()
                AppSharedPref.clearCustomerData(context)
                val i = Intent(context, SignInSignUpActivity::class.java)
                i.putExtra(
                    BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY,
                    requireActivity().javaClass.simpleName
                )
                startActivity(i)
            }
        } else {

            binding.countrySpinner.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                countryStateData.getCountryNameList(requireContext())
            )

            binding.countrySpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View, countryPos: Int, id: Long) {
                        signUpData.country = countryStateData.countries[countryPos].id
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }

            binding.countrySpinner.setSelection(0)

        }
    }

    private fun getCountryStateSpinnerData() {
        triggerIntent(SignUpIntent.GetCountryStateData)
    }



    private fun onSignUpBtnClicked() {
        signUpData.phoneNumber = binding.emailEt.text.toString()
        signUpData.name = binding.nameEt.text.toString()
        signUpData.password = binding.passwordEt.text.toString()
        signUpData.confirmPassword = binding.confirmPasswordEt.text.toString()
        signUpData.profileURL = binding.profileUrlEt.text.toString()
        signUpData.isMarketPlaceTermAndCondition = binding.marketplaceTncCb.isChecked
        signUpData.isTermAndCondition = binding.termsAndConditionsCb.isChecked

        triggerIntent(SignUpIntent.SignUp(signUpData))
    }

    private fun showIsSeller(visible: Int) {
        binding.countryLayout.visibility = visible
        binding.urlLayout.visibility = visible
        binding.marketplaceTncLayout.visibility = visible
    }

    private fun setEmptyUsernameError() {
        binding.emailLayout.error =
            String.format(
                "%s %s",
                getString(R.string.phone_number),
                getString(R.string.error_is_required)
            )
    }

    private fun setEmptyPasswordError() {
        binding.passwordLayout.error = String.format(
            "%s %s",
            getString(R.string.password),
            getString(R.string.error_is_required)
        )

    }

    private fun setInvalidPasswordError() {
        binding.passwordLayout.error = String.format(
            "%s %s", getString(R.string.password), String.format(
                Locale.getDefault(),
                getString(R.string.error_password_length_x),
                BuildConfig.MIN_PASSWORD_LENGTH
            )
        )

    }

    private fun setEmptyNameError() {
        binding.nameLayout.error =
            getString(R.string.your_name).toString() + " " + getString(R.string.error_is_required)
    }

    private fun setInvalidConfirmPasswordError() {
        binding.confirmPasswordLayout.error = getString(R.string.error_password_not_match)
    }

    private fun setEmptyProfileUrlError() {
        binding.profileUrlLayout.error = String.format(
            "%s %s",
            getString(R.string.profile_url),
            getString(R.string.error_is_required)
        )
    }

    private fun setTermsAndConditionsError() {
        SnackbarHelper.getSnackbar(
            requireActivity(),
            getString(R.string.plese_accept_tnc),
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun setInvalidSignUpDetailsError() {
        SnackbarHelper.getSnackbar(
            requireActivity(),
            getString(R.string.error_enter_valid_login_details),
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun setEmptyCountryError() {
        SnackbarHelper.getSnackbar(
            requireActivity(),
            getString(R.string.error_enter_valid_login_details),
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun setErrorToNull() {
        binding.emailLayout.isErrorEnabled = false
        binding.passwordLayout.isErrorEnabled = false
        binding.confirmPasswordLayout.isErrorEnabled = false
        binding.nameLayout.isErrorEnabled = false
        binding.profileUrlLayout.isErrorEnabled = false
    }

}