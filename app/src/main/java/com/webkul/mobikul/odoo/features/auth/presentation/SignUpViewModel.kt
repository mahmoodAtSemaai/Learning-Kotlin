package com.webkul.mobikul.odoo.features.auth.presentation

import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.features.auth.data.models.SignUpData
import com.webkul.mobikul.odoo.features.auth.domain.enums.SignUpFieldsValidation
import com.webkul.mobikul.odoo.features.auth.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val countryStateUseCase: CountryStateUseCase,
    private val billingAddressUseCase: BillingAddressUseCase,
    private val viewMarketPlaceTnCUseCase: ViewMarketPlaceTnCUseCase,
    private val viewTnCUseCase: ViewTnCUseCase
) : BaseViewModel(), IModel<SignUpState, SignUpIntent> {

    override val intents: Channel<SignUpIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableStateFlow<SignUpState>(SignUpState.Idle)
    override val state: StateFlow<SignUpState>
        get() = _state

    init {
        handlerIntent()
    }

    override fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect {
                when (it) {
                    is SignUpIntent.SignUp -> signUpUser(it.signUpData)
                    is SignUpIntent.IsSeller -> showSellerView(it.isSeller)
                    is SignUpIntent.GetCountryStateData -> getCountryStateData()
                    is SignUpIntent.GetBillingAddress -> getBillingAddressData()
                    is SignUpIntent.ViewMarketPlaceTnC -> getMarketPlaceTnC()
                    is SignUpIntent.ViewSignUpTnC -> getSignUpTnC()


                }
            }
        }

    }

    private fun getSignUpTnC() {
        viewModelScope.launch {
            _state.value = SignUpState.Loading
            _state.value = try {
                val signUp = viewTnCUseCase.invoke()
                var signUpState: SignUpState = SignUpState.Idle

                signUp.collect {
                    signUpState = when (it) {
                        is Resource.Default -> SignUpState.Idle
                        is Resource.Failure -> SignUpState.Error(it.message,it.failureStatus)
                        is Resource.Loading -> SignUpState.Loading
                        is Resource.Success -> SignUpState.MarketPlaceTnCSuccess(it.value)
                    }

                }
                signUpState
            } catch (e: Exception) {
                SignUpState.Error(e.localizedMessage ,FailureStatus.OTHER)
            }
        }
    }


    private fun getMarketPlaceTnC() {
        viewModelScope.launch {
            _state.value = SignUpState.Loading
            _state.value = try {
                val signUp = viewMarketPlaceTnCUseCase.invoke()
                var signUpState: SignUpState = SignUpState.Idle

                signUp.collect {
                    signUpState = when (it) {
                        is Resource.Default -> SignUpState.Idle
                        is Resource.Failure -> SignUpState.Error(it.message,it.failureStatus)
                        is Resource.Loading -> SignUpState.Loading
                        is Resource.Success -> SignUpState.SignUpTnCSuccess(it.value)
                    }
                }
                signUpState
            } catch (e: Exception) {
                SignUpState.Error(e.localizedMessage , FailureStatus.OTHER)
            }
        }
    }

    private fun showSellerView(isSeller: Boolean) {
        viewModelScope.launch {
            _state.value = SignUpState.IsSeller(isSeller)
        }
    }

    private fun getCountryStateData() {
        viewModelScope.launch {
            _state.value = SignUpState.Idle
            _state.value = try {
                val signUp = countryStateUseCase.invoke()
                var signUpState: SignUpState = SignUpState.Idle

                signUp.collect {
                    signUpState = when (it) {
                        is Resource.Default -> SignUpState.Idle
                        is Resource.Failure -> SignUpState.Idle
                        is Resource.Loading -> SignUpState.Idle
                        is Resource.Success -> SignUpState.CountryStateDataSuccess(it.value)
                    }

                }
                signUpState
            } catch (e: Exception) {
                SignUpState.Error(e.localizedMessage,FailureStatus.OTHER)
            }
        }
    }


    private fun getBillingAddressData() {
        viewModelScope.launch {
            _state.value = SignUpState.Loading
            _state.value = try {
                val signUp = billingAddressUseCase.invoke()
                var signUpState: SignUpState = SignUpState.Loading

                signUp.collect {
                    signUpState = when (it) {
                        is Resource.Default -> SignUpState.Loading
                        is Resource.Failure -> SignUpState.Error(it.message,it.failureStatus)
                        is Resource.Loading -> SignUpState.Loading
                        is Resource.Success -> SignUpState.BillingAddressDataSuccess(it.value)
                    }

                }
                signUpState
            } catch (e: Exception) {
                SignUpState.Error(e.localizedMessage,FailureStatus.OTHER)
            }
        }
    }

    private fun signUpUser(signUpData: SignUpData) {

        viewModelScope.launch {
            _state.value = SignUpState.Loading
            _state.value = try {
                val signUp = signUpUseCase.invoke(signUpData)
                var signUpState: SignUpState = SignUpState.Idle

                signUp.catch {
                    when (it.message?.toInt()) {
                        SignUpFieldsValidation.EMPTY_PHONE_NO.value -> signUpState =
                            SignUpState.InvalidSignUpDetailsError(SignUpFieldsValidation.EMPTY_PHONE_NO)
                        SignUpFieldsValidation.EMPTY_NAME.value -> signUpState =
                            SignUpState.InvalidSignUpDetailsError(SignUpFieldsValidation.EMPTY_NAME)
                        SignUpFieldsValidation.EMPTY_PASSWORD.value -> signUpState =
                            SignUpState.InvalidSignUpDetailsError(SignUpFieldsValidation.EMPTY_PASSWORD)
                        SignUpFieldsValidation.INVALID_PASSWORD.value -> signUpState =
                            SignUpState.InvalidSignUpDetailsError(SignUpFieldsValidation.INVALID_PASSWORD)
                        SignUpFieldsValidation.UNEQUAL_PASS_AND_CONFIRM_PASS.value -> signUpState =
                            SignUpState.InvalidSignUpDetailsError(SignUpFieldsValidation.UNEQUAL_PASS_AND_CONFIRM_PASS)
                        SignUpFieldsValidation.EMPTY_TERMS_CONDITIONS.value -> signUpState =
                            SignUpState.InvalidSignUpDetailsError(SignUpFieldsValidation.EMPTY_TERMS_CONDITIONS)
                        SignUpFieldsValidation.EMPTY_PROFILE_URL.value -> signUpState =
                            SignUpState.InvalidSignUpDetailsError(SignUpFieldsValidation.EMPTY_PROFILE_URL)
                        SignUpFieldsValidation.EMPTY_COUNTRY.value -> signUpState =
                            SignUpState.InvalidSignUpDetailsError(SignUpFieldsValidation.EMPTY_COUNTRY)


                    }
                }.collect {
                    when (it) {
                        is Resource.Default -> signUpState = SignUpState.Idle
                        is Resource.Failure -> signUpState = SignUpState.Error(it.message,it.failureStatus)
                        is Resource.Loading -> signUpState = SignUpState.Loading
                        is Resource.Success -> signUpState = SignUpState.SignUp(it.value)
                    }
                }
                signUpState
            } catch (e: Exception) {
                SignUpState.Error(e.localizedMessage,FailureStatus.OTHER)
            }
        }

    }


}