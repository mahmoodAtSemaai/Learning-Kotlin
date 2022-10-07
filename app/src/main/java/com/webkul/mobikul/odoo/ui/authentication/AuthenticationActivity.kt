package com.webkul.mobikul.odoo.ui.authentication

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.extension.inTransaction
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseActivity
import com.webkul.mobikul.odoo.databinding.ActivityAuthenticationBinding
import com.webkul.mobikul.odoo.ui.authentication.viewmodel.AuthActivityViewModel
import com.webkul.mobikul.odoo.ui.authentication.effect.AuthActivityEffect
import com.webkul.mobikul.odoo.ui.authentication.fragment.AuthenticationFragment
import com.webkul.mobikul.odoo.ui.authentication.fragment.LoginOptionsFragment
import com.webkul.mobikul.odoo.ui.authentication.fragment.LoginOtpFragment
import com.webkul.mobikul.odoo.ui.authentication.fragment.LoginPasswordFragment
import com.webkul.mobikul.odoo.ui.authentication.intent.AuthActivityIntent
import com.webkul.mobikul.odoo.ui.authentication.state.AuthActivityState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AuthenticationActivity @Inject constructor() :
    BindingBaseActivity<ActivityAuthenticationBinding>(),
    IView<AuthActivityIntent, AuthActivityState, AuthActivityEffect> {

    private val viewModel: AuthActivityViewModel by viewModels()

    override val contentViewId: Int
        get() = R.layout.activity_authentication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
        setUpToolbar()
        setBackStack()
        setPhoneNumberScreen()
    }

    private fun setObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.state.collect {
                render(it)
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.effect.collect {
                render(it)
            }
        }
    }

    private fun setToolbar() {
        triggerIntent(AuthActivityIntent.InitToolBar)
    }

    private fun setBackStack() {
        triggerIntent(AuthActivityIntent.SetupFragmentBackStack)
    }

    private fun setPhoneNumberScreen() {
        triggerIntent(AuthActivityIntent.SetupPhoneNumberScreen)
    }

    override fun render(state: AuthActivityState) {
        when (state) {
            is AuthActivityState.Idle -> {}
            is AuthActivityState.InitToolBar -> setUpToolbar()
            is AuthActivityState.SetBackStackListener -> {
                setBackStackChangedListener()
            }
            is AuthActivityState.SetToolBarContent -> setToolBarContent(state.showBackButton, state.title)
        }
    }

    override fun render(effect: AuthActivityEffect) {
        when (effect) {
            is AuthActivityEffect.SetupPhoneScreen -> setUpPhoneNumberScreen()
        }
    }

    override fun triggerIntent(intent: AuthActivityIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }

    private fun setBackStackChangedListener() {
        supportFragmentManager.addOnBackStackChangedListener {
            val currentFragment = supportFragmentManager.fragments.last()
            if(currentFragment is AuthenticationFragment)
                triggerIntent(AuthActivityIntent.SetupToolbarContent("",false))
            if(currentFragment is LoginOptionsFragment)
                triggerIntent(AuthActivityIntent.SetupToolbarContent("",true))
            if(currentFragment is LoginOtpFragment)
                triggerIntent(AuthActivityIntent.SetupToolbarContent(getString(R.string.otp_code),true))
            if(currentFragment is LoginPasswordFragment)
                triggerIntent(AuthActivityIntent.SetupToolbarContent(getString(R.string.enter_password),true))
        }
    }

    private fun setUpPhoneNumberScreen() {
        supportFragmentManager.inTransaction {
            add(
                R.id.fragment_container_authentication,
                AuthenticationFragment.newInstance(),
                AuthenticationFragment::class.java.simpleName
            )
        }
    }

    private fun setToolBarContent(showBackButton: Boolean, title: String) {
        setToolbarTitle(title)
        if(showBackButton)
            showToolbarBackButton()
        else
            hideToolbarBackButton()
    }

    private fun setUpToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.background_appbar_color)
        }
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        hideToolbarBackButton()
        setToolbarTitle("")
    }

    private fun showToolbarBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    private fun hideToolbarBackButton(){
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    private fun setToolbarTitle(title: String) {
        binding.tvToolBar.text = title
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //TODO check if it can be handled via intent
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}