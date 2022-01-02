package com.webkul.mobikul.odoo.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.connection.ApiConnection
import com.webkul.mobikul.odoo.connection.CustomObserver
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.databinding.ActivityNewAddressBinding
import com.webkul.mobikul.odoo.firebase.FirebaseAnalyticsImpl
import com.webkul.mobikul.odoo.helper.IntentHelper
import com.webkul.mobikul.odoo.helper.SnackbarHelper
import com.webkul.mobikul.odoo.model.BaseResponse
import com.webkul.mobikul.odoo.model.customer.address.AddressFormResponse
import com.webkul.mobikul.odoo.model.customer.address.AddressRequestBody
import com.webkul.mobikul.odoo.model.customer.address.addressBodyParams.*
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.DistrictListResponse
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.StateListResponse
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.SubDistrictListResponse
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.VillageListResponse
import com.webkul.mobikul.odoo.model.generic.DistrictData
import com.webkul.mobikul.odoo.model.generic.StateData
import com.webkul.mobikul.odoo.model.generic.SubDistrictData
import com.webkul.mobikul.odoo.model.generic.VillageData
import com.webkul.mobikul.odoo.model.home.HomePageResponse
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class NewAddressActivity : AppCompatActivity() {

    private val TAG = "NewAddressFragment"
    private val MAP_PIN_LOCATION_REQUEST_CODE = 103
    var mBinding: ActivityNewAddressBinding? = null
    var countryListHashmap = HashMap<String, Int>()
    var stateListHashmap = HashMap<String, StateData>()
    var districtListHashmap = HashMap<String, DistrictData>()
    var subDistrictListHashmap = HashMap<String, SubDistrictData>()
    var villageListHashmap = HashMap<String, VillageData>()

    var countryList = ArrayList<String>()
    var statesList = ArrayList<String>()
    var districtsList = ArrayList<String>()
    var subDistrictsList = ArrayList<String>()
    var villagesList = ArrayList<String>()


    var statesAvailable = true
    var districtsAvailable = true
    var subDistrictsAvailable = true
    var villagesAvailable = true

    var addressRequestBody = AddressRequestBody()

    var homePageResponse: HomePageResponse? = null
    var isMissingDetails: Boolean = false
    var selectedStateId = ""

    var isEditMode = false
    var addressUrl : String = ""
    var STATE_SPINNER_INITIAL_SELECTION = -1

    var alertDialog : SweetAlertDialog? = null

    private val RESET_SPINNERS_FROM_STATE_UPTO_VILLAGE = 1
    private val RESET_SPINNERS_FROM_DISTRICT_UPTO_VILLAGE = 2
    private val RESET_SPINNERS_FROM_SUB_DISTRICT_UPTO_VILLAGE = 3
    private val RESET_SPINNERS_VILLAGE = 4

    private val COMPANY_ID = 1
    private val UNSELECTED_POSITION = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_new_address)
        setBundleData()
        setTitle(getString(R.string.add_new_address))
        fetchCountry()

        mBinding!!.saveAddressBtn.setOnClickListener {
            if(checkIfSubRegionsAreLoading()) {
                if (isMissingDetails)
                    validateMandatoryFeilds(selectedStateId)
                else
                    validateAddressEditTextFeilds()
            }
            else{
                showShortToast(getString(R.string.missing_feilds_in_address_form))
            }
        }
    }

    private fun showShortToast(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    private fun checkIfSubRegionsAreLoading(): Boolean {
        return statesAvailable && districtsAvailable && subDistrictsAvailable && villagesAvailable
    }

    private fun setBundleData() {
        if (intent.hasExtra(BundleConstant.BUNDLE_KEY_HOME_PAGE_RESPONSE))
            homePageResponse =
                intent.extras?.getParcelable(BundleConstant.BUNDLE_KEY_HOME_PAGE_RESPONSE)
        if (intent.hasExtra(BundleConstant.BUNDLE_KEY_NAME))
            mBinding?.nameEt?.setText(intent.extras?.getString(BundleConstant.BUNDLE_KEY_NAME))
        else
            showNameET()
        if (intent.hasExtra(BundleConstant.BUNDLE_KEY_PHONE_NUMBER))
            mBinding?.telephoneEt?.setText(intent.extras?.getString(BundleConstant.BUNDLE_KEY_PHONE_NUMBER))
        else
            showTelephoneEt()

        if(intent.hasExtra(BundleConstant.BUNDLE_KEY_URL)) {
            setEditmodeData()
        }
    }

    private fun setEditmodeData() {
        isEditMode = true
        addressUrl = intent.extras?.getString(BundleConstant.BUNDLE_KEY_URL) ?: ""
        fetchCurrentAddress(addressUrl)
    }

    private fun fetchCurrentAddress(url: String) {
        ApiConnection.getAddressFormData(this,url).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(getCurrentAddressObserver())
    }

    private fun getCurrentAddressObserver(): Observer<BaseResponse> {
        return object : CustomObserver<BaseResponse>(this) {
            override fun onNext(baseResponse: BaseResponse) {
                super.onNext(baseResponse)
                resetSpinners(RESET_SPINNERS_FROM_STATE_UPTO_VILLAGE)
                val addressFormResponse = baseResponse as AddressFormResponse
                setDataOnFeilds(addressFormResponse)
                statesAvailable = false
                checkIfStateIdMissing(addressFormResponse)
            }
        }
    }

    private fun checkIfStateIdMissing(addressFormResponse: AddressFormResponse) {
        if(!addressFormResponse.stateId.isNullOrEmpty())
            fetchStates(addressFormResponse.stateId.toInt())
        else
            fetchStates(UNSELECTED_POSITION)
    }

    private fun setDataOnFeilds(addressFormResponse: AddressFormResponse) {
        mBinding?.apply {
            nameEt.setText(addressFormResponse.name)
            telephoneEt.setText(addressFormResponse.phone)
            if(!addressFormResponse.stateId.isNullOrEmpty())
                STATE_SPINNER_INITIAL_SELECTION = addressFormResponse.stateId.toInt()
        }
    }

    private fun showTelephoneEt() {
        mBinding?.apply {
            telephoneEtContainer.visibility = View.VISIBLE
        }
    }

    private fun showNameET() {
        mBinding?.apply {
            nameEtContainer.visibility = View.VISIBLE
        }
    }

    private fun resetSpinners(i: Int) {
        when (i) {
            RESET_SPINNERS_FROM_STATE_UPTO_VILLAGE -> {
                resetSpinnerAndPostalCode(mBinding!!.provinceSpinner)
                resetSpinnerAndPostalCode(mBinding!!.districtSpinner)
                resetSpinnerAndPostalCode(mBinding!!.subDistrictSpinner)
                resetSpinnerAndPostalCode(mBinding!!.villageSpinner)
            }
            RESET_SPINNERS_FROM_DISTRICT_UPTO_VILLAGE -> {
                resetSpinnerAndPostalCode(mBinding!!.districtSpinner)
                resetSpinnerAndPostalCode(mBinding!!.subDistrictSpinner)
                resetSpinnerAndPostalCode(mBinding!!.villageSpinner)
            }
            RESET_SPINNERS_FROM_SUB_DISTRICT_UPTO_VILLAGE -> {
                resetSpinnerAndPostalCode(mBinding!!.subDistrictSpinner)
                resetSpinnerAndPostalCode(mBinding!!.villageSpinner)
            }
            RESET_SPINNERS_VILLAGE -> resetSpinnerAndPostalCode(mBinding!!.villageSpinner)
            else -> resetSpinnerAndPostalCode(mBinding!!.provinceSpinner)
        }
    }

    private fun resetSpinnerAndPostalCode(spinner: AppCompatSpinner) {
        spinner.adapter = null
        mBinding!!.villageCodeEt.text = ""
    }

    private fun fetchCountry() {
        addCountriesToList()
        setCountrySpinner()
    }

    private fun addCountriesToList() {
        countryList.add(getString(R.string.indonesia))
    }

    private fun setCountrySpinner() {
        setCountrySpinnerAdapter()
        setCountrySpinnerSelectionListener()
    }

    private fun setCountrySpinnerAdapter() {
        mBinding!!.countrySpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            countryList
        )
    }

    private fun setCountrySpinnerSelectionListener() {
        mBinding!!.countrySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    addressRequestBody.setCompany_id(COMPANY_ID.toString())
                    resetSpinners(RESET_SPINNERS_FROM_STATE_UPTO_VILLAGE)
                    fetchStates(UNSELECTED_POSITION);
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }


    private fun fetchStates(stateId: Int) {
        ApiConnection.getStates(this, COMPANY_ID).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<StateListResponse?>(this) {
                override fun onNext(stateListResponse: StateListResponse) {
                    super.onNext(stateListResponse)
                    val pos = refreshStatesData(stateListResponse, stateId)
                    setUpStateSpinner()
                    setSpinnerItemSelection(pos)
                }
            })
    }

    private fun setSpinnerItemSelection(pos: Int) {
        if (pos != UNSELECTED_POSITION) mBinding!!.provinceSpinner.setSelection(pos)
    }

    private fun setUpStateSpinner() {
        statesAvailable = true
        setUpStateSpinnerAdapter()
        setUpStateSpinnerAdapterListener()
    }

    private fun setUpStateSpinnerAdapter() {
        val stateArrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            statesList
        )
        mBinding!!.provinceSpinner.adapter = stateArrayAdapter
    }

    private fun setUpStateSpinnerAdapterListener() {
        mBinding!!.provinceSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    onStateSelected(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun onStateSelected(position: Int) {
        val selectedState = statesList[position]
        selectedStateId = stateListHashmap[selectedState]!!.id.toString()
        isMissingDetails = if (stateListHashmap[selectedState]!!.isAvailable) {
            resetSpinners(RESET_SPINNERS_FROM_DISTRICT_UPTO_VILLAGE)
            districtsAvailable = false
            fetchDistricts(stateListHashmap[selectedState]!!.id)
            setSubRegionFieldsVisibility(true)
            false
        } else {
            resetSpinners(RESET_SPINNERS_FROM_DISTRICT_UPTO_VILLAGE)
            setSubRegionFieldsVisibility(false)
            true
        }
    }

    private fun setSubRegionFieldsVisibility(show: Boolean) {
        mBinding?.apply {
            districtContainer.visibility = if(show) View.VISIBLE else View.GONE
            subDistrictContainer.visibility = if(show) View.VISIBLE else View.GONE
            villageContainer.visibility = if(show) View.VISIBLE else View.GONE
            postalCodeContainer.visibility = if(show) View.VISIBLE else View.GONE
            streetContainer.visibility = if(show) View.VISIBLE else View.GONE
        }
    }

    private fun validateMandatoryFeilds(selectedState: String) {
        val isFormFilledup = checkMandatoryFeilds()
        if(isFormFilledup) {
            showUnavailabilityAlertDialog(selectedStateId)
        } else {
            showErrorMessage(getString(R.string.missing_feilds_in_address_form))
        }
    }

    private fun showErrorMessage(errorMessage: String) {
        SnackbarHelper.getSnackbar(
            this,
            errorMessage,
            Snackbar.LENGTH_SHORT,
            SnackbarHelper.SnackbarType.TYPE_WARNING
        ).show()
    }

    private fun showUnavailabilityAlertDialog(unavailableStateId: Any) {
        alertDialog = SweetAlertDialog(this@NewAddressActivity, SweetAlertDialog.WARNING_TYPE)
            .setTitleText(getString(R.string.service_unavailable))
            .setContentText(getString(R.string.service_unavailablity_text))
            .setConfirmText(getString(R.string.confirm_small))
            .setConfirmClickListener { sweetAlertDialog: SweetAlertDialog ->
                makeEmptyRequestBody(selectedStateId)
            }

        alertDialog?.show()
    }

    private fun checkMandatoryFeilds(): Boolean {
        return isValid(mBinding!!.nameEt) && isValid(mBinding!!.telephoneEt)
    }

    private fun makeEmptyRequestBody(selectedState: String) {
        addressRequestBody.setName(mBinding!!.nameEt.text.toString())
        addressRequestBody.setPhone(mBinding!!.telephoneEt.text.toString())
        addressRequestBody.setStreet(mBinding!!.streetEt.text.toString())
        addressRequestBody.setState_id(selectedState)
        addressRequestBody.setZip("")
        addressRequestBody.setDistrict_id("")
        addressRequestBody.setSub_district_id("")
        addressRequestBody.setVillage_id("")
        saveAddress(addressRequestBody)
    }


    private fun refreshStatesData(stateListResponse: StateListResponse, stateId: Int): Int {
        statesList.clear()
        stateListHashmap.clear()
        var pos = UNSELECTED_POSITION
        for (i in stateListResponse.data.indices) {
            val stateData = stateListResponse.data[i]
            statesList.add(stateData.name)
            stateListHashmap[stateData.name] = stateData
            if (stateId == stateData.id.toInt()) pos = i
        }
        return pos
    }


    private fun fetchDistricts(state_id: Int) {
        ApiConnection.getDistricts(this, state_id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<DistrictListResponse?>(this) {
                override fun onNext(districtListResponse: DistrictListResponse) {
                    super.onNext(districtListResponse)
                    refreshDistrictData(districtListResponse)
                    setUpDistrictSpinner()
                }
            })
    }

    private fun refreshDistrictData(districtListResponse: DistrictListResponse) {
        districtsList.clear()
        districtListHashmap.clear()
        for (districtData in districtListResponse.data) {
            districtsList.add(districtData.name)
            districtListHashmap[districtData.name] = districtData
        }
    }

    private fun setUpDistrictSpinner() {
        districtsAvailable = true
        setUpDistrictSpinnerAdapter()
        setUpDistrictSpinnerAdapterListener()
    }

    private fun setUpDistrictSpinnerAdapter() {
        val districtArrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            districtsList
        )
        mBinding!!.districtSpinner.adapter = districtArrayAdapter
    }

    private fun setUpDistrictSpinnerAdapterListener() {
        mBinding!!.districtSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    onDistrictSelected(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun onDistrictSelected(position: Int) {
        val selectedState = districtsList[position]
        resetSpinners(RESET_SPINNERS_FROM_SUB_DISTRICT_UPTO_VILLAGE)
        subDistrictsAvailable = false
        districtListHashmap[selectedState]?.let { fetchSubDistricts(it.id) }
        addressRequestBody.setDistrict_id(districtListHashmap[selectedState]?.id.toString())
    }


    private fun fetchSubDistricts(district_id: Int) {
        ApiConnection.getSubDistricts(this, district_id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<SubDistrictListResponse?>(this) {
                override fun onNext(subDistrictListResponse: SubDistrictListResponse) {
                    super.onNext(subDistrictListResponse)
                    refreshSubdistrictData(subDistrictListResponse)
                    setUpSubdistrictSpinner()
                }
            })
    }

    private fun refreshSubdistrictData(subDistrictListResponse: SubDistrictListResponse) {
        subDistrictsList.clear()
        subDistrictListHashmap.clear()
        for (subDistrictData in subDistrictListResponse.data) {
            subDistrictsList.add(subDistrictData.name)
            subDistrictListHashmap[subDistrictData.name] = subDistrictData
        }
    }

    private fun setUpSubdistrictSpinner() {
        subDistrictsAvailable = true
        setUpSubdistrictSpinnerAdapter()
        setUpSubdistrictSpinnerAdapterListener()
    }

    private fun setUpSubdistrictSpinnerAdapter() {
        val subDistrictArrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            subDistrictsList
        )
        mBinding!!.subDistrictSpinner.adapter = subDistrictArrayAdapter
    }

    private fun setUpSubdistrictSpinnerAdapterListener() {
        mBinding!!.subDistrictSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    onSubDistrictSelected(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun onSubDistrictSelected(position: Int) {
        val selectedSubDistrict = subDistrictsList[position]
        resetSpinners(RESET_SPINNERS_VILLAGE)
        villagesAvailable = false
        subDistrictListHashmap[selectedSubDistrict]?.let { fetchVillage(it.id) }
        addressRequestBody.setSub_district_id(subDistrictListHashmap[selectedSubDistrict]?.id.toString())
    }


    private fun fetchVillage(subdistrict_id: Int) {
        addressRequestBody.setVillage_id(null)
        ApiConnection.getVillages(this, subdistrict_id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<VillageListResponse?>(this) {
                override fun onNext(villageListResponse: VillageListResponse) {
                    super.onNext(villageListResponse)
                    refreshVillageData(villageListResponse)
                    setUpVillageSpinner(villageListResponse)
                }
            })
    }

    private fun refreshVillageData(villageListResponse: VillageListResponse) {
        villagesList.clear()
        villageListHashmap.clear()
        for (villageData in villageListResponse.data) {
            villagesList.add(villageData.name)
            villageListHashmap[villageData.name] = villageData
        }
    }

    private fun setUpVillageSpinner(villageListResponse: VillageListResponse) {
        villagesAvailable = true
        setUpVillageSpinnerAdapter()
        setUpVillageSpinnerAdapterListener(villageListResponse)
    }

    private fun setUpVillageSpinnerAdapter() {
        val districtArrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            villagesList
        )
        mBinding!!.villageSpinner.adapter = districtArrayAdapter
    }

    private fun setUpVillageSpinnerAdapterListener(villageListResponse: VillageListResponse) {
        mBinding!!.villageSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    onVillageSelected(villageListResponse, position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }


    private fun onVillageSelected(villageListResponse: VillageListResponse, position: Int) {
        val villageData = villageListResponse.getData()[position]
        addressRequestBody.setVillage_id(villageData.id.toString())
        if (villageData.zip.equals(getString(R.string.false_), ignoreCase = true)) {
            setUnavailableVillageData(villageData)
        } else {
            setAvailableVillageData(villageData)
        }
    }

    private fun setAvailableVillageData(villageData: VillageData) {
        addressRequestBody.setZip(villageData.zip)
        addressRequestBody.setVillage_id(villageData.id.toString())
        mBinding!!.villageCodeEt.text = villageData.zip
    }

    private fun setUnavailableVillageData(villageData: VillageData) {
        addressRequestBody.setVillage_id(villageData.id.toString())
        setUnserviceableAreaDetails()
        showShortToast(getString(R.string.service_not_availabe_text))
        mBinding!!.villageCodeEt.text = ""
        addressRequestBody.setVillage_id("")
    }

    private fun setUnserviceableAreaDetails() {
        val missingState = stateListHashmap[addressRequestBody.getState_id()]!!.name
        val missingDistrict = districtListHashmap[addressRequestBody.getDistrict_id()]!!.name
        val missingSubDistrict = subDistrictListHashmap[addressRequestBody.getSub_district_id()]!!.name
        val missingVillage = villageListHashmap[addressRequestBody.getSub_district_id()]!!.name
        logFirebaseEvent(missingState, missingDistrict, missingSubDistrict, missingVillage)
    }

    private fun logFirebaseEvent(
        missingState: String,
        missingDistrict: String,
        missingSubDistrict: String,
        missingVillage: String
    ) {
        FirebaseAnalyticsImpl.logPostalCodeUnavailable(
            this,
            missingState,
            missingDistrict,
            missingSubDistrict,
            missingVillage
        )
    }


    private fun validateAddressEditTextFeilds() {
        if (isValid(mBinding!!.nameEt) && isValid(mBinding!!.telephoneEt) && addressRequestBody.village_id != null
        ) {
            makeRequestBody()
        } else {
            SnackbarHelper.getSnackbar(
                this,
                getString(R.string.missing_feilds_in_address_form),
                Snackbar.LENGTH_SHORT,
                SnackbarHelper.SnackbarType.TYPE_WARNING
            ).show()
        }
    }

    fun isValid(etFeild: TextInputEditText): Boolean {
        return !etFeild.text.isNullOrEmpty()
    }

    private fun makeRequestBody() {
        addressRequestBody.setName(mBinding!!.nameEt.text.toString())
        addressRequestBody.setPhone(mBinding!!.telephoneEt.text.toString())
        addressRequestBody.setStreet(mBinding?.streetEt?.text.toString())
        addressRequestBody.setState_id(selectedStateId)
        saveAddress(addressRequestBody)
    }

    private fun saveAddress(addressRequestBody: AddressRequestBody) {
        if(isEditMode) {
            ApiConnection.editAddress(this,addressRequestBody.newAddressData,addressUrl)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(getAddressResponseObserver(getString(R.string.address_edit_text)))
        }
        else {
            ApiConnection.addNewAddress(this, addressRequestBody.newAddressData)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(getAddressResponseObserver(getString(R.string.address_added_text)))
        }
    }

    private fun getAddressResponseObserver(string:String): Observer<BaseResponse> {
        return object : CustomObserver<BaseResponse>(this) {
            override fun onNext(baseResponse: BaseResponse) {
                super.onNext(baseResponse)
                if (baseResponse.isSuccess) {
                    showShortToast(string)
                    if (homePageResponse != null)
                        IntentHelper.continueShopping(this@NewAddressActivity, homePageResponse);
                    else {
                        finish()
                    }
                    hideDialog()
                }
            }
        }
    }

    private fun hideDialog() {
        if(alertDialog?.isShowing == true) {
            alertDialog?.dismiss()
        }
    }

}