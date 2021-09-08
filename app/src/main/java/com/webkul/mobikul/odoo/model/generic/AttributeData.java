package com.webkul.mobikul.odoo.model.generic;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham.agarwal on 22/5/17.
 */

public class AttributeData implements Parcelable {
    public static final Creator<AttributeData> CREATOR = new Creator<AttributeData>() {
        @Override
        public AttributeData createFromParcel(Parcel in) {
            return new AttributeData(in);
        }

        @Override
        public AttributeData[] newArray(int size) {
            return new AttributeData[size];
        }
    };
    @SuppressWarnings("unused")
    private static final String TAG = "AttributeData";
    @SerializedName("values")
    @Expose
    private List<AttributeOptionData> mAttributeOptionDatas;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("attributeId")
    @Expose
    private String attributeId;
    @SerializedName("name")
    @Expose
    private String name;

    protected AttributeData(Parcel in) {
        mAttributeOptionDatas = in.createTypedArrayList(AttributeOptionData.CREATOR);
        type = in.readString();
        attributeId = in.readString();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mAttributeOptionDatas);
        dest.writeString(type);
        dest.writeString(attributeId);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public List<AttributeOptionData> getAttributeOptionDatas() {
        if (mAttributeOptionDatas == null) {
            return new ArrayList<>();
        }
        return mAttributeOptionDatas;
    }

    public String[] getAttributeOptionNameArr() {
        String[] attrOptnNames = new String[getAttributeOptionDatas().size()];
        List<AttributeOptionData> attributeOptionDatas = getAttributeOptionDatas();
        for (int attrOptnIdx = 0; attrOptnIdx < attributeOptionDatas.size(); attrOptnIdx++) {
            attrOptnNames[attrOptnIdx] = attributeOptionDatas.get(attrOptnIdx).getName();
        }
        return attrOptnNames;
    }


    public String getType() {
        if (type == null) {
            return "";
        }

        return type;
    }

    public String getAttributeId() {
        if (attributeId == null) {
            return "";
        }

        return attributeId;
    }

    public String getName() {
        if (name == null) {
            return "";
        }

        return name;
    }
}
