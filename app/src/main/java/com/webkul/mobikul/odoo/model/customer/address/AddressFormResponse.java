package com.webkul.mobikul.odoo.model.customer.address;

import android.content.Context;
import androidx.databinding.Bindable;
import android.os.Parcel;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.BR;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.fragment.NewAddressFragment;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.generic.CountryStateData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

/**
 * Created by shubham.agarwal on 11/5/17.
 */

public class AddressFormResponse extends BaseResponse {
    @SuppressWarnings("unused")
    private static final String TAG = "AddressFormResponse";


    private static final String KEY_NAME = "name";
    private static final String KEY_CITY = "city";
    private static final String KEY_ZIP = "zip";
    private static final String KEY_STREET = "street";
    private static final String KEY_PHONE = "phone";
    //    private static final String KEY_COMPANY_NAME = "company_name";
    private static final String KEY_STATE_ID = "state_id";
    private static final String KEY_COUNTRY_ID = "country_id";
//    private static final String KEY_SET_AS_DEFAUT_SHIPPING_ADDRESS = "setAsDefault";

    @SerializedName(KEY_CITY)
    @Expose
    private String city;
    @SerializedName(KEY_ZIP)
    @Expose
    private String zip;
    @SerializedName(KEY_NAME)
    @Expose
    private String name;
    @SerializedName("country_id")
    @Expose
    private String countryId;
    @SerializedName(KEY_PHONE)
    @Expose
    private String phone;
    @SerializedName(KEY_STREET)
    @Expose
    private String street;
    @SerializedName(KEY_STATE_ID)
    @Expose
    private String stateId;


    private Context mContext;
    private boolean displayError;


    /**
     * Country data is added asynchronously via Rx
     */
    private CountryStateData mCountryStateData;
    private NewAddressFragment.AddressType mAddressType;

//    private boolean setAsDefaultShippingAddress;

    public AddressFormResponse(@Nullable Parcel in) {
        super(in);

    }

    /*CITY*/
    @Bindable
    public String getCity() {
        if (city == null) {
            return "";
        }
        return city;
    }

    public void setCity(String city) {
        this.city = city;
        notifyPropertyChanged(BR.city);
    }

    @Bindable({"city", "displayError"})
    public String getCityError() {
        if (!isDisplayError()) {
            return "";
        }
        if (getCity().isEmpty()) {
            return getContext().getString(R.string.error_this_is_a_required_field);
        }
        return "";
    }


    /*ZIP*/
    @Bindable
    public String getZip() {
        if (zip == null) {
            return "";
        }
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
        notifyPropertyChanged(BR.zip);
    }

    /*NAME*/
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
            return getContext().getString(R.string.error_this_is_a_required_field);
        }
        return "";
    }

    /*COUNTRY ID*/
    public String getCountryId() {
        if (countryId == null) {
            return "";
        }
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    /*PHONE*/
    @Bindable
    public String getPhone() {
        if (phone == null) {
            return "";
        }
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
    }

    @Bindable({"phone", "displayError"})
    public String getPhoneError() {
        if (!isDisplayError()) {
            return "";
        }
        if (getPhone().isEmpty()) {
            return getContext().getString(R.string.error_this_is_a_required_field);
        }
        return "";
    }


    /*Street*/
    @Bindable
    public String getStreet() {
        if (street == null) {
            return "";
        }
        return street;
    }


    public void setStreet(String street) {
        this.street = street;
        notifyPropertyChanged(BR.street);
    }

    @Bindable({"street", "displayError"})
    public String getStreetError() {
        if (!isDisplayError()) {
            return "";
        }

        if (getStreet().isEmpty()) {
            return getContext().getString(R.string.error_this_is_a_required_field);
        }
        return "";
    }

    @Bindable({"zip", "displayError"})
    public String getZipCodeError() {
        if (!isDisplayError()) {
            return "";
        }

        if (!getZip().isEmpty() && !isValidNameZipCode(getZip())) {
            return getContext().getString(R.string.error_in_zip_code);
        }
        return "";
    }


    /*company name*/


    /*STATE ID*/
    public String getStateId() {
        if (stateId == null) {
            return "";
        }
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }


    /*Context*/

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }
    /*DISPLAY ERROR*/

    @Bindable
    public boolean isDisplayError() {
        return displayError;
    }

    public void setDisplayError(boolean displayError) {
        this.displayError = displayError;
        notifyPropertyChanged(BR.displayError);
    }

    /*COU*NTRY STATE DATA*/
    public CountryStateData getCountryStateData() {
        if (mCountryStateData == null) {
            return new CountryStateData(null);
        }
        return mCountryStateData;
    }

    public void setCountryStateData(CountryStateData countryStateData) {
        mCountryStateData = countryStateData;
    }


    /*SET AS DEFAULT SHIPPING ADDRESS*/

