package com.webkul.mobikul.odoo.ui.checkout.address

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl
import com.webkul.mobikul.odoo.analytics.models.AnalyticsAddressDataModel
import com.webkul.mobikul.odoo.core.extension.close
import com.webkul.mobikul.odoo.core.extension.makeGone
import com.webkul.mobikul.odoo.core.extension.makeVisible
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.*
import com.webkul.mobikul.odoo.databinding.FragmentUpdateAddressV1Binding
import com.webkul.mobikul.odoo.helper.AlertDialogHelper
import com.webkul.mobikul.odoo.helper.ErrorConstants
import com.webkul.mobikul.odoo.helper.SnackbarHelper
import com.webkul.mobikul.odoo.model.customer.address.AddressRequestBody
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UpdateAddressFragmentV1 @Inject constructor() : BindingBaseFragment<FragmentUpdateAddressV1Binding>(),
    IView<UpdateAddressIntent, UpdateAddressState, UpdateAddressEffect> {


    override val layoutId = R.layout.fragment_update_address_v1
    private val viewModel: UpdateAddressViewModel by viewModels()
    private val analyticsModel = AnalyticsAddressDataModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setOnClickListeners()
        triggerIntent(UpdateAddressIntent.BundleData(arguments))
    }

    private fun setObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.state.collect {
                render(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.effect.collect{
                render(it)
            }
        }
    }

    private fun setOnClickListeners() {

        binding.tbAddress.setNavigationOnClickListener {
            this.close()
        }

        binding.btnSaveAddress.setOnClickListener {

                triggerIntent(UpdateAddressIntent.ValidateMandatoryFields(
                    viewModel.isServiceAvailable,
                    AddressRequestBody().apply {

                        name = binding.etName.text?.toString().orEmpty()
                        phone = binding.etTelephone.text?.toString().orEmpty()
                        company_id = COMPANY_ID.toString()
                        country_id = viewModel.selectedCountryId?.toString().orEmpty()
                        state_id = viewModel.selectedStateId.orEmpty()
                        district_id =
                            if (viewModel.isServiceAvailable) viewModel.selectedDistrictId.orEmpty() else ""
                        sub_district_id =
                            if (viewModel.isServiceAvailable) viewModel.selectedSubDistrictId.orEmpty() else ""
                        village_id =
                            if (viewModel.isServiceAvailable) viewModel.selectedVillageId.orEmpty() else ""
                        zip =
                            if (viewModel.isServiceAvailable) viewModel.selectedZip.orEmpty() else ""
                        street = if (viewModel.isServiceAvailable) binding.etStreet.text?.toString()
                            .orEmpty() else ""

                    }
                ))

        }
    }

    override fun render(state: UpdateAddressState) {
        when(state) {
            is UpdateAddressState.AddAddress -> onAddressAdded()
            is UpdateAddressState.EditAddress -> onAddressEdited()
            is UpdateAddressState.Error -> onError(state.message, state.failureStatus)
            is UpdateAddressState.Toast -> showToast(state.message.toString())
            is UpdateAddressState.UpdateError -> onAddressUpdateFailed(state.message, state.failureStatus, state.code ?: 0)
            is UpdateAddressState.FetchAddressFormData -> onAddressFormDataReceived(state.addressFormEntity)
            is UpdateAddressState.FetchDistricts -> onDistrictListReceived(state.districtList)
            is UpdateAddressState.FetchStates -> onStateListReceived(state.stateList)
            is UpdateAddressState.FetchSubDistricts -> onSubDistrictListReceived(state.subDistrictList)
            is UpdateAddressState.FetchVillages -> onVillageListReceived(state.villageList)
            is UpdateAddressState.Idle -> {}
            is UpdateAddressState.Loading -> {}
            is UpdateAddressState.WarningSnackBar -> onWarningSnackBar(state.message)
            is UpdateAddressState.OnSuccessfulValidateFields -> onSuccessfulValidation(state.addressRequestBody)
            is UpdateAddressState.DistrictSelected -> onDistrictSelected(state.districtEntity)
            is UpdateAddressState.ServiceNotAvailable -> onServiceNotAvailable()
            is UpdateAddressState.StateSelected -> onStateSelected(state.stateEntity)
            is UpdateAddressState.SubDistrictSelected -> onSubDistrictSelected(state.subDistrictEntity)
            is UpdateAddressState.VillageSelected -> onVillageSelected(state.villageEntity)
        }
    }

    override fun triggerIntent(intent: UpdateAddressIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }

    private fun onAddressFormDataReceived(addressFormEntity: AddressFormEntity) {
        binding.apply {
            addressFormEntity.name?.let { etName.setText(it) }
            addressFormEntity.phone?.let { etTelephone.setText(it) }
            addressFormEntity.street?.let { etStreet.setText(it) }
        }

        triggerIntent(UpdateAddressIntent.FetchStates(COMPANY_ID))
    }


    private fun onStateListReceived(stateListEntity: StateListEntity) {
        var selectedStatePosition = UNSELECTED_SPINNER_POSITION

        viewModel.selectedStateId?.let {
            selectedStatePosition = stateListEntity.states.indexOfFirst { state -> state.id.toString().equals(it, true) }
        }

        val stateArrayAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            stateListEntity.states.map { it.name.toString() }
        )

        binding.llProvinceContainer.visibility = View.VISIBLE
        binding.spProvince.adapter = stateArrayAdapter

        if (selectedStatePosition != UNSELECTED_SPINNER_POSITION) binding.spProvince.setSelection(selectedStatePosition)

        binding.spProvince.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    triggerIntent(UpdateAddressIntent.SelectState(stateListEntity.states[position]))
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

    }

    private fun onStateSelected(selectedState: StateEntity) {

        if (selectedState.available == true) {

            triggerIntent(UpdateAddressIntent.FetchDistricts(selectedState.id))

        } else {

            triggerIntent(UpdateAddressIntent.ServiceNotAvailable)
        }

        analyticsModel.province = selectedState.name.toString()
    }

    private fun onServiceNotAvailable() {

        binding.llDistrictContainer.makeGone()
        binding.llSubDistrictContainer.makeGone()
        binding.llVillageContainer.makeGone()
        binding.llPostalCodeContainer.makeGone()
        binding.llStreetContainer.makeGone()

    }

    private fun onDistrictListReceived(districtListEntity: DistrictListEntity) {
        var selectedDistrictPosition = UNSELECTED_SPINNER_POSITION

        viewModel.selectedDistrictId?.let {
            selectedDistrictPosition = districtListEntity.data.indexOfFirst { district -> district.id.toString().equals(it, true) }
        }

        val districtArrayAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            districtListEntity.data.map { it.name.toString() }
        )

        binding.llDistrictContainer.makeVisible()
        binding.spDistrict.adapter = districtArrayAdapter

        if (selectedDistrictPosition != UNSELECTED_SPINNER_POSITION) binding.spDistrict.setSelection(selectedDistrictPosition)

        binding.spDistrict.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    triggerIntent(UpdateAddressIntent.SelectDistrict(districtListEntity.data[position]))

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }


    }

    private fun onDistrictSelected(selectedDistrict: DistrictEntity) {

        triggerIntent(UpdateAddressIntent.FetchSubDistricts(selectedDistrict.id))
        analyticsModel.district = selectedDistrict.name.toString()
    }

    private fun onSubDistrictListReceived(subDistrictListEntity: SubDistrictListEntity) {
        var selectedSubDistrictPosition = UNSELECTED_SPINNER_POSITION

        viewModel.selectedDistrictId?.let {
            selectedSubDistrictPosition = subDistrictListEntity.subDistricts.indexOfFirst { subDistrict -> subDistrict.id.toString().equals(it, true) }
        }

        val subDistrictArrayAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            subDistrictListEntity.subDistricts.map { it.name.toString() }
        )

        binding.spSubDistrict.adapter = subDistrictArrayAdapter
        binding.llSubDistrictContainer.visibility =  View.VISIBLE

        if (selectedSubDistrictPosition != UNSELECTED_SPINNER_POSITION) binding.spSubDistrict.setSelection(selectedSubDistrictPosition)

        binding.spSubDistrict.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    triggerIntent(UpdateAddressIntent.SelectSubDistrict(subDistrictListEntity.subDistricts[position]))
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

    }

    private fun onSubDistrictSelected(subDistrictEntity: SubDistrictEntity) {

        triggerIntent(UpdateAddressIntent.FetchVillages(subDistrictEntity.id))
        analyticsModel.subDistrict = subDistrictEntity.name.toString()
    }

    private fun onVillageListReceived(villageListEntity: VillageListEntity) {
        var selectedVillagePosition = UNSELECTED_SPINNER_POSITION

        viewModel.selectedVillageId?.let {
            selectedVillagePosition = villageListEntity.villages.indexOfFirst { village -> village.id.toString().equals(it, true) }
        }

        val villageArrayAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            villageListEntity.villages.map { it.name.toString() }
        )

        binding.spVillage.adapter = villageArrayAdapter
        binding.llVillageContainer.visibility =  View.VISIBLE
        binding.llPostalCodeContainer.visibility =  View.VISIBLE
        binding.llStreetContainer.visibility =  View.VISIBLE


        if (selectedVillagePosition != UNSELECTED_SPINNER_POSITION) binding.spVillage.setSelection(selectedVillagePosition)

        binding.spVillage.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    triggerIntent(UpdateAddressIntent.SelectVillage(villageListEntity.villages[position]))

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun onVillageSelected(villageEntity: VillageEntity) {

        binding.etVillageCode.text = villageEntity.zip.toString()
        analyticsModel.village = villageEntity.name.toString()
    }

    private fun onSuccessfulValidation(addressRequestBody: AddressRequestBody) {

        analyticsModel.isServicable = viewModel.isServiceAvailable

        if (viewModel.isServiceAvailable) {

            if (viewModel.isEditMode) {

                AnalyticsImpl.trackAddressUpdateSubmitted(
                    analyticsModel
                )
                triggerIntent(
                    UpdateAddressIntent.EditAddress(
                        viewModel.addressUrl.toString(),
                        addressRequestBody
                    )
                )
            } else {

                AnalyticsImpl.trackAddressAdded(
                    analyticsModel
                )
                triggerIntent(
                    UpdateAddressIntent.AddAddress(
                        addressRequestBody
                    )
                )
            }

        } else {

            AlertDialogHelper.getAlertDialog(
                requireContext(),
                SweetAlertDialog.WARNING_TYPE,
                getString(R.string.service_unavailable),
                getString(R.string.service_unavailablity_text),
                false,
                false
            ).also { dialog ->

                dialog.setConfirmText(getString(R.string.confirm_small))
                    .setConfirmClickListener { sweetAlertDialog: SweetAlertDialog? ->
                        dialog.hide()

                        if (viewModel.isEditMode) {

                            AnalyticsImpl.trackAddressUpdateSubmitted(
                                analyticsModel
                            )
                            triggerIntent(
                                UpdateAddressIntent.EditAddress(
                                    viewModel.addressUrl.toString(),
                                    addressRequestBody
                                )
                            )
                        } else {

                            AnalyticsImpl.trackAddressAdded(
                                analyticsModel
                            )
                            triggerIntent(
                                UpdateAddressIntent.AddAddress(
                                    addressRequestBody
                                )
                            )
                        }

                    }
                dialog.show()
            }

        }
    }


    private fun onError( message: String?, failureStatus: FailureStatus) {
        showErrorState(failureStatus, message)
    }

    private fun onAddressUpdateFailed (message: String?, failureStatus: FailureStatus, code: Int) {
        showErrorState(failureStatus, message)
        AnalyticsImpl.trackAddressUpdateFailed(
            analyticsModel,
            code,
            ErrorConstants.AddressUpdateError.errorType,
            message

        )
    }

    private fun onWarningSnackBar(message: String?) {
        showSnackbarMessage(message.toString(), SnackbarHelper.SnackbarType.TYPE_WARNING)
    }

    private fun onAddressAdded() {
        showToast(getString(R.string.address_added_text))
        AnalyticsImpl.trackAddressUpdateSuccessfull(analyticsModel)
        requireActivity().supportFragmentManager.setFragmentResult(
            CheckoutAddressBookFragmentV1.UPDATE_ADDRESS_RESULT,
            Bundle()
        )
        this.close()
    }

    private fun onAddressEdited() {
        showToast(getString(R.string.address_edit_text))
        AnalyticsImpl.trackAddressUpdateSuccessfull(analyticsModel)
        requireActivity().supportFragmentManager.setFragmentResult(
            CheckoutAddressBookFragmentV1.UPDATE_ADDRESS_RESULT,
            Bundle()
        )
        this.close()
    }


    override fun render(effect: UpdateAddressEffect) {

    }


    companion object {

        const val URL = "URL"
        const val IS_EDIT = "IS_EDIT"
        const val COMPANY_ID = 1
        const val UNSELECTED_SPINNER_POSITION = -1

        @JvmStatic
        fun newInstance(isEdit: Boolean, url: String? = null) =
            UpdateAddressFragmentV1().apply {
                arguments = Bundle().apply {
                    putBoolean(IS_EDIT, isEdit)
                    putString(URL, url)
                }
            }
    }

}