package com.webkul.mobikul.odoo.ui.account.viewModel

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.AccountInfoEntity
import com.webkul.mobikul.odoo.data.request.UserDetailsRequest
import com.webkul.mobikul.odoo.domain.usecase.account.GetAccountInfoDataUseCase
import com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding.ContinueUserDetailsUseCase
import com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding.GetOnboardingDataUseCase
import com.webkul.mobikul.odoo.features.authentication.domain.usecase.HomePageDataUseCase
import com.webkul.mobikul.odoo.ui.account.effect.AccountInfoEffect
import com.webkul.mobikul.odoo.ui.account.intent.AccountInfoIntent
import com.webkul.mobikul.odoo.ui.account.state.AccountInfoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountInfoViewModel @Inject constructor(
    private val continueUserDetailsUseCase: ContinueUserDetailsUseCase,
    private val getOnboardingDataUseCase: GetOnboardingDataUseCase,
    private val getAccountInfoDataUseCase: GetAccountInfoDataUseCase,
    private val homePageDataUseCase: HomePageDataUseCase
) : BaseViewModel(),
    IModel<AccountInfoState, AccountInfoIntent, AccountInfoEffect> {
    override val intents: Channel<AccountInfoIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableStateFlow<AccountInfoState>(AccountInfoState.Idle)
    override val state: StateFlow<AccountInfoState>
        get() = _state

    private val _effect = Channel<AccountInfoEffect>()
    override val effect: Flow<AccountInfoEffect>
        get() = _effect.receiveAsFlow()

    var etValue = ""
    var isName = false
    var userId = ""
    var customerId = ""
    var accountInfoData = AccountInfoEntity()

    init {
        handlerIntent()
    }

    override fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect {
                when (it) {
                    is AccountInfoIntent.GetArgs -> getArgs(it.arguments)
                    is AccountInfoIntent.SetViews -> getViews()
                    is AccountInfoIntent.Discard -> getHomePageData()
                    is AccountInfoIntent.KeepEditing -> keepEditing()
                    is AccountInfoIntent.EditName -> navigateToEditAccountInfo(
                        accountInfoData,
                        it.title,
                        true
                    )
                    is AccountInfoIntent.EditGroupName -> navigateToEditAccountInfo(
                        accountInfoData,
                        it.title,
                        false
                    )
                    is AccountInfoIntent.BackNavigation -> backNavigation()
                    is AccountInfoIntent.Save -> saveInfo(it.updateUserDetails)
                    is AccountInfoIntent.GetUserIdCustomerId -> getUserIdCustomerId()
                    is AccountInfoIntent.EditValue -> editValueListner()
                    is AccountInfoIntent.InitViews -> initViews()
                }
            }
        }
    }

    private fun initViews() {
        _state.value = AccountInfoState.InitViews
    }

    private fun editValueListner() {
        if(accountInfoData.isEdited){
            _state.value = AccountInfoState.EnableContinue
        } else {
            _state.value = AccountInfoState.DisableContinue
        }
    }

    private fun saveInfo(updateUserDetails: UserDetailsRequest) {
        viewModelScope.launch {
            _state.value = AccountInfoState.Loading
            try {
                val userDetails =
                    continueUserDetailsUseCase(userId, customerId, updateUserDetails)
                userDetails.collect {
                    when (it) {
                        is Resource.Default -> _state.value = AccountInfoState.Idle
                        is Resource.Loading -> _state.value = AccountInfoState.Loading
                        is Resource.Success -> getHomePageData()
                        is Resource.Failure -> _state.value =
                            AccountInfoState.Error(it.message, it.failureStatus)
                    }
                }
            } catch (e: Exception) {
                _state.value = AccountInfoState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun backNavigation() {
        if (accountInfoData.isEdited) {
            _state.value = AccountInfoState.ShowDialog
        } else {
            getHomePageData()
        }
    }

    private fun keepEditing() {
        _state.value = AccountInfoState.KeepEditing
    }

    private fun getArgs(arguments: Bundle?) {
        etValue = arguments?.getString(BundleConstant.BUNDLE_KEY_VALUE) ?: ""
        isName = arguments?.getBoolean(BundleConstant.BUNDLE_KEY_IS_USER_NAME_EDIT) ?: false
        accountInfoData =
            arguments?.getParcelable(BundleConstant.BUNDLE_KEY_ACCOUNT_INFO_DATA) ?: accountInfoData
        _state.value = AccountInfoState.SetData(accountInfoData)
    }

    private suspend fun navigateToEditAccountInfo(
        value: AccountInfoEntity,
        title: String,
        isName: Boolean
    ) {
        _effect.send(AccountInfoEffect.NavigateToEditAccountInfo(value, title, isName))
    }

    private fun getViews() {
        accountInfoData = getAccountInfoDataUseCase()
        _state.value = AccountInfoState.GetArgs
    }

    private fun getUserIdCustomerId() {
        viewModelScope.launch {
            _state.value = AccountInfoState.Loading
            when (val onBoardingData = getOnboardingDataUseCase()) {
                is Resource.Success -> {
                    userId = onBoardingData.value[0]
                    customerId = onBoardingData.value[1]
                    _state.value = AccountInfoState.Idle
                }
                is Resource.Failure -> _state.value =
                    AccountInfoState.Error(onBoardingData.message, onBoardingData.failureStatus)
            }
        }
    }

    private fun getHomePageData() {
        viewModelScope.launch {
            _state.value = AccountInfoState.Loading
            try {
                val homePage = homePageDataUseCase()

                homePage.collect {
                    when (it) {
                        is Resource.Default -> _state.value = AccountInfoState.Idle
                        is Resource.Loading -> _state.value = AccountInfoState.Loading
                        is Resource.Failure -> _state.value = AccountInfoState.Error(
                            it.message,
                            it.failureStatus
                        )
                        is Resource.Success -> _effect.send(AccountInfoEffect.NavigateToAccount(it.value))
                    }
                }
            } catch (e: Exception) {
                AccountInfoState.Error(e.localizedMessage, FailureStatus.OTHER)
            }

        }
    }
}