package com.webkul.mobikul.odoo.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.webkul.mobikul.odoo.*
import com.webkul.mobikul.odoo.activity.NewHomeActivity
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity
import com.webkul.mobikul.odoo.activity.SplashScreenActivity
import com.webkul.mobikul.odoo.activity.UserApprovalActivity
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.extension.isGone
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseActivity
import com.webkul.mobikul.odoo.core.utils.DeeplinkManager
import com.webkul.mobikul.odoo.databinding.ActivitySplashScreenV1Binding
import com.webkul.mobikul.odoo.features.onboarding.presentation.OnboardingActivity
import com.webkul.mobikul.odoo.model.home.HomePageResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_splash_screen_v1.*
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
        checkLogin()
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
            is SplashState.Loading -> pb_main.visibility = View.VISIBLE
            is SplashState.Idle -> pb_main.visibility = View.GONE
            is SplashState.UserUnAuthenticated -> onUnauthenticatedUser()
            is SplashState.UserLoggedIn -> initUserAnalytics()
            is SplashState.InitiatedUserAnalytics -> onUserAnalytics()
            is SplashState.NotificationIntent -> handleDeepLink()
            is SplashState.SplashIntent -> startInitSplashData()
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
            is SplashEffect.NavigateToHomeScreen -> navigateToHomeScreen(effect.homePageResponse)
            is SplashEffect.NavigateToOnBoardingActivity -> navigateToOnBoardingActivity()
            is SplashEffect.NavigateToSignInSignUpActivity -> navigateToSignInSignUpActivity()
            is SplashEffect.NavigateToUserApprovalActivity -> navigateToUserUnApprovedScreen()
        }
    }

    private fun setLanguage() = localeManager.setLocale(false, this)

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

    private fun navigateToUserUnApprovedScreen() {
        startActivity(Intent(this, UserApprovalActivity::class.java))
        finish()
    }

    private fun navigateToSignInSignUpActivity() {
        val intent = Intent(this, SignInSignUpActivity::class.java)
        intent.also {
            it.putExtra(
                    BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY,
                    SplashScreenActivity::class.java.simpleName
            )
            startActivity(it)
        }
        finish()
    }

    private fun navigateToHomeScreen(homePageResponse: HomePageResponse) {
        val intent = Intent(this, NewHomeActivity::class.java)
        intent.also {
            it.putExtra(BundleConstant.BUNDLE_KEY_HOME_PAGE_RESPONSE, homePageResponse)
            startActivity(it)
        }
        finish()
    }

    override fun triggerIntent(intent: SplashIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }
}