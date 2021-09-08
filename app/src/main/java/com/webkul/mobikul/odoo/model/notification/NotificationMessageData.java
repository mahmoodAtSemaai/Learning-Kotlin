package com.webkul.mobikul.odoo.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shubham.agarwal on 27/6/17.
 */

public class NotificationMessageData {
    @SuppressWarnings("unused")
    private static final String TAG = "NotificationMessageData";

    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("subtitle")
    @Expose
    private String subtitle;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("is_read")
    @Expose
    private boolean isRead;
    @SerializedName("create_date")
    @Expose
    private String createDate;
    @SerializedName("banner")
    @Expose
    private String banner;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("period")
    @Expose
    private String period;

    public String getBody() {
        if (body == null) {
            return "";
        }
        return body;
    }

    public String getSubtitle() {
        if (subtitle == null) {
            return "";
        }

        return subtitle;
    }

    public String getName() {
        if (name == null) {
            return "";
        }

        return name;
    }

    public String getTitle() {
        if (title == null) {
            return "";
        }

        return title;
    }

    public boolean isRead() {
        return isRead;
    }

    public String getCreateDate() {
        if (createDate == null) {
            return "";
        }

        return createDate;
    }

    public String getBanner() {
        if (banner == null) {
            return "";
        }
        return banner;
    }

    public String getId() {
        if (id == null) {
            return "";
        }
        return id;
    }

    public String getIcon() {
        if (icon == null) {
            return "";
        }

        return icon;
    }

    public String getPeriod() {
        if (period == null) {
            return "";
        }

        return period;
    }

}
