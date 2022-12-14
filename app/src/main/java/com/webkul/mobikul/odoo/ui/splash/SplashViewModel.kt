package com.webkul.mobikul.odoo.ui.splash

import android.content.Intent
import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.AppConfigEntity
import com.webkul.mobikul.odoo.data.entity.UserEntity
import com.webkul.mobikul.odoo.domain.usecase.analytics.UserAnalyticsUseCase
import com.webkul.mobikul.odoo.domain.usecase.appConfig.GetAppConfigUseCase
import com.webkul.mobikul.odoo.domain.usecase.auth.IsFirstTimeUseCase
import com.webkul.mobikul.odoo.domain.usecase.auth.IsUserLoggedInUseCase
import com.webkul.mobikul.odoo.domain.usecase.chat.CreateChatChannelUseCase
import com.webkul.mobikul.odoo.domain.usecase.home.HomeUseCase
import com.webkul.mobikul.odoo.domain.usecase.session.IsValidSessionUseCase
import com.webkul.mobikul.odoo.domain.usecase.splash.SplashUseCase
import com.webkul.mobikul.odoo.domain.usecase.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
	private val userAnalyticsUseCase: UserAnalyticsUseCase,
	private val splashUseCase: SplashUseCase,
	private val isUserLoggedInUseCase: IsUserLoggedInUseCase,
	private val isFirstTimeUseCase: IsFirstTimeUseCase,
	private val isValidSessionUseCase: IsValidSessionUseCase,
	private val createChatChannelUseCase: CreateChatChannelUseCase,
	private val getAppConfigUseCase: GetAppConfigUseCase,
	private val getUserUseCase: GetUserUseCase
) :
	BaseViewModel(), IModel<SplashState, SplashIntent, SplashEffect> {

	override val intents: Channel<SplashIntent> = Channel(Channel.UNLIMITED)

	private val _state = MutableStateFlow<SplashState>(SplashState.Idle)
	override val state: StateFlow<SplashState>
		get() = _state

	private val _effect = Channel<SplashEffect>()
	override val effect: Flow<SplashEffect>
		get() = _effect.receiveAsFlow()

	private var appConfigEntity: AppConfigEntity? = null
	private var userEntity: UserEntity? = null

	init {
		handlerIntent()
	}

	override fun handlerIntent() {
		viewModelScope.launch {
			intents.consumeAsFlow().collect {
				when (it) {
					is SplashIntent.GetConfigs -> getConfigs()
					is SplashIntent.CheckUserLoggedIn -> checkUserLoggedIn()
					is SplashIntent.CheckFirstTimeUser -> isFirstTime()
					is SplashIntent.StartInitUserAnalytics -> initUserAnalytics()
					is SplashIntent.CheckIntent -> handleIntentData(it.intent)
					is SplashIntent.StartInit -> initSplash()
					is SplashIntent.DeeplinkSplashPageData -> executeDeepLink()
					is SplashIntent.CreateChatChannel -> createChatChannel()
				}
			}
		}
	}

	private fun getConfigs() {
		viewModelScope.launch {
			_state.value = SplashState.Loading
			try {
				getAppConfigUseCase().zip(getUserUseCase()) { appConfig, user ->
					when (appConfig) {
						is Resource.Default -> appConfig
						is Resource.Failure -> appConfig
						is Resource.Loading -> appConfig
						is Resource.Success -> {
							appConfigEntity = appConfig.value
							user
						}
					}
				}.collect {
					when (it) {
						is Resource.Success -> {
							userEntity = it.value
							_state.value = SplashState.ConfigurationReceived
						}
						is Resource.Default -> _state.value = SplashState.Idle
						is Resource.Failure -> _state.value =
							SplashState.Error(it.message, it.failureStatus)
						is Resource.Loading -> _state.value = SplashState.Loading
					}
				}

			} catch (e: Exception) {
				_state.value = SplashState.Error(e.localizedMessage, FailureStatus.OTHER)
			}
		}
	}

	private fun checkUserLoggedIn() {
		viewModelScope.launch {
			_state.value = SplashState.Loading
			_state.value = try {
				val intent = isUserLoggedInUseCase(userEntity)
				var splashState: SplashState = SplashState.Idle

				intent.collect {
					splashState = when (it) {
						is Resource.Default -> SplashState.Loading
						is Resource.Failure -> SplashState.UserUnAuthenticated
						is Resource.Loading -> SplashState.Loading
						is Resource.Success -> {
							if (it.value) {
								SplashState.UserLoggedIn
							} else {
								SplashState.UserUnAuthenticated
							}
						}
					}
				}
				splashState
			} catch (e: Exception) {
				SplashState.Error(e.localizedMessage, FailureStatus.OTHER)
			}
		}
	}

	private fun isFirstTime() {
		viewModelScope.launch {
			_state.value = SplashState.Loading
			try {
				val intent = isFirstTimeUseCase()
				intent.collect {
					when (it) {
						is Resource.Default -> _state.value = SplashState.Idle
						is Resource.Failure -> _effect.send(SplashEffect.NavigateToSignInSignUpActivity)
						is Resource.Loading -> _state.value = SplashState.Loading
						is Resource.Success -> {
							if (it.value) {
								_effect.send(SplashEffect.NavigateToOnBoardingActivity)
							} else {
								_effect.send(SplashEffect.NavigateToSignInSignUpActivity)
							}
						}
					}
				}
			} catch (e: Exception) {
				_state.value = SplashState.Error(e.localizedMessage, FailureStatus.OTHER)
			}
		}
	}

	private fun initUserAnalytics() {
		viewModelScope.launch {
			_state.value = SplashState.Loading
			_state.value = try {
				val intent = userAnalyticsUseCase(userEntity)
				var splashState: SplashState = SplashState.Idle

				intent.collect {
					splashState = SplashState.InitiatedUserAnalytics
				}

				splashState
			} catch (e: Exception) {
				SplashState.InitiatedUserAnalytics
			}

		}
	}

	private fun handleIntentData(intent: Intent) {
		if (intent.extras != null && intent.hasExtra(BundleConstant.BUNDLE_KEY_NOTIFICATION_ID)) {
			_state.value = SplashState.NotificationIntent
		} else {
			_state.value = SplashState.SplashIntent
		}
	}

	private fun initSplash() {
		viewModelScope.launch {
			val isValidSessionUseCase = isValidSessionUseCase(appConfigEntity, userEntity)
			if (isValidSessionUseCase is Resource.Success) {
				_state.value = SplashState.Loading
				try {
					splashUseCase().collect {
						when (it) {
							is Resource.Success -> _effect.send(SplashEffect.NavigateToHomeScreen)
							is Resource.Default -> _state.value = SplashState.Idle
							is Resource.Failure -> _state.value =
								SplashState.Error(it.message, it.failureStatus)
							is Resource.Loading -> _state.value = SplashState.Loading
						}
					}
				} catch (e: Exception) {
					_state.value = SplashState.Error(e.localizedMessage, FailureStatus.OTHER)
				}

			} else {
				_state.value = SplashState.Error("", FailureStatus.ACCESS_DENIED)
			}
		}
	}

	private fun executeDeepLink() {
		viewModelScope.launch {
			val isValidSessionUseCase = isValidSessionUseCase(appConfigEntity, userEntity)
			if (isValidSessionUseCase is Resource.Success) {
				_state.value = SplashState.Loading
				try {
					splashUseCase().collect {
						when (it) {
							is Resource.Default -> _state.value = SplashState.Idle
							is Resource.Failure -> _state.value =
								SplashState.Error(it.message, it.failureStatus)
							is Resource.Loading -> _state.value = SplashState.Loading
							is Resource.Success -> _effect.send(SplashEffect.ExecuteDeepLink)
						}
					}
				} catch (e: Exception) {
					_state.value = SplashState.Error(e.localizedMessage, FailureStatus.OTHER)
				}
			} else {
				_state.value = SplashState.Error("", FailureStatus.ACCESS_DENIED)
			}
		}
	}

	private fun createChatChannel() {
		viewModelScope.launch {
			try {
				createChatChannelUseCase(userEntity).collect()
			} catch (e: Exception) {
				e.printStackTrace()
			}
		}
	}
}