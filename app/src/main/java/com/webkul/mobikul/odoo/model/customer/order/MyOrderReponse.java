package com.webkul.mobikul.odoo.model.customer.order;

import androidx.databinding.Bindable;
import android.os.Parcel;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.BR;
import com.webkul.mobikul.odoo.model.BaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham.agarwal on 10/5/17.
 */

public class MyOrderReponse extends BaseResponse {

    @SerializedName("tcount")
    @Expose
    private int totalCount;
    @SerializedName("recentOrders")
    @Expose
    private List<OrderData> orders = null;

    private boolean lazyLoading;

    protected MyOrderReponse(@Nullable Parcel in) {
        super(in);
    }


    public int getTotalCount() {
        return totalCount;
    }

    public List<OrderData> getOrders() {
        if (orders == null) {
            return new ArrayList<>();
        }
        return orders;
    }

    @Bindable
    public boolean isLazyLoading() {
        return lazyLoading;
    }

    public void setLazyLoading(boolean lazyLoading) {
        this.lazyLoading = lazyLoading;
        notifyPropertyChanged(BR.lazyLoading);
    }

}
