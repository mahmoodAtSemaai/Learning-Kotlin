package com.webkul.mobikul.odoo.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.custom.BadgeDrawable;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Shubham Agarwal on 4/1/17. @Webkul Software Private limited
 */
public class Helper {

    private static final String TAG = "Helper";
    private static final String IMAGE_RESOLUTION_128 = "image_128";
    private static final String IMAGE_RESOLUTION_256 = "image_256";
    private static final String IMAGE_RESOLUTION_512 = "image_512";
    private static final String IMAGE_RESOLUTION_1024 = "image_1024";
    private static final String IMAGE_RESOLUTION_1920 = "image_1920";

    private static final class Delimitter {
        private static final String DOT = "\\.";
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public static int getScreenWidth() {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return metrics.widthPixels;
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public static int getScreenHeight() {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return metrics.heightPixels;
    }

    public static int getScreenDensityDpi(Context context) {
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    public static String getUpdateImageUrlForScreen(Context context, String imageUrl) {
        int screenDensityDpi = getScreenDensityDpi(context);
        String oldReplacingChar = IMAGE_RESOLUTION_1920;
        switch (screenDensityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                imageUrl = imageUrl.replace(oldReplacingChar, IMAGE_RESOLUTION_128);
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                imageUrl = imageUrl.replace(oldReplacingChar, IMAGE_RESOLUTION_256);
                break;
            case DisplayMetrics.DENSITY_HIGH:
                imageUrl = imageUrl.replace(oldReplacingChar, IMAGE_RESOLUTION_512);
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                imageUrl = imageUrl.replace(oldReplacingChar, IMAGE_RESOLUTION_1024);
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
            case DisplayMetrics.DENSITY_XXXHIGH:
                break;
        }
        return imageUrl;
    }

    @SuppressWarnings("unused")
    public static void hideKeyboard(Context context) {
        if (context instanceof Activity) {
            hideKeyboard((Activity) context);
        }
    }

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            //noinspection ConstantConditions
            if (activity.getCurrentFocus().getWindowToken() != null)
                inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            Log.e(TAG, "hideKeyboard: " + e.getMessage());
        }
    }

    public static void setBadgeCount(Context context, LayerDrawable icon, int cartCount) {
        BadgeDrawable badge;
        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_menu_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }
        badge.setCount(String.valueOf(cartCount));
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_menu_badge, badge);
    }

    public static void setBadgeCount(Context context, LayerDrawable icon, int cartCount, int badgeColor) {
        BadgeDrawable badge;
        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_menu_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context, badgeColor);
        }
        badge.setCount(String.valueOf(cartCount));
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_menu_badge, badge);
    }

    @SuppressWarnings("unused")
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String initCap(String string) {
        try {
            return string.substring(0, 1).toUpperCase() + string.substring(1);
        } catch (StringIndexOutOfBoundsException e) {
            return "";
        }
    }

    @SuppressWarnings("unused")
    static String hideEmailChars(String email) {
        String[] emailStrings = email.split("@", 2);
        return emailStrings[0].replaceAll("[\\w\\W]", "*").concat(String.valueOf(emailStrings[0].charAt(emailStrings[0].length() - 1))).concat("@").concat(emailStrings[1]);
    }

    @SuppressWarnings("WeakerAccess")
    public static void logBundleData(Bundle bundle) {
        Log.d(TAG, "logBundleData: ");
        if (bundle != null) {
            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                Log.d(TAG, String.format("%s %s (%s)", key, value == null ? "null" : value.toString(), value == null ? "null" : value.getClass().getName()));
            }
            return;
        }

        Log.i(TAG, "BUNDLE DATA IS NULL");
    }

    @SuppressWarnings("unused")
    public static void logIntentBundleData(Intent intent) {
        Log.d(TAG, "logIntentBundleData: ");
        if (intent != null) {
            logBundleData(intent.getExtras());
            return;
        }
        Log.i(TAG, "INTENT DATA IS NULL");

    }

    @SuppressWarnings("unused")
    public static float round(float value, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(value));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }


    @SuppressWarnings("unused")
    public static String removeLink(String commentstr) {
        String urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern p = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(commentstr);
        int i = 0;
        while (m.find()) {
            commentstr = commentstr.replaceAll(m.group(i), "").trim();
            i++;
        }
        return commentstr;
    }

    public static void showKeyboard(View view) {
        view.requestFocus();

        if (!isHardKeyboardAvailable(view)) {
            InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(view, 0);
        }
    }

    private static boolean isHardKeyboardAvailable(View view) {
        return view.getContext().getResources().getConfiguration().keyboard != Configuration.KEYBOARD_NOKEYS;
    }

    public static boolean isVoiceAvailable(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        return activities.size() > 0;
    }

    public static void hiderAllMenuItems(Menu menu) {
        /*Hiding All the menu item */
        for (int menuItemIdx = 0; menuItemIdx < menu.size(); menuItemIdx++) {
            menu.getItem(menuItemIdx).setVisible(false);
        }
    }

    public static void showCartMenuItem(Menu menu) {
        for (int menuItemIdx = 0; menuItemIdx < menu.size(); menuItemIdx++) {
            if (menu.getItem(menuItemIdx).getItemId() == R.id.menu_item_bag) {
                menu.getItem(menuItemIdx).setVisible(true);
                return;
            }
        }
    }

    public static String getStringDateAndTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        return simpleDateFormat.format(new Date());
    }


    public static boolean hasCamera(Context context) {
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static void enableDarkModeInWebView(Context context, WebView webview) {
        /**
         * Checks if webview supports darkmode or not
         * if yes the check if system's darkmode is enabled
         * if yes then enable darkmode in webview
         */
        if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
            if (AppSharedPref.isDarkMode(context))
                WebSettingsCompat.setForceDark(webview.getSettings(), WebSettingsCompat.FORCE_DARK_ON);
        }
    }

    public static boolean isRemoteVersionHigher(String remoteVersion, String currentVersion) {
        if (currentVersion.isEmpty() || remoteVersion.isEmpty()) return false;
        String[] remoteVersionArray = remoteVersion.split(Delimitter.DOT),
                currentVersionArray = currentVersion.split(Delimitter.DOT);
        for (int i = 0; i < remoteVersionArray.length; i++) {
            int remoteVersionInt = Integer.parseInt(remoteVersionArray[i]),
                    currentVersionInt = Integer.parseInt(currentVersionArray[i]);
            if (remoteVersionInt > currentVersionInt)
                return true;
            else if (remoteVersionInt < currentVersionInt)
                return false;
        }
        return false;
    }

    public static String getScreenName(Context context) {
        return context.getApplicationContext().getClass().getSimpleName().replaceAll("Activity|Fragment", "");
    }

    public static String getScreenName(String fragmentOrActivityName) {
        return fragmentOrActivityName.replaceAll("Activity|Fragment", "");
    }

}