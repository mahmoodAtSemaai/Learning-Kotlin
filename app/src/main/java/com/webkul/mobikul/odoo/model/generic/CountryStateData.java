package com.webkul.mobikul.odoo.model.generic;

import android.content.Context;
import android.os.Parcel;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.model.BaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham.agarwal on 11/5/17.
 */

public class CountryStateData extends BaseResponse {
    @SuppressWarnings("unused")
    private static final String TAG = "CountryStateData";


    @SerializedName("countries")
    @Expose
    private List<CountryData> countries = null;

    public CountryStateData(@Nullable Parcel in) {
        super(in);
    }


    @SuppressWarnings("WeakerAccess")
    public List<CountryData> getCountries() {
        if (countries == null) {
            return new ArrayList<>();
        }

        return countries;
    }


    public List<String> getCountryNameList(Context context) {
        List<String> countryNames = new ArrayList<>();
        for (CountryData eachCountryData : getCountries()) {
            countryNames.add(eachCountryData.getName());
        }
        return countryNames;
    }

    public List<String> getStateNameList(int selectedCountryPosn) {
        List<String> stateNames = new ArrayList<>();
        for (StateData eachStateData : getCountries().get(selectedCountryPosn).getStates()) {
            stateNames.add(eachStateData.getName());
        }
        return stateNames;
    }

    public int getStatePosition(int selectedCountryPos, @Nullable String stateId) {
        if (stateId == null) {
            return 0;
        }
        List<StateData> stateDatas = getCountries().get(selectedCountryPos).getStates();
        for (int i = 0; i < stateDatas.size(); i++) {
            StateData eachStateData = stateDatas.get(i);
            if (String.valueOf(eachStateData.getId()).equals(stateId)) {
                return i;
            }
        }
        return 0;
    }

    public String getStateId(int selectedCountryPos, @Nullable String stateName) {
        if (stateName == null) {
            return "";
        }
        List<StateData> stateDatas = getCountries().get(selectedCountryPos).getStates();
        for (int i = 0; i < stateDatas.size(); i++) {
            StateData eachStateData = stateDatas.get(i);
            if (eachStateData.getName().equalsIgnoreCase(stateName)) {
                return String.valueOf(eachStateData.getId());
            }
        }
        return "";
    }

    public int getCountryPosition(@Nullable String countryId) {
        if (countryId == null) {
            return 0;
        }
        List<CountryData> countries1 = getCountries();
        for (int i = 0; i < countries1.size(); i++) {
            CountryData eachCountryData = countries1.get(i);
            if (eachCountryData.getId().equals(countryId)) {
                return i;
            }
        }
        return 0;
    }

    public int getCountryPositionFromCountryName(@Nullable String countryName) {
        if (countryName == null) {
            return 0;
        }
        List<CountryData> countries = getCountries();
        for (int i = 0; i < countries.size(); i++) {
            CountryData eachCountryData = countries.get(i);
            if (eachCountryData.getName().equalsIgnoreCase(countryName)) {
                return i;
            }
        }
        return 0;
    }

}
