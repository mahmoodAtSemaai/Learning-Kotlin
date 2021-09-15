package com.webkul.mobikul.odoo.model.customer.signup;

import android.content.Context;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.fragment.app.Fragment;

import com.webkul.mobikul.odoo.BR;
import com.webkul.mobikul.odoo.BuildConfig;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.fragment.SignUpFragment;

import java.util.Locale;

/**
 * Created by shubham.agarwal on 29/12/16. @Webkul Software Pvt. Ltd
 */

public class SignUpData extends BaseObservable {
    private static final String KEY_NAME = "name";
    private static final String KEY_LOGIN = "login";
    private String email;
    private String phoneNumber;
    private String name;
    private String password;
    private String confirmPassword;
    private boolean displayError;
    private Context mContext;
    private boolean isTermAndCondition = false;

    // Marketplace required fields
    private String profileURL;


    public SignUpData(Context context) {
        mContext = context;
    }

    /*USER NAME*/

    @Bindable
    public String getEmail() {
        if (email == null) {
            return "";
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable({"displayError", "email"})
    public String getEmailError() {
        if (!isDisplayError()) {
            return "";
        }
        if (getEmail().isEmpty()) {
            return String.format("%s %s", mContext.getString(R.string.email), mContext.getResources().getString(R.string.error_is_required));
        }

//        if (!Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches()) {
//            return mContext.getResources().getString(R.string.error_enter_valid_email);
//        }
        return "";
    }


    @Bindable
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Bindable({"displayError", "phoneNumber"})
    public String getPhoneNumberError() {
        if (!isDisplayError()) {
            return "";
        }
        if (getPhoneNumber().isEmpty()) {
            return String.format("%s %s", mContext.getString(R.string.phone_number), mContext.getResources().getString(R.string.error_is_required));
        }
        return "";
    }

    /*name*/
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


    @Bindable({"displayError", "name"})
    public String getNameError() {
        if (!isDisplayError()) {
            return "";
        }

        if (getName().isEmpty()) {
            return mContext.getString(R.string.your_name) + " " + mContext.getResources().getString(R.string.error_is_required);
        }

        return "";
    }

    /*password*/

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
            return String.format("%s %s", mContext.getString(R.string.password), mContext.getResources().getString(R.string.error_is_required));
        }

        if (getPassword().length() < BuildConfig.MIN_PASSWORD_LENGTH) {
            return String.format("%s %s", mContext.getString(R.string.password), String.format(Locale.getDefault(), mContext.getString(R.string.error_password_length_x), BuildConfig.MIN_PASSWORD_LENGTH));
        }

        return "";
    }

    /*confirm password*/


    @Bindable
    public String getConfirmPassword() {
        if (confirmPassword == null) {
            return "";
        }
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        notifyPropertyChanged(BR.confirmPassword);
    }

    @Bindable({"displayError", "confirmPassword"})
    public String getConfirmPasswordError() {
        if (!isDisplayError()) {
            return "";
        }

        if (getConfirmPassword().isEmpty()) {
            return String.format("%s %s", mContext.getString(R.string.confirm_password), mContext.getResources().getString(R.string.error_is_required));
        }

        if (getConfirmPassword().length() < BuildConfig.MIN_PASSWORD_LENGTH) {
            return String.format("%s %s", mContext.getString(R.string.confirm_password), String.format(Locale.getDefault(), mContext.getString(R.string.error_password_length_x), BuildConfig.MIN_PASSWORD_LENGTH));
        }

        if (!getConfirmPassword().equals(getPassword())) {
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
        Fragment fragment = ((BaseActivity) mContext).mSupportFragmentManager.findFragmentByTag(SignUpFragment.class.getSimpleName());
        if (fragment != null && fragment.isAdded()) {
            SignUpFragment signUpFragment = (SignUpFragment) fragment;
            if (!getEmailError().isEmpty()) {
                signUpFragment.mBinding.emailEt.requestFocus();
                return false;
            }
            if (!getNameError().isEmpty()) {
                signUpFragment.mBinding.nameEt.requestFocus();
                return false;
            }
            if (!getPasswordError().isEmpty()) {
                signUpFragment.mBinding.passwordEt.requestFocus();
                return false;
            }

            if (!getConfirmPasswordError().isEmpty()) {
                signUpFragment.mBinding.confirmPasswordEt.requestFocus();
                return false;
            }

            if (BuildConfig.isMarketplace) {
                if (!getProfileURLError().isEmpty() && signUpFragment.mHandler.isSeller()) {
                    signUpFragment.mBinding.profileUrlEt.requestFocus();
                    return false;
                }
            }
            setDisplayError(false);
            return true;
        }

        return false;
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
            return String.format("%s %s", mContext.getString(R.string.profile_url), mContext.getResources().getString(R.string.error_is_required));
        }
        return "";
    }


}