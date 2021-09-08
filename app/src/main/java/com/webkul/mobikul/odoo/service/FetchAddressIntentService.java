package com.webkul.mobikul.odoo.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.webkul.mobikul.odoo.helper.LocationHelper;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_LOCATION_RESULT_ADDRESS;
import static com.webkul.mobikul.odoo.helper.LocationHelper.LOCATION_DATA_EXTRA;
import static com.webkul.mobikul.odoo.helper.LocationHelper.LOCATION_FAILURE_RESULT;
import static com.webkul.mobikul.odoo.helper.LocationHelper.LOCATION_RECEIVER;
import static com.webkul.mobikul.odoo.helper.LocationHelper.LOCATION_SUCCESS_RESULT;

/**
 * Asynchronously handles an intent using a worker thread. Receives a ResultReceiver object and a
 * location through an intent. Tries to fetch the address for the location using a Geocoder, and
 * sends the result to the ResultReceiver.
 */
public class FetchAddressIntentService extends IntentService {
    private static final String TAG = "FetchAddressIS";

    private ResultReceiver mResultReceiver;

    public FetchAddressIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mResultReceiver = intent.getParcelableExtra(LOCATION_RECEIVER);
        if (mResultReceiver == null) {
            Log.wtf(TAG, "No receiver received. There is nowhere to send the results.");
            return;
        }

        Location location = intent.getParcelableExtra(LOCATION_DATA_EXTRA);
        Address address = LocationHelper.getAddress(this, location.getLatitude(), location.getLongitude(), 1);
        Log.d(TAG, "onHandleIntent: " + address);
        Log.d(TAG, "onHandleIntent: long " + location.getLongitude());
        Log.d(TAG, "onHandleIntent: lat " + location.getLatitude());

        if (address == null) {
            deliverResultToReceiver(LOCATION_FAILURE_RESULT, null);
        } else {
            deliverResultToReceiver(LOCATION_SUCCESS_RESULT, address);
        }
    }

    private void deliverResultToReceiver(int resultCode, Address address) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_KEY_LOCATION_RESULT_ADDRESS, address);
        mResultReceiver.send(resultCode, bundle);
    }
}
