package com.webkul.mobikul.marketplace.odoo.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.marketplace.odoo.model.SellerOrderLineDetail;

/**
 * Created by aastha.gupta on 1/3/18 in Mobikul_Odoo_Application.
 */

public class SellerOrderDetailsResponse {

    @SerializedName("sellerOrderLineDetail")
    @Expose
    public SellerOrderLineDetail sellerOrderLineDetail;
}
