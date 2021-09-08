package com.webkul.mobikul.odoo.model.customer.signin;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shubham.agarwal on 11/4/17.
 */

public class FacebookGraphResponse implements Parcelable {
    public static final Creator<FacebookGraphResponse> CREATOR = new Creator<FacebookGraphResponse>() {
        @Override
        public FacebookGraphResponse createFromParcel(Parcel in) {
            return new FacebookGraphResponse(in);
        }

        @Override
        public FacebookGraphResponse[] newArray(int size) {
            return new FacebookGraphResponse[size];
        }
    };
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("picture")
    @Expose
    private FacebookPictureData picture;

    private FacebookGraphResponse(Parcel in) {
        id = in.readString();
        name = in.readString();
        email = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        picture = in.readParcelable(FacebookPictureData.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeParcelable(picture, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getId() {
        if (id == null) {
            return "";
        }
        return id;
    }

    public String getName() {
        if (name == null) {
            return "";
        }
        return name;
    }

    public String getEmail() {
        if (email == null) {
            return "";
        }
        return email;
    }

    public String getFirstName() {
        if (firstName == null) {
            return "";
        }
        return firstName;
    }

    public String getLastName() {
        if (lastName == null) {
            return "";
        }
        return lastName;
    }

    public FacebookPictureData getPicture() {
        if (picture == null) {
            return new FacebookPictureData(null);
        }
        return picture;
    }

}
