package com.webkul.mobikul.marketplace.odoo.model;

import android.app.Activity;
import android.content.Context;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.google.android.material.snackbar.Snackbar;

import com.webkul.mobikul.marketplace.odoo.databinding.ActivityBecomeSellerBinding;
import com.webkul.mobikul.odoo.BR;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.SnackbarHelper;

/**
 * Webkul Software.
 * <p>
 * Android Studio version 3.0+
 * Java version 1.7+
 *
 * @author Webkul <support@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul.marketplace.odoo.model
 * @copyright 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */
public class BecomeSellerData extends BaseObservable{

    private String profileURL;
    private boolean displayError;
    private Context mContext;
    private boolean isTermAndCondition = false;
    private String countryId;

    public BecomeSellerData(Context mContext) {
        this.mContext = mContext;
    }

    @Bindable
    public String getProfileURL() {
        if (profileURL == null) {
            return "";
        }
        return profileURL;
    }

    public void setProfileURL(String url) {
        this.profileURL = url;
        notifyPropertyChanged(BR.profileURL);
    }

    @Bindable({"displayError", "profileURL"})
    public String getProfileURLError() {
        if (!isDisplayError()) {
            return "";
        }
        if (getProfileURL().isEmpty()) {
            return String.format("%s %s", mContext.getString(com.webkul.mobikul.odoo.R.string.profile_url), mContext.getResources().getString(com.webkul.mobikul.odoo.R.string.error_is_required));
        }
        return "";
    }

    @Bindable
    public boolean isDisplayError() {
        return displayError;
    }

    public void setDisplayError(boolean displayError) {
        this.displayError = displayError;
        notifyPropertyChanged(BR.displayError);
    }

    public boolean isTermAndCondition() {
        return isTermAndCondition;
    }

    public void setTermAndCondition(boolean termAndCondition) {
        isTermAndCondition = termAndCondition;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }


    public boolean isFormValidated(ActivityBecomeSellerBinding mBinding) {
        setDisplayError(true);
        if (AppSharedPref.isTermAndCondition(mContext) && !isTermAndCondition()){
            SnackbarHelper.getSnackbar((Activity) mContext, mContext.getString(com.webkul.mobikul.odoo.R.string.plese_accept_tnc), Snackbar.LENGTH_LONG).show();
            return false;
        }

        if (!getProfileURLError().isEmpty()) {
            mBinding.profileUrlEt.requestFocus();
            return false;
        }



        setDisplayError(false);
        return  true;
    }
}
