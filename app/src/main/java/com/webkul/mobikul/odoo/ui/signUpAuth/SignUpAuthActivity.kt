package com.webkul.mobikul.odoo.ui.signUpAuth

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.extension.inTransaction
import com.webkul.mobikul.odoo.core.extension.makeGone
import com.webkul.mobikul.odoo.core.extension.makeVisible
import com.webkul.mobikul.odoo.core.platform.BindingBaseActivity
import com.webkul.mobikul.odoo.databinding.ActivitySignUpAuthBinding
import com.webkul.mobikul.odoo.ui.signUpAuth.fragment.SignUpAuthFragment
import com.webkul.mobikul.odoo.ui.signUpAuth.fragment.SignUpOptionsFragment
import com.webkul.mobikul.odoo.ui.signUpAuth.fragment.SignUpOtpFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpAuthActivity  @Inject constructor() :
    BindingBaseActivity<ActivitySignUpAuthBinding>() {

    override val contentViewId: Int
        get() = R.layout.activity_sign_up_auth

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        localeManager.setLocale(false, this)
        setBackStackChangedListener()
        setUpPhoneNumberScreen()
    }

    private fun setBackStackChangedListener() {
        supportFragmentManager.addOnBackStackChangedListener {
            val currentFragment = supportFragmentManager.fragments.last()

            if (currentFragment is SignUpAuthFragment) {
                setToolbarBackButtonVisibility(false)
                setToolbarTitle("")
                binding.appBarLayout.makeGone()
            }

            if (currentFragment is SignUpOptionsFragment) {
                setToolbarBackButtonVisibility(true)
                setToolbarTitle("")
                binding.appBarLayout.makeVisible()
            }

            if (currentFragment is SignUpOtpFragment) {
                setToolbarBackButtonVisibility(true)
                setToolbarTitle(getString(R.string.otp_code))
                binding.appBarLayout.makeVisible()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setToolbar() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.background_appbar_color)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setToolbarBackButtonVisibility(false)
        setToolbarTitle("")
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setUpPhoneNumberScreen() {
        binding.appBarLayout.makeGone()
        supportFragmentManager.inTransaction {
            add(
                R.id.fl_auth_container,
                SignUpAuthFragment.newInstance(),
                SignUpAuthFragment::class.java.simpleName
            )
        }
        setToolbar()
    }

    private fun setToolbarBackButtonVisibility(isVisible: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(isVisible)
        supportActionBar?.setDisplayShowHomeEnabled(isVisible)
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