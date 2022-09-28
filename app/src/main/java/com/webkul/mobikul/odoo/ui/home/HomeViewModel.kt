package com.webkul.mobikul.odoo.ui.home

import android.os.Handler
import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.AddressFormEntity
import com.webkul.mobikul.odoo.data.entity.BannerEntity
import com.webkul.mobikul.odoo.data.entity.StateListEntity
import com.webkul.mobikul.odoo.domain.enums.HomeRefreshState
import com.webkul.mobikul.odoo.domain.usecase.address.AddressFormDataUseCase
import com.webkul.mobikul.odoo.domain.usecase.address.CheckStateAvailabilityUseCase
import com.webkul.mobikul.odoo.domain.usecase.address.StateListUseCase
import com.webkul.mobikul.odoo.domain.usecase.auth.BillingAddressUseCase
import com.webkul.mobikul.odoo.domain.usecase.banner.BannersUseCase
import com.webkul.mobikul.odoo.domain.usecase.home.HandleRefreshStateUseCase
import com.webkul.mobikul.odoo.domain.usecase.productCategories.CategoriesUseCase
import com.webkul.mobikul.odoo.domain.usecase.user.UserDetailUseCase
import com.webkul.mobikul.odoo.model.customer.address.AddressData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val billingAddressUseCase: BillingAddressUseCase,
    private val addressFormDataUseCase: AddressFormDataUseCase,
    private val stateListUseCase: StateListUseCase,
    private val checkStateAvailabilityUseCase: CheckStateAvailabilityUseCase,
    private val bannersUseCase: BannersUseCase,
    private val userDetailUseCase: UserDetailUseCase,
    private val categoriesUseCase: CategoriesUseCase,
    private val handleRefreshStateUseCase: HandleRefreshStateUseCase
) : BaseViewModel(), IModel<HomeState, HomeIntent, HomeEffect> {

    override val intents: Channel<HomeIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableStateFlow<HomeState>(HomeState.Idle)
    override val state: StateFlow<HomeState>
        get() = _state

    private val _effect = Channel<HomeEffect>()
    override val effect: Flow<HomeEffect>
        get() = _effect.receiveAsFlow()

    var appBarOffset = 0
    var isImageVisible = false
    val handler = Handler()

    init {
        handlerIntent()
    }

    override fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect {
                when (it) {
                    is HomeIntent.InitView -> initView()
                    is HomeIntent.GetBillingAddress -> getBillingAddressData()
                    is HomeIntent.GetAddressFormData -> getAddressFormData(it.addressData)
                    is HomeIntent.GetStates -> getStates(
                        it.companyId,
                        it.addressFormEntity,
                        it.addressData
                    )
                    is HomeIntent.CheckStateAvailability -> checkStateAvailability(
                        it.stateListEntity,
                        it.addressFormEntity,
                        it.addressData
                    )
                    is HomeIntent.GetBannerData -> getBannerData()
                    is HomeIntent.GetProductCategories -> getProductCategories()
                    is HomeIntent.UpdateUserDetails -> updateUserDetails()
                    is HomeIntent.SelectCategory -> selectCategory(it.position, it.previousPosition)
                    is HomeIntent.SetOnclickListeners -> setOnclickListeners()
                    is HomeIntent.OnBannerClick -> onBannerClick(it.bannerEntity)
                    is HomeIntent.BackPressed -> backPressed()
                    is HomeIntent.GetLoyaltyPoints -> getLoyaltyPoints()
                    is HomeIntent.RefreshStateChanged -> handleScrollStateChange(it.homeRefreshState)
                }
            }
        }
    }

    private suspend fun initView() {
        _effect.send(HomeEffect.InitView)
    }

    private suspend fun getLoyaltyPoints() {
        _effect.send(HomeEffect.GetLoyaltyPoints)
    }

    private suspend fun backPressed() {
        _effect.send(HomeEffect.BackPressed)
    }

    private suspend fun onBannerClick(bannerEntity: BannerEntity) {
        _effect.send(HomeEffect.OnBannerClick(bannerEntity))
    }

    private suspend fun setOnclickListeners() {
        _effect.send(HomeEffect.SetOnclickListeners)
    }

    private suspend fun selectCategory(position: Int, previousPosition: Int) {
        _effect.send(HomeEffect.SelectCategory(position, previousPosition))
    }


    private fun updateUserDetails() {
        viewModelScope.launch {
            try {
                userDetailUseCase().collect {
                    when (it) {
                        is Resource.Default -> HomeState.Loading
                        is Resource.Failure -> HomeState.Error(it.message, it.failureStatus)
                        is Resource.Loading -> HomeState.Loading
                        is Resource.Success -> {
                            _effect.send(HomeEffect.UpdateCartCount)
                            HomeState.Idle
                        }
                    }
                }
            } catch (e: Exception) {
                HomeState.Error(e.localizedMessage, FailureStatus.API_FAIL)
            }
        }
    }


    private fun handleScrollStateChange(homeRefreshState: HomeRefreshState) {
        viewModelScope.launch {
            try {
                handleRefreshStateUseCase(homeRefreshState).collect {
                    when (it) {
                        is Resource.Default -> HomeState.Idle
                        is Resource.Failure -> HomeState.Error(it.message, it.failureStatus)
                        is Resource.Loading -> HomeState.Loading
                        is Resource.Success -> HomeState.RefreshState(it.value)
                    }
                }
            } catch (e: Exception) {
                HomeState.Error(e.localizedMessage, FailureStatus.API_FAIL)
            }
        }
    }

    private fun getProductCategories() {
        viewModelScope.launch {
            try {
                categoriesUseCase().collect {
                    when (it) {
                        is Resource.Failure -> {
                            _effect.send(HomeEffect.Error(it.message, it.failureStatus))
                            _effect.send(HomeEffect.HideCategoriesShimmer)
                        }
                        is Resource.Success -> {
                            _effect.send(HomeEffect.ProductCategoriesSuccess(it.value))
                            _effect.send(HomeEffect.HideCategoriesShimmer)
                        }
                        else -> _effect.send(HomeEffect.ShowCategoriesShimmer)
                    }
                }

            } catch (e: Exception) {
                HomeState.Error(e.localizedMessage, FailureStatus.API_FAIL)
            }
        }
    }

    private fun getBannerData() {
        viewModelScope.launch {
            try {
                bannersUseCase().collect {
                    when (it) {
                        is Resource.Failure -> {
                            _effect.send(HomeEffect.Error(it.message, it.failureStatus))
                            _effect.send(HomeEffect.HideBannerShimmer)
                        }
                        is Resource.Success -> {
                            _effect.send(HomeEffect.BannerDataSuccess(it.value))
                            _effect.send(HomeEffect.HideBannerShimmer)
                        }
                        else -> _effect.send(HomeEffect.ShowCategoriesShimmer)
                    }
                }
            } catch (e: Exception) {
                HomeState.Error(e.localizedMessage, FailureStatus.API_FAIL)
            }
        }
    }


    private fun checkStateAvailability(
        stateListEntity: StateListEntity,
        addressFormEntity: AddressFormEntity,
        addressData: AddressData
    ) {
        viewModelScope.launch {
            _state.value = HomeState.Loading
            _state.value = try {
                val signUp =
                    checkStateAvailabilityUseCase(stateListEntity, addressFormEntity)
                var homeState: HomeState = HomeState.Loading

                signUp.collect {
                    homeState = when (it) {
                        is Resource.Default -> HomeState.Idle
                        is Resource.Failure -> HomeState.Error(it.message, it.failureStatus)
                        is Resource.Loading -> HomeState.Loading
                        is Resource.Success -> {
                            if (it.value.not()) {
                                _effect.send(
                                    HomeEffect.ShowPromptToCompleteBillingAddress(
                                        addressData
                                    )
                                )
                            }
                            HomeState.Idle
                        }
                    }
                }
                homeState
            } catch (e: Exception) {
                HomeState.Error(e.localizedMessage, FailureStatus.API_FAIL)
            }
        }
    }


    private fun getAddressFormData(addressData: AddressData) {
        viewModelScope.launch {
            _state.value = HomeState.Loading
            _state.value = try {
                val signUp = addressFormDataUseCase(addressData)
                var homeState: HomeState = HomeState.Loading

                signUp.collect {
                    homeState = when (it) {
                        is Resource.Default -> HomeState.Loading
                        is Resource.Failure -> HomeState.Error(it.message, it.failureStatus)
                        is Resource.Loading -> HomeState.Loading
                        is Resource.Success -> HomeState.AddressFormDataSuccess(
                            it.value,
                            addressData
                        )
                    }
                }
                homeState
            } catch (e: Exception) {
                HomeState.Error(e.localizedMessage, FailureStatus.API_FAIL)
            }
        }
    }

    private fun getStates(
        companyId: Int,
        addressFormEntity: AddressFormEntity,
        addressData: AddressData,
    ) {
        viewModelScope.launch {
            _state.value = HomeState.Loading
            _state.value = try {
                val intent = stateListUseCase(companyId)
                var homeState: HomeState = HomeState.Loading

                intent.collect {
                    homeState = when (it) {
                        is Resource.Default -> HomeState.Loading
                        is Resource.Failure -> HomeState.Error(it.message, it.failureStatus)
                        is Resource.Loading -> HomeState.Loading
                        is Resource.Success -> HomeState.StateListSuccess(
                            it.value,
                            addressFormEntity,
                            addressData
                        )
                    }

                }
                homeState
            } catch (e: Exception) {
                HomeState.Error(e.localizedMessage, FailureStatus.API_FAIL)
            }
        }
    }


    private fun getBillingAddressData() {
        viewModelScope.launch {
            _state.value = HomeState.Loading
            _state.value = try {
                val intent = billingAddressUseCase()
                var homeState: HomeState = HomeState.Loading

                intent.collect {
                    homeState = when (it) {
                        is Resource.Default -> HomeState.Loading
                        is Resource.Failure -> HomeState.Error(it.message, it.failureStatus)
                        is Resource.Loading -> HomeState.Loading
                        is Resource.Success -> HomeState.BillingAddressDataSuccess(it.value)
                    }

                }
                homeState
            } catch (e: Exception) {
                HomeState.Error(e.localizedMessage, FailureStatus.API_FAIL)
            }
        }
    }
}