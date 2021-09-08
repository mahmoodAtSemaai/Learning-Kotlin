package com.webkul.mobikul.odoo.helper;

import android.app.Activity;
import android.graphics.Color;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.webkul.mobikul.odoo.R;

import org.jetbrains.annotations.NotNull;


/**
 * Created by shubham.agarwal on 21/2/17. @Webkul Software Pvt. Ltd
 */

public class SnackbarHelper {
    @SuppressWarnings("unused")
    private static final String TAG = "SnackbarHelper";

    public static Snackbar getSnackbar(@NotNull Activity activity, String message, int duration) {
        return getSnackbar(activity, message, duration, SnackbarType.TYPE_NORMAL);
    }

    public static Snackbar getSnackbar(@NotNull Activity activity, String message, int duration, SnackbarType snackbarType) {
        Snackbar snackbar;
        snackbar = Snackbar.make(activity.findViewById(android.R.id.content), message, duration);
        ViewGroup group = (ViewGroup) snackbar.getView();
        if (snackbarType == SnackbarType.TYPE_WARNING) {
            group.setBackgroundColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.red_500));
        } else {
            group.setBackgroundColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.colorPrimaryDark));
        }
        TextView tv = group.findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setMaxLines(10);
        tv.setTextColor(Color.WHITE);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) group.getLayoutParams();
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        group.setLayoutParams(params);
        return snackbar;
    }

    public enum SnackbarType {
        TYPE_NORMAL, TYPE_WARNING
    }


}
