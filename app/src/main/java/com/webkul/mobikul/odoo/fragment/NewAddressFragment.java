package com.webkul.mobikul.odoo.fragment;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_ADDRESS_TYPE;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_TITLE;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_URL;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl;
import com.webkul.mobikul.odoo.analytics.models.AnalyticsAddressDataModel;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.databinding.FragmentNewAddressBinding;
import com.webkul.mobikul.odoo.firebase.FirebaseAnalyticsImpl;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.ErrorConstants;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.helper.SnackbarHelper;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.customer.address.AddressFormResponse;
import com.webkul.mobikul.odoo.model.customer.address.AddressRequestBody;
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.DistrictListResponse;
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.StateListResponse;
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.SubDistrictListResponse;
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.VillageListResponse;
import com.webkul.mobikul.odoo.model.generic.DistrictData;
import com.webkul.mobikul.odoo.model.generic.StateData;
import com.webkul.mobikul.odoo.model.generic.SubDistrictData;
import com.webkul.mobikul.odoo.model.generic.VillageData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;


public class NewAddressFragment extends BaseFragment {

    @SuppressWarnings("unused")
    public static final String TAG = "NewAddressFragment";
    private static final int MAP_PIN_LOCATION_REQUEST_CODE = 103;
    public FragmentNewAddressBinding binding;
    public HashMap<String, Integer> countryListHashmap = new HashMap<>();
    public HashMap<String, StateData> stateListHashmap = new HashMap<>();
    public HashMap<String, DistrictData> districtListHashmap = new HashMap<>();
    public HashMap<String, SubDistrictData> subDistrictListHashmap = new HashMap<>();
    public HashMap<String, VillageData> villageListHashmap = new HashMap<>();

    public ArrayList<String> countryList = new ArrayList<>();
    public ArrayList<String> statesList = new ArrayList<>();
    public ArrayList<String> districtsList = new ArrayList<>();
    public ArrayList<String> subDistrictsList = new ArrayList<>();
    public ArrayList<String> villagesList = new ArrayList<>();


    public boolean statesAvailable = true;
    public boolean districtsAvailable = true;
    public boolean subDistrictsAvailable = true;
    public boolean villagesAvailable = true;

    public AddressRequestBody addressRequestBody = new AddressRequestBody();
    AnalyticsAddressDataModel analyticsAddressDataModel = new AnalyticsAddressDataModel();
    public AddressFormResponse addressFormResponse = new AddressFormResponse(null);

    public boolean isEditMode = false;
    public String addressUrl = "";
    public String selectedStateId = "";
    public boolean isMissingDetails = false;
    private int FIRST_TIME_SELECTION = 0;
    private int FILL_UP_FEILDS = 1;

    private final int RESET_SPINNERS_FROM_STATE_UPTO_VILLAGE = 1;
    private final int RESET_SPINNERS_FROM_DISTRICT_UPTO_VILLAGE = 2;
    private final int RESET_SPINNERS_FROM_SUB_DISTRICT_UPTO_VILLAGE = 3;
    private final int RESET_SPINNERS_VILLAGE = 4;

    private final int COMPANY_ID = 1;
    private final int COUNTRY_ID = 100;     // For Indonesia
    private final int UNSELECTED_POSITION = -1;

    private SweetAlertDialog alertDialog;

