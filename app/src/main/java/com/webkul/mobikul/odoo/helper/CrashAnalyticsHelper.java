package com.webkul.mobikul.odoo.helper;

/**
 * Created by shubham.agarwal on 27/6/17.
 */

public class CrashAnalyticsHelper {

    @SuppressWarnings("unused")
    private static final String TAG = "CrashAnalyticsHelper";

    public void forceCrash() {
        throw new RuntimeException("This is a crash");
    }


}
