package com.webkul.mobikul.odoo.features.authentication.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.PRIVACY_POLICY_URL
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.features.authentication.domain.enums.VerifyPhoneNumberValidation
import com.webkul.mobikul.odoo.features.authentication.domain.usecase.ContinuePhoneNumberUseCase
import com.webkul.mobikul.odoo.features.authentication.domain.usecase.VerifyPhoneNumberUseCase
import com.webkul.mobikul.odoo.features.authentication.presentation.intent.AuthenticationIntent
import com.webkul.mobikul.odoo.features.authentication.presentation.intent.LoginPasswordIntent
import com.webkul.mobikul.odoo.features.authentication.presentation.state.AuthenticationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val verifyPhoneNumberUseCase: VerifyPhoneNumberUseCase,
    private val continuePhoneNumberUseCase: ContinuePhoneNumberUseCase,
    private val appPreferences: AppPreferences
) : BaseViewModel(), IModel<AuthenticationState, AuthenticationIntent> {

    //        //TODO -> Handle preferences related stuff in data layer
    override val intents: Channel<AuthenticationIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableStateFlow<AuthenticationState>(AuthenticationState.Idle)
    override val state: StateFlow<AuthenticationState>
        get() = _state

    init {
        handlerIntent()
    }

    override fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect {
                when (it) {
                    is AuthenticationIntent.Continue -> continuePhoneNumber(it.phoneNumber)
                    is AuthenticationIntent.Verify -> validatePhoneNumber(it.phoneNumber)
                    is AuthenticationIntent.SignUp -> redirectToSignup()
                    is AuthenticationIntent.PrivacyPolicy -> showPrivacyPolicy()
                    is AuthenticationIntent.SavePrivacyPolicy -> { setPrivacyPolicyUrl() }
                }
            }
        }
    }


    private fun setPrivacyPolicyUrl() {
        if (appPreferences.privacyUrl?.isEmpty() == true)
            appPreferences.privacyUrl = PRIVACY_POLICY_URL
    }


    private fun showPrivacyPolicy() {
        viewModelScope.launch {
            _state.value = AuthenticationState.PrivacyPolicy
        }
    }


    private fun redirectToSignup() {
        viewModelScope.launch {
            _state.value = AuthenticationState.SignUp
        }
    }

    private fun validatePhoneNumber(phoneNumber: String) {
        viewModelScope.launch {
            _state.value = try {
                val verifyPhoneNumber = verifyPhoneNumberUseCase.invoke(phoneNumber)
                var authenticationState: AuthenticationState = AuthenticationState.Idle

                verifyPhoneNumber.catch {
                    when (it.message?.toInt()) {
                        VerifyPhoneNumberValidation.INCORRECT_PHONE_NUMBER_FORMAT.value -> {
                            authenticationState = AuthenticationState.IncorrectPhoneNumberFormat
                        }
                        VerifyPhoneNumberValidation.INCORRECT_PHONE_NUMBER.value -> {
                            authenticationState = AuthenticationState.IncorrectPhoneNumber
                        }
                    }
                }.collect {
                    authenticationState = when (it) {
                        is Resource.Default -> AuthenticationState.EmptyPhoneNumber
                        is Resource.Loading -> AuthenticationState.Loading
                        is Resource.Success -> AuthenticationState.ValidPhoneNumber
                        is Resource.Failure -> AuthenticationState.Error(
                            it.message,
                            FailureStatus.OTHER
                        )
                    }
                }
                authenticationState
            } catch (e: Exception) {
                AuthenticationState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun continuePhoneNumber(phoneNumber: String) {
        viewModelScope.launch {
            _state.value = AuthenticationState.Loading
            _state.value = try {
                val continuePhoneNumber = continuePhoneNumberUseCase.invoke(phoneNumber)
                var authenticationState: AuthenticationState = AuthenticationState.Idle

                continuePhoneNumber.collect {
                    authenticationState = when (it) {
                        is Resource.Default -> AuthenticationState.Idle
                        is Resource.Loading -> AuthenticationState.Loading
                        is Resource.Success -> AuthenticationState.VerifiedPhoneNumber
                        is Resource.Failure -> AuthenticationState.Error(
                            it.message,
                            FailureStatus.OTHER
                        )
                    }

                }
                authenticationState
            } catch (e: Exception) {
                AuthenticationState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

}