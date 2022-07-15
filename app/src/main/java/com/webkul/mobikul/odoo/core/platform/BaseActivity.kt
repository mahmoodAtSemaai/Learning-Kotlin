package com.webkul.mobikul.odoo.core.platform

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity
import com.webkul.mobikul.odoo.activity.SplashScreenActivity
import com.webkul.mobikul.odoo.activity.UserApprovalActivity
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.constant.PageConstants.Companion.DEFAULT_PAGE
import com.webkul.mobikul.odoo.core.extension.inTransaction
import com.webkul.mobikul.odoo.core.extension.showDefaultWarningDialog
import com.webkul.mobikul.odoo.core.utils.ERROR_INTERNET_CONNECTION
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.LocaleManager
import com.webkul.mobikul.odoo.helper.SnackbarHelper
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {
    @Inject
    lateinit var localeManager: LocaleManager
    abstract val contentViewId: Int

    open fun setupViews() {}

    open fun getPageName(): String {
        return DEFAULT_PAGE
    }

    fun showSnackbarMessage(message: String) {
        SnackbarHelper.getSnackbar(
                this,
                message,
                Snackbar.LENGTH_LONG
        ).show()
    }

    fun addFragment(containerViewId: Int, fragment: BaseFragment, tag: String) {
        supportFragmentManager.inTransaction {
            add(containerViewId, fragment, tag)
        }
    }

    fun addFragmentWithBackStack(containerViewId: Int, fragment: BaseFragment, tag: String) {
        supportFragmentManager.inTransaction {
            add(containerViewId, fragment, tag).addToBackStack(tag)
        }
    }

    fun replaceFragment(containerViewId: Int, fragment: BaseFragment, tag: String) {
        supportFragmentManager.inTransaction {
            replace(containerViewId, fragment).addToBackStack(tag)
        }
    }

    fun showErrorDialog(title: String?, message: String?) {
        showDefaultWarningDialog(
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
            FailureStatus.USER_UNAPPROVED -> navigateToUserUnApprovedScreen()
        }
    }

    private fun navigateToSignInSignUpActivity() {
        startActivity(Intent(this, SignInSignUpActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .putExtra(
                        BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY,
                        this::class.java.simpleName
                ))
        finish()
    }

    private fun navigateToUserUnApprovedScreen() {
        startActivity(Intent(this, UserApprovalActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }

}