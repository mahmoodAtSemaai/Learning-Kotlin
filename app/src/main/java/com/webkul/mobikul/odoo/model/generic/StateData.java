package com.webkul.mobikul.odoo.model.generic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shubham.agarwal on 11/5/17.
 */

public class StateData {
    @SuppressWarnings("unused")
    private static final String TAG = "StateData";

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;

    public String getId() {
        if (id == null) {
            return "";
        }
        return id;
    }

    public String getName() {
        if (name == null) {
            return "";
        }

        return name;
    }
}
