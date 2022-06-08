package com.webkul.mobikul.odoo.features.authentication.presentation

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.extension.inTransaction
import com.webkul.mobikul.odoo.core.platform.BindingBaseActivity
import com.webkul.mobikul.odoo.databinding.ActivityAuthenticationBinding
import com.webkul.mobikul.odoo.features.authentication.presentation.fragment.AuthenticationFragment
import com.webkul.mobikul.odoo.features.authentication.presentation.fragment.LoginOptionsFragment
import com.webkul.mobikul.odoo.features.authentication.presentation.fragment.LoginOtpFragment
import com.webkul.mobikul.odoo.features.authentication.presentation.fragment.LoginPasswordFragment
import com.webkul.mobikul.odoo.helper.AppSharedPref
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthenticationActivity @Inject constructor() :
    BindingBaseActivity<ActivityAuthenticationBinding>() {

    override val contentViewId: Int
        get() = R.layout.activity_authentication

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBackStackChangedListener()
        setUpPhoneNumberScreen()
    }

    private fun setBackStackChangedListener() {
        supportFragmentManager.addOnBackStackChangedListener {
            val currentFragment = supportFragmentManager.fragments.last()

            if (currentFragment is AuthenticationFragment) {
                setToolbarBackButtonVisibility(false)
                setToolbarTitle("")
            }

            if (currentFragment is LoginOptionsFragment) {
                setToolbarBackButtonVisibility(true)
                setToolbarTitle("")
            }

            if (currentFragment is LoginOtpFragment) {
                setToolbarBackButtonVisibility(true)
                setToolbarTitle(getString(R.string.otp_code))
            }

            if (currentFragment is LoginPasswordFragment) {
                setToolbarBackButtonVisibility(true)
                setToolbarTitle(getString(R.string.enter_password))
            }
        };
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setToolbar() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.background_appbar_color)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false);
        setToolbarBackButtonVisibility(false)
        setToolbarTitle("")
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setUpPhoneNumberScreen() {
        supportFragmentManager.inTransaction {
            add(
                R.id.fragment_container_authentication,
                AuthenticationFragment.newInstance(),
                AuthenticationFragment::class.java.simpleName
            )
        }
        setToolbar()
    }

    private fun setToolbarBackButtonVisibility(isVisible: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(isVisible);
        supportActionBar?.setDisplayShowHomeEnabled(isVisible);
        supportActionBar?.setHomeButtonEnabled(true)
    }

    private fun setToolbarTitle(title: String) {
        binding.tvToolBar.text = title
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}