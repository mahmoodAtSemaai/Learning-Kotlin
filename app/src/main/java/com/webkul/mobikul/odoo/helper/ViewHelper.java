package com.webkul.mobikul.odoo.helper;

import android.content.Context;
import androidx.core.widget.NestedScrollView;
import android.util.TypedValue;

/**
 * Created by shubham.agarwal on 18/5/17.
 */

public class ViewHelper {

    @SuppressWarnings("unused")
    private static final String TAG = "ViewHelper";

    public static int dpToPx(Context context, int sizeInDp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (sizeInDp * scale + 0.5f);
    }

    @SuppressWarnings("WeakerAccess")
    public static float pxToDp(Context context, int sizeInPx) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sizeInPx, context.getResources().getDisplayMetrics());
    }


    public static void focusOnView(NestedScrollView nestedScrollView, int y) {
        nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, y));
    }

    public static int getSpanCount(Context context) {
        if (context.getResources().getDisplayMetrics().widthPixels >= 600) {
            return 3;
        } else {
            return 2;
        }
    }
}
