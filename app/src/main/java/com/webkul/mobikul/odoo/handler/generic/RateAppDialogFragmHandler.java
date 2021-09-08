package com.webkul.mobikul.odoo.handler.generic;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.fragment.app.Fragment;

import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.dialog_frag.RateAppDialogFragm;
import com.webkul.mobikul.odoo.helper.AppSharedPref;

/**
 * Created by shubham.agarwal on 25/5/17.
 */

public class RateAppDialogFragmHandler {
    @SuppressWarnings("unused")
    private static final String TAG = "RateAppDialogFragmHandl";
    private Context mContext;

    public RateAppDialogFragmHandler(Context context) {
        mContext = context;
    }


    public void reviewLater() {
        AppSharedPref.setLaunchcount(mContext, 0);
        dismiss();
    }

    private void dismiss() {
        Fragment fragment = ((BaseActivity) mContext).mSupportFragmentManager.findFragmentByTag(RateAppDialogFragm.class.getSimpleName());
        if (fragment != null && fragment.isAdded()) {
            ((RateAppDialogFragm) fragment).dismiss();
        }
    }

    public void reviewNow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Uri uri = Uri.parse("market://details?id=" + mContext.getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                mContext.startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + mContext.getPackageName())));
            }
        } else {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + mContext.getPackageName())));
        }
        dismiss();

    }

}