//    public boolean isSetAsDefaultShippingAddress() {
//        return setAsDefaultShippingAddress;
//    }

    /*USED IN DATA BINDING THUS CAN BE IGNORED*/
//    @SuppressWarnings("unused")
//    public void setSetAsDefaultShippingAddress(boolean setAsDefaultShippingAddress) {
//        this.setAsDefaultShippingAddress = setAsDefaultShippingAddress;
//    }

    /*Address Type*/

    private NewAddressFragment.AddressType getAddressType() {
        if (mAddressType == null) {
            return NewAddressFragment.AddressType.TYPE_BILLING;
        }
        return mAddressType;
    }

    public boolean isFormValidated() {

        setDisplayError(true);
        Fragment fragment = ((AppCompatActivity) mContext).getSupportFragmentManager().findFragmentByTag(NewAddressFragment.class.getSimpleName());
        if (fragment != null && fragment.isAdded()) {
            NewAddressFragment newAddressFragment = (NewAddressFragment) fragment;


            if (!getNameError().isEmpty()) {
                newAddressFragment.mBinding.nameEt.requestFocus();
                return false;
            }


            if (!getPhoneError().isEmpty()) {
                newAddressFragment.mBinding.telephoneEt.requestFocus();
                return false;
            }


            if (!getStreetError().isEmpty()) {
                newAddressFragment.mBinding.streetEt.requestFocus();
                return false;
            }


            if (!getCityError().isEmpty()) {
                newAddressFragment.mBinding.cityEt.requestFocus();
                return false;
            }
            if (!getZipCodeError().isEmpty()) {
                newAddressFragment.mBinding.zipEt.requestFocus();
                return false;
            }
            return true;
        }


        return false;
    }

    public String getNewAddressData() {
        JSONObject addressFormDataJson = new JSONObject();
        try {
            addressFormDataJson.put(KEY_NAME, getName());
            addressFormDataJson.put(KEY_PHONE, getPhone());
            addressFormDataJson.put(KEY_STREET, getStreet());
            addressFormDataJson.put(KEY_CITY, getCity());
            addressFormDataJson.put(KEY_ZIP, getZip());
            addressFormDataJson.put(KEY_COUNTRY_ID, getCountryId());
            addressFormDataJson.put(KEY_STATE_ID, getStateId());
//            if (getAddressType() != NewAddressFragment.AddressType.TYPE_BILLING && getAddressType() != NewAddressFragment.AddressType.TYPE_SHIPPING) {
//            }
//            addressFormDataJson.put(KEY_SET_AS_DEFAUT_SHIPPING_ADDRESS, isSetAsDefaultShippingAddress());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "getNewAddressData: " + addressFormDataJson.toString());
        return addressFormDataJson.toString();
    }

    public void init(Context context, CountryStateData countryStateData, NewAddressFragment.AddressType addressType) {
        mContext = context;
        mCountryStateData = countryStateData;
        mAddressType = addressType;

//        if (mAddressType == NewAddressFragment.AddressType.TYPE_SHIPPING) {
//            setSetAsDefaultShippingAddress(true);
//        }
    }

    public boolean isValidNameZipCode(String zip) {
        String nameRegex = "^[^!<>,;?=+()@#\"°{}_$%:]*$";
        return !zip.trim().isEmpty() && Pattern.compile(nameRegex).matcher(zip).matches();
    }
}
