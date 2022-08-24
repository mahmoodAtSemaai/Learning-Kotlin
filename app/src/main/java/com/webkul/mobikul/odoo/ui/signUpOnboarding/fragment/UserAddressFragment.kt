package com.webkul.mobikul.odoo.ui.signUpOnboarding.fragment

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.constant.ApplicationConstant
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.extension.*
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.address.DistrictEntity
import com.webkul.mobikul.odoo.data.entity.address.StateEntity
import com.webkul.mobikul.odoo.data.entity.address.SubDistrictEntity
import com.webkul.mobikul.odoo.data.entity.address.VillageEntity
import com.webkul.mobikul.odoo.data.request.UserAddressRequest
import com.webkul.mobikul.odoo.data.request.UserLocationRequest
import com.webkul.mobikul.odoo.databinding.FragmentUserAddressBinding
import com.webkul.mobikul.odoo.databinding.ItemDialogDiscardChangesBinding
import com.webkul.mobikul.odoo.databinding.ItemUnavailableAddressBinding
import com.webkul.mobikul.odoo.ui.signUpOnboarding.adapter.*
import com.webkul.mobikul.odoo.ui.signUpOnboarding.effect.UserAddressEffect
import com.webkul.mobikul.odoo.ui.signUpOnboarding.intent.UserAddressIntent
import com.webkul.mobikul.odoo.ui.signUpOnboarding.intent.UserOnboardingIntent
import com.webkul.mobikul.odoo.ui.signUpOnboarding.state.UserAddressState
import com.webkul.mobikul.odoo.ui.signUpOnboarding.viewModel.UserAddressViewModel
import com.webkul.mobikul.odoo.ui.signUpOnboarding.viewModel.UserOnboardingViewModel
import com.webkul.mobikul.odoo.utils.GpsUtils
import com.webkul.mobikul.odoo.utils.GpsUtilsInterface
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
//TODO - Optimize as per coding principles and standards
class UserAddressFragment @Inject constructor() :
    BindingBaseFragment<FragmentUserAddressBinding>(),
    IView<UserAddressIntent, UserAddressState, UserAddressEffect>,
    ProvinceRecyclerViewClickInterface, SubDistrictRecyclerViewClickInterface,
    DistrictRecyclerViewClickInterface,
    VillageRecyclerViewClickInterface, GpsUtilsInterface {

    override val layoutId: Int = R.layout.fragment_user_address
    private val viewModel: UserAddressViewModel by viewModels()
    lateinit var userOnboardingViewModel: UserOnboardingViewModel
    private val locationUtils = GpsUtils(this@UserAddressFragment)

    private val provinceType = 1
    private val districtType = 2
    private val subDistrictType = 3
    private val villageType = 4
    private var province = emptyList<View>()
    private var district = emptyList<View>()
    private var subDistrict = emptyList<View>()
    private var village = emptyList<View>()
    private lateinit var dialog: Dialog
    private lateinit var dialogBinding: ItemDialogDiscardChangesBinding

    companion object {
        fun newInstance(isAddressStagePending: Boolean) =
            UserAddressFragment().also { userAddressFragment ->
                userAddressFragment.arguments = Bundle().apply {
                    putBoolean(BundleConstant.BUNDLE_KEY_IS_ADDRESS_PENDING, isAddressStagePending)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = Dialog(requireContext())
        dialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.item_dialog_discard_changes,
            null,
            false
        )
        dialogBinding.tvLocation.text = requireActivity().applicationContext.getString(R.string.exit_the_app)
        dialogBinding.btnKeepEditing.text = requireActivity().applicationContext.getString(R.string.no)
        dialogBinding.btnDiscard.text = requireActivity().applicationContext.getString(R.string.yes)
        setObservers()
        setListners()

        getCustomerId()
        setViews()
        setUserOnboardingViewModel()
    }

    private fun setViews() {
        province = listOf(
            binding.clProvince, binding.tvProvinceValue,
            binding.ivProvinceDropdownArrow, binding.clProvinceSpinner,
            binding.etProvinceSearch, binding.rvProvinceList, binding.tvNoResultFound
        )

        district = listOf(
            binding.clDistrictCity,
            binding.tvDistrictCityValue,
            binding.ivDistrictCityDropdownArrow,
            binding.clDistrictCitySpinner,
            binding.etDistrictCitySearch,
            binding.rvDistrictCityList,
            binding.tvDistrictNoResultFound
        )

        subDistrict = listOf(
            binding.clSubDistrict,
            binding.tvSubDistrictValue,
            binding.ivSubDistrictDropdownArrow,
            binding.clSubDistrictSpinner,
            binding.etSubDistrictSearch,
            binding.rvSubDistrictList,
            binding.tvSubDistrictNoResultFound
        )

        village = listOf(
            binding.clVillage, binding.tvVillageValue,
            binding.ivVillageDropdownArrow, binding.clVillageSpinner,
            binding.etVillageSearch, binding.rvVillageList, binding.tvVillageNoResultFound
        )

        triggerIntent(UserAddressIntent.SpinnerState(subDistrictType))
        triggerIntent(UserAddressIntent.SpinnerState(villageType))
    }

    private fun getCustomerId() {
        triggerIntent(UserAddressIntent.GetCustomerId)
    }

    private fun setUserOnboardingViewModel() {
        activity?.let {
            userOnboardingViewModel =
                ViewModelProviders.of(it).get(UserOnboardingViewModel::class.java)
        }
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.state.collect {
                render(it)
            }
        }

        lifecycleScope.launch {
            viewModel.effect.collect {
                render(it)
            }
        }
    }

    private fun setListners() {
        binding.btnContinue.setOnClickListener {
            triggerIntent(
                UserAddressIntent.Continue(
                    UserAddressRequest(
                        street = binding.etStreet.text.toString(),
                        street2 = binding.etAddressAdditionalInfo.text.toString()
                    )
                )
            )
        }
        binding.etStreet.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                triggerIntent(
                    UserAddressIntent.VerifyFields(
                        UserAddressRequest(
                            street = s.toString(),
                            street2 = binding.etStreet.text.toString()
                        )
                    )
                )
            }
        })
        binding.clVillage.setOnClickListener {
            triggerIntent(
                UserAddressIntent.ExpandSpinner(
                    binding.clVillageSpinner,
                    binding.ivVillageDropdownArrow,
                    villageType
                )
            )
        }
        binding.clProvince.setOnClickListener {
            triggerIntent(
                UserAddressIntent.ExpandSpinner(
                    binding.clProvinceSpinner,
                    binding.ivProvinceDropdownArrow,
                    provinceType
                )
            )
        }
        binding.clDistrictCity.setOnClickListener {
            triggerIntent(
                UserAddressIntent.ExpandSpinner(
                    binding.clDistrictCitySpinner,
                    binding.ivDistrictCityDropdownArrow,
                    districtType
                )
            )
        }
        binding.clSubDistrict.setOnClickListener {
            triggerIntent(
                UserAddressIntent.ExpandSpinner(
                    binding.clSubDistrictSpinner,
                    binding.ivSubDistrictDropdownArrow,
                    subDistrictType
                )
            )
        }

        dialogBinding.btnDiscard.setOnClickListener {
            triggerIntent(UserAddressIntent.CloseApp)
        }

        dialogBinding.btnKeepEditing.setOnClickListener {
            dialog.dismiss()
        }

        handleBack()
    }

    private fun getArgs() {
        triggerIntent(UserAddressIntent.GetArgs(arguments))
    }

    private fun setMandatoryFieldsObserver() {
        triggerIntent(
            UserAddressIntent.VerifyFields(
                UserAddressRequest(
                    street = binding.etStreet.text.toString(),
                    street2 = binding.etAddressAdditionalInfo.text.toString()
                )
            )
        )
    }

    override fun render(effect: UserAddressEffect) {
        when(effect){
            is UserAddressEffect.LocationDialog -> {
                dismissProgressDialog()
                showLocationDialog()
            }
            is UserAddressEffect.ResetDistrictSpinner -> resetDistrictSpinner(effect.provinceId)
            is UserAddressEffect.ResetSubDistrictSpinner -> resetSubDistrictSpinner(effect.districtId)
            is UserAddressEffect.ResetVillageSpinner -> resetVillageSpinner(effect.subDistrictId)
            is UserAddressEffect.EnableSpinner -> enableSpinner(effect.enable, effect.spinnerType)
        }
    }

    override fun triggerIntent(intent: UserAddressIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }

    private fun triggerActivityIntent(intent: UserOnboardingIntent) {
        lifecycleScope.launch {
            userOnboardingViewModel.intents.send(intent)
        }
    }

    override fun render(state: UserAddressState) {
        when (state) {
            is UserAddressState.Idle -> { dismissProgressDialog()}
            is UserAddressState.Loading -> showProgressDialog()
            is UserAddressState.FetchProvinceData -> getProvinceData()
            is UserAddressState.GetProvinceData -> {
                dismissProgressDialog()
                setProvinceRv(state.provinceData)
            }
            is UserAddressState.AvailableProvince -> availableProvince(
                state.provinceName,
            )
            is UserAddressState.UnavailableProvince -> unavailableProvince(state.provinceName)
            is UserAddressState.ProvinceSearchResult -> provinceRvSearchResult(
                state.adapter,
                state.provinceData
            )
            is UserAddressState.GetDistrictData -> {
                dismissProgressDialog()
                setDistrictRv(state.districtData)
            }
            is UserAddressState.SelectedDistrict -> selectedDistrict(
                state.districtName,
            )
            is UserAddressState.DistrictSearchResult -> districtRvSearchResult(
                state.adapter,
                state.districtData
            )
            is UserAddressState.GetSubDistrictData -> {
                dismissProgressDialog()
                setSubDistrictRv(state.subDistrictData)
            }
            is UserAddressState.SelectedSubDistrict -> selectedSubDistrict(
                state.subDistrictName,
            )
            is UserAddressState.SubDistrictSearchResult -> subDistrictRvSearchResult(
                state.adapter,
                state.subDistrictData
            )
            is UserAddressState.GetVillageData -> {
                dismissProgressDialog()
                setVillageRv(state.villageData)
            }
            is UserAddressState.SelectedVillage -> selectedVillage(
                state.villageName,
                state.postalCode
            )
            is UserAddressState.VillageSearchResult -> villageRvSearchResult(
                state.adapter,
                state.villageData
            )
            is UserAddressState.ExpandSpinner -> expandSpinner(state.spinner, state.dropDown)
            is UserAddressState.ContinueBtn -> enableContinueBtn(state.enable)
            is UserAddressState.GetHomeData -> triggerIntent(UserAddressIntent.GetHomePageData)
            is UserAddressState.Error -> {
                if (state.failureStatus == FailureStatus.NO_INTERNET) {
                    noInternetState()
                }else{
                    showErrorState(state.failureStatus, state.message)
                }
            }
            is UserAddressState.CompletedAddress -> {
                dismissProgressDialog()
                setCompleted()
            }
            is UserAddressState.GetArgs -> getArgs()
            is UserAddressState.ShowLocationDialog -> {
                dismissProgressDialog()
                showLocationDialog() }
            is UserAddressState.EmptySearch -> emptySearch(state.spinnerType)
            is UserAddressState.CloseApp -> closeApp()
        }
    }

    private fun closeApp() {
        dialog.dismiss()
        requireActivity().finish()
    }

    private fun getSpinnerViews(spinnerValue: Int): List<View> {
        return when (spinnerValue) {
            provinceType -> province
            districtType -> district
            subDistrictType -> subDistrict
            else -> village
        }
    }

    private fun emptySearch(spinnerValue: Int) {
        val spinnerView = getSpinnerViews(spinnerValue)
        spinnerView[6].makeVisible()
        spinnerView[5].makeGone()
    }

    private fun resetDistrictSpinner(provinceId: Int) {
        binding.tvDistrictCityValue.text = getString(R.string.select_district_city)
        binding.clDistrictCitySpinner.makeGone()
        resetSubDistrictValues()
        setMandatoryFieldsObserver()
        triggerIntent(UserAddressIntent.DistrictSpinner(provinceId))
    }

    private fun resetSubDistrictSpinner(districtId: Int) {
        resetSubDistrictValues()
        setMandatoryFieldsObserver()
        triggerIntent(UserAddressIntent.SubDistrictSpinner(districtId))
    }

    private fun resetVillageSpinner(subDistrictId: Int) {
        resetVillageValues()
        setMandatoryFieldsObserver()
        triggerIntent(UserAddressIntent.VillageSpinner(subDistrictId))
    }

    private fun resetSubDistrictValues() {
        binding.tvSubDistrictValue.text = getString(R.string.select_sub_district)
        binding.clSubDistrictSpinner.makeGone()
        resetVillageValues()
        triggerIntent(UserAddressIntent.SpinnerState(subDistrictType))
    }

    private fun resetVillageValues() {
        binding.tvVillageValue.text = getString(R.string.select_village)
        binding.tvPostalCodeValue.text = ""
        binding.clVillageSpinner.makeGone()
        triggerIntent(UserAddressIntent.SpinnerState(villageType))
    }

    private fun enableSpinner(enable: Boolean, spinnerType: Int) {
        val view = getSpinnerViews(spinnerType)
        if (enable) {
            view[0].setBackground(R.drawable.shape_rectangular_white_bg_dark_gray_border_1dp_corner_4dp)
            (view[1] as TextView).setfontColor(R.color.text_color_primary)
            (view[2] as ImageView).setColor(R.color.background_orange)
        } else {
            view[0].setBackground(R.drawable.shape_disabled_address_bg)
            (view[1] as TextView).setfontColor(R.color.colorDarkGrey)
            (view[2] as ImageView).setColor(R.color.colorDarkGrey)
        }
    }

    private fun enableContinueBtn(enable: Boolean) {
        binding.btnContinue.isEnabled = enable
    }

    private fun expandSpinner(spinner: View, dropDown: ImageView) {
        spinner.makeVisible()
        dropDown.setImageResource(R.drawable.ic_up_arrow_color_primary)
    }

    private fun selectedDistrict(districtName: String) {
        setViewAfterSelect(district, districtName)
        enableSpinner(true, subDistrictType)
        setMandatoryFieldsObserver()
        triggerIntent(UserAddressIntent.ResetSubDistrictSpinner)
    }

    private fun selectedSubDistrict(subDistrictName: String) {
        setViewAfterSelect(subDistrict, subDistrictName)
        setMandatoryFieldsObserver()
        enableSpinner(true, villageType)
        triggerIntent(UserAddressIntent.ResetVillageSpinner)
    }

    private fun selectedVillage(villageName: String, postalCode: String) {
        binding.tvPostalCodeValue.text = postalCode
        setViewAfterSelect(village, villageName)
        setMandatoryFieldsObserver()
    }

    private fun unavailableProvince(provinceName: String) {
        binding.clAddressContiner.makeInvisible()
        setViewAfterSelect(province, provinceName)
        showUnavailableProvinceDialog()
        setMandatoryFieldsObserver()
        triggerIntent(UserAddressIntent.ResetDistrictSpinner)
    }

    private fun availableProvince(provinceName: String) {
        binding.clAddressContiner.makeVisible()
        setViewAfterSelect(province, provinceName)
        setMandatoryFieldsObserver()
        triggerIntent(UserAddressIntent.ResetDistrictSpinner)
    }

    private fun setViewAfterSelect(view: List<View>, selectedName: String) {
        view[3].makeGone()
        (view[2] as ImageView).setImageResource(R.drawable.ic_down_arrow_color_primary)
        (view[1] as TextView).text = selectedName
    }

    private fun showUnavailableProvinceDialog() {
        val dialog = Dialog(requireContext())
        val dialogBinding: ItemUnavailableAddressBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.item_unavailable_address,
            null,
            false
        )
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
        dialogBinding.btnDialogContinue.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun getProvinceData() {
        triggerIntent(UserAddressIntent.ProvinceSpinner(ApplicationConstant.COMPANY_ID))
    }

    private fun setProvinceRv(provinceData: List<StateEntity>) {
        val provinceAdapterSpinner = ProvinceSpinnerAdapter(provinceData, this@UserAddressFragment)
        setCommonRv(binding.rvProvinceList, provinceData.size)
        binding.rvProvinceList.adapter = provinceAdapterSpinner
        setProvinceSpinnerEditTextListner(
            binding.etProvinceSearch,
            provinceAdapterSpinner,
            provinceData
        )
    }

    private fun setDistrictRv(districtData: List<DistrictEntity>) {
        val districtAdapterSpinner = DistrictSpinnerAdapter(districtData, this@UserAddressFragment)
        setCommonRv(binding.rvDistrictCityList, districtData.size)
        binding.rvDistrictCityList.adapter = districtAdapterSpinner
        setDistrictSpinnerEditTextListner(
            binding.etDistrictCitySearch,
            districtAdapterSpinner,
            districtData
        )
    }

    private fun setSubDistrictRv(subDistrictData: List<SubDistrictEntity>) {
        val subDistrictAdapterSpinner =
            SubDistrictSpinnerAdapter(subDistrictData, this@UserAddressFragment)
        setCommonRv(binding.rvSubDistrictList, subDistrictData.size)
        binding.rvSubDistrictList.adapter = subDistrictAdapterSpinner
        setSubDistrictSpinnerEditTextListner(
            binding.etSubDistrictSearch,
            subDistrictAdapterSpinner,
            subDistrictData
        )
    }

    private fun setVillageRv(villageData: List<VillageEntity>) {
        val villageAdapterSpinner = VillageSpinnerAdapter(villageData, this@UserAddressFragment)
        setCommonRv(binding.rvVillageList, villageData.size)
        binding.rvVillageList.adapter = VillageSpinnerAdapter(villageData, this@UserAddressFragment)
        setVillageSpinnerEditTextListner(
            binding.etVillageSearch,
            villageAdapterSpinner,
            villageData
        )
    }

    private fun setCommonRv(recyclerViewSpinner: RecyclerView, listSize: Int) {
        recyclerViewSpinner.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(
                    recyclerViewSpinner.context,
                    LinearLayoutManager.VERTICAL
                )
            )
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            layoutParams = getRvParams(listSize, this)
        }
    }

    private fun getRvParams(listSize: Int, recyclerViewSpinner: RecyclerView): ViewGroup.LayoutParams {
        val params: ViewGroup.LayoutParams = recyclerViewSpinner.layoutParams
        if (listSize < ApplicationConstant.MAX_ADDRESS_LIST_SIZE) {
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        } else {
            params.height = requireContext().dpToPx(150)
        }
        return params
    }

    override fun provinceOnItemClick(position: Int, addressList: List<StateEntity>) {
        triggerIntent(UserAddressIntent.ProvinceOnItemClick(position, addressList))
    }

    override fun districtOnItemClick(position: Int, addressList: List<DistrictEntity>) {
        triggerIntent(UserAddressIntent.DistrictOnItemClick(position, addressList))
    }

    override fun subDistrictOnItemClick(position: Int, addressList: List<SubDistrictEntity>) {
        triggerIntent(UserAddressIntent.SubDistrictOnItemClick(position, addressList))
    }

    override fun villageOnItemClick(position: Int, addressList: List<VillageEntity>) {
        triggerIntent(UserAddressIntent.VillageOnItemClick(position, addressList))
    }

    private fun setProvinceSpinnerEditTextListner(editText: EditText, adapter: ProvinceSpinnerAdapter, provinceData: List<StateEntity>) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(query: Editable?) {}
            override fun onTextChanged(
                query: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                triggerIntent(
                    UserAddressIntent.ProvinceSearch(
                        query.toString(),
                        provinceData,
                        adapter,
                        provinceType
                    )
                )
            }
        })
    }

    private fun setDistrictSpinnerEditTextListner(editText: EditText, adapter: DistrictSpinnerAdapter, districtData: List<DistrictEntity>) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(query: Editable?) {}
            override fun onTextChanged(
                query: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                triggerIntent(
                    UserAddressIntent.DistrictSearch(
                        query.toString(),
                        districtData,
                        adapter,
                        districtType
                    )
                )
            }
        })
    }

    private fun setSubDistrictSpinnerEditTextListner(editText: EditText, adapter: SubDistrictSpinnerAdapter, subDistrictData: List<SubDistrictEntity>) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(query: Editable?) {}
            override fun onTextChanged(
                query: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                triggerIntent(
                    UserAddressIntent.SubDistrictSearch(
                        query.toString(),
                        subDistrictData,
                        adapter,
                        subDistrictType
                    )
                )
            }
        })
    }

    private fun setVillageSpinnerEditTextListner(editText: EditText, adapter: VillageSpinnerAdapter, villageData: List<VillageEntity>) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(query: Editable?) {}
            override fun onTextChanged(query: CharSequence?, start: Int, before: Int, count: Int) {
                triggerIntent(
                    UserAddressIntent.VillageSearch(
                        query.toString(),
                        villageData,
                        adapter,
                        villageType
                    )
                )
            }
        })
    }

    private fun villageRvSearchResult(adapter: VillageSpinnerAdapter, villageData: List<VillageEntity>) {
        setViewAfterSearch(village, villageData.size)
        adapter.filterList(villageData)
    }

    private fun subDistrictRvSearchResult(adapter: SubDistrictSpinnerAdapter, subDistrictData: List<SubDistrictEntity>) {
        setViewAfterSearch(subDistrict, subDistrictData.size)
        adapter.filterList(subDistrictData)
    }

    private fun districtRvSearchResult(adapter: DistrictSpinnerAdapter, districtData: List<DistrictEntity>) {
        setViewAfterSearch(district, districtData.size)
        adapter.filterList(districtData)
    }

    private fun provinceRvSearchResult(adapter: ProvinceSpinnerAdapter, provinceData: List<StateEntity>) {
        setViewAfterSearch(province, provinceData.size)
        adapter.filterList(provinceData)
    }

    private fun setViewAfterSearch(spinnerView: List<View>, listSize: Int) {
        spinnerView[6].makeGone()
        spinnerView[5].apply {
            makeVisible()
            layoutParams = getRvParams(listSize, this as RecyclerView)
        }
    }

    private fun setCompleted() {
        val completedStage =
            listOf(getString(R.string.address_stage), getString(R.string.location_stage))
        triggerActivityIntent(UserOnboardingIntent.StageCompleted(completedStage))
    }

    private fun showLocationDialog() {
        locationUtils.showLocationDialog(requireContext(),requireActivity())
    }

    private fun noInternetState() {
        triggerActivityIntent(UserOnboardingIntent.Refresh)
    }

    override fun onResume() {
        super.onResume()
        triggerIntent(UserAddressIntent.CheckPendingStage)
    }

    private fun handleBack() {
        activity?.onBackPressedDispatcher?.addCallback(
            requireActivity(),
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    showBackDialog()
                }
            })

        binding.toolbar.setNavigationOnClickListener {
            showBackDialog()
        }
    }

    fun showBackDialog() {
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
    }

    override fun location(locationType : String ,latitude: Double, longitude: Double) {
        triggerIntent(UserAddressIntent.LocationContinue(UserLocationRequest(locationType, latitude.toString(), longitude.toString(), true)))
    }
}