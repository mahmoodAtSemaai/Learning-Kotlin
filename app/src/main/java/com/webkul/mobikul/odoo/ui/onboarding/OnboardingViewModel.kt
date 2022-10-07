package com.webkul.mobikul.odoo.ui.onboarding

import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.domain.usecase.auth.SetIsFirstTimeUseCase
import com.webkul.mobikul.odoo.domain.usecase.onboarding.CountdownTimerUseCase
import com.webkul.mobikul.odoo.domain.usecase.onboarding.OnboardingUseCase
import com.webkul.mobikul.odoo.ui.onboarding.effect.OnBoardingEffect
import com.webkul.mobikul.odoo.ui.onboarding.intent.OnboardingIntent
import com.webkul.mobikul.odoo.ui.onboarding.state.OnboardingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingUseCase: OnboardingUseCase,
    private val countdownTimerUseCase: CountdownTimerUseCase,
    private val setIsFirstTimeUseCase: SetIsFirstTimeUseCase
) : BaseViewModel(), IModel<OnboardingState, OnboardingIntent, OnBoardingEffect> {

    override val intents: Channel<OnboardingIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableStateFlow<OnboardingState>(OnboardingState.Idle)
    override val state: StateFlow<OnboardingState>
        get() = _state

    private val _effect = Channel<OnBoardingEffect>()
    override val effect: Flow<OnBoardingEffect>
        get() = _effect.receiveAsFlow()

    private var countDownTimer: Job = Job()
    private val COUNTDOWN_TIMER_INTERVAL = 5_000L

    init {
        handlerIntent()
    }

    override fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect {
                when (it) {
                    is OnboardingIntent.GetOnboardingData -> {
                        setFirstTimeLaunch()
                        getOnboardingData()
                    }
                    is OnboardingIntent.NextSlider -> slide()
                    is OnboardingIntent.SkipSliders -> skipSliders()
                    is OnboardingIntent.StartTimer -> {
                        startTimer(it.time)
                    }
                    is OnboardingIntent.SetIdle -> {
                        setIdleState()
                    }
                    is OnboardingIntent.redirectToSignInSignUpScreen -> _effect.send(
                        OnBoardingEffect.NavigateToSignInSignUpScreen
                    )
                }
            }
        }
    }

    private fun setIdleState() {
        viewModelScope.launch {
            _state.value = OnboardingState.Idle
        }
    }

    private fun setFirstTimeLaunch() {
        viewModelScope.launch {
            setIsFirstTimeUseCase()
        }
    }

    private fun skipSliders() {
        viewModelScope.launch {
            _state.value = OnboardingState.SkipSlides
        }
    }


    private fun slide() {
        viewModelScope.launch {
            _state.value = OnboardingState.NextSlide
        }
    }

    private fun getOnboardingData() {
        viewModelScope.launch {
            val onboardingData = onboardingUseCase()
            _state.value = OnboardingState.Onboarding(onboardingData)
        }
    }

    private fun startTimer(time: Int) {
        countDownTimer = viewModelScope.launch {
            val timerData = countdownTimerUseCase(time, COUNTDOWN_TIMER_INTERVAL)

            timerData.collect {
                _state.value = OnboardingState.CountDownTimer(it)
                if (it == 0) {
                    _state.value = OnboardingState.CountDownTimerFinish
                }
            }
        }
    }

}