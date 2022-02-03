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
import com.webkul.mobikul.odoo.activity.UserApprovalActivity;
import com.webkul.mobikul.odoo.dialog_frag.ProductAddedToBagDialogFrag;
import com.webkul.mobikul.odoo.firebase.FirebaseAnalyticsImpl;
import com.webkul.mobikul.odoo.model.home.HomePageResponse;

import java.util.Arrays;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_HOME_PAGE_RESPONSE;

/**
 * Created by shubham.agarwal on 31/5/17.
 */

public class IntentHelper {
    @SuppressWarnings("unused")
    private static final String TAG = "IntentHelper";

    public static void continueShopping(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void goToUserUnapporvedScreen(Context context) {
        context.startActivity(new Intent(context, UserApprovalActivity.class));
    }

    public static void continueShopping(Context context, HomePageResponse homePageResponse) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(BUNDLE_KEY_HOME_PAGE_RESPONSE, homePageResponse);
        context.startActivity(intent);
    }

    public static void goToWhatsApp(Context context) {
        Uri uri = Uri.parse(context.getString(R.string.open_whatsapp_for_support));
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


    //    public static void proceedToCheckout(Context context) {
//        ApiConnection.getAddressBookData(context, new BaseLazyRequest(0, 0)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<MyAddressesResponse>(context) {
//
//            @Override
//            public void onSubscribe(@NonNull Disposable d) {
//                super.onSubscribe(d);
//                AlertDialogHelper.showDefaultProgressDialog(context);
//            }
//
//            @Override
//            public void onNext(@NonNull MyAddressesResponse myAddressesResponse) {
//                super.onNext(myAddressesResponse);
//                if (myAddressesResponse.getAddresses().get(0).getDisplayName().replaceAll("\\n", "").trim().isEmpty()) {
//                    ((BaseActivity) context).mSweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
//                            .setTitleText(context.getString(R.string.error_default_address_not_found))
//                            .setContentText(context.getString(R.string.question_add_new_address_to_place_order))
//                            .setConfirmText(context.getString(R.string.ok))
//                            .setConfirmClickListener(sDialog -> {
//                                // reuse previous dialog instance
//                                FragmentHelper.replaceFragment(R.id.container, context, NewAddressFragment.newInstance(String.valueOf(myAddressesResponse.getAddresses().get(0).getUrl()), context.getString(R.string.edit_billing_address)
//                                        , NewAddressFragment.AddressType.TYPE_BILLING_CHECKOUT), NewAddressFragment.class.getSimpleName(), true, false);
//                                sDialog.dismiss();
//                            });
//                    ((BaseActivity) context).mSweetAlertDialog.show();
//                    ((BaseActivity) context).mSweetAlertDialog.showCancelButton(true);
//                } else {
//                    IntentHelper.beginCheckout(context);
//                }
//            }
//
//            @Override
//            public void onError(@NonNull Throwable t) {
//                super.onError(t);
//            }
//
//            @Override
//            public void onComplete() {
//
//
//            }
//        });
//    }
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
}
