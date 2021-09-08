package com.webkul.mobikul.odoo.model.generic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham.agarwal on 11/5/17.
 */

public class CountryData {

    @SuppressWarnings("unused")
    private static final String TAG = "CountryData";

    @SerializedName("states")
    @Expose
    private List<StateData> states = null;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;

    public List<StateData> getStates() {
        if (states == null) {
            return new ArrayList<>();
        }

        return states;
    }

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
