package com.webkul.mobikul.odoo.ui.signUpOnboarding.viewModel

import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.request.UserRequest
import com.webkul.mobikul.odoo.domain.enums.ReferralCodeValidation
import com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding.*
import com.webkul.mobikul.odoo.ui.signUpOnboarding.effect.UserDetailsEffect
import com.webkul.mobikul.odoo.ui.signUpOnboarding.intent.UserDetailsIntent
import com.webkul.mobikul.odoo.ui.signUpOnboarding.state.UserDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val continueUserDetailsUseCase: ContinueUserDetailsUseCase,
    private val validateReferralCodeUseCase: ValidateReferralCodeUseCase,
    private val verifyUserDetailsUseCase: VerifyUserDetailsUseCase,
    private val getUserDetailsViewsUseCase: GetUserDetailsViewsUseCase,
    private val getOnboardingDataUseCase: GetOnboardingDataUseCase
) :
    BaseViewModel(), IModel<UserDetailsState, UserDetailsIntent, UserDetailsEffect> {


    override val intents: Channel<UserDetailsIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableStateFlow<UserDetailsState>(UserDetailsState.Idle)
    override val state: StateFlow<UserDetailsState>
        get() = _state

    private val _effect = Channel<UserDetailsEffect>()
    override val effect: Flow<UserDetailsEffect>
        get() = _effect.receiveAsFlow()

    private var userId = ""
    private var customerId = ""
    private var base64UserImage = ""

    init {
        handlerIntent()
    }

    override fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect {
                when (it) {
                    is UserDetailsIntent.GetUserIdCustomerId -> getOnboardingData()
                    is UserDetailsIntent.FetchViews -> fetchViews()
                    is UserDetailsIntent.VerifyReferralCode -> verifyReferralCode(it.referralCode)
                    is UserDetailsIntent.UserDetails -> setUserDetails(
                        it.userDetailsRequest
                    )
                    is UserDetailsIntent.SetUserPicture -> {
                        _state.value = UserDetailsState.UserPicture
                    }
                    is UserDetailsIntent.VerifyFields -> validateMandatoryFields(
                        it.name,
                        it.groupName
                    )
                    is UserDetailsIntent.Base64UserPicture -> setBase64UserPicture(it.base64Image)
                    is UserDetailsIntent.HideInvalidReferralCode -> _effect.send(UserDetailsEffect.HideInvalidReferralCode)
                    is UserDetailsIntent.CloseApp -> closeApp()
                    is UserDetailsIntent.CloseCamera -> closeCamera()
                }
            }
        }
    }

    private fun closeCamera() {
        _state.value = UserDetailsState.Idle
    }

    private fun closeApp() {
        _state.value = UserDetailsState.CloseApp
    }

    private fun setBase64UserPicture(base64Image: String) {
        base64UserImage = base64Image
        _state.value = UserDetailsState.Idle
    }

    private fun setUserDetails(
        userDetailsRequest: UserRequest
    ) {
        viewModelScope.launch {
            _state.value = UserDetailsState.Loading
            userDetailsRequest.customerProfileImage = base64UserImage
            try {
                val userDetails =
                    continueUserDetailsUseCase(userDetailsRequest.apply {
                        this.userId = this@UserDetailsViewModel.userId
                        this.customerId = this@UserDetailsViewModel.customerId
                    })
                userDetails.collect {
                    when (it) {
                        is Resource.Default -> _state.value = UserDetailsState.Idle
                        is Resource.Loading -> _state.value = UserDetailsState.Loading
                        is Resource.Success -> _state.value = UserDetailsState.CompletedUserDetails
                        is Resource.Failure -> _state.value =
                            UserDetailsState.Error(it.message, it.failureStatus)
                    }
                }
            } catch (e: Exception) {
                _state.value = UserDetailsState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun verifyReferralCode(referralCode: String) {
        viewModelScope.launch {
            _state.value = UserDetailsState.Idle
            try {
                val verifyReferralCode = validateReferralCodeUseCase(referralCode)
                verifyReferralCode.catch {
                    when (it.message?.toInt()) {
                        ReferralCodeValidation.INVALID_REFERRAL_CODE.value ->
                            _state.value = UserDetailsState.InvalidReferralCode
                    }
                }.collect {
                    when (it) {
                        is Resource.Default -> _state.value = UserDetailsState.Idle
                        is Resource.Loading -> _state.value = UserDetailsState.Loading
                        is Resource.Success -> _state.value = UserDetailsState.ValidReferralCode
                        is Resource.Failure -> _state.value =
                            UserDetailsState.Error(it.message, it.failureStatus)
                    }
                }
            } catch (e: Exception) {
                _state.value = UserDetailsState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun fetchViews() {
        viewModelScope.launch {
            _state.value = UserDetailsState.Idle
            try {
                val userDetailsViews = getUserDetailsViewsUseCase()
                _state.value = UserDetailsState.SetUpViews(userDetailsViews)
            } catch (e: Exception) {
                _state.value = UserDetailsState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun validateMandatoryFields(name: String, groupName: String) {
        viewModelScope.launch {
            _state.value = UserDetailsState.Idle
            try {
                val mandatoryFields =
                    verifyUserDetailsUseCase(name, groupName)
                mandatoryFields.collect {
                    if (it) {
                        _state.value = UserDetailsState.EnableContinue
                    } else {
                        _state.value = UserDetailsState.DisableContinue
                    }
                }
            } catch (e: Exception) {
                _state.value = UserDetailsState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun getOnboardingData() {
        viewModelScope.launch {
            _state.value = UserDetailsState.Loading
            when (val onBoardingData = getOnboardingDataUseCase()) {
                is Resource.Success -> {
                    userId = onBoardingData.value[0]
                    customerId = onBoardingData.value[1]
                    _state.value = UserDetailsState.FetchViews
                }
                is Resource.Failure -> _state.value =
                    UserDetailsState.Error(onBoardingData.message, onBoardingData.failureStatus)
            }
        }
    }
}