package com.webkul.mobikul.odoo.helper;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.view.ContextThemeWrapper;

import java.util.Locale;

/**
 * Created by shubham.agarwal on 14/1/17. @Webkul Software Pvt. Ltd
 */
public class LocaleHelper {
    private static Locale sLocale;


    public static void updateConfig(ContextThemeWrapper wrapper) {
        if (sLocale != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Configuration configuration = new Configuration();
            configuration.setLocale(sLocale);
            wrapper.applyOverrideConfiguration(configuration);
        }
    }

//    @SuppressWarnings("unused")
//    public static void changeLanguange(Context context) {
//        LocaleHelper.setLocale(new Locale(AppSharedPref.getStoreCode(((BaseActivity) context).getApplication())));
//        LocaleHelper.updateConfig(((BaseActivity) context).getApplication()
//                , ((BaseActivity) context).getBaseContext().getResources().getConfiguration());
//        Intent intent = new Intent(context, SplashScreenActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        context.startActivity(intent);
//        ((BaseActivity) context).finish();
//    }

    /*currently application have single store*/
    public static void setLocale(Locale locale) {
        sLocale = locale;
        if (sLocale != null) {
            Locale.setDefault(sLocale);
        }
    }

    @SuppressWarnings("deprecation")
    static void updateConfig(Application app, Configuration configuration) {
        if (sLocale != null && Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //Wrapping the configuration to avoid Activity endless loop
            Configuration config = new Configuration(configuration);
            // We must use the now-deprecated config.locale and res.updateConfiguration here,
            // because the replacements aren't available till API level 24 and 17 respectively.
            config.locale = sLocale;
            Resources res = app.getBaseContext().getResources();
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
    }

}