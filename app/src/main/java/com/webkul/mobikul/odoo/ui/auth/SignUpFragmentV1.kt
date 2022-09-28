package com.webkul.mobikul.odoo.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.WebView
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.webkul.mobikul.odoo.BuildConfig
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.UpdateAddressActivity
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.extension.close
import com.webkul.mobikul.odoo.core.extension.showDefaultSuccessDialogWithDismissListener
import com.webkul.mobikul.odoo.core.extension.showDefaultWarningDialogWithDismissListener
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BaseActivity
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.data.entity.*
import com.webkul.mobikul.odoo.databinding.FragmentSignUpV1Binding
import com.webkul.mobikul.odoo.domain.enums.SignUpFieldsValidation
import com.webkul.mobikul.odoo.helper.AppSharedPref
import com.webkul.mobikul.odoo.helper.Helper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragmentV1 @Inject constructor() : BindingBaseFragment<FragmentSignUpV1Binding>(),
        IView<SignUpIntent, SignUpState, SignUpEffect> {

    override val layoutId: Int = R.layout.fragment_sign_up_v1
    private val viewModel: SignUpViewModel by viewModels()
    private lateinit var signUpData: SignUpData
    private lateinit var signUpEntity: SignUpEntity

    companion object {
        fun newInstance() = SignUpFragmentV1().also { signUpFragment ->
            val bundle = Bundle()
            signUpFragment.arguments = bundle
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signUpData = SignUpData()

        setObservers()
        setOnClickListeners()
        getCountryStateSpinnerData()
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

        binding.viewMarketplaceTnc.setOnClickListener {
            triggerIntent(SignUpIntent.ViewMarketPlaceTnC)
        }

        binding.viewTermsAndConditions.setOnClickListener {
            triggerIntent(SignUpIntent.ViewSignUpTnC)
        }

        binding.logInNow.setOnClickListener {
            triggerIntent(SignUpIntent.NavigateToLogin)
        }

        binding.toolbar.setNavigationOnClickListener {
            triggerIntent(SignUpIntent.CloseSignUpScreen)
        }
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

    override fun render(state: SignUpState) {
        when (state) {
            is SignUpState.Loading -> {
                showProgressDialog()
                setErrorToNull()
            }

            is SignUpState.Error -> {
                dismissProgressDialog()
                showErrorState(state.failureStatus, state.message)
            }

            is SignUpState.SignUp -> {
                signUpEntity = state.data
                showSignUpSuccessDialog()
            }

            is SignUpState.BillingAddressDataSuccess -> {
                dismissProgressDialog()
                navigateToHomeScreen(state.myAddressResponse)
            }

            is SignUpState.InvalidSignUpDetailsError -> {
                dismissProgressDialog()
                setInvalidSignUpDetailsError()
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

            is SignUpState.MarketPlaceTnCSuccess -> {
                dismissProgressDialog()
                showMarketPlaceTnC(state.termAndConditionResponse)
            }

            is SignUpState.SignUpTnCSuccess -> {
                dismissProgressDialog()
                showMarketPlaceTnC(state.termAndConditionResponse)
            }

            is SignUpState.CountryStateDataSuccess -> setCountrySpinnerAdapter(state.countryStateData)

            is SignUpState.Idle -> {
            }

            is SignUpState.IsSeller -> showIsSeller(if (state.isSeller) View.VISIBLE else View.GONE)

            is SignUpState.CloseSignUpScreen -> this.close()

            is SignUpState.NavigateToLogin -> navigateToLoginScreen()

            is SignUpState.AccessDenied -> showAccessDeniedDialog()
        }
    }

    override fun render(effect: SignUpEffect) {
        //TODO("Not yet implemented")
    }

    private fun getCountryStateSpinnerData() {
        triggerIntent(SignUpIntent.GetCountryStateData)
    }

    private fun getBillingAddress() {
        triggerIntent(SignUpIntent.GetBillingAddress)
    }

    private fun navigateToLoginScreen() {
        this.close()
        (requireActivity() as BaseActivity).addFragmentWithBackStack(
                R.id.fragment_container_v1,
                LoginFragmentV1.newInstance(),
                LoginFragmentV1::class.java.simpleName
        )
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

    private fun showMarketPlaceTnC(termAndConditionResponse: TermsAndConditionsEntity) {

        val addedLayout = LinearLayout(requireContext())
        addedLayout.orientation = LinearLayout.VERTICAL
        val myWebView = WebView(requireContext())

        Helper.enableDarkModeInWebView(requireContext(), myWebView)

        val mime = "text/html"
        val encoding = "utf-8"
        myWebView.loadDataWithBaseURL(
                "",
                if (TextUtils.isEmpty(termAndConditionResponse.termsAndConditions)) requireActivity().getString(
                        R.string.no_terms_and_conditions_to_display
                ) else termAndConditionResponse.termsAndConditions,
                mime,
                encoding,
                ""
        )
        addedLayout.addView(myWebView)
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setView(addedLayout)
        dialog.show()
    }


    private fun navigateToHomeScreen(myAddressesResponse: AddressEntity) {
        val billingAddressUrl = myAddressesResponse.addresses[0].url

        requireActivity().startActivity(
                Intent(
                        requireContext(),
                        UpdateAddressActivity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .putExtra(BundleConstant.BUNDLE_KEY_NAME, signUpData.name)
                        .putExtra(BundleConstant.BUNDLE_KEY_PHONE_NUMBER, signUpData.phoneNumber)
                        .putExtra(BundleConstant.BUNDLE_KEY_URL, billingAddressUrl)
        )
        requireActivity().finish()
    }

    private fun showAccessDeniedDialog() {
        requireContext().showDefaultWarningDialogWithDismissListener(
                getString(R.string.error_login_failure),
                getString(R.string.access_denied_message)
        ) { sweetAlertDialog: SweetAlertDialog ->

            sweetAlertDialog.dismiss()
            AppSharedPref.clearCustomerData(context)
            val i = Intent(context, SignInSignUpActivityV1::class.java)
            i.putExtra(
                    BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY,
                    requireActivity().javaClass.simpleName
            )
            startActivity(i)
        }
    }

    private fun setCountrySpinnerAdapter(countryStateData: CountryEntity) {
        binding.countrySpinner.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                countryStateData.getCountryNameList()
        )

        binding.countrySpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View,
                            countryPos: Int,
                            id: Long
                    ) {
                        signUpData.country = countryStateData.countries[countryPos].id
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
        binding.countrySpinner.setSelection(0)
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
                "${getString(R.string.your_name)} ${getString(R.string.error_is_required)}"
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
        showSnackbarMessage(getString(R.string.plese_accept_tnc))
    }

    private fun setInvalidSignUpDetailsError() {
        showSnackbarMessage(getString(R.string.error_enter_valid_login_details))
    }

    private fun setEmptyCountryError() {
        showSnackbarMessage(getString(R.string.error_enter_valid_login_details))
    }

    private fun setErrorToNull() {
        binding.emailLayout.isErrorEnabled = false
        binding.passwordLayout.isErrorEnabled = false
        binding.confirmPasswordLayout.isErrorEnabled = false
        binding.nameLayout.isErrorEnabled = false
        binding.profileUrlLayout.isErrorEnabled = false
    }

    private fun showSignUpSuccessDialog() {
        //TODO->Add string message for successful signup
        //As of now we are getting something went wrong as message
        //Once figure out we will remove 'message' from 'signUpEntity'
        requireContext().showDefaultSuccessDialogWithDismissListener(
                getString(R.string.account_created_successfully),
                signUpEntity.message,
                getString(R.string.continue_shopping)
        ) { sweetAlertDialog: SweetAlertDialog ->

            sweetAlertDialog.dismiss()
            getBillingAddress()
        }
    }

}