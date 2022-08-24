package com.webkul.mobikul.odoo.ui.signUpOnboarding.viewModel

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.address.DistrictEntity
import com.webkul.mobikul.odoo.data.entity.address.StateEntity
import com.webkul.mobikul.odoo.data.entity.address.SubDistrictEntity
import com.webkul.mobikul.odoo.data.entity.address.VillageEntity
import com.webkul.mobikul.odoo.data.request.UserAddressRequest
import com.webkul.mobikul.odoo.data.request.UserLocationRequest
import com.webkul.mobikul.odoo.domain.enums.AddressFieldValidation
import com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding.*
import com.webkul.mobikul.odoo.ui.signUpOnboarding.adapter.DistrictSpinnerAdapter
import com.webkul.mobikul.odoo.ui.signUpOnboarding.adapter.ProvinceSpinnerAdapter
import com.webkul.mobikul.odoo.ui.signUpOnboarding.adapter.SubDistrictSpinnerAdapter
import com.webkul.mobikul.odoo.ui.signUpOnboarding.adapter.VillageSpinnerAdapter
import com.webkul.mobikul.odoo.ui.signUpOnboarding.effect.UserAddressEffect
import com.webkul.mobikul.odoo.ui.signUpOnboarding.intent.UserAddressIntent
import com.webkul.mobikul.odoo.ui.signUpOnboarding.state.UserAddressState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

//TODO - Optimize according to coding standards and principles
@HiltViewModel
class UserAddressViewModel @Inject constructor(
    private val continueUserAddressUseCase: ContinueUserAddressUseCase,
    private val continueUserLocationUseCase: ContinueUserLocationUseCase,
    private val provinceDataUseCase: ProvinceDataUseCase,
    private val districtDataUseCase: DistrictDataUseCase,
    private val subDistrictDataUseCase: SubDistrictDataUseCase,
    private val villageDataUseCase: VillageDataUseCase,
    private val verifyUserAddressUseCase: VerifyUserAddressUseCase,
    private val getOnboardingDataUseCase: GetOnboardingDataUseCase,
    private val isProvinceAvailableUseCase: IsProvinceAvailableUseCase,
    private val provinceSearchUseCase: ProvinceSearchUseCase,
    private val districtSearchUseCase: DistrictSearchUseCase,
    private val subDistrictSearchUseCase: SubDistrictSearchUseCase,
    private val villageSearchUseCase: VillageSearchUseCase
) :
    BaseViewModel(), IModel<UserAddressState, UserAddressIntent, UserAddressEffect> {
    override val intents: Channel<UserAddressIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableStateFlow<UserAddressState>(UserAddressState.Idle)
    override val state: StateFlow<UserAddressState>
        get() = _state

    private val _effect = Channel<UserAddressEffect>()
    override val effect: Flow<UserAddressEffect>
        get() = _effect.receiveAsFlow()

    private var customerId = ""
    private var isAddressStagePending = true
    private var selectedProvinceAvailable = true
    private var selectedProvinceId = ""
    private var selectedDistrictId = ""
    private var selectedSubDistrictId = ""
    private var selectedVillageId = ""
    private var selectedPostalCode = ""
    private val subDistrictSpinnerType = 3
    private val villageSpinnerType = 4

    init {
        handlerIntent()
    }

    override fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect {
                when (it) {
                    is UserAddressIntent.GetArgs -> getArgs(it.arguments)
                    is UserAddressIntent.CheckPendingStage -> checkPendingStage()
                    is UserAddressIntent.ProvinceSpinner -> getProvinceData(it.companyId)
                    is UserAddressIntent.ProvinceOnItemClick -> provinceOnItemClick(
                        it.position,
                        it.addressList
                    )
                    is UserAddressIntent.ProvinceSearch -> provinceSearch(
                        it.adapter,
                        it.addressList,
                        it.query,
                        it.spinnerType
                    )

                    is UserAddressIntent.DistrictSpinner -> getDistrictData(it.stateId)
                    is UserAddressIntent.DistrictOnItemClick -> districtOnItemClick(
                        it.position,
                        it.addressList
                    )
                    is UserAddressIntent.DistrictSearch -> districtSearch(
                        it.adapter,
                        it.addressList,
                        it.query,
                        it.spinnerType
                    )

                    is UserAddressIntent.SubDistrictSpinner -> getSubDistrictData(it.districtId)
                    is UserAddressIntent.SubDistrictOnItemClick -> subDistrictOnItemClick(
                        it.position,
                        it.addressList
                    )
                    is UserAddressIntent.SubDistrictSearch -> subDistrictSearch(
                        it.adapter,
                        it.addressList,
                        it.query,
                        it.spinnerType
                    )

                    is UserAddressIntent.VillageSpinner -> getVillageData(it.subDistrictId)
                    is UserAddressIntent.VillageOnItemClick -> villageOnItemClick(
                        it.position,
                        it.addressList
                    )
                    is UserAddressIntent.VillageSearch -> villageSearch(
                        it.adapter,
                        it.addressList,
                        it.query,
                        it.spinnerType
                    )

                    is UserAddressIntent.ExpandSpinner -> expandSpinner(it.spinner, it.dropDown, it.type)
                    is UserAddressIntent.VerifyFields -> validateMandatoryFields(it.userAddressRequest)
                    is UserAddressIntent.LocationContinue -> setUserLocation(it.userLocationRequest)
                    is UserAddressIntent.Continue -> setUserAddress(it.userAddressRequest)
                    is UserAddressIntent.GetCustomerId -> getCustomerId()

                    is UserAddressIntent.ResetDistrictSpinner -> resetDistrictSpinner()
                    is UserAddressIntent.ResetSubDistrictSpinner -> resetSubDistrictSpinner()
                    is UserAddressIntent.ResetVillageSpinner -> resetVillageSpinner()
                    is UserAddressIntent.SpinnerState -> getSpinnerState(it.spinnerType)
                    is UserAddressIntent.CloseApp -> closeApp()
                }
            }
        }
    }

    private fun closeApp() {
        _state.value = UserAddressState.CloseApp
    }

    private suspend fun getSpinnerState(spinnerType: Int) {
        if(spinnerType == subDistrictSpinnerType){
            _effect.send(UserAddressEffect.EnableSpinner(spinnerType,selectedDistrictId.isNotEmpty()))
        }else if (spinnerType == villageSpinnerType){
            _effect.send(UserAddressEffect.EnableSpinner(spinnerType,selectedSubDistrictId.isNotEmpty()))
        }
    }

    private suspend fun resetDistrictSpinner() {
        selectedDistrictId = ""
        selectedSubDistrictId = ""
        selectedVillageId = ""
        selectedPostalCode = ""
        _effect.send(UserAddressEffect.ResetDistrictSpinner(selectedProvinceId.toInt()))
    }

    private suspend fun resetSubDistrictSpinner() {
        selectedSubDistrictId = ""
        selectedVillageId = ""
        selectedPostalCode = ""
        _effect.send(UserAddressEffect.ResetSubDistrictSpinner(selectedDistrictId.toInt()))
    }

    private suspend fun resetVillageSpinner() {
        selectedVillageId = ""
        selectedPostalCode = ""
        _effect.send(UserAddressEffect.ResetVillageSpinner(selectedSubDistrictId.toInt()))
    }

    private fun getCustomerId() {
        viewModelScope.launch {
            _state.value = UserAddressState.Loading
            when (val onBoardingData = getOnboardingDataUseCase()) {
                is Resource.Success -> {
                    customerId = onBoardingData.value[1]
                    _state.value = UserAddressState.GetArgs
                }
                is Resource.Failure -> _state.value =
                    UserAddressState.Error(onBoardingData.message, onBoardingData.failureStatus)
            }
        }
    }

    private fun villageSearch(
        adapter: VillageSpinnerAdapter,
        addressList: List<VillageEntity>,
        query: String,
        spinnerType: Int
    ) {
        viewModelScope.launch {
            try {
                val subDistrictResult = villageSearchUseCase(query, addressList)
                subDistrictResult.catch {
                    when (it.message?.toInt()) {
                        AddressFieldValidation.NO_RESULTS_FOUND.value -> _state.value =
                            UserAddressState.EmptySearch(spinnerType)
                    }
                }.collect {
                    _state.value = UserAddressState.VillageSearchResult(adapter, it)
                }
            } catch (e: Exception) {
                _state.value = UserAddressState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun villageOnItemClick(position: Int, addressList: List<VillageEntity>) {
        selectedVillageId = addressList[position].id.toString()
        selectedPostalCode = addressList[position].zip
        _state.value = UserAddressState.SelectedVillage(
            addressList[position].name,
            addressList[position].zip
        )
    }

    private fun subDistrictSearch(
        adapter: SubDistrictSpinnerAdapter,
        addressList: List<SubDistrictEntity>,
        query: String,
        spinnerType: Int
    ) {
        viewModelScope.launch {
            try {
                val subDistrictResult = subDistrictSearchUseCase(query, addressList)
                subDistrictResult.catch {
                    when (it.message?.toInt()) {
                        AddressFieldValidation.NO_RESULTS_FOUND.value -> _state.value =
                            UserAddressState.EmptySearch(spinnerType)
                    }
                }.collect {
                    _state.value = UserAddressState.SubDistrictSearchResult(adapter, it)
                }
            } catch (e: Exception) {
                _state.value = UserAddressState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun subDistrictOnItemClick(position: Int, addressList: List<SubDistrictEntity>) {
        selectedSubDistrictId = addressList[position].id.toString()
        _state.value = UserAddressState.SelectedSubDistrict(
            addressList[position].name,
            addressList[position].id
        )
    }

    private fun districtSearch(
        adapter: DistrictSpinnerAdapter,
        addressList: List<DistrictEntity>,
        query: String,
        spinnerType: Int
    ) {
        viewModelScope.launch {
            try {
                val districtResult = districtSearchUseCase(query, addressList)
                districtResult.catch {
                    when (it.message?.toInt()) {
                        AddressFieldValidation.NO_RESULTS_FOUND.value -> _state.value =
                            UserAddressState.EmptySearch(spinnerType)
                    }
                }.collect {
                    _state.value = UserAddressState.DistrictSearchResult(adapter, it)
                }
            } catch (e: Exception) {
                _state.value = UserAddressState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun districtOnItemClick(position: Int, addressList: List<DistrictEntity>) {
        selectedDistrictId = addressList[position].id.toString()
        _state.value =
            UserAddressState.SelectedDistrict(addressList[position].name, addressList[position].id)
    }

    private fun provinceSearch(
        adapter: ProvinceSpinnerAdapter,
        addressList: List<StateEntity>,
        query: String,
        spinnerType: Int
    ) {
        viewModelScope.launch {
            try {
                val provinceResult = provinceSearchUseCase(query, addressList)
                provinceResult.catch {
                    when (it.message?.toInt()) {
                        AddressFieldValidation.NO_RESULTS_FOUND.value -> _state.value =
                            UserAddressState.EmptySearch(spinnerType)
                    }
                }.collect {
                    _state.value = UserAddressState.ProvinceSearchResult(adapter, it)
                }
            } catch (e: Exception) {
                _state.value = UserAddressState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun provinceOnItemClick(position: Int, addressList: List<StateEntity>) {
        viewModelScope.launch {
            _state.value = UserAddressState.Idle
            try {
                val provinceData = isProvinceAvailableUseCase(position, addressList)
                provinceData.collect {
                    selectedProvinceAvailable = it
                    selectedProvinceId = addressList[position].id.toString()
                    if (it) {
                        _state.value = UserAddressState.AvailableProvince(
                            addressList[position].name,
                            addressList[position].id
                        )
                    } else {
                        _state.value =
                            UserAddressState.UnavailableProvince(addressList[position].name)
                    }
                }
            } catch (e: Exception) {
                _state.value = UserAddressState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private suspend fun getArgs(arguments: Bundle?) {
        isAddressStagePending = arguments?.getBoolean(BundleConstant.BUNDLE_KEY_IS_ADDRESS_PENDING) ?: false
        checkPendingStage()
    }

    private suspend fun checkPendingStage(){
        if(isAddressStagePending){
            _state.value = UserAddressState.FetchProvinceData
        }else{
            _effect.send(UserAddressEffect.LocationDialog)
        }
    }

    private fun setUserAddress(userAddressRequest: UserAddressRequest) {
        userAddressRequest.stateId = selectedProvinceId
        userAddressRequest.districtId = selectedDistrictId
        userAddressRequest.subDistrictId = selectedSubDistrictId
        userAddressRequest.villageId = selectedVillageId
        userAddressRequest.zip = selectedPostalCode
        viewModelScope.launch {
            _state.value = UserAddressState.Loading
            try {
                val userAddress = continueUserAddressUseCase(customerId, userAddressRequest)
                userAddress.collect {
                    when (it) {
                        is Resource.Default -> _state.value = UserAddressState.Idle
                        is Resource.Loading -> _state.value = UserAddressState.Loading
                        is Resource.Success -> {
                            isAddressStagePending = false
                            _effect.send(UserAddressEffect.LocationDialog)
                        }
                        is Resource.Failure -> _state.value = UserAddressState.Error(it.message, it.failureStatus)
                    }
                }
            } catch (e: Exception) {
                _state.value = UserAddressState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun setUserLocation(userLocationRequest: UserLocationRequest) {
        viewModelScope.launch {
            _state.value = UserAddressState.Loading
            try {
                val userLocation =
                    continueUserLocationUseCase(customerId, userLocationRequest)
                userLocation.collect {
                    when (it) {
                        is Resource.Default -> _state.value = UserAddressState.Idle
                        is Resource.Loading -> _state.value = UserAddressState.Loading
                        is Resource.Success -> _state.value = UserAddressState.CompletedAddress
                        is Resource.Failure -> _state.value = UserAddressState.Error(it.message, it.failureStatus)
                    }
                }
            } catch (e: Exception) {
                _state.value = UserAddressState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun validateMandatoryFields(userAddressRequest: UserAddressRequest) {
        userAddressRequest.stateId = selectedProvinceId
        userAddressRequest.districtId = selectedDistrictId
        userAddressRequest.subDistrictId = selectedSubDistrictId
        userAddressRequest.villageId = selectedVillageId
        userAddressRequest.zip = selectedPostalCode
        viewModelScope.launch {
            _state.value = UserAddressState.Idle
            _state.value = try {
                val mandatoryFeilds =
                    verifyUserAddressUseCase(
                        selectedProvinceAvailable,
                        userAddressRequest
                    )
                var userAddressState: UserAddressState = UserAddressState.Idle

                mandatoryFeilds.collect {
                    userAddressState = UserAddressState.ContinueBtn(it)
                }
                userAddressState
            } catch (e: Exception) {
                UserAddressState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun expandSpinner(spinner: View, dropDown: ImageView, type: Int) {
        if(type == subDistrictSpinnerType){
            if(selectedDistrictId.isNotEmpty()){
                _state.value = UserAddressState.ExpandSpinner(spinner, dropDown)
            }
        }
        else if(type == villageSpinnerType){
            if(selectedSubDistrictId.isNotEmpty()){
                _state.value = UserAddressState.ExpandSpinner(spinner, dropDown)
            }
        }else {
            _state.value = UserAddressState.ExpandSpinner(spinner, dropDown)
        }
    }

    private fun getVillageData(subDistrictId: Int) {
        viewModelScope.launch {
            _state.value = UserAddressState.Idle
            _state.value = try {
                val villageData =
                    villageDataUseCase(subDistrictId)
                var userAddressState: UserAddressState = UserAddressState.Idle

                villageData.collect {
                    userAddressState = when (it) {
                        is Resource.Default -> UserAddressState.Idle
                        is Resource.Loading -> UserAddressState.Loading
                        is Resource.Success -> UserAddressState.GetVillageData(it.value.villageList)
                        is Resource.Failure -> UserAddressState.Error(it.message, it.failureStatus)
                    }
                }
                userAddressState
            } catch (e: Exception) {
                UserAddressState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun getSubDistrictData(districtId: Int) {
        viewModelScope.launch {
            _state.value = UserAddressState.Idle
            _state.value = try {
                val subDistrictData =
                    subDistrictDataUseCase(districtId)
                var userAddressState: UserAddressState = UserAddressState.Idle

                subDistrictData.collect {
                    userAddressState = when (it) {
                        is Resource.Default -> UserAddressState.Idle
                        is Resource.Loading -> UserAddressState.Loading
                        is Resource.Success -> UserAddressState.GetSubDistrictData(it.value.subDistrictList)
                        is Resource.Failure -> UserAddressState.Error(it.message, it.failureStatus)
                    }
                }
                userAddressState
            } catch (e: Exception) {
                UserAddressState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun getDistrictData(stateId: Int) {
        viewModelScope.launch {
            _state.value = UserAddressState.Idle
            _state.value = try {
                val districtData =
                    districtDataUseCase(stateId)
                var userAddressState: UserAddressState = UserAddressState.Idle

                districtData.collect {
                    userAddressState = when (it) {
                        is Resource.Default -> UserAddressState.Idle
                        is Resource.Loading -> UserAddressState.Loading
                        is Resource.Success -> UserAddressState.GetDistrictData(it.value.districtList)
                        is Resource.Failure -> UserAddressState.Error(it.message, it.failureStatus)
                    }
                }
                userAddressState
            } catch (e: Exception) {
                UserAddressState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun getProvinceData(companyId: Int) {
        viewModelScope.launch {
            _state.value = UserAddressState.Idle
            _state.value = try {
                val provinceData =
                    provinceDataUseCase(companyId)
                var userAddressState: UserAddressState = UserAddressState.Idle

                provinceData.collect {
                    userAddressState = when (it) {
                        is Resource.Default -> UserAddressState.Idle
                        is Resource.Loading -> UserAddressState.Loading
                        is Resource.Success -> UserAddressState.GetProvinceData(it.value.stateList)
                        is Resource.Failure -> UserAddressState.Error(it.message, it.failureStatus)
                    }
                }
                userAddressState
            } catch (e: Exception) {
                UserAddressState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }
}