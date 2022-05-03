package com.webkul.mobikul.odoo.features.auth.presentation

import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.features.auth.domain.enums.SignUpFieldsValidation
import com.webkul.mobikul.odoo.features.auth.domain.usecase.SignUpUseCase
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
                }
            }
        }

    }

    private fun showSellerView(isSeller: Boolean) {
        viewModelScope.launch {
            _state.value = SignUpState.IsSeller(isSeller)
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
                        SignUpFieldsValidation.EMPTY_PHONE_NO.value -> signUpState = SignUpState.InvalidSignUpDetailsError(SignUpFieldsValidation.EMPTY_PHONE_NO)
                        SignUpFieldsValidation.EMPTY_PASSWORD.value -> signUpState = SignUpState.InvalidSignUpDetailsError(SignUpFieldsValidation.EMPTY_PASSWORD)
                        SignUpFieldsValidation.INVALID_PASSWORD.value -> signUpState = SignUpState.InvalidSignUpDetailsError(SignUpFieldsValidation.INVALID_PASSWORD)
                    }
                }.collect {
                    when (it) {
                        is Resource.Default -> {}
                        is Resource.Failure -> signUpState = SignUpState.Error("Error Message")
                        is Resource.Loading -> signUpState = SignUpState.Loading
                        is Resource.Success -> signUpState = SignUpState.SignUp(it.value)
                    }
                }
                signUpState
            } catch (e: Exception) {
                SignUpState.Error(e.localizedMessage)
            }
        }

    }


}