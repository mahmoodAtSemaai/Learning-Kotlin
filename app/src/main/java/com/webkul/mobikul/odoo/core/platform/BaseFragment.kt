package com.webkul.mobikul.odoo.core.platform

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.snackbar.Snackbar
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.NewHomeActivity
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.constant.PageConstants.Companion.DEFAULT_PAGE
import com.webkul.mobikul.odoo.core.extension.getDefaultProgressDialog
import com.webkul.mobikul.odoo.core.extension.showDefaultWarningDialog
import com.webkul.mobikul.odoo.core.utils.ERROR_INTERNET_CONNECTION
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.helper.SnackbarHelper
import com.webkul.mobikul.odoo.ui.signUpOnboarding.UserOnboardingActivity

abstract class BaseFragment : Fragment() {
    abstract val layoutId: Int

    private lateinit var progressDialog: SweetAlertDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = requireContext().getDefaultProgressDialog()
    }

    open fun setupViews() {}

    open fun getPageName(): String {
        return DEFAULT_PAGE
    }

    fun showProgressDialog() {
        progressDialog.show()
    }

    fun dismissProgressDialog() {
        progressDialog.dismiss()
    }

    fun showSnackbarMessage(message: String) {
        SnackbarHelper.getSnackbar(
            requireActivity(),
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun showErrorDialog(title: String?, message: String?) {
        requireContext().showDefaultWarningDialog(
            title,
            message
        )
    }

    fun showErrorState(failureStatus: FailureStatus, message: String?) {
        val showMessage = message ?: getString(R.string.error_something_went_wrong)
        when (failureStatus) {
            FailureStatus.API_FAIL -> showSnackbarMessage(showMessage)
            FailureStatus.EMPTY -> showSnackbarMessage(getString(R.string.error_something_went_wrong))
            FailureStatus.NO_INTERNET -> showSnackbarMessage(ERROR_INTERNET_CONNECTION)
            FailureStatus.OTHER -> showSnackbarMessage(showMessage)
            FailureStatus.ACCESS_DENIED -> navigateToSignInSignUpActivity()
            FailureStatus.USER_UNAPPROVED -> navigateToHomeScreen()
            FailureStatus.INCOMPLETE_ONBOARDING -> navigateToUserOnboardingScreen()
        }
    }

    fun showErrorDialogState(title: String, message: String) {
        showErrorDialog(title, message)
    }

    private fun navigateToSignInSignUpActivity() {
        startActivity(
            Intent(requireActivity(), SignInSignUpActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .putExtra(
                    BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY,
                    requireActivity()::class.java.simpleName
                )
        )
        requireActivity().finish()
    }

    private fun navigateToUserOnboardingScreen() {
        startActivity(
            Intent(
                requireActivity(),
                UserOnboardingActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        )
        requireActivity().finish()
    }

    private fun navigateToHomeScreen() {
        startActivity(
            Intent(
                requireActivity(),
                NewHomeActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
    }

}