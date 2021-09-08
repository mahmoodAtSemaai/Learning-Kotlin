package com.webkul.mobikul.odoo.helper;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by shubham.agarwal on 24/3/17.
 */

public class ColorHelper {
    @SuppressWarnings("unused")
    private static final String TAG = "ColorHelper";

    public static int getColor(Context context, int attrId) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(attrId, typedValue, true);
        return typedValue.data;
    }

}
