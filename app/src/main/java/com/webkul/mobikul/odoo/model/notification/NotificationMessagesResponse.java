package com.webkul.mobikul.odoo.model.notification;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.model.BaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham.agarwal on 27/6/17.
 */

public class NotificationMessagesResponse extends BaseResponse {
    @SuppressWarnings("unused")
    private static final String TAG = "NotificationMessagesRes";

    @SerializedName("all_notification_messages")
    @Expose
    private List<NotificationMessageData> allNotificationMessages = null;

    protected NotificationMessagesResponse(Parcel in) {
        super(in);
    }

    public List<NotificationMessageData> getAllNotificationMessages() {
        if (allNotificationMessages == null) {
            return new ArrayList<>();
        }
        return allNotificationMessages;
    }
}