    public static NewAddressFragment newInstance(@Nullable String url, String title, AddressType addressType) {
        Bundle args = new Bundle();
        args.putString(BUNDLE_KEY_URL, url);
        args.putString(BUNDLE_KEY_TITLE, title);
        args.putSerializable(BUNDLE_KEY_ADDRESS_TYPE, addressType);
        NewAddressFragment fragment = new NewAddressFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_address, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.mapMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMapFragment();

            }
        });
        getActivity().setTitle(getArguments().getString(BUNDLE_KEY_TITLE));
        binding.setAddressType((AddressType) getArguments().getSerializable(BUNDLE_KEY_ADDRESS_TYPE));
        String url = getArguments().getString(BUNDLE_KEY_URL);
        fetchCountry();
        if (url == null) {
            resetSpinners(RESET_SPINNERS_FROM_STATE_UPTO_VILLAGE);
            statesAvailable = false;
            fetchStates(UNSELECTED_POSITION);
        } else {
            isEditMode = true;
            addressUrl = url;
            FIRST_TIME_SELECTION = FILL_UP_FEILDS;
            fetchCurrentAddress(url);
        }

        binding.saveAddressBtn.setOnClickListener(v -> {
            if (checkIfSubRegionsAreLoading()) {
                if (isMissingDetails)
                    validateMandatoryFeilds(selectedStateId);
                else
                    validateAddressEditTextFeilds();
            } else {
                showShortToast(getString(R.string.missing_feilds_in_address_form));
            }
        });
    }

    private boolean checkIfSubRegionsAreLoading() {
        return statesAvailable && districtsAvailable && subDistrictsAvailable && villagesAvailable;
    }

    private void fetchCurrentAddress(String url) {
        ApiConnection.getAddressFormData(getContext(), url).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<AddressFormResponse>(getContext()) {
            @Override
            public void onNext(@androidx.annotation.NonNull AddressFormResponse addressFormResponse) {
                super.onNext(addressFormResponse);
                resetSpinners(RESET_SPINNERS_FROM_STATE_UPTO_VILLAGE);
                setCurrentAddressDetails(addressFormResponse);
                statesAvailable = false;
                if (addressFormResponse.getStateId().isEmpty())
                    fetchStates(UNSELECTED_POSITION);
                else
                    fetchStates(Integer.parseInt(addressFormResponse.getStateId()));
            }
        });
    }

    private void setCurrentAddressDetails(AddressFormResponse addressFormResponse) {
        this.addressFormResponse = addressFormResponse;
        binding.nameEt.setText(addressFormResponse.getName());
        binding.telephoneEt.setText(addressFormResponse.getPhone());
        binding.streetEt.setText(addressFormResponse.getStreet());
    }

    private void validateAddressEditTextFeilds() {
        if (isValid(binding.nameEt) && isValid(binding.telephoneEt) && addressRequestBody.village_id != null) {
            makeRequestBody();
        } else {
            SnackbarHelper.getSnackbar(getActivity(), getString(R.string.missing_feilds_in_address_form), Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
        }
    }

    private void makeRequestBody() {
        addressRequestBody.setName(binding.nameEt.getText().toString());
        addressRequestBody.setPhone(binding.telephoneEt.getText().toString());
        addressRequestBody.setStreet(binding.streetEt.getText().toString());
        addressRequestBody.setState_id(selectedStateId);
        addressRequestBody.setCountry_id(String.valueOf(COUNTRY_ID));
        callApiToSaveAddress(addressRequestBody);
    }

    private void callApiToSaveAddress(AddressRequestBody addressRequestBody) {

        if (isEditMode) {
            AnalyticsImpl.INSTANCE.trackAddressUpdateSubmitted(analyticsAddressDataModel);
            ApiConnection.editAddress(getContext(), addressRequestBody.getNewAddressData(), addressUrl).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(getAddressResponseObserver(getString(R.string.address_edit_text)));
        } else {
            AnalyticsImpl.INSTANCE.trackAddressAdded(analyticsAddressDataModel);
            ApiConnection.addNewAddress(getContext(), addressRequestBody.getNewAddressData()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(getAddressResponseObserver(getString(R.string.address_added_text)));
        }
    }

//    private AnalyticsAddressDataModel getAnalyticsAddressDataModelFromAddressBody(AddressRequestBody addressRequestBody)
//    {
//        AnalyticsAddressDataModel temp= new AnalyticsAddressDataModel();
//     temp.setProvince();
//    }

    private Observer<? super BaseResponse> getAddressResponseObserver(String string) {
        return new CustomObserver<BaseResponse>(getContext()) {
            @Override
            public void onNext(@androidx.annotation.NonNull BaseResponse baseResponse) {
                super.onNext(baseResponse);
                showToastAndFinish(string);
                if (baseResponse.isSuccess())
                    AnalyticsImpl.INSTANCE.trackAddressUpdateSuccessfull(analyticsAddressDataModel);
                else
                    AnalyticsImpl.INSTANCE.trackAddressUpdateFailed(analyticsAddressDataModel, baseResponse.getResponseCode(), ErrorConstants.AddressUpdateError.INSTANCE.getErrorType(), baseResponse.getMessage());
            }

            @Override
            public void onError(@androidx.annotation.NonNull Throwable t) {
                super.onError(t);
                AnalyticsImpl.INSTANCE.trackAddressUpdateFailed(analyticsAddressDataModel, ErrorConstants.AddressUpdateError.INSTANCE.getErrorCode(), ErrorConstants.AddressUpdateError.INSTANCE.getErrorType(), null);

            }
        };
    }

    private void showToastAndFinish(String address_edit_text) {
        showShortToast(address_edit_text);
        requireActivity().finish();
        hideDialog();
    }

    private void hideDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    private boolean isValid(TextInputEditText etFeild) {
        return (etFeild.getText() != null && etFeild.getText().length() > 0);
    }


    private void fetchCountry() {
        addCountriesToList();
        setCountrySpinner();
    }

    private void addCountriesToList() {
        countryList.add(getString(R.string.indonesia));
    }

    private void setCountrySpinner() {
        setCountrySpinnerAdapter();
        setCountrySpinnerSelectionListener();
    }

    private void setCountrySpinnerAdapter() {
        binding.countrySpinner.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, countryList));
    }

    private void setCountrySpinnerSelectionListener() {
        binding.countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addressRequestBody.setCompany_id(String.valueOf(COMPANY_ID));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    private void resetSpinners(int i) {
        switch (i) {
            case RESET_SPINNERS_FROM_STATE_UPTO_VILLAGE:
                resetSpinner(binding.provinceSpinner);
            case RESET_SPINNERS_FROM_DISTRICT_UPTO_VILLAGE:
                resetSpinner(binding.districtSpinner);
            case RESET_SPINNERS_FROM_SUB_DISTRICT_UPTO_VILLAGE:
                resetSpinner(binding.subDistrictSpinner);
            case RESET_SPINNERS_VILLAGE:
                resetSpinner(binding.villageSpinner);
                break;
            default:
                resetSpinner(binding.provinceSpinner);
        }
    }

    private void resetSpinner(AppCompatSpinner spinner) {
        spinner.setAdapter(null);
        resetVillageCode("");
    }

    private void resetVillageCode(String s) {
        binding.villageCodeEt.setText(s);
    }


    private void fetchStates(int stateId) {
        ApiConnection.getStates(getContext(), COMPANY_ID).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<StateListResponse>(getContext()) {
            @Override
            public void onNext(@NonNull StateListResponse stateListResponse) {
                super.onNext(stateListResponse);
                int pos = refreshStatesData(stateListResponse, stateId);
                setUpStateSpinner();
                setStateSpinnerItemSelection(pos);
            }
        });
    }

    private void setStateSpinnerItemSelection(int pos) {
        if (pos != UNSELECTED_POSITION)
            binding.provinceSpinner.setSelection(pos);
    }

    private void setUpStateSpinner() {
        statesAvailable = true;
        setUpStateSpinnerAdapter();
        setUpStateSpinnerAdapterListener();
    }

    private void setUpStateSpinnerAdapter() {
        ArrayAdapter stateArrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, statesList);
        binding.provinceSpinner.setAdapter(stateArrayAdapter);
    }

    private void setUpStateSpinnerAdapterListener() {
        binding.provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onStateSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void onStateSelected(int position) {
        String selectedState = statesList.get(position);
        analyticsAddressDataModel.setProvince(selectedState);
        selectedStateId = String.valueOf(stateListHashmap.get(selectedState).getId());
        if (stateListHashmap.get(selectedState).isAvailable()) {
            resetSpinners(RESET_SPINNERS_FROM_DISTRICT_UPTO_VILLAGE);
            districtsAvailable = false;
            fetchDistricts(stateListHashmap.get(selectedState).getId());
            setSubRegionFieldsVisibility(true);
            isMissingDetails = false;
        } else {
            resetSpinners(RESET_SPINNERS_FROM_DISTRICT_UPTO_VILLAGE);
            setSubRegionFieldsVisibility(false);
            isMissingDetails = true;
        }
    }

    private void setSubRegionFieldsVisibility(boolean show) {
        binding.districtContainer.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.subDistrictContainer.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.villageContainer.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.postalCodeContainer.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.streetContainer.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private int refreshStatesData(StateListResponse stateListResponse, int stateId) {
        statesList.clear();
        stateListHashmap.clear();
        int pos = UNSELECTED_POSITION;
        for (int i = 0; i < stateListResponse.data.size(); i++) {
            StateData stateData = stateListResponse.data.get(i);
            statesList.add(stateData.getName());
            stateListHashmap.put(stateData.getName(), stateData);
            if (stateId == stateData.getId())
                pos = i;
        }
        return pos;
    }


    private void fetchDistricts(int state_id) {
        ApiConnection.getDistricts(getContext(), state_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<DistrictListResponse>(getContext()) {
            @Override
            public void onNext(@androidx.annotation.NonNull DistrictListResponse districtListResponse) {
                super.onNext(districtListResponse);
                int pos = refreshDistrictData(districtListResponse);
                setUpDistrictSpinner();
                setDistrictSpinnerSelection(pos);
            }
        });
    }

    private void setDistrictSpinnerSelection(int pos) {
        if (pos != UNSELECTED_POSITION)
            binding.districtSpinner.setSelection(pos);
    }

    private int refreshDistrictData(DistrictListResponse districtListResponse) {
        districtsList.clear();
        districtListHashmap.clear();
        int pos = UNSELECTED_POSITION;
        for (int i = 0; i < districtListResponse.data.size(); i++) {
            DistrictData districtData = districtListResponse.data.get(i);
            districtsList.add(districtData.getName());
            districtListHashmap.put(districtData.getName(), districtData);
            if (isEditMode && !addressFormResponse.getDistrictId().isEmpty() && FIRST_TIME_SELECTION == FILL_UP_FEILDS &&
                    addressFormResponse.getDistrictId().equals(String.valueOf(districtData.getId())))
                pos = i;
        }
        return pos;
    }

    private void setUpDistrictSpinner() {
        districtsAvailable = true;
        setUpDistrictSpinnerAdapter();
        setUpDistrictSpinnerAdapterListener();
    }

    private void setUpDistrictSpinnerAdapter() {
        ArrayAdapter districtArrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, districtsList);
        binding.districtSpinner.setAdapter(districtArrayAdapter);
    }

    private void setUpDistrictSpinnerAdapterListener() {
        binding.districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onDistrictSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void onDistrictSelected(int position) {
        String selectedState = districtsList.get(position);
        analyticsAddressDataModel.setDistrict(selectedState);
        resetSpinners(RESET_SPINNERS_FROM_SUB_DISTRICT_UPTO_VILLAGE);
        subDistrictsAvailable = false;
        fetchSubDistricts(districtListHashmap.get(selectedState).getId());
        addressRequestBody.setDistrict_id(String.valueOf(districtListHashmap.get(selectedState).getId()));
    }

    private void validateMandatoryFeilds(String unavailable_state_id) {
        boolean isFormFilledup = checkMandatoryFeilds();
        if (isFormFilledup) {
            analyticsAddressDataModel.setServicable(false);
            showUnavailabilityAlertDialog(unavailable_state_id);
        } else {
            showErrorMessage(getString(R.string.missing_feilds_in_address_form));
        }
    }

    private void showErrorMessage(String errorMessage) {
        SnackbarHelper.getSnackbar(getActivity(), errorMessage, Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
    }

    private void showUnavailabilityAlertDialog(String unavailable_state_id) {
        alertDialog = new SweetAlertDialog(requireContext(), SweetAlertDialog.NORMAL_TYPE)
                .setTitleText(getString(R.string.service_unavailable))
                .setContentText(getString(R.string.service_unavailablity_text))
                .setConfirmText(getString(R.string.confirm_small))
                .setConfirmClickListener(sweetAlertDialog -> {
                    makeEmptyRequestBody(unavailable_state_id);
                });
        alertDialog.show();
    }

    private boolean checkMandatoryFeilds() {
        return (isValid(binding.nameEt) && isValid(binding.telephoneEt));
    }

    private void makeEmptyRequestBody(String unavailable_state_id) {
        addressRequestBody.setName(binding.nameEt.getText().toString());
        addressRequestBody.setPhone(binding.telephoneEt.getText().toString());
        addressRequestBody.setStreet(binding.streetEt.getText().toString());
        addressRequestBody.setState_id(unavailable_state_id);
        addressRequestBody.setDistrict_id("");
        addressRequestBody.setSub_district_id("");
        addressRequestBody.setVillage_id("");
        addressRequestBody.setZip("");
        addressRequestBody.setCountry_id(String.valueOf(COUNTRY_ID));
        addressRequestBody.setStreet("");
        callApiToSaveAddress(addressRequestBody);
    }


    private void fetchSubDistricts(int district_id) {
        ApiConnection.getSubDistricts(getContext(), district_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<SubDistrictListResponse>(getContext()) {
            @Override
            public void onNext(@androidx.annotation.NonNull SubDistrictListResponse subDistrictListResponse) {
                super.onNext(subDistrictListResponse);
                int pos = refreshSubdistrictData(subDistrictListResponse);
                setUpSubdistrictSpinner();
                setSubDistrictSpinnerSelection(pos);
            }
        });
    }

    private void setSubDistrictSpinnerSelection(int pos) {
        if (pos != UNSELECTED_POSITION)
            binding.subDistrictSpinner.setSelection(pos);
    }

    private int refreshSubdistrictData(SubDistrictListResponse subDistrictListResponse) {
        subDistrictsList.clear();
        subDistrictListHashmap.clear();
        int pos = UNSELECTED_POSITION;
        for (int i = 0; i < subDistrictListResponse.data.size(); i++) {
            SubDistrictData subDistrictData = subDistrictListResponse.data.get(i);
            subDistrictsList.add(subDistrictData.getName());
            subDistrictListHashmap.put(subDistrictData.getName(), subDistrictData);
            if (isEditMode && !addressFormResponse.getSub_district_id().isEmpty() && FIRST_TIME_SELECTION == FILL_UP_FEILDS &&
                    addressFormResponse.getSub_district_id().equals(String.valueOf(subDistrictData.getId())))
                pos = i;
        }
        return pos;
    }

    private void setUpSubdistrictSpinner() {
        subDistrictsAvailable = true;
        setUpSubdistrictSpinnerAdapter();
        setUpSubdistrictSpinnerAdapterListener();
    }

    private void setUpSubdistrictSpinnerAdapter() {
        ArrayAdapter districtArrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, subDistrictsList);
        binding.subDistrictSpinner.setAdapter(districtArrayAdapter);
    }

    private void setUpSubdistrictSpinnerAdapterListener() {
        binding.subDistrictSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onSubDistrictSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void onSubDistrictSelected(int position) {
        String selectedSubDistrict = subDistrictsList.get(position);
        analyticsAddressDataModel.setSubDistrict(selectedSubDistrict);
        resetSpinners(RESET_SPINNERS_VILLAGE);
        villagesAvailable = false;
        fetchVillage(subDistrictListHashmap.get(selectedSubDistrict).getId());
        addressRequestBody.setSub_district_id(String.valueOf(subDistrictListHashmap.get(selectedSubDistrict).getId()));
    }


    private void fetchVillage(int subdistrict_id) {
        addressRequestBody.setVillage_id(null);
        ApiConnection.getVillages(getContext(), subdistrict_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<VillageListResponse>(getContext()) {
            @Override
            public void onNext(@androidx.annotation.NonNull VillageListResponse villageListResponse) {
                super.onNext(villageListResponse);
                int pos = refreshVillageData(villageListResponse);
                setUpVillageSpinner(villageListResponse);
                setVillageSpinnerSelection(pos);
            }
        });
    }

    private void setVillageSpinnerSelection(int pos) {
        if (pos != UNSELECTED_POSITION)
            binding.villageSpinner.setSelection(pos);
    }

    private int refreshVillageData(VillageListResponse villageListResponse) {
        villagesList.clear();
        villageListHashmap.clear();
        int pos = UNSELECTED_POSITION;
        for (int i = 0; i < villageListResponse.data.size(); i++) {
            VillageData villageData = villageListResponse.data.get(i);
            villagesList.add(villageData.getName());
            villageListHashmap.put(villageData.getName(), villageData);
            if (isEditMode && !addressFormResponse.getVillage_id().isEmpty() && FIRST_TIME_SELECTION == FILL_UP_FEILDS &&
                    addressFormResponse.getVillage_id().equals(String.valueOf(villageData.getId())))
                pos = i;
        }
        return pos;
    }

    private void setUpVillageSpinner(VillageListResponse villageListResponse) {
        villagesAvailable = true;
        setUpVillageSpinnerAdapter();
        setUpVillageSpinnerAdapterListener(villageListResponse);
    }

    private void setUpVillageSpinnerAdapter() {
        ArrayAdapter districtArrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, villagesList);
        binding.villageSpinner.setAdapter(districtArrayAdapter);
    }

    private void setUpVillageSpinnerAdapterListener(VillageListResponse villageListResponse) {
        binding.villageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onVillageSelected(villageListResponse, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void onVillageSelected(VillageListResponse villageListResponse, int position) {
        VillageData villageData = villageListResponse.getData().get(position);
        analyticsAddressDataModel.setVillage(villageData.getName());
        addressRequestBody.setVillage_id(String.valueOf(villageData.getId()));
        if (FIRST_TIME_SELECTION == FILL_UP_FEILDS)
            FIRST_TIME_SELECTION++;
        if (villageData.getZip().equalsIgnoreCase(getString(R.string.false_))) {
            setUnavailableVillageData(villageData);
        } else {
            setAvailableVillageData(villageData);
        }
    }

    private void setAvailableVillageData(VillageData villageData) {
        addressRequestBody.setZip(villageData.getZip());
        addressRequestBody.setVillage_id(String.valueOf(villageData.getId()));
        binding.villageCodeEt.setText(villageData.getZip());
    }

    private void setUnavailableVillageData(VillageData villageData) {
        addressRequestBody.setVillage_id(String.valueOf(villageData.getId()));
        setUnserviceableAreaDetails();
        showShortToast(getString(R.string.service_not_availabe_text));
        binding.villageCodeEt.setText("");
        addressRequestBody.setVillage_id("");
    }

    private void setUnserviceableAreaDetails() {
        String missingState = stateListHashmap.get(addressRequestBody.getState_id()).getName();
        String missingDistrict = districtListHashmap.get(addressRequestBody.getDistrict_id()).getName();
        String missingSubDistrict = subDistrictListHashmap.get(addressRequestBody.getSub_district_id()).getName();
        String missingVillage = villageListHashmap.get(addressRequestBody.getSub_district_id()).getName();
        logFirebaseEvent(missingState, missingDistrict, missingSubDistrict, missingVillage);
    }

    private void logFirebaseEvent(String missingState, String missingDistrict, String missingSubDistrict, String missingVillage) {
        FirebaseAnalyticsImpl.logPostalCodeUnavailable(requireContext(), missingState, missingDistrict, missingSubDistrict, missingVillage);
    }


    public void updateCurrentAddress(Address address) {
        if (address == null) {
            SnackbarHelper.getSnackbar(getActivity(), getString(R.string.error_could_get_location_at_this_point), Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
            return;
        }

        /*STREET ADDRESS*/
        StringBuilder streetSb = new StringBuilder();
        for (int addressLineIdx = 0; addressLineIdx <= 1; addressLineIdx++) {
            String line = address.getAddressLine(addressLineIdx);
            if (line != null) {
                streetSb.append(line);
                streetSb.append(',');
                streetSb.append(' ');
            }
        }
        binding.getData().setStreet(streetSb.toString());
        binding.getData().setZip(address.getPostalCode() == null ? "" : address.getPostalCode());
        int countryPos = binding.getData().getCountryStateData().getCountryPositionFromCountryName(address.getCountryName());


        /*UPDATE STATE AS WELL*/
        if (!binding.getData().getCountryStateData().getStateNameList(countryPos).isEmpty()) {
            binding.getData().setStateId(binding.getData().getCountryStateData().getStateId(countryPos, address.getAdminArea()));
        }
        binding.countrySpinner.setSelection(countryPos);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Helper.hiderAllMenuItems(menu);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions, @androidx.annotation.NonNull int[] grantResults) {
        if (requestCode == MAP_PIN_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showMapFragment();
            } else {
                showShortToast(getString(R.string.permission_denied));
            }
        }
    }

    private void showShortToast(String string) {
        Toast.makeText(getContext(), string, Toast.LENGTH_SHORT).show();
    }

    private void showMapFragment() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            DialogInterface.OnClickListener listener = (dialog, which) -> {
                String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
                requestPermissions(permissions, MAP_PIN_LOCATION_REQUEST_CODE);
                dialog.dismiss();
            };
            if (requireContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                AlertDialogHelper.showPermissionDialog(getContext(), getResources().getString(R.string.permission_confirmation), getResources().getString(R.string.permission_confirmation), listener);
                return;
            }
        }
        MapBottomSheetFragment fragmentDialog = MapBottomSheetFragment.Companion.newInstance(binding.getData().getStreet());
        fragmentDialog.setMapListener(address -> {
            String message = address.getAddressLine(0);
            String title = address.getFeatureName();

            binding.streetEt.setText(message);
            binding.zipEt.setText(address.getPostalCode());

            if (address.getCountryName() != null && binding.getData().getCountryStateData().getCountryNameList(getContext()).contains(address.getCountryName())) {
                int pos = binding.getData().getCountryStateData().getCountryPositionFromCountryName(address.getCountryName());
                binding.countrySpinner.setSelection(pos);
                if (address.getAdminArea() != null && binding.getData().getCountryStateData().getStateNameList(pos).contains(address.getAdminArea())) {
                    String stateId = binding.getData().getCountryStateData().getStateId(pos, address.getAdminArea());
                    binding.provinceSpinner.setSelection(binding.getData().getCountryStateData().getStatePosition(pos, stateId));

                }
            }


        });
        fragmentDialog.setCancelable(false);
        fragmentDialog.show(requireActivity().getSupportFragmentManager(), "Custom Bottom Sheet");
    }

    /**
     * TYPE_BILLING_CHECKOUT : Used to set billing address and redirect to checkout page.
     */

    public enum AddressType {
        TYPE_BILLING, TYPE_SHIPPING, TYPE_ADDITIONAL, TYPE_NEW, TYPE_BILLING_CHECKOUT
    }

    @androidx.annotation.NonNull
    @Override
    public String getTitle() {
        return TAG;
    }

    @Override
    public void setTitle(@androidx.annotation.NonNull String title) {

    }
}
