package com.webkul.mobikul.odoo.core.platform

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.constant.PageConstants.Companion.DEFAULT_PAGE
import com.webkul.mobikul.odoo.core.extension.inTransaction
import com.webkul.mobikul.odoo.core.utils.ERROR_INTERNET_CONNECTION
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.helper.SnackbarHelper

abstract class BaseActivity : AppCompatActivity() {
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

    fun showErrorState(failureStatus: FailureStatus, message: String?) {
        val showMessage = message ?: getString(R.string.error_something_went_wrong)
        when (failureStatus) {
            FailureStatus.API_FAIL -> showSnackbarMessage(showMessage)
            FailureStatus.EMPTY -> showSnackbarMessage(getString(R.string.error_something_went_wrong))
            FailureStatus.NO_INTERNET -> showSnackbarMessage(ERROR_INTERNET_CONNECTION)
            FailureStatus.OTHER -> showSnackbarMessage(showMessage)
        }
    }

}