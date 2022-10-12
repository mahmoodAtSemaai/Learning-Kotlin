package com.webkul.mobikul.odoo.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity
import com.webkul.mobikul.odoo.constant.ApplicationConstant
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseActivity
import com.webkul.mobikul.odoo.databinding.ActivityOnboardingBinding
import com.webkul.mobikul.odoo.data.entity.OnboardingData
import com.webkul.mobikul.odoo.ui.onboarding.effect.OnBoardingEffect
import com.webkul.mobikul.odoo.ui.onboarding.intent.OnboardingIntent
import com.webkul.mobikul.odoo.ui.onboarding.state.OnboardingState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingActivity @Inject constructor() :
        BindingBaseActivity<ActivityOnboardingBinding>(),
        IView<OnboardingIntent, OnboardingState, OnBoardingEffect> {

    private val viewModel: OnboardingViewModel by viewModels()


    override val contentViewId: Int
        get() = R.layout.activity_onboarding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
        setListeners()
        triggerIntent(OnboardingIntent.GetOnboardingData)
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

    private fun setListeners() {
        binding.ivNextSlide.setOnClickListener {
            triggerIntent(OnboardingIntent.NextSlider)
        }
        binding.tvSkipSlides.setOnClickListener {
            triggerIntent(OnboardingIntent.SkipSliders)
        }
    }


    override fun triggerIntent(intent: OnboardingIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }


    override fun render(state: OnboardingState) {
        when (state) {
            is OnboardingState.Error -> {}
            is OnboardingState.Idle -> {}
            is OnboardingState.Onboarding -> setupViewPager(state.onboardingData)
            is OnboardingState.CountDownTimer -> slidePager()
            is OnboardingState.CountDownTimerFinish -> triggerIntent(OnboardingIntent.redirectToSignInSignUpScreen)
            is OnboardingState.NextSlide -> slidePager()
            is OnboardingState.SkipSlides -> triggerIntent(OnboardingIntent.redirectToSignInSignUpScreen)
        }
    }

    override fun render(effect: OnBoardingEffect) {
        when(effect){
            is OnBoardingEffect.NavigateToSignInSignUpScreen -> redirectToSignInSignUpActivity()
        }
    }

    private fun slidePager() {
        if (binding.vpOnboarding.currentItem == 4) {
            triggerIntent(OnboardingIntent.redirectToSignInSignUpScreen)
        } else {
            binding.vpOnboarding.currentItem += 1
            triggerIntent(OnboardingIntent.SetIdle)
        }
    }

    private fun setupViewPager(onboardingData: MutableList<OnboardingData>) {
        binding.vpOnboarding.adapter =
                OnboardingPagerAdapter(this@OnboardingActivity, onboardingData)
        binding.bannerDotsTabLayout.setupWithViewPager(binding.vpOnboarding, true)
        triggerIntent(OnboardingIntent.StartTimer(2 * ApplicationConstant.SECONDS_IN_A_MINUTE))
        binding.tvSkipSlides.text = applicationContext.getString(R.string.skip)
    }


    private fun redirectToSignInSignUpActivity() {
        startActivity(Intent(this, SignInSignUpActivity::class.java))
        finishAffinity()
    }

}