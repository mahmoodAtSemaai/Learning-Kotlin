package com.webkul.mobikul.odoo.ui.signUpAuth.viewModel

import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.domain.enums.VerifyPhoneNumberValidation
import com.webkul.mobikul.odoo.domain.usecase.auth.ContinuePhoneNumberUseCase
import com.webkul.mobikul.odoo.domain.usecase.auth.VerifyPhoneNumberUseCase
import com.webkul.mobikul.odoo.domain.usecase.auth.ViewTnCUseCase
import com.webkul.mobikul.odoo.ui.signUpAuth.effect.SignUpAuthEffect
import com.webkul.mobikul.odoo.ui.signUpAuth.intent.SignUpAuthIntent
import com.webkul.mobikul.odoo.ui.signUpAuth.state.SignUpAuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpAuthViewModel @Inject constructor(
    private val verifyPhoneNumberUseCase: VerifyPhoneNumberUseCase,
    private val continuePhoneNumberUseCase: ContinuePhoneNumberUseCase,
    private val viewTnCUseCase: ViewTnCUseCase
) :
    BaseViewModel(), IModel<SignUpAuthState, SignUpAuthIntent, SignUpAuthEffect> {

    override val intents: Channel<SignUpAuthIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableStateFlow<SignUpAuthState>(SignUpAuthState.Idle)
    override val state: StateFlow<SignUpAuthState>
        get() = _state

    private val _effect = Channel<SignUpAuthEffect>()
    override val effect: Flow<SignUpAuthEffect>
        get() = _effect.receiveAsFlow()

    init {
        handlerIntent()
    }

    override fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect {
                when (it) {
                    is SignUpAuthIntent.Verify -> validatePhoneNumber(it.phoneNumber)
                    is SignUpAuthIntent.Continue -> continuePhoneNumber(it.phoneNumber)
                    is SignUpAuthIntent.Login -> navigateToLogin()
                    is SignUpAuthIntent.NewUser -> navigateToSignUpOptions(it.phoneNumber)
                    is SignUpAuthIntent.ViewMarketPlaceTnC -> getSignUpTnC()
                }
            }
        }
    }

    private suspend fun navigateToSignUpOptions(phoneNumber: String) {
        _effect.send(SignUpAuthEffect.NavigateToSignUpAuthOptions(phoneNumber))
    }

    private suspend fun navigateToLogin() {
        _effect.send(SignUpAuthEffect.NavigateToLogin)
    }

    private fun continuePhoneNumber(phoneNumber: String) {
        viewModelScope.launch {
            _state.value = SignUpAuthState.Loading
            _state.value = try {
                val continuePhoneNumber = continuePhoneNumberUseCase(phoneNumber)
                var authenticationState: SignUpAuthState = SignUpAuthState.Idle

                continuePhoneNumber.collect {
                    authenticationState = when (it) {
                        is Resource.Default -> SignUpAuthState.Idle
                        is Resource.Loading -> SignUpAuthState.Loading
                        is Resource.Success -> SignUpAuthState.ExistingUser
                        is Resource.Failure -> SignUpAuthState.Error(
                            it.message,
                            it.failureStatus
                        )
                    }

                }
                authenticationState
            } catch (e: Exception) {
                SignUpAuthState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun validatePhoneNumber(phoneNumber: String) {
        viewModelScope.launch {
            _state.value = try {
                val verifyPhoneNumber = verifyPhoneNumberUseCase(phoneNumber)
                var authenticationState: SignUpAuthState = SignUpAuthState.Idle

                verifyPhoneNumber.catch {
                    when (it.message?.toInt()) {
                        VerifyPhoneNumberValidation.INCORRECT_PHONE_NUMBER_FORMAT.value -> {
                            authenticationState = SignUpAuthState.IncorrectPhoneNumberFormat
                        }
                        VerifyPhoneNumberValidation.INCORRECT_PHONE_NUMBER.value -> {
                            authenticationState = SignUpAuthState.IncorrectPhoneNumber
                        }
                    }
                }.collect {
                    authenticationState = when (it) {
                        is Resource.Default -> SignUpAuthState.EmptyPhoneNumber
                        is Resource.Loading -> SignUpAuthState.Loading
                        is Resource.Success -> SignUpAuthState.ValidPhoneNumber
                        is Resource.Failure -> SignUpAuthState.Error(
                            it.message,
                            FailureStatus.OTHER
                        )
                    }
                }
                authenticationState
            } catch (e: Exception) {
                SignUpAuthState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun getSignUpTnC() {
        viewModelScope.launch {
            _state.value = SignUpAuthState.Loading
            _state.value = try {
                val signUp = viewTnCUseCase()
                var signUpState: SignUpAuthState = SignUpAuthState.Idle

                signUp.collect {
                    signUpState = when (it) {
                        is Resource.Default -> SignUpAuthState.Idle
                        is Resource.Failure -> SignUpAuthState.Error(it.message, it.failureStatus)
                        is Resource.Loading -> SignUpAuthState.Loading
                        is Resource.Success -> {
                            SignUpAuthState.MarketPlaceTnCSuccess(it.value)
                        }
                    }

                }
                signUpState
            } catch (e: Exception) {
                SignUpAuthState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }
}