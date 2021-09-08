package com.webkul.mobikul.odoo.helper;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by shubham.agarwal on 3/5/17.
 */

@SuppressWarnings("unused")
public class JsonHelper {
    @SuppressWarnings("unused")
    private static final String TAG = "JsonHelper";


    public static String getJsonStr(Context context, String path) {
        String json = "";
        try {
            InputStream is = context.getAssets().open(path);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return "";
        }
        return json;

    }
}
