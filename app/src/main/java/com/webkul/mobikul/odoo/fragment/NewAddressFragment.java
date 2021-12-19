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
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.databinding.FragmentNewAddressBinding;
import com.webkul.mobikul.odoo.firebase.FirebaseAnalyticsImpl;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
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


/**
 * Webkul Software.
 *
 * @author Webkul <support@webkul.com>
 * @package Mobikul App
 * @Category Mobikul
 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

public class NewAddressFragment extends BaseFragment {

    @SuppressWarnings("unused")
    private static final String TAG = "NewAddressFragment";
    private static final int MAP_PIN_LOCATION_REQUEST_CODE = 103;
    public FragmentNewAddressBinding mBinding;
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

    public AddressRequestBody addressRequestBody = new AddressRequestBody();

    public boolean isEditMode = false;
    public String addressUrl = "";
    public String selectedStateId = "";
    public boolean isMissingDetails = false;

    private final int RESET_SPINNERS_FROM_STATE_UPTO_VILLAGE = 1;
    private final int RESET_SPINNERS_FROM_DISTRICT_UPTO_VILLAGE = 2;
    private final int RESET_SPINNERS_FROM_SUB_DISTRICT_UPTO_VILLAGE = 3;
    private final int RESET_SPINNERS_VILLAGE = 4;

    private final int COMPANY_ID = 1;
    private final int UNSELECTED_STATE = -1;

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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_address, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBinding.mapMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMapFragment();

            }
        });
        getActivity().setTitle(getArguments().getString(BUNDLE_KEY_TITLE));
        mBinding.setAddressType((AddressType) getArguments().getSerializable(BUNDLE_KEY_ADDRESS_TYPE));
        String url = getArguments().getString(BUNDLE_KEY_URL);
        fetchCountry();
        if (url == null) {
            resetSpinners(RESET_SPINNERS_FROM_STATE_UPTO_VILLAGE);
            fetchStates(UNSELECTED_STATE);
        } else {
            isEditMode = true;
            addressUrl = url;
            fetchCurrentAddress(url);
        }

        mBinding.saveAddressBtn.setOnClickListener(v -> {
            if(isMissingDetails)
                showUnavailabilityAlertDialog(selectedStateId);
            else
                validateAddressEditTextFeilds();
        });
    }

    private void fetchCurrentAddress(String url) {
        ApiConnection.getAddressFormData(getContext(), url).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<AddressFormResponse>(getContext()) {
            @Override
            public void onNext(@androidx.annotation.NonNull AddressFormResponse addressFormResponse) {
                super.onNext(addressFormResponse);
                resetSpinners(RESET_SPINNERS_FROM_STATE_UPTO_VILLAGE);
                fetchStates(Integer.parseInt(addressFormResponse.getStateId()));
                setCurrentAddressDetails(addressFormResponse);
            }
        });
    }

    private void setCurrentAddressDetails(AddressFormResponse addressFormResponse) {
        mBinding.nameEt.setText(addressFormResponse.getName());
        mBinding.telephoneEt.setText(addressFormResponse.getPhone());
        mBinding.streetEt.setText(addressFormResponse.getStreet());
    }

    private void validateAddressEditTextFeilds() {
        if (isValid(mBinding.nameEt) && isValid(mBinding.streetEt) &&
                isValid(mBinding.telephoneEt) && addressRequestBody.village_id != null) {
            makeRequestBody();
        } else {
            SnackbarHelper.getSnackbar(getActivity(), getString(R.string.missing_feilds_in_address_form), Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
        }
    }

    private void makeRequestBody() {
        addressRequestBody.setName(mBinding.nameEt.getText().toString());
        addressRequestBody.setPhone(mBinding.telephoneEt.getText().toString());
        addressRequestBody.setStreet(mBinding.streetEt.getText().toString());
        addressRequestBody.setState_id(selectedStateId);
        callApiToSaveAddress(addressRequestBody);
    }

    private void callApiToSaveAddress(AddressRequestBody addressRequestBody) {
        if (isEditMode) {
            ApiConnection.editAddress(getContext(), addressRequestBody.getNewAddressData(), addressUrl).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(getAddressResponseObserver(getString(R.string.address_edit_text)));
        } else {
            ApiConnection.addNewAddress(getContext(), addressRequestBody.getNewAddressData()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(getAddressResponseObserver(getString(R.string.address_added_text)));
        }
    }

    private Observer<? super BaseResponse> getAddressResponseObserver(String string) {
        return new CustomObserver<BaseResponse>(getContext()) {
            @Override
            public void onNext(@androidx.annotation.NonNull BaseResponse baseResponse) {
                super.onNext(baseResponse);
                showToastAndFinish(string);
            }
        };
    }

    private void showToastAndFinish(String address_edit_text) {
        Toast.makeText(requireContext(), address_edit_text , Toast.LENGTH_SHORT).show();
        requireActivity().finish();
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
        mBinding.countrySpinner.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, countryList));
    }

    private void setCountrySpinnerSelectionListener() {
        mBinding.countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addressRequestBody.setCountry_id(String.valueOf(COMPANY_ID));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    private void resetSpinners(int i) {
        switch (i) {
            case RESET_SPINNERS_FROM_STATE_UPTO_VILLAGE:
                resetSpinner(mBinding.provinceSpinner);
            case RESET_SPINNERS_FROM_DISTRICT_UPTO_VILLAGE:
                resetSpinner(mBinding.districtSpinner);
            case RESET_SPINNERS_FROM_SUB_DISTRICT_UPTO_VILLAGE:
                resetSpinner(mBinding.subDistrictSpinner);
            case RESET_SPINNERS_VILLAGE:
                resetSpinner(mBinding.villageSpinner);
                break;
            default:
                resetSpinner(mBinding.provinceSpinner);
        }
    }

    private void resetSpinner(AppCompatSpinner spinner) {
        spinner.setAdapter(null);
        resetVillageCode("");
    }

    private void resetVillageCode(String s) {
        mBinding.villageCodeEt.setText(s);
    }


    private void fetchStates(int stateId) {
        ApiConnection.getStates(getContext(), COMPANY_ID).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<StateListResponse>(getContext()) {
            @Override
            public void onNext(@NonNull StateListResponse stateListResponse) {
                super.onNext(stateListResponse);
                int pos = refreshStatesData(stateListResponse,stateId);
                setUpStateSpinner();
                setSpinnerItemSelection(pos);
            }
        });
    }

    private void setSpinnerItemSelection(int pos) {
        if (pos != UNSELECTED_STATE)
            mBinding.provinceSpinner.setSelection(pos);
    }

    private void setUpStateSpinner() {
        setUpStateSpinnerAdapter();
        setUpStateSpinnerAdapterListener();
    }

    private void setUpStateSpinnerAdapter() {
        ArrayAdapter stateArrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, statesList);
        mBinding.provinceSpinner.setAdapter(stateArrayAdapter);
    }

    private void setUpStateSpinnerAdapterListener() {
        mBinding.provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        selectedStateId = String.valueOf(stateListHashmap.get(selectedState).getId());
        if(stateListHashmap.get(selectedState).isAvailable()) {
            resetSpinners(RESET_SPINNERS_FROM_DISTRICT_UPTO_VILLAGE);
            fetchDistricts(stateListHashmap.get(selectedState).getId());
            setSubRegionFieldsVisibility(true);
            isMissingDetails = false;
        }
        else{
            resetSpinners(RESET_SPINNERS_FROM_DISTRICT_UPTO_VILLAGE);
            setSubRegionFieldsVisibility(false);
            isMissingDetails = true;
        }
    }

    private void setSubRegionFieldsVisibility(boolean show) {
        mBinding.districtContainer.setVisibility(show? View.VISIBLE : View.INVISIBLE);
        mBinding.subDistrictContainer.setVisibility(show? View.VISIBLE : View.INVISIBLE);
        mBinding.villageContainer.setVisibility(show? View.VISIBLE : View.INVISIBLE);
        mBinding.postalCodeContainer.setVisibility(show? View.VISIBLE : View.INVISIBLE);
        mBinding.streetContainer.setVisibility(show? View.VISIBLE : View.INVISIBLE);
    }

    private int refreshStatesData(StateListResponse stateListResponse, int stateId) {
        statesList.clear();
        stateListHashmap.clear();
        int pos = UNSELECTED_STATE;
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
                refreshDistrictData(districtListResponse);
                setUpDistrictSpinner();
            }
        });
    }

    private void refreshDistrictData(DistrictListResponse districtListResponse) {
        districtsList.clear();
        districtListHashmap.clear();
        for (DistrictData districtData : districtListResponse.data) {
            districtsList.add(districtData.getName());
            districtListHashmap.put(districtData.getName(), districtData);
        }
    }

    private void setUpDistrictSpinner() {
        setUpDistrictSpinnerAdapter();
        setUpDistrictSpinnerAdapterListener();
    }

    private void setUpDistrictSpinnerAdapter() {
        ArrayAdapter districtArrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, districtsList);
        mBinding.districtSpinner.setAdapter(districtArrayAdapter);
    }

    private void setUpDistrictSpinnerAdapterListener() {
        mBinding.districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        resetSpinners(RESET_SPINNERS_FROM_SUB_DISTRICT_UPTO_VILLAGE);
        fetchSubDistricts(districtListHashmap.get(selectedState).getId());
        addressRequestBody.setDistrict_id(String.valueOf(districtListHashmap.get(selectedState).getId()));
    }

    private void showUnavailabilityAlertDialog(String unavailable_state_id) {
        new SweetAlertDialog(requireContext(), SweetAlertDialog.NORMAL_TYPE)
                .setTitleText(getString(R.string.service_unavailable))
                .setContentText(getString(R.string.service_unavailablity_text))
                .setConfirmText(getString(R.string.confirm_small))
                .setConfirmClickListener(sweetAlertDialog -> {
                    makeEmptyRequestBody(unavailable_state_id);
                })
                .show();
    }

    private void makeEmptyRequestBody(String unavailable_state_id) {
        addressRequestBody.setName(mBinding.nameEt.getText().toString());
        addressRequestBody.setPhone(mBinding.telephoneEt.getText().toString());
        addressRequestBody.setStreet(mBinding.streetEt.getText().toString());
        addressRequestBody.setState_id(unavailable_state_id);
        addressRequestBody.setDistrict_id("");
        addressRequestBody.setSub_district_id("");
        addressRequestBody.setVillage_id("");
        callApiToSaveAddress(addressRequestBody);
    }


    private void fetchSubDistricts(int district_id) {
        ApiConnection.getSubDistricts(getContext(), district_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<SubDistrictListResponse>(getContext()) {
            @Override
            public void onNext(@androidx.annotation.NonNull SubDistrictListResponse subDistrictListResponse) {
                super.onNext(subDistrictListResponse);
                refreshSubdistrictData(subDistrictListResponse);
                setUpSubdistrictSpinner();
            }
        });
    }

    private void refreshSubdistrictData(SubDistrictListResponse subDistrictListResponse) {
        subDistrictsList.clear();
        subDistrictListHashmap.clear();
        for (SubDistrictData subDistrictData : subDistrictListResponse.data) {
            subDistrictsList.add(subDistrictData.getName());
            subDistrictListHashmap.put(subDistrictData.getName(), subDistrictData);
        }
    }

    private void setUpSubdistrictSpinner() {
        setUpSubdistrictSpinnerAdapter();
        setUpSubdistrictSpinnerAdapterListener();
    }

    private void setUpSubdistrictSpinnerAdapter() {
        ArrayAdapter districtArrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, subDistrictsList);
        mBinding.subDistrictSpinner.setAdapter(districtArrayAdapter);
    }

    private void setUpSubdistrictSpinnerAdapterListener() {
        mBinding.subDistrictSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        resetSpinners(RESET_SPINNERS_VILLAGE);
        fetchVillage(subDistrictListHashmap.get(selectedSubDistrict).getId());
        addressRequestBody.setVillage_id(String.valueOf(subDistrictListHashmap.get(selectedSubDistrict).getId()));
    }


    private void fetchVillage(int subdistrict_id) {
        addressRequestBody.setVillage_id(null);
        ApiConnection.getVillages(getContext(), subdistrict_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<VillageListResponse>(getContext()) {
            @Override
            public void onNext(@androidx.annotation.NonNull VillageListResponse villageListResponse) {
                super.onNext(villageListResponse);
                refreshVillageData(villageListResponse);
                setUpVillageSpinner(villageListResponse);
            }
        });
    }

    private void refreshVillageData(VillageListResponse villageListResponse) {
        villagesList.clear();
        villageListHashmap.clear();
        for (VillageData villageData : villageListResponse.data) {
            villagesList.add(villageData.getName());
            villageListHashmap.put(villageData.getName(), villageData);
        }
    }

    private void setUpVillageSpinner(VillageListResponse villageListResponse) {
        setUpVillageSpinnerAdapter();
        setUpVillageSpinnerAdapterListener(villageListResponse);
    }

    private void setUpVillageSpinnerAdapter() {
        ArrayAdapter districtArrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, villagesList);
        mBinding.villageSpinner.setAdapter(districtArrayAdapter);
    }

    private void setUpVillageSpinnerAdapterListener(VillageListResponse villageListResponse) {
        mBinding.villageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onVillageSelected(villageListResponse,position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void onVillageSelected(VillageListResponse villageListResponse, int position) {
        VillageData villageData = villageListResponse.getData().get(position);
        addressRequestBody.setVillage_id(String.valueOf(villageData.getId()));
        if (villageData.getZip().equalsIgnoreCase(getString(R.string.false_))) {
            setUnavailableVillageData(villageData);
        } else {
            setAvailableVillageData(villageData);
        }
    }

    private void setAvailableVillageData(VillageData villageData) {
        addressRequestBody.setZip(villageData.getZip());
        addressRequestBody.setVillage_id(String.valueOf(villageData.getId()));
        mBinding.villageCodeEt.setText(villageData.getZip());
    }

    private void setUnavailableVillageData(VillageData villageData) {
        addressRequestBody.setVillage_id(String.valueOf(villageData.getId()));
        setUnserviceableAreaDetails();
        Toast.makeText(getContext(), getString(R.string.service_not_availabe_text), Toast.LENGTH_SHORT).show();
        mBinding.villageCodeEt.setText("");
        addressRequestBody.setVillage_id("");
    }

    private void setUnserviceableAreaDetails() {
        String missingState = stateListHashmap.get(addressRequestBody.getState_id()).getName();
        String missingDistrict = districtListHashmap.get(addressRequestBody.getDistrict_id()).getName();
        String missingSubDistrict = subDistrictListHashmap.get(addressRequestBody.getSub_district_id()).getName();
        String missingVillage = villageListHashmap.get(addressRequestBody.getSub_district_id()).getName();
        logFirebaseEvent(missingState,missingDistrict,missingSubDistrict,missingVillage);
    }

    private void logFirebaseEvent(String missingState, String missingDistrict, String missingSubDistrict, String missingVillage) {
        FirebaseAnalyticsImpl.logPostalCodeUnavailable(requireContext(), missingState,missingDistrict,missingSubDistrict,missingVillage);
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
        mBinding.getData().setStreet(streetSb.toString());
        mBinding.getData().setZip(address.getPostalCode() == null ? "" : address.getPostalCode());
        int countryPos = mBinding.getData().getCountryStateData().getCountryPositionFromCountryName(address.getCountryName());


        /*UPDATE STATE AS WELL*/
        if (!mBinding.getData().getCountryStateData().getStateNameList(countryPos).isEmpty()) {
            mBinding.getData().setStateId(mBinding.getData().getCountryStateData().getStateId(countryPos, address.getAdminArea()));
        }
        mBinding.countrySpinner.setSelection(countryPos);
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
                Toast.makeText(getContext(), getContext().getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showMapFragment() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            DialogInterface.OnClickListener listener = (dialog, which) -> {
                String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
                requestPermissions(permissions, MAP_PIN_LOCATION_REQUEST_CODE);
                dialog.dismiss();
            };
            if (Objects.requireNonNull(getContext()).checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                AlertDialogHelper.showPermissionDialog(getContext(), getResources().getString(R.string.permission_confirmation), getResources().getString(R.string.permission_confirmation), listener);
                return;
            }
        }
        MapBottomSheetFragment fragmentDialog = MapBottomSheetFragment.Companion.newInstance(mBinding.getData().getStreet());
        fragmentDialog.setMapListener(address -> {
            String message = address.getAddressLine(0);
            String title = address.getFeatureName();

            mBinding.streetEt.setText(message);
            mBinding.zipEt.setText(address.getPostalCode());

            if (address.getCountryName() != null && mBinding.getData().getCountryStateData().getCountryNameList(getContext()).contains(address.getCountryName())) {
                int pos = mBinding.getData().getCountryStateData().getCountryPositionFromCountryName(address.getCountryName());
                mBinding.countrySpinner.setSelection(pos);
                if (address.getAdminArea() != null && mBinding.getData().getCountryStateData().getStateNameList(pos).contains(address.getAdminArea())) {
                    String stateId = mBinding.getData().getCountryStateData().getStateId(pos, address.getAdminArea());
                    mBinding.provinceSpinner.setSelection(mBinding.getData().getCountryStateData().getStatePosition(pos, stateId));

                }
            }


        });
        fragmentDialog.setCancelable(false);
        fragmentDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Custom Bottom Sheet");
    }

    /**
     * TYPE_BILLING_CHECKOUT : Used to set billing address and redirect to checkout page.
     */

    public enum AddressType {
        TYPE_BILLING, TYPE_SHIPPING, TYPE_ADDITIONAL, TYPE_NEW, TYPE_BILLING_CHECKOUT
    }


}
