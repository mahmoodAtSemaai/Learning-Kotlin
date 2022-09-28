package com.webkul.mobikul.odoo.ui.signUpOnboarding.viewModel

import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import com.webkul.mobikul.odoo.domain.enums.ExistingUserValidation
import com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding.*
import com.webkul.mobikul.odoo.domain.usecase.splash.SplashUseCase
import com.webkul.mobikul.odoo.ui.signUpOnboarding.effect.UserOnboardingEffect
import com.webkul.mobikul.odoo.ui.signUpOnboarding.intent.UserOnboardingIntent
import com.webkul.mobikul.odoo.ui.signUpOnboarding.state.UserOnboardingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserOnboardingViewModel @Inject constructor(
    private val onboardingStageUseCase: OnboardingStageUseCase,
    private val userOnboardingStageUseCase: UserOnboardingStageUseCase,
    private val getOnboardingDataUseCase: GetOnboardingDataUseCase,
    private val incompleteStagesUseCase: IncompleteStagesUseCase,
    private val nextOnboardingStageUseCase: NextOnboardingStageUseCase,
    private val splashUseCase: SplashUseCase,
    private val resourcesProvider: ResourcesProvider
) :
    BaseViewModel(), IModel<UserOnboardingState, UserOnboardingIntent, UserOnboardingEffect> {

    override val intents: Channel<UserOnboardingIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableStateFlow<UserOnboardingState>(UserOnboardingState.Idle)
    override val state: StateFlow<UserOnboardingState>
        get() = _state

    private val _effect = Channel<UserOnboardingEffect>()
    override val effect: Flow<UserOnboardingEffect>
        get() = _effect.receiveAsFlow()

    private var userId = ""
    private var customerId = ""
    private var completedStageIds = emptyList<Int>()
    private var allStageInfo = LinkedHashMap<Int, String>()
    private var incompleteStageInfo = LinkedHashMap<Int, String>()

    init {
        handlerIntent()
    }

    override fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect {
                when (it) {
                    is UserOnboardingIntent.SetToolbar -> setToolbar()
                    is UserOnboardingIntent.GetUserIdCustomerId -> getUserIdCustomerId()
                    is UserOnboardingIntent.GetUserOnboardingStage -> getUserOnboardingState()
                    is UserOnboardingIntent.GetHomeData -> initSplash()
                    is UserOnboardingIntent.GetOnboardingStage -> getOnboardingStages()
                    is UserOnboardingIntent.GetIncompleteStage -> getIncompleteStages()
                    is UserOnboardingIntent.StageCompleted -> stageCompleted(it.stageName)
                    is UserOnboardingIntent.LaunchNextStage -> launchNextStage()
                    is UserOnboardingIntent.Refresh -> refreshActivity()
                }
            }
        }
    }

    private suspend fun refreshActivity() {
        _effect.send(UserOnboardingEffect.RelaunchActivity)
    }

    private suspend fun launchNextStage() {
        val value = nextOnboardingStageUseCase(incompleteStageInfo)
        //TODO Optimize comparisions
        if (value.equals(resourcesProvider.getString(R.string.group_selection_stage))) {
            _state.value = UserOnboardingState.Idle
            _effect.send(UserOnboardingEffect.NavigateToCustomerGroup)
        } else if (value.equals(resourcesProvider.getString(R.string.details_stage))) {
            _state.value = UserOnboardingState.Idle
            _effect.send(UserOnboardingEffect.NavigateToDetailsScreen)
        } else if (value.equals(resourcesProvider.getString(R.string.address_stage))) {
            _state.value = UserOnboardingState.Idle
            _effect.send(UserOnboardingEffect.NavigateToAddressScreen(true))
        } else if (value.equals(resourcesProvider.getString(R.string.location_stage))) {
            _state.value = UserOnboardingState.Idle
            _effect.send(UserOnboardingEffect.NavigateToAddressScreen(false))
        } else if (value.equals(resourcesProvider.getString(R.string.home_page_stage))) {
            _state.value = UserOnboardingState.GetHomePageData
        }
    }

    private fun stageCompleted(stageName: List<String>) {
        for (item in stageName) {
            incompleteStageInfo.values.remove(item)
        }
        _state.value = UserOnboardingState.LaunchNextStage
    }

    private fun setToolbar() {
        _state.value = UserOnboardingState.SetToolbar
    }

    private fun getUserOnboardingState() {
        viewModelScope.launch {
            _state.value = UserOnboardingState.Loading
            try {
                val getStage = userOnboardingStageUseCase(userId)
                getStage.catch {
                    when (it.message?.toInt()) {
                        ExistingUserValidation.EXISTING_USER.value -> {
                            _state.value = UserOnboardingState.ExistingUser
                        }
                    }
                }.collect {
                    when (it) {
                        is Resource.Default -> _state.value = UserOnboardingState.Idle
                        is Resource.Loading -> _state.value = UserOnboardingState.Loading
                        is Resource.Success -> {
                            completedStageIds = it.value.stageId
                            _state.value = UserOnboardingState.OnboardingStage
                        }
                        is Resource.Failure -> _state.value = UserOnboardingState.Error(
                            it.message,
                            it.failureStatus
                        )
                    }
                }
            } catch (e: Exception) {
                _state.value = UserOnboardingState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun getUserIdCustomerId() {
        viewModelScope.launch {
            _state.value = UserOnboardingState.Loading
            when (val onBoardingData = getOnboardingDataUseCase()) {
                is Resource.Success -> {
                    userId = onBoardingData.value[0]
                    customerId = onBoardingData.value[1]
                    _state.value = UserOnboardingState.GetUserOnboardingStage
                }
                is Resource.Failure -> _state.value =
                    UserOnboardingState.Error(onBoardingData.message, onBoardingData.failureStatus)
            }
        }
    }

    private fun getOnboardingStages() {
        viewModelScope.launch {
            _state.value = UserOnboardingState.Loading
            try {
                val onboardingStages = onboardingStageUseCase()
                onboardingStages.collect {
                    when (it) {
                        is Resource.Default -> _state.value = UserOnboardingState.Idle
                        is Resource.Loading -> _state.value = UserOnboardingState.Loading
                        is Resource.Failure -> _state.value = UserOnboardingState.Error(
                            it.message,
                            it.failureStatus
                        )
                        is Resource.Success -> {
                            allStageInfo = it.value
                            _state.value = UserOnboardingState.GetIncompleteStage
                        }
                    }
                }
            } catch (e: Exception) {
                _state.value = UserOnboardingState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun getIncompleteStages() {
        val incompleteStage = incompleteStagesUseCase(completedStageIds, allStageInfo)
        incompleteStageInfo = incompleteStage
        _state.value = UserOnboardingState.LaunchNextStage
    }

    private fun initSplash() {
        viewModelScope.launch {
            _state.value = UserOnboardingState.Loading
            try {
                splashUseCase().collect{
                    when(it){
                        is Resource.Success -> _effect.send(UserOnboardingEffect.NavigateToHomeScreen)
                        is Resource.Default -> _state.value = UserOnboardingState.Idle
                        is Resource.Failure -> _state.value = UserOnboardingState.Error(it.message, it.failureStatus)
                        is Resource.Loading -> _state.value = UserOnboardingState.Loading
                    }
                }
            } catch (e: Exception) {
                _state.value = UserOnboardingState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }
}