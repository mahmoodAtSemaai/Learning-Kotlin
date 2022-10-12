package com.webkul.mobikul.odoo.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.NewHomeActivity
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity
import com.webkul.mobikul.odoo.activity.SplashScreenActivity
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.extension.isGone
import com.webkul.mobikul.odoo.core.extension.makeGone
import com.webkul.mobikul.odoo.core.extension.makeVisible
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseActivity
import com.webkul.mobikul.odoo.core.utils.DeeplinkManager
import com.webkul.mobikul.odoo.databinding.ActivitySplashScreenV1Binding
import com.webkul.mobikul.odoo.ui.onboarding.OnboardingActivity
import com.webkul.mobikul.odoo.ui.signUpOnboarding.UserOnboardingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreenActivityV1() :
        BindingBaseActivity<ActivitySplashScreenV1Binding>(), IView<SplashIntent, SplashState, SplashEffect> {

    private val viewModel: SplashViewModel by viewModels()
    override val contentViewId: Int = R.layout.activity_splash_screen_v1

    @Inject
    lateinit var deeplinkManager: DeeplinkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
        setLanguage()
        getConfigs()
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

    override fun render(state: SplashState) {
        when (state) {
            is SplashState.Loading -> binding.pbMain.makeVisible()
            is SplashState.Idle -> binding.pbMain.makeGone()
            is SplashState.ConfigurationReceived -> checkLogin()
            is SplashState.UserUnAuthenticated -> onUnauthenticatedUser()
            is SplashState.UserLoggedIn -> initUserAnalytics()
            is SplashState.InitiatedUserAnalytics -> onUserAnalytics()
            is SplashState.NotificationIntent -> handleDeepLink()
            is SplashState.SplashIntent -> {
                triggerIntent(SplashIntent.CreateChatChannel)
                startInitSplashData()
            }
            is SplashState.Error -> {
                binding.pbMain.isGone()
                showErrorState(
                        state.failureStatus,
                        state.message ?: getString(R.string.error_something_went_wrong)
                )
            }
        }
    }

    override fun render(effect: SplashEffect) {
        when (effect) {
            is SplashEffect.ExecuteDeepLink -> executeDeepLink()
            is SplashEffect.NavigateToHomeScreen -> navigateToHomeScreen()
            is SplashEffect.NavigateToOnBoardingActivity -> navigateToOnBoardingActivity()
            is SplashEffect.NavigateToSignInSignUpActivity -> navigateToSignInSignUpActivity()
        }
    }

    private fun setLanguage() = localeManager.setLocale(false, this)

    private fun getConfigs() = triggerIntent(SplashIntent.GetConfigs)

    private fun checkLogin() = triggerIntent(SplashIntent.CheckUserLoggedIn)

    private fun handleDeepLink() {
        val notificationIntent = deeplinkManager.getNotificationIntent(this, intent)
        if (notificationIntent == null) {
            triggerIntent(SplashIntent.StartInit)
        } else {
            triggerIntent(SplashIntent.DeeplinkSplashPageData)
        }
    }

    private fun executeDeepLink() {
        val notificationIntent = deeplinkManager.getNotificationIntent(this, intent)
        deeplinkManager.handleDeeplink(this, notificationIntent)
        finish()
    }

    private fun startInitSplashData() = triggerIntent(SplashIntent.StartInit)

    private fun onUnauthenticatedUser() = triggerIntent(SplashIntent.CheckFirstTimeUser)

    private fun initUserAnalytics() = triggerIntent(SplashIntent.StartInitUserAnalytics)

    private fun onUserAnalytics() = triggerIntent(SplashIntent.CheckIntent(intent))

    private fun navigateToOnBoardingActivity() {
        startActivity(Intent(this, OnboardingActivity::class.java))
        finish()
    }

    private fun navigateToUserOnboardingScreen() {
        startActivity(Intent(this, UserOnboardingActivity::class.java))
        finish()
    }

    private fun navigateToSignInSignUpActivity() {
        Intent(this, SignInSignUpActivity::class.java).apply {
            startActivity(this)
        }
        finish()
    }

    private fun navigateToHomeScreen() {
        Intent(this, NewHomeActivity::class.java).apply {
            startActivity(this)
        }
        finish()
    }

    override fun triggerIntent(intent: SplashIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }
}