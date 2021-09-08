package com.webkul.mobikul.odoo.model.generic;

import android.content.Context;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import android.text.TextUtils;

import com.webkul.mobikul.odoo.BR;
import com.webkul.mobikul.odoo.R;

import org.jsoup.helper.StringUtil;

/**
 * Created by shubham.agarwal on 6/6/17.
 */

public class EnterQtyDialogFragmentData extends BaseObservable {
    @SuppressWarnings("unused")
    private static final String TAG = "EnterQtyDialogFragmentD";
    private Context mContext;


    private String qty;

    public EnterQtyDialogFragmentData(Context context, int currentQty) {
        mContext = context;
        qty = String.valueOf(currentQty);
    }

    @Bindable
    public String getQty() {
        if (qty == null) {
            return "1";
        }
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
        notifyPropertyChanged(BR.qty);
    }

    @Bindable({"qty"})
    public String getQtyError() {
        try{

            if (!StringUtil.isNumeric(getQty()) || Integer.parseInt(getQty()) < 1) {
                return mContext.getString(R.string.error_inappropriate_details);
            }
        }catch (NumberFormatException e){
            e.printStackTrace();
            return mContext.getString(R.string.error_inappropriate_details);
        }

        return "";
    }

    public boolean isFormValidated() {

        if (TextUtils.isEmpty(getQtyError())){
            return true;
        }else {
            return false;
        }



//        return getQtyError().isEmpty();
    }
}
