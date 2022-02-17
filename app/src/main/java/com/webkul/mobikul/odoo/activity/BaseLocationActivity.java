package com.webkul.mobikul.odoo.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.snackbar.Snackbar;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.fragment.NewAddressFragment;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.LocationHelper;
import com.webkul.mobikul.odoo.helper.SnackbarHelper;
import com.webkul.mobikul.odoo.service.FetchAddressIntentService;

import static com.webkul.mobikul.odoo.activity.HomeActivity.RC_ACCESS_FINE_LOCATION_NEW_ADDRESS;
import static com.webkul.mobikul.odoo.activity.HomeActivity.RC_CHECK_LOCATION_SETTINGS;
import static com.webkul.mobikul.odoo.constant.ApplicationConstant.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS;
import static com.webkul.mobikul.odoo.constant.ApplicationConstant.UPDATE_INTERVAL_IN_MILLISECONDS;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_LOCATION_RESULT_ADDRESS;
import static com.webkul.mobikul.odoo.helper.LocationHelper.LOCATION_DATA_EXTRA;
import static com.webkul.mobikul.odoo.helper.LocationHelper.LOCATION_RECEIVER;
import static com.webkul.mobikul.odoo.helper.LocationHelper.LOCATION_SUCCESS_RESULT;
import static com.webkul.mobikul.odoo.helper.LocationHelper.getLastLocation;

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

public abstract class BaseLocationActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult>, LocationListener {
    @SuppressWarnings("unused")
    private static final String TAG = "BaseLocationActivity";

    private LocationAddressResultReceiver mLocationAddressResultReceiver;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private GoogleApiClient mGoogleApiClient;
    private boolean mRequestingLocationUpdates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationAddressResultReceiver = new LocationAddressResultReceiver(new Handler());

    }


    private void checkPermission(int code) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            DialogInterface.OnClickListener listener = (dialog, which) -> {
                String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
                requestPermissions(permissions, code);
                dialog.dismiss();
            };
            AlertDialogHelper.showPermissionDialog(this, getResources().getString(R.string.permission_confirmation), getResources().getString(R.string.permission_confirmation), listener);
        }
    }

    public void startFindLocation() {
        createLocationRequest();
        buildLocationSettingsRequest();
        buildGoogleApiClient();
        mGoogleApiClient.connect();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setNumUpdates(1);
    }


    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest); /*dialog will not occur (may be on time) if location setting is not added*/
        mLocationSettingsRequest = builder.build();
    }

    private synchronized void buildGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestPermission();
    }

    public void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkPermission(RC_ACCESS_FINE_LOCATION_NEW_ADDRESS);
            }
        } else {
            if (mRequestingLocationUpdates) {
                checkLocationSettings();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {
        startFetchAddressIntentServ(location);
        stopLocationUpdates();
    }

    public void checkLocationSettings() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                checkPermission(RC_ACCESS_FINE_LOCATION_NEW_ADDRESS);
//            }
//        } else {
        if (mRequestingLocationUpdates) {
            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, mLocationSettingsRequest);
            result.setResultCallback(this);
        }
//        }
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                startFetchAddressIntentServ(LocationHelper.getLastLocation(mGoogleApiClient));
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {
                    status.startResolutionForResult(this, RC_CHECK_LOCATION_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i(TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                break;
        }
    }

    private void startFetchAddressIntentServ(Location location) {
        if (location == null) {
            startLocationUpdates();
            return;
        }

        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(LOCATION_RECEIVER, mLocationAddressResultReceiver);
        intent.putExtra(LOCATION_DATA_EXTRA, location);
        startService(intent);
    }

    protected void startLocationUpdates() {
        //noinspection MissingPermission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            checkPermission(324);

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this).setResultCallback(status -> mRequestingLocationUpdates = true);
    }


    protected void onStart() {

        super.onStart();
    }

    protected void onStop() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
        super.onStop();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this).setResultCallback(status -> mRequestingLocationUpdates = false);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_ACCESS_FINE_LOCATION_NEW_ADDRESS) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkLocationSettings();
            } else {
                SnackbarHelper.getSnackbar(this, getString(R.string.error_please_provide_permission_to_pick_location), Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
            }
        } else if (requestCode == 324) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, BaseLocationActivity.this).setResultCallback(status -> mRequestingLocationUpdates = true);
            else
                Toast.makeText(this, this.getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_CHECK_LOCATION_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        startFetchAddressIntentServ(getLastLocation(mGoogleApiClient));
                        break;
                    case Activity.RESULT_CANCELED:
                        SnackbarHelper.getSnackbar(this, getString(R.string.error_enable_location_of_device), Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
                        break;
                }
                break;
        }
    }

    @SuppressWarnings("unused")
    public boolean isRequestingLocationUpdates() {
        return mRequestingLocationUpdates;
    }

    public void setRequestingLocationUpdates(boolean requestingLocationUpdates) {
        mRequestingLocationUpdates = requestingLocationUpdates;
    }

    private class LocationAddressResultReceiver extends ResultReceiver {
        LocationAddressResultReceiver(Handler handler) {
            //noinspection RestrictedApi
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == LOCATION_SUCCESS_RESULT) {
                Fragment fragment = mSupportFragmentManager.findFragmentByTag(NewAddressFragment.class.getSimpleName());
                if (fragment != null && fragment.isAdded()) {
                    ((NewAddressFragment) fragment).updateCurrentAddress(resultData.getParcelable(BUNDLE_KEY_LOCATION_RESULT_ADDRESS));
                }
            }
        }
    }
}
