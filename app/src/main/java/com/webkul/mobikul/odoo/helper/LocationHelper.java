package com.webkul.mobikul.odoo.helper;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.webkul.mobikul.odoo.BuildConfig;
import com.webkul.mobikul.odoo.connection.ApiInterface;
import com.webkul.mobikul.odoo.connection.RetrofitClient;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by shubham.agarwal on 16/5/17.
 */

public class LocationHelper {
    public static final int LOCATION_SUCCESS_RESULT = 0;
    public static final int LOCATION_FAILURE_RESULT = 1;
    public static final String LOCATION_RECEIVER = BuildConfig.APPLICATION_ID + ".RECEIVER";
    public static final String LOCATION_DATA_EXTRA = BuildConfig.APPLICATION_ID + ".LOCATION_DATA_EXTRA";
    @SuppressWarnings("unused")
    private static final String TAG = "LocationHelper";

    public static Location getLastLocation(GoogleApiClient googleApiClient) {
        if (!googleApiClient.isConnected()) {
            googleApiClient.connect();
            Log.e(TAG, "getLastLocationFromFusedLocationApi: Google API client is not connected");
            return null;
        }
        //noinspection MissingPermission
        return LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
    }


    @Nullable
    public static Address getAddress(Context context, double latitude, double longitude, int maxResults) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, maxResults);
        } catch (IOException ioException) {
            Log.e(TAG, "Unavailable", ioException);
            return null;
        } catch (IllegalArgumentException illegalArgumentException) {
            Log.e(TAG, "Unavailable" + ". " + "Latitude = " + latitude + ", Longitude = " + longitude, illegalArgumentException);
            return null;
        }
        if (addresses == null || addresses.isEmpty()) {
            return null;
        } else {
            return addresses.get(0);
        }
    }

    @SuppressWarnings("unused")
    @NonNull
    public static String getAddressStr(@Nullable Address address, @Nullable CharSequence delimiter) {
        if (address == null) {
            return "";
        }
        ArrayList<String> addressFragments = new ArrayList<>();
        for (int addressLinesIdx = 0; addressLinesIdx < address.getMaxAddressLineIndex(); addressLinesIdx++) {
            addressFragments.add(address.getAddressLine(addressLinesIdx));
        }
        return TextUtils.join(delimiter == null ? System.getProperty("line.separator") : delimiter, addressFragments);
    }
    public static LatLng getLatLang(Context context, @NotNull String strAddress) {
        LatLng latLng;
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            if (address.size() == 0) {
                return null;
            }


            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (final Exception ex) {
            ex.printStackTrace();
//            if (ex instanceof IOException) {
            latLng = null;
           /* String url = "json?address=" + strAddress+"&sensor=true&key="+context.getString(com.webkul.mobikul.R.string.maps_api_key);
            Call<MapResponse> call = RetrofitClient.getCLientForMap().create(ApiInterface.class).getGoogleMapResponse(url);
            call.enqueue(mapResponseCallback);
            while (latLng == null){

            }*/
//            }
        }
        return latLng;
    }
}


