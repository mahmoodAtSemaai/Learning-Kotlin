package com.webkul.mobikul.odoo.model.generic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.model.customer.address.addressBodyParams.AddressAPIConstants;

public class SubDistrictData {
    private static final String TAG = "SubDistrictData";

    @SerializedName(AddressAPIConstants.ID)
    @Expose
    private int id;
    @SerializedName(AddressAPIConstants.NAME)
    @Expose
    private String name;
    @SerializedName(AddressAPIConstants.AVAILABILITY)
    @Expose
    private boolean available;

    public static String getTAG() {
        return TAG;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIs_available() {
        return available;
    }

    public void setIs_available(boolean is_available) {
        this.available = is_available;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        if (name == null) {
            return "";
        }

        return name;
    }

}
