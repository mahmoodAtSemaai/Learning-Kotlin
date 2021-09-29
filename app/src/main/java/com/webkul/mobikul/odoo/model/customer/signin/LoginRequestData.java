package com.webkul.mobikul.odoo.model.customer.signin;

import android.content.Context;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.fragment.app.Fragment;

import com.webkul.mobikul.odoo.BR;
import com.webkul.mobikul.odoo.BuildConfig;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.fragment.LoginFragment;

import java.util.Locale;

/**
 * Created by shubham.agarwal on 29/12/16. @Webkul Software Pvt. Ltd
 */
public class LoginRequestData extends BaseObservable {
    @SuppressWarnings("unused")
    private static final String TAG = "LoginRequestData";
    private String username;
    private String password;
    private boolean displayError;
    private Context mContext;

    public LoginRequestData(Context context) {
        mContext = context;
    }

    /*USER NAME*/

    @Bindable
    public String getUsername() {
        if (username == null) {
            return "";
        }
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    @Bindable({"displayError", "username"})
    public String getUsernameError() {
        if (!isDisplayError()) {
            return "";
        }

        if (getUsername().isEmpty()) {
            return String.format("%s %s", mContext.getString(R.string.phone_number_or_username), mContext.getResources().getString(R.string.error_is_required));
        }

        return "";
    }

    /*PASSWORD*/
    @Bindable
    public String getPassword() {
        if (password == null) {
            return "";
        }
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable({"displayError", "password"})
    public String getPasswordError() {
        if (!isDisplayError()) {
            return "";
        }

        if (getPassword().isEmpty()) {
            return mContext.getString(R.string.password) + " " + mContext.getResources().getString(R.string.error_is_required);
        }

        if (getPassword().length() < BuildConfig.MIN_PASSWORD_LENGTH) {
            return String.format("%s %s", mContext.getString(R.string.password), String.format(Locale.getDefault(), mContext.getString(R.string.error_password_length_x), BuildConfig.MIN_PASSWORD_LENGTH));
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
        Fragment fragment = ((BaseActivity) mContext).mSupportFragmentManager.findFragmentByTag(LoginFragment.class.getSimpleName());
        if (fragment != null && fragment.isAdded()) {
            LoginFragment loginFragment = (LoginFragment) fragment;
            if (!getUsernameError().isEmpty()) {
                loginFragment.mBinding.usernameEt.requestFocus();
                return false;
            }
            if (!getPasswordError().isEmpty()) {
                loginFragment.mBinding.passwordEt.requestFocus();
                return false;
            }
            setDisplayError(false);
            return true;
        }
        return false;
    }

}