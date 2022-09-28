package com.webkul.mobikul.odoo.features.onboarding.presentation

import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.features.onboarding.domain.usecase.CountdownTimerUseCase
import com.webkul.mobikul.odoo.features.onboarding.domain.usecase.OnboardingUseCase
import com.webkul.mobikul.odoo.features.onboarding.presentation.effect.OnBoardingEffect
import com.webkul.mobikul.odoo.features.onboarding.presentation.intent.OnboardingIntent
import com.webkul.mobikul.odoo.features.onboarding.presentation.state.OnboardingState
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
        private val appPreferences: AppPreferences
) : BaseViewModel(), IModel<OnboardingState, OnboardingIntent, OnBoardingEffect> {

    override val intents: Channel<OnboardingIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableStateFlow<OnboardingState>(OnboardingState.Idle)
    override val state: StateFlow<OnboardingState>
        get() = _state

    private val _effect = Channel<OnBoardingEffect>()
    override val effect: Flow<OnBoardingEffect>
        get() = _effect.receiveAsFlow()

    private var countDownTimer: Job = Job()

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
        appPreferences.isFirstTime = false
        //TODO -> Handle preferences related stuff in data layer
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
            _state.value = try {
                val onboardingData = onboardingUseCase()
                var onboardingState: OnboardingState = OnboardingState.Idle

                onboardingData.collect {
                    onboardingState = when (it) {
                        is Resource.Default -> OnboardingState.Idle
                        is Resource.Failure -> OnboardingState.Error(it.message, it.failureStatus)
                        is Resource.Loading -> OnboardingState.Idle
                        is Resource.Success -> OnboardingState.Onboarding(it.value)
                    }
                }
                onboardingState
            } catch (e: Exception) {
                OnboardingState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun startTimer(time: Int) {
        countDownTimer = viewModelScope.launch {
            val timerData = countdownTimerUseCase(time, 5_000)

            timerData.collect {
                _state.value = OnboardingState.CountDownTimer(it)
                if (it == 0) {
                    _state.value = OnboardingState.CountDownTimerFinish
                }
            }
        }
    }

}