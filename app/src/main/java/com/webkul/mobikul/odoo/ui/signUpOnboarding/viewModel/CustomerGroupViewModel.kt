package com.webkul.mobikul.odoo.ui.signUpOnboarding.viewModel

import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.request.CustomerGroupRequest
import com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding.ContinueCustomerGroupUseCase
import com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding.CustomerGroupUseCase
import com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding.GetOnboardingDataUseCase
import com.webkul.mobikul.odoo.ui.signUpOnboarding.effect.CustomerGroupEffect
import com.webkul.mobikul.odoo.ui.signUpOnboarding.intent.CustomerGroupIntent
import com.webkul.mobikul.odoo.ui.signUpOnboarding.state.CustomerGroupState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerGroupViewModel @Inject constructor(
    private val continueCustomerGroupUseCase: ContinueCustomerGroupUseCase,
    private val customerGroupUseCase: CustomerGroupUseCase,
    private val getOnboardingDataUseCase: GetOnboardingDataUseCase
) :
    BaseViewModel(), IModel<CustomerGroupState, CustomerGroupIntent, CustomerGroupEffect> {

    override val intents: Channel<CustomerGroupIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableStateFlow<CustomerGroupState>(CustomerGroupState.Idle)
    override val state: StateFlow<CustomerGroupState>
        get() = _state

    private val _effect = Channel<CustomerGroupEffect>()
    override val effect: Flow<CustomerGroupEffect>
        get() = _effect.receiveAsFlow()

    private var userId = ""
    private var selectedGroupId = ""
    private var selectedGroupName = ""

    init {
        handlerIntent()
    }

    override fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect {
                when (it) {
                    is CustomerGroupIntent.GetUserId -> getUserId()
                    is CustomerGroupIntent.FetchCustomer -> getCustomerGroups()
                    is CustomerGroupIntent.Continue -> setCustomerGroup()
                    is CustomerGroupIntent.GroupSelected -> selectedGroup(it.id, it.name)
                    is CustomerGroupIntent.CloseApp -> close()
                }
            }
        }
    }

    private fun close() {
        _state.value = CustomerGroupState.CloseApp
    }

    private fun selectedGroup(id: String, name: String) {
        selectedGroupId = id
        selectedGroupName = name
        _state.value = CustomerGroupState.GroupSelected
    }

    private fun setCustomerGroup() {
        viewModelScope.launch {
            _state.value = CustomerGroupState.Loading
            try {
                val customerGroup =
                    continueCustomerGroupUseCase(CustomerGroupRequest(customerGrpType = selectedGroupId,customerGrpName = selectedGroupName, userId = userId))
                customerGroup.collect {
                    when (it) {
                        is Resource.Default -> _state.value = CustomerGroupState.Idle
                        is Resource.Loading -> _state.value = CustomerGroupState.Loading
                        is Resource.Success -> _state.value = CustomerGroupState.CompletedCustomerGroup
                        is Resource.Failure -> _state.value =  CustomerGroupState.Error(
                            it.message,
                            it.failureStatus
                        )
                    }
                }
            } catch (e: Exception) {
                _state.value = CustomerGroupState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun getCustomerGroups() {
        viewModelScope.launch {
            _state.value = CustomerGroupState.Loading
            _state.value = try {
                val customerGroup = customerGroupUseCase()
                var customerGroupState: CustomerGroupState = CustomerGroupState.Idle

                customerGroup.collect {
                    customerGroupState = when (it) {
                        is Resource.Default -> CustomerGroupState.Idle
                        is Resource.Loading -> CustomerGroupState.Loading
                        is Resource.Success -> CustomerGroupState.CustomerGroups(it.value)
                        is Resource.Failure -> CustomerGroupState.Error(
                            it.message,
                            it.failureStatus
                        )
                    }
                }
                customerGroupState
            } catch (e: Exception) {
                CustomerGroupState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun getUserId() {
        viewModelScope.launch {
            _state.value = CustomerGroupState.Loading
            when (val onBoardingData = getOnboardingDataUseCase()) {
                is Resource.Success -> {
                    userId = onBoardingData.value[0]
                    _state.value = CustomerGroupState.FetchCustomerGroups
                }
                is Resource.Failure -> _state.value =
                    CustomerGroupState.Error(onBoardingData.message, onBoardingData.failureStatus)
            }
        }
    }
}