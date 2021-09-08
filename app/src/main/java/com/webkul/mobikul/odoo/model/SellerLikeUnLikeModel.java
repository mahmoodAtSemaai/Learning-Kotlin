package com.webkul.mobikul.odoo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SellerLikeUnLikeModel implements Parcelable {
    @SerializedName("create_date")
    @Expose
    private String createDate;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("not_helpful")
    @Expose
    private String notHelpful;
    @SerializedName("total_votes")
    @Expose
    private Integer totalVotes;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("message_is_follower")
    @Expose
    private Boolean messageIsFollower;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("helpful")
    @Expose
    private String helpful;
    @SerializedName("email")
    @Expose
    private Boolean email;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;


    protected SellerLikeUnLikeModel(Parcel in) {
        createDate = in.readString();
        if (in.readByte() == 0) {
            rating = null;
        } else {
            rating = in.readInt();
        }
        if (in.readByte() == 0) {
            notHelpful = null;
        } else {
            notHelpful = in.readString();
        }
        if (in.readByte() == 0) {
            totalVotes = null;
        } else {
            totalVotes = in.readInt();
        }
        displayName = in.readString();
        byte tmpMessageIsFollower = in.readByte();
        messageIsFollower = tmpMessageIsFollower == 0 ? null : tmpMessageIsFollower == 1;
        title = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        msg = in.readString();
        if (in.readByte() == 0) {
            helpful = null;
        } else {
            helpful = in.readString();
        }
        byte tmpEmail = in.readByte();
        email = tmpEmail == 0 ? null : tmpEmail == 1;
        name = in.readString();
        image = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(createDate);
        if (rating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(rating);
        }
        if (notHelpful == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeString(notHelpful);
        }
        if (totalVotes == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(totalVotes);
        }
        dest.writeString(displayName);
        dest.writeByte((byte) (messageIsFollower == null ? 0 : messageIsFollower ? 1 : 2));
        dest.writeString(title);
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(msg);
        if (helpful == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeString(helpful);
        }
        dest.writeByte((byte) (email == null ? 0 : email ? 1 : 2));
        dest.writeString(name);
        dest.writeString(image);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SellerLikeUnLikeModel> CREATOR = new Creator<SellerLikeUnLikeModel>() {
        @Override
        public SellerLikeUnLikeModel createFromParcel(Parcel in) {
            return new SellerLikeUnLikeModel(in);
        }

        @Override
        public SellerLikeUnLikeModel[] newArray(int size) {
            return new SellerLikeUnLikeModel[size];
        }
    };

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getNotHelpful() {
        return notHelpful;
    }

    public void setNotHelpful(String notHelpful) {
        this.notHelpful = notHelpful;
    }

    public Integer getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(Integer totalVotes) {
        this.totalVotes = totalVotes;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Boolean getMessageIsFollower() {
        return messageIsFollower;
    }

    public void setMessageIsFollower(Boolean messageIsFollower) {
        this.messageIsFollower = messageIsFollower;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getHelpful() {
        return helpful;
    }

    public void setHelpful(String helpful) {
        this.helpful = helpful;
    }

    public Boolean getEmail() {
        return email;
    }

    public void setEmail(Boolean email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
