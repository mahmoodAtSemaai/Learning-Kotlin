package com.webkul.mobikul.odoo.features.onboarding.presentation

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.BaseActivity
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity
import com.webkul.mobikul.odoo.constant.ApplicationConstant
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseActivity
import com.webkul.mobikul.odoo.databinding.ActivityOnboardingBinding
import com.webkul.mobikul.odoo.features.onboarding.data.models.OnboardingData
import com.webkul.mobikul.odoo.features.onboarding.presentation.effect.OnBoardingEffect
import com.webkul.mobikul.odoo.features.onboarding.presentation.intent.OnboardingIntent
import com.webkul.mobikul.odoo.features.onboarding.presentation.state.OnboardingState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingActivity @Inject constructor() :
        BindingBaseActivity<ActivityOnboardingBinding>(),
        IView<OnboardingIntent, OnboardingState, OnBoardingEffect> {

    var isFirstTimeLaunched: Boolean = true
    private lateinit var viewModel: OnboardingViewModel


    override val contentViewId: Int
        get() = R.layout.activity_onboarding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[OnboardingViewModel::class.java]
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
            is OnboardingState.CountDownTimerFinish -> redirectToSignInSignUpActivity()
            is OnboardingState.NextSlide -> slidePager()
            is OnboardingState.SkipSlides -> redirectToSignInSignUpActivity()
        }
    }

    override fun render(effect: OnBoardingEffect) {
        //TODO("Not yet implemented")
    }

    private fun slidePager() {
        if (binding.vpOnboarding.currentItem == 4) {
            redirectToSignInSignUpActivity()
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