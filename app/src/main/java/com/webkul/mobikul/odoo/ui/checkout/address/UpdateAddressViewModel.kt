package com.webkul.mobikul.odoo.ui.checkout.address

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.mvicore.IEffect
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import com.webkul.mobikul.odoo.data.entity.*
import com.webkul.mobikul.odoo.domain.usecase.checkout.*
import com.webkul.mobikul.odoo.model.customer.address.AddressRequestBody
import com.webkul.mobikul.odoo.ui.checkout.address.UpdateAddressFragmentV1.Companion.IS_EDIT
import com.webkul.mobikul.odoo.ui.checkout.address.UpdateAddressFragmentV1.Companion.URL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateAddressViewModel @Inject constructor(
    private val resourcesProvider: ResourcesProvider,
    private val fetchAddressFormDataUseCase: FetchAddressFormDataUseCase,
    private val addAddAddressUseCase: AddAddressUseCase,
    private val editAddressUseCase: EditAddressUseCase,
    private val fetchStatesUseCase: FetchStatesUseCase,
    private val fetchDistrictsUseCase: FetchDistrictsUseCase,
    private val fetchSubDistrictsUseCase: FetchSubDistrictsUseCase,
    private val fetchVillagesUseCase: FetchVillagesUseCase,
    private val validateMandatoryAddressFieldsUseCase: ValidateMandatoryAddressFieldsUseCase
) : BaseViewModel() , IModel<UpdateAddressState, UpdateAddressIntent,UpdateAddressEffect> {

    override val intents: Channel<UpdateAddressIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableStateFlow<UpdateAddressState>(UpdateAddressState.Idle)
    override val state: StateFlow<UpdateAddressState>
        get() = _state

    private val _effect = Channel<UpdateAddressEffect>()
    override val effect: Flow<UpdateAddressEffect>
        get() = _effect.receiveAsFlow()

    var isEditMode: Boolean = false
    var isServiceAvailable: Boolean = false

    var isAllSpinnerLoaded: Boolean = false
        get() = if (isServiceAvailable) selectedVillageId != null
                else selectedStateId != null

    var addressUrl: String? = null

    var selectedCountryId: Int? = 100
    var selectedStateId: String? = null
    var selectedDistrictId: String? = null
    var selectedSubDistrictId: String? = null
    var selectedVillageId: String? = null
    var selectedZip: String? = null


    init {
        handlerIntent()
    }

    override fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect {
                when (it) {
                    is UpdateAddressIntent.AddAddress -> addAddress(it.addressRequestBody)
                    is UpdateAddressIntent.EditAddress -> editAddress(it.addressRequestBody, it.url)
                    is UpdateAddressIntent.FetchAddressData -> fetchAddressFormData(it.url)
                    is UpdateAddressIntent.FetchDistricts -> fetchDistrictList(it.stateId)
                    is UpdateAddressIntent.FetchStates -> fetchStateList(it.companyId)
                    is UpdateAddressIntent.FetchSubDistricts -> fetchSubDistrictList(it.districtId)
                    is UpdateAddressIntent.FetchVillages -> fetchVillageList(it.subDistrictId)
                    is UpdateAddressIntent.ValidateMandatoryFields -> validateMandatoryFields(it.isServiceAvailable, it.addressRequestBody)
                    is UpdateAddressIntent.BundleData -> setUpBundleData(it.bundle)
                    is UpdateAddressIntent.SelectDistrict -> onDistrictSelection(it.districtEntity)
                    is UpdateAddressIntent.SelectState -> onStateSelection(it.stateEntity)
                    is UpdateAddressIntent.SelectSubDistrict -> onSubDistrictSelection(it.subDistrictEntity)
                    is UpdateAddressIntent.SelectVillage -> onVillageSelection(it.villageEntity)
                    is UpdateAddressIntent.ServiceNotAvailable -> onServiceNotAvailable()
                }
            }
        }
    }

    private fun onSubDistrictSelection(subDistrictEntity: SubDistrictEntity) {

        selectedSubDistrictId = subDistrictEntity.id.toString()
        _state.value = UpdateAddressState.SubDistrictSelected(subDistrictEntity)

    }

    private fun onVillageSelection(villageEntity: VillageEntity) {

        selectedVillageId = villageEntity.id.toString()
        selectedZip = villageEntity.zip.toString()
        _state.value = UpdateAddressState.VillageSelected(villageEntity)
    }

    private fun onDistrictSelection(districtEntity: DistrictEntity) {
            selectedDistrictId = districtEntity.id.toString()
            _state.value = UpdateAddressState.DistrictSelected(districtEntity)
    }

    private fun onServiceNotAvailable() {

            selectedDistrictId = null
            selectedSubDistrictId = null
            selectedVillageId = null
            selectedZip = null

            _state.value = UpdateAddressState.ServiceNotAvailable
    }

    private fun onStateSelection(selectedState: StateEntity) {
            selectedStateId = selectedState.id.toString()
            isServiceAvailable = selectedState.available == true

            _state.value = UpdateAddressState.StateSelected(selectedState)
    }

    private fun addAddress(addressRequestBody: AddressRequestBody) {

        viewModelScope.launch {
            _state.value = UpdateAddressState.Loading
            _state.value = try {
                val response =  addAddAddressUseCase(addressRequestBody)

                var addAddressState: UpdateAddressState = UpdateAddressState.Idle

                response
                    .collect {
                        when (it) {
                            is Resource.Default -> {}
                            is Resource.Failure -> addAddressState =
                                UpdateAddressState.UpdateError(
                                    it.message,
                                    it.failureStatus,
                                    it.code
                                )
                            is Resource.Loading -> addAddressState = UpdateAddressState.Loading
                            is Resource.Success -> addAddressState = UpdateAddressState.AddAddress
                        }
                    }
                addAddressState
            } catch (e: Exception) {
                UpdateAddressState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun editAddress(addressRequestBody: AddressRequestBody, url: String) {

        viewModelScope.launch {
            _state.value = UpdateAddressState.Loading
            _state.value = try {
                val response =  editAddressUseCase.invoke( url, addressRequestBody)

                var editAddressState: UpdateAddressState = UpdateAddressState.Idle

                response
                    .collect {
                        when (it) {
                            is Resource.Default -> {}
                            is Resource.Failure -> editAddressState =
                                UpdateAddressState.UpdateError(
                                    it.message,
                                    it.failureStatus,
                                    it.code
                                )
                            is Resource.Loading -> editAddressState = UpdateAddressState.Loading
                            is Resource.Success -> editAddressState = UpdateAddressState.EditAddress
                        }
                    }
                editAddressState
            } catch (e: Exception) {
                UpdateAddressState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun validateMandatoryFields(isServiceAvailable: Boolean, addressRequestBody: AddressRequestBody) {
        if (!isAllSpinnerLoaded) {
            _state.value =
                UpdateAddressState.Toast(resourcesProvider.getString(R.string.missing_feilds_in_address_form))
            return
        }

        viewModelScope.launch {
            _state.value = UpdateAddressState.Loading
            _state.value = try {
                val response =  validateMandatoryAddressFieldsUseCase.invoke(isServiceAvailable, addressRequestBody)

                var state: UpdateAddressState = UpdateAddressState.Idle

                response
                    .collect {
                        if (it) {
                            state =
                                UpdateAddressState.OnSuccessfulValidateFields(addressRequestBody)
                        } else {
                            state =
                                UpdateAddressState.WarningSnackBar(resourcesProvider.getString(R.string.missing_feilds_in_address_form))
                        }
                    }
                state
            } catch (e: Exception) {
                UpdateAddressState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }

    }

    private fun fetchAddressFormData(url: String) {
        viewModelScope.launch {
            _state.value = UpdateAddressState.Loading
            _state.value = try {
                val response =  fetchAddressFormDataUseCase.invoke(url)

                var state: UpdateAddressState = UpdateAddressState.Idle

                response
                    .collect {
                        when (it) {
                            is Resource.Default -> {}
                            is Resource.Failure -> state =
                                UpdateAddressState.Error(it.message, it.failureStatus)
                            is Resource.Loading -> state = UpdateAddressState.Loading
                            is Resource.Success -> {

                                state = UpdateAddressState.FetchAddressFormData(it.value)
                                selectedStateId = it.value.stateId
                                selectedDistrictId = it.value.districtId
                                selectedSubDistrictId = it.value.subDistrictId
                                selectedVillageId = it.value.villageId
                                selectedZip = it.value.zip


                            }

                        }
                    }
                state
            } catch (e: Exception) {
                UpdateAddressState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }


    private fun fetchStateList(companyId: Int) {
        viewModelScope.launch {
            _state.value = UpdateAddressState.Loading
            _state.value = try {
                val response =  fetchStatesUseCase.invoke(companyId)

                var state: UpdateAddressState = UpdateAddressState.Idle

                response
                    .collect {
                        when (it) {
                            is Resource.Default -> {}
                            is Resource.Failure -> state =
                                UpdateAddressState.Error(it.message, it.failureStatus)
                            is Resource.Loading -> state = UpdateAddressState.Loading
                            is Resource.Success -> state = UpdateAddressState.FetchStates(it.value)
                        }
                    }
                state
            } catch (e: Exception) {
                UpdateAddressState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }

    }

    private fun fetchDistrictList(stateId: Int) {
        viewModelScope.launch {
            _state.value = UpdateAddressState.Loading
            _state.value = try {
                val response =  fetchDistrictsUseCase.invoke(stateId)

                var state: UpdateAddressState = UpdateAddressState.Idle

                response
                    .collect {
                        when (it) {
                            is Resource.Default -> {}
                            is Resource.Failure -> state =
                                UpdateAddressState.Error(it.message, it.failureStatus)
                            is Resource.Loading -> state = UpdateAddressState.Loading
                            is Resource.Success -> state =
                                UpdateAddressState.FetchDistricts(it.value)
                        }
                    }
                state
            } catch (e: Exception) {
                UpdateAddressState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }

    }

    private fun fetchSubDistrictList(districtId: Int) {
        viewModelScope.launch {
            _state.value = UpdateAddressState.Loading
            _state.value = try {
                val response =  fetchSubDistrictsUseCase.invoke(districtId)

                var state: UpdateAddressState = UpdateAddressState.Idle

                response
                    .collect {
                        when (it) {
                            is Resource.Default -> {}
                            is Resource.Failure -> state =
                                UpdateAddressState.Error(it.message, it.failureStatus)
                            is Resource.Loading -> state = UpdateAddressState.Loading
                            is Resource.Success -> state =
                                UpdateAddressState.FetchSubDistricts(it.value)
                        }
                    }
                state
            } catch (e: Exception) {
                UpdateAddressState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }

    }

    private fun fetchVillageList(subDistrictId: Int) {
        viewModelScope.launch {
            _state.value = UpdateAddressState.Loading
            _state.value = try {
                val response =  fetchVillagesUseCase.invoke(subDistrictId)

                var state: UpdateAddressState = UpdateAddressState.Idle

                response
                    .collect {
                        when (it) {
                            is Resource.Default -> {}
                            is Resource.Failure -> state =
                                UpdateAddressState.Error(it.message, it.failureStatus)
                            is Resource.Loading -> state = UpdateAddressState.Loading
                            is Resource.Success -> state =
                                UpdateAddressState.FetchVillages(it.value)
                        }
                    }
                state
            } catch (e: Exception) {
                UpdateAddressState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun setUpBundleData(arguments : Bundle?) {
        arguments?.let {
            isEditMode = arguments.getBoolean(IS_EDIT)
            addressUrl = arguments.getString(URL)
        }
        if (isEditMode) {
            fetchAddressFormData(addressUrl.toString())
        } else {
            fetchStateList(UpdateAddressFragmentV1.COMPANY_ID)
        }
    }
}