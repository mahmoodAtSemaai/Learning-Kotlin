package com.webkul.mobikul.odoo.ui.authentication.viewmodel

import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.domain.enums.VerifyPhoneNumberValidation
import com.webkul.mobikul.odoo.domain.usecase.auth.ContinuePhoneNumberUseCase
import com.webkul.mobikul.odoo.domain.usecase.auth.VerifyPhoneNumberUseCase
import com.webkul.mobikul.odoo.domain.usecase.auth.ViewPrivacyPolicyUseCase
import com.webkul.mobikul.odoo.ui.authentication.effect.AuthenticationEffect
import com.webkul.mobikul.odoo.ui.authentication.intent.AuthenticationIntent
import com.webkul.mobikul.odoo.ui.authentication.state.AuthenticationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val verifyPhoneNumberUseCase: VerifyPhoneNumberUseCase,
    private val continuePhoneNumberUseCase: ContinuePhoneNumberUseCase,
    private val viewPrivacyPolicyUseCase: ViewPrivacyPolicyUseCase
) : BaseViewModel(), IModel<AuthenticationState, AuthenticationIntent, AuthenticationEffect> {

    override val intents: Channel<AuthenticationIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableStateFlow<AuthenticationState>(AuthenticationState.Idle)
    override val state: StateFlow<AuthenticationState>
        get() = _state

    private val _effect = Channel<AuthenticationEffect>()
    override val effect: Flow<AuthenticationEffect>
        get() = _effect.receiveAsFlow()

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
                    is AuthenticationIntent.PrivacyPolicy -> viewPrivacyPolicy()
                    is AuthenticationIntent.SetIdle -> _state.value = AuthenticationState.Idle
                    is AuthenticationIntent.GetFocus -> setFocusOnInputField()
                }
            }
        }
    }

    private fun setFocusOnInputField() {
        _state.value = AuthenticationState.SetFocusOnInputField
    }

    private fun viewPrivacyPolicy() {
        viewModelScope.launch {
            try {
                val intent = viewPrivacyPolicyUseCase()
                intent.collect {
                    when (it) {
                        is Resource.Default -> _state.value = AuthenticationState.Idle
                        is Resource.Failure -> _state.value =
                            AuthenticationState.Error(it.message, it.failureStatus)
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            _effect.send(AuthenticationEffect.ShowPrivacyPolicy(it.value))
                        }
                    }
                }
            } catch (e: Exception) {
                AuthenticationState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }


    private suspend fun redirectToSignup() {
        _effect.send(AuthenticationEffect.NavigateToSignUp)
    }

    private fun validatePhoneNumber(phoneNumber: String) {
        viewModelScope.launch {
            try {
                val verifyPhoneNumber = verifyPhoneNumberUseCase(phoneNumber)

                verifyPhoneNumber.catch {
                    when (it.message?.toInt()) {
                        VerifyPhoneNumberValidation.INCORRECT_PHONE_NUMBER_FORMAT.value -> {
                            _state.value = AuthenticationState.IncorrectPhoneNumberFormat
                        }
                        VerifyPhoneNumberValidation.INCORRECT_PHONE_NUMBER.value -> {
                            _state.value = AuthenticationState.IncorrectPhoneNumber
                        }
                    }
                }.collect {
                    when (it) {
                        is Resource.Default -> _state.value = AuthenticationState.EmptyPhoneNumber
                        is Resource.Loading -> _state.value = AuthenticationState.Loading
                        is Resource.Success -> _state.value = AuthenticationState.ValidPhoneNumber
                        is Resource.Failure -> _state.value = AuthenticationState.Error(
                            it.message,
                            it.failureStatus
                        )
                    }
                }
            } catch (e: Exception) {
                _state.value = AuthenticationState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun continuePhoneNumber(phoneNumber: String) {
        viewModelScope.launch {
            _state.value = AuthenticationState.Loading
            try {
                val continuePhoneNumber = continuePhoneNumberUseCase(phoneNumber)
                continuePhoneNumber.catch {
                    when (it.message?.toInt()) {
                        VerifyPhoneNumberValidation.INVALID_PHONE_NUMBER.value -> {
                            _state.value = AuthenticationState.InvalidNumber
                        }
                    }
                }.collect {
                    when (it) {
                        is Resource.Default -> _state.value = AuthenticationState.Idle
                        is Resource.Loading -> _state.value = AuthenticationState.Loading
                        is Resource.Success -> _effect.send(
                            AuthenticationEffect.NavigateToLoginOptionsScreen(
                                phoneNumber, it.value.passwordEnabled
                            )
                        )
                        is Resource.Failure -> _state.value = AuthenticationState.Error(
                            it.message,
                            it.failureStatus
                        )
                    }

                }
            } catch (e: Exception) {
                _state.value = AuthenticationState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

}