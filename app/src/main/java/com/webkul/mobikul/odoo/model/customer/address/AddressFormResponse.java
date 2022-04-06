package com.webkul.mobikul.odoo.model.customer.address;

import android.content.Context;
import android.os.Parcel;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.Bindable;
import androidx.fragment.app.Fragment;

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
    public static final String KEY_PROVINCE = "province";
    public static final String KEY_DISTRICT = "district";
    public static final String KEY_SUB_DISTRICT = "sub_district";
    public static final String KEY_VILLAGE = "village";
    @SuppressWarnings("unused")
    private static final String TAG = "AddressFormResponse";
    private static final String KEY_NAME = "name";
    private static final String KEY_ZIP = "zip";
    private static final String KEY_STREET = "street";
    private static final String KEY_PHONE = "phone";
    //    private static final String KEY_COMPANY_NAME = "company_name";
    private static final String KEY_STATE_ID = "state_id";
    private static final String KEY_COUNTRY_ID = "country_id";
//    private static final String KEY_SET_AS_DEFAUT_SHIPPING_ADDRESS = "setAsDefault";
    private static final String KEY_DISTRICT_ID = "district_id";
    private static final String KEY_SUB_DISTRICT_ID = "sub_district_id";
    private static final String KEY_VILLAGE_ID = "village_id";
    private static final String KEY_CITY = "city";
    @SerializedName(KEY_ZIP)
    @Expose
    private String zip;
    @SerializedName(KEY_NAME)
    @Expose
    private String name;
    @SerializedName(KEY_COUNTRY_ID)
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
    @SerializedName(KEY_DISTRICT_ID)
    @Expose
    private String districtId;
    @SerializedName(KEY_SUB_DISTRICT_ID)
    @Expose
    private String sub_district_id;
    @SerializedName(KEY_VILLAGE_ID)
    @Expose
    private String village_id;
    @SerializedName(KEY_CITY)
    @Expose
    private String city;
    private Context mContext;
    private boolean displayError;
    /**
     * Country data is added asynchronously via Rx
     */
    private CountryStateData mCountryStateData;
    private NewAddressFragment.AddressType mAddressType;


    public AddressFormResponse(@Nullable Parcel in) {
        super(in);

    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getSub_district_id() {
        return sub_district_id;
    }

    public void setSub_district_id(String sub_district_id) {
        this.sub_district_id = sub_district_id;
    }

    public String getVillage_id() {
        return village_id;
    }

    public void setVillage_id(String village_id) {
        this.village_id = village_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public CountryStateData getmCountryStateData() {
        return mCountryStateData;
    }

    public void setmCountryStateData(CountryStateData mCountryStateData) {
        this.mCountryStateData = mCountryStateData;
    }

    public NewAddressFragment.AddressType getmAddressType() {
        return mAddressType;
    }

//    private boolean setAsDefaultShippingAddress;

    public void setmAddressType(NewAddressFragment.AddressType mAddressType) {
        this.mAddressType = mAddressType;
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
                newAddressFragment.binding.nameEt.requestFocus();
                return false;
            }


            if (!getPhoneError().isEmpty()) {
                newAddressFragment.binding.telephoneEt.requestFocus();
                return false;
            }


            if (!getStreetError().isEmpty()) {
                newAddressFragment.binding.streetEt.requestFocus();
                return false;
            }


            if (!getZipCodeError().isEmpty()) {
                newAddressFragment.binding.zipEt.requestFocus();
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
