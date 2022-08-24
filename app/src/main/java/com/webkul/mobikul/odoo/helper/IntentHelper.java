package com.webkul.mobikul.odoo.helper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.BagActivity;
import com.webkul.mobikul.odoo.activity.CheckoutActivity;
import com.webkul.mobikul.odoo.activity.HomeActivity;
import com.webkul.mobikul.odoo.activity.NewHomeActivity;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.activity.UserApprovalActivity;
import com.webkul.mobikul.odoo.constant.BundleConstant;
import com.webkul.mobikul.odoo.dialog_frag.ProductAddedToBagDialogFrag;
import com.webkul.mobikul.odoo.firebase.FirebaseAnalyticsImpl;
import com.webkul.mobikul.odoo.model.home.HomePageResponse;
import com.webkul.mobikul.odoo.ui.signUpOnboarding.UserOnboardingActivity;

import java.util.Arrays;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_HOME_PAGE_RESPONSE;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by shubham.agarwal on 31/5/17.
 */

public class IntentHelper {
    @SuppressWarnings("unused")
    private static final String TAG = "IntentHelper";

    public static void continueShopping(Context context) {
        Intent intent = new Intent(context, NewHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public static void goToUserUnapprovedScreen(Context context) {
        context.startActivity(new Intent(context, UserOnboardingActivity.class));
    }

    public static void goToUserOnboardingScreen(Context context) {
        context.startActivity(new Intent(context, UserOnboardingActivity.class));
    }

    public static void continueShopping(Context context, HomePageResponse homePageResponse) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(BUNDLE_KEY_HOME_PAGE_RESPONSE, homePageResponse);
        context.startActivity(intent);
    }

    public static void goToWhatsApp(Context context) {
        String semaaiSupportUrl = String.format(context.getString(R.string.semaai_support_whatsapp_url),
                context.getString(R.string.whatsapp_contant_wo_cc).replaceFirst("0", "+62"),
                context.getString(R.string.whatsapp_message));
        Uri uri = Uri.parse(semaaiSupportUrl);
        context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    public static void beginCheckout(Context context) {
        FirebaseAnalyticsImpl.logBeginCheckoutEvent(context);
        Intent intent = new Intent(context, CheckoutActivity.class);
        context.startActivity(intent);
    }


    public static void goToBag(Context context) {
        ProductAddedToBagDialogFrag.dismissDialog();
        context.startActivity(new Intent(context, BagActivity.class));
    }


    public static String intentToString(Intent intent) {
        if (intent == null) {
            return "intent == null ";
        }

        return intent.toString() + " " + bundleToString(intent.getExtras());
    }

    public static String bundleToString(Bundle bundle) {
        StringBuilder out = new StringBuilder("Bundle[");

        if (bundle == null) {
            out.append("null");
        } else {
            boolean first = true;
            for (String key : bundle.keySet()) {
                if (!first) {
                    out.append(", ");
                }

                out.append(key).append('=');

                Object value = bundle.get(key);

                if (value instanceof int[]) {
                    out.append(Arrays.toString((int[]) value));
                } else if (value instanceof byte[]) {
                    out.append(Arrays.toString((byte[]) value));
                } else if (value instanceof boolean[]) {
                    out.append(Arrays.toString((boolean[]) value));
                } else if (value instanceof short[]) {
                    out.append(Arrays.toString((short[]) value));
                } else if (value instanceof long[]) {
                    out.append(Arrays.toString((long[]) value));
                } else if (value instanceof float[]) {
                    out.append(Arrays.toString((float[]) value));
                } else if (value instanceof double[]) {
                    out.append(Arrays.toString((double[]) value));
                } else if (value instanceof String[]) {
                    out.append(Arrays.toString((String[]) value));
                } else if (value instanceof CharSequence[]) {
                    out.append(Arrays.toString((CharSequence[]) value));
                } else if (value instanceof Parcelable[]) {
                    out.append(Arrays.toString((Parcelable[]) value));
                } else if (value instanceof Bundle) {
                    out.append(bundleToString((Bundle) value));
                } else {
                    out.append(value);
                }

                first = false;
            }
        }

        out.append("]");
        return out.toString();
    }


    public static void redirectToSignUpActivity(Context context) {
        AlertDialogHelper.showDefaultWarningDialogWithDismissListener(context, context.getString(R.string.error_login_failure),
                context.getString(R.string.access_denied_message), sweetAlertDialog -> {
                    sweetAlertDialog.dismiss();
                    AppSharedPref.clearCustomerData(context);
                    Intent i = new Intent(context, SignInSignUpActivity.class);
                    context.startActivity(i);
                });

    }
}
