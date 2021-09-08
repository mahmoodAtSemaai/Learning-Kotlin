package com.webkul.mobikul.odoo.model.customer.account;

import android.content.Context;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.fragment.app.Fragment;

import com.webkul.mobikul.odoo.BR;
import com.webkul.mobikul.odoo.BuildConfig;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.fragment.AccountInfoFragment;
import com.webkul.mobikul.odoo.helper.AppSharedPref;

import java.util.Locale;

/**
 * Created by shubham.agarwal on 30/5/17.
 */

public class AccountInforData extends BaseObservable {
    @SuppressWarnings("unused")
    private static final String TAG = "AccountInforData";

    private String name;
    private boolean changePassword;
    private String newPassword;
    private String confirmNewPassword;
    private boolean displayError;
    private Context mContext;
    private String temorary = "temporary";
    private String permanent = "permanent";

    public AccountInforData(Context context) {
        mContext = context;
        setName(AppSharedPref.getCustomerName(context));
    }

    @Bindable
    public String getName() {
        if (name == null) {
            return "";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable({"name", "displayError"})
    public String getNameError() {
        if (!isDisplayError()) {
            return "";
        }

        if (getName().isEmpty()) {
            return mContext.getString(R.string.error_this_is_a_required_field);
        }
        return "";
    }

    @Bindable
    public boolean isChangePassword() {
        return changePassword;
    }

    public void setChangePassword(boolean changePassword) {
        this.changePassword = changePassword;
        notifyPropertyChanged(BR.changePassword);
    }

    @Bindable
    public String getNewPassword() {
        if (newPassword == null) {
            return "";
        }
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
        notifyPropertyChanged(BR.newPassword);
    }

    @Bindable({"newPassword", "displayError"})
    public String getNewPasswordError() {
        if (!isDisplayError()) {
            return "";
        }

        if (getNewPassword().isEmpty()) {
            return String.format("%s %s", mContext.getString(R.string.new_password), mContext.getResources().getString(R.string.error_is_required));
        }

        if (getNewPassword().length() < BuildConfig.MIN_PASSWORD_LENGTH) {
            return String.format("%s %s", mContext.getString(R.string.new_password), String.format(Locale.getDefault(), mContext.getString(R.string.error_password_length_x), BuildConfig.MIN_PASSWORD_LENGTH));
        }
        return "";
    }


    @Bindable
    public String getConfirmNewPassword() {
        if (confirmNewPassword == null) {
            return "";
        }
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
        notifyPropertyChanged(BR.confirmNewPassword);
    }

    @Bindable({"confirmNewPassword", "displayError"})
    public String getConfirmNewPasswordError() {

        if (!isDisplayError()) {
            return "";
        }

        if (getNewPassword().isEmpty()) {
            return String.format("%s %s", mContext.getString(R.string.confirm_password), mContext.getResources().getString(R.string.error_is_required));
        }

        if (getNewPassword().length() < BuildConfig.MIN_PASSWORD_LENGTH) {
            return String.format("%s %s", mContext.getString(R.string.confirm_password), String.format(Locale.getDefault(), mContext.getString(R.string.error_password_length_x), BuildConfig.MIN_PASSWORD_LENGTH));
        }

        if (!getNewPassword().equals(getConfirmNewPassword())) {
            return mContext.getString(R.string.error_password_not_match);
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


    public boolean isFormValidated() {
        setDisplayError(true);
        Fragment frag = ((BaseActivity) mContext).mSupportFragmentManager.findFragmentByTag(AccountInfoFragment.class.getSimpleName());
        if (frag != null && frag.isAdded()) {
            AccountInfoFragment accountInfoFragment = (AccountInfoFragment) frag;
            if (!getNameError().isEmpty()) {
                accountInfoFragment.mBinding.nameEt.requestFocus();
                return false;
            }

            if (isChangePassword()) {
                if (!getNewPasswordError().isEmpty()) {
                    accountInfoFragment.mBinding.newPasswordEt.requestFocus();
                    return false;
                }

                if (!getConfirmNewPasswordError().isEmpty()) {
                    accountInfoFragment.mBinding.confirmpasswordEt.requestFocus();
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public String getTemorary() {
        return temorary;
    }

    public String getPermanent() {
        return permanent;
    }
}
