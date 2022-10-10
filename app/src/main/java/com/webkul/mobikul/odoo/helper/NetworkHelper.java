package com.webkul.mobikul.odoo.helper;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import com.google.android.material.snackbar.Snackbar;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.BaseActivity;

import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.Response;

/**
 * Created by shubham.agarwal on 2/2/17. @Webkul Software Pvt. Ltd
 */

public class NetworkHelper {

    /*NETWORK RESPONSE CODE*/

    @SuppressWarnings("unused")
    /*unknown failure , however this will never occur*/
    public static final int NW_RESPONSE_CODE_INVALID_REQUEST_TYPE = 0;
    public static final int NW_RESPONSE_CODE_SUCCESS = 1;
    public static final int NW_RESPONSE_CODE_NEW_AUTH_KEY_GENERATED = 2;
    public static final int NW_RESPONSE_CODE_AUTH_FAILURE = 3;


    @SuppressWarnings("unused")
    private static final String TAG = "NetworkHelper";

    public static void onFailure(Throwable t, Activity activity) {
        if (activity == null) {
            return;
        }
        /*dismiss dialog*/
        if(activity instanceof BaseActivity) {
            AlertDialogHelper.dismiss(((BaseActivity) activity).mSweetAlertDialog);
        }
        t.printStackTrace();

        /*display message*/
        if (t instanceof SocketTimeoutException) {
            SnackbarHelper.getSnackbar(activity, activity.getString(R.string.error_request_timeout), Snackbar.LENGTH_LONG, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
        } else if (t instanceof IOException) {
            /*NO NETWORK */
            if (isNetworkAvailable(activity)) {
                SnackbarHelper.getSnackbar(activity, activity.getString(R.string.error_request_timeout), Snackbar.LENGTH_LONG, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
            } else {
                AlertDialogHelper.showDefaultWarningDialog(activity, activity.getString(R.string.error_network_unavailble), activity.getString(R.string.error_no_network_cross_check_connection));
            }
        } else {
            SnackbarHelper.getSnackbar(activity, activity.getString(R.string.error_request_failed), Snackbar.LENGTH_LONG, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
        }
    }


    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


    public static boolean isValidResponse(Activity activity, Response responseObj, boolean displayError) {
        if (activity == null) {
            return false;
        }
        if (responseObj == null || !responseObj.isSuccessful() || responseObj.body() == null) {
            if (displayError) {
                SnackbarHelper.getSnackbar(activity, activity.getString(R.string.error_request_failed), Snackbar.LENGTH_LONG, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
            }
            AlertDialogHelper.dismiss(activity);
            return false;
        }
        return true;
    }

}
