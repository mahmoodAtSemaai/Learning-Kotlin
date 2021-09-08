package com.webkul.mobikul.odoo.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.databinding.FragmentNewAddressBinding;
import com.webkul.mobikul.odoo.handler.customer.NewAddressFragHandler;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.helper.SnackbarHelper;
import com.webkul.mobikul.odoo.model.customer.address.AddressFormResponse;
import com.webkul.mobikul.odoo.model.generic.CountryStateData;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_ADDRESS_TYPE;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_TITLE;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_URL;


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
    public FragmentNewAddressBinding mBinding;
    private static final int MAP_PIN_LOCATION_REQUEST_CODE = 103;

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
        if (getArguments().getString(BUNDLE_KEY_URL) == null) {
            ApiConnection.getCountryStateData(getContext()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<CountryStateData>(getContext()) {
                @Override
                public void onNext(@NonNull CountryStateData countryStateData) {
                    super.onNext(countryStateData);
                    if (isAdded()) {

                        if (countryStateData.isAccessDenied()) {
                            AlertDialogHelper.showDefaultWarningDialogWithDismissListener(getContext(), getString(R.string.error_login_failure), getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    AppSharedPref.clearCustomerData(getContext());
                                    Intent i = new Intent(getContext(), SignInSignUpActivity.class);
                                    i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, getActivity().getClass().getSimpleName());
                                    startActivity(i);
                                }
                            });
                        } else {
                            mBinding.setData(new AddressFormResponse(null));
                            mBinding.getData().init(getContext(), countryStateData, (AddressType) getArguments().getSerializable(BUNDLE_KEY_ADDRESS_TYPE));
                            setUpCountryStateSpinners(mBinding.getData());
                        }
                    }
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    super.onError(e);
                }

                @Override
                public void onComplete() {
                    mBinding.setHandler(new NewAddressFragHandler(getContext(), mBinding.getData(), null, (AddressType) getArguments().getSerializable(BUNDLE_KEY_ADDRESS_TYPE)));
                }
            });
        } else {
            Observable<AddressFormResponse> addressFormResponseDataObservable = ApiConnection.getAddressFormData(getContext(), getArguments().getString(BUNDLE_KEY_URL)).subscribeOn(Schedulers.io());
            Observable<CountryStateData> countryStateDataObservable = ApiConnection.getCountryStateData(getContext()).subscribeOn(Schedulers.io());

            Observable.zip(addressFormResponseDataObservable, countryStateDataObservable, (addressFormResponseData, countryStateData) -> {
                if (addressFormResponseData.isAccessDenied()) {
                    addressFormResponseData.setAccessDenied(true);
                }

                if (countryStateData.isAccessDenied()) {
                    addressFormResponseData.setAccessDenied(true);
                }

                addressFormResponseData.setCountryStateData(countryStateData);
                return addressFormResponseData;
            })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CustomObserver<AddressFormResponse>(getContext()) {
                        @Override
                        public void onNext(@NonNull AddressFormResponse addressFormResponse) {
                            super.onNext(addressFormResponse);
                            if (addressFormResponse.isAccessDenied()) {
                                AlertDialogHelper.showDefaultWarningDialogWithDismissListener(getContext(), getString(R.string.error_login_failure), getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        AppSharedPref.clearCustomerData(getContext());
                                        Intent i = new Intent(getContext(), SignInSignUpActivity.class);
                                        i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, getActivity().getClass().getSimpleName());
                                        startActivity(i);
                                    }
                                });
                            } else {
                                addressFormResponse.init(getContext(), addressFormResponse.getCountryStateData(), (AddressType) getArguments().getSerializable(BUNDLE_KEY_ADDRESS_TYPE));
                                mBinding.setData(addressFormResponse);
                                setUpCountryStateSpinners(addressFormResponse);
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            super.onError(e);
                        }

                        @Override
                        public void onComplete() {
                            mBinding.setHandler(new NewAddressFragHandler(getContext(), mBinding.getData(), getArguments().getString(BUNDLE_KEY_URL), (AddressType) getArguments().getSerializable(BUNDLE_KEY_ADDRESS_TYPE)));
                        }
                    });
        }

    }

    private void setUpCountryStateSpinners(AddressFormResponse addressFormResponse) {
        mBinding.countrySpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, addressFormResponse.getCountryStateData().getCountryNameList(getContext())));
        mBinding.countrySpinner.setSelection(addressFormResponse.getCountryStateData().getCountryPosition(mBinding.getData().getCountryId()));
        mBinding.countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int countryPos, long id) {
                addressFormResponse.setCountryId(addressFormResponse.getCountryStateData().getCountries().get(countryPos).getId());
                if (!addressFormResponse.getCountryStateData().getStateNameList(countryPos).isEmpty()) {
                    mBinding.stateContainer.setVisibility(View.VISIBLE);
                    mBinding.stateSpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, addressFormResponse.getCountryStateData().getStateNameList(countryPos)));
                    mBinding.stateSpinner.setSelection(addressFormResponse.getCountryStateData().getStatePosition(countryPos, addressFormResponse.getStateId()));
                    mBinding.stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int statePos, long id) {
                            addressFormResponse.setStateId(addressFormResponse.getCountryStateData().getCountries().get(countryPos).getStates().get(statePos).getId());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });

                } else {
                    addressFormResponse.setStateId("");
                    mBinding.stateContainer.setVisibility(View.GONE);
                    mBinding.stateSpinner.setAdapter(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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
        mBinding.getData().setCity(address.getLocality() == null ? "" : address.getLocality());
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

    /**
     * TYPE_BILLING_CHECKOUT : Used to set billing address and redirect to checkout page.
     */

    public enum AddressType {
        TYPE_BILLING, TYPE_SHIPPING, TYPE_ADDITIONAL, TYPE_NEW, TYPE_BILLING_CHECKOUT
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
            mBinding.cityEt.setText(address.getLocality());
            mBinding.zipEt.setText(address.getPostalCode());

            if (address.getCountryName() != null && mBinding.getData().getCountryStateData().getCountryNameList(getContext()).contains(address.getCountryName())) {
                int pos = mBinding.getData().getCountryStateData().getCountryPositionFromCountryName(address.getCountryName());
                mBinding.countrySpinner.setSelection(pos);
                if (address.getAdminArea() != null && mBinding.getData().getCountryStateData().getStateNameList(pos).contains(address.getAdminArea())) {
                    String stateId = mBinding.getData().getCountryStateData().getStateId(pos, address.getAdminArea());
                    mBinding.stateSpinner.setSelection(mBinding.getData().getCountryStateData().getStatePosition(pos, stateId));

                }
            }


        });
        fragmentDialog.setCancelable(false);
        fragmentDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Custom Bottom Sheet");
    }


}
