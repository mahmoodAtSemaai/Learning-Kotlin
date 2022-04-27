package com.webkul.mobikul.odoo.firebase;

import static com.webkul.mobikul.odoo.constant.ApplicationConstant.TYPE_CATEGORY;
import static com.webkul.mobikul.odoo.constant.ApplicationConstant.TYPE_CUSTOM;
import static com.webkul.mobikul.odoo.constant.ApplicationConstant.TYPE_NONE;
import static com.webkul.mobikul.odoo.constant.ApplicationConstant.TYPE_PRODUCT;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATEGORY_ID;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATEGORY_NAME;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_ID;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_NAME;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_SEARCH_DOMAIN;
import static com.webkul.mobikul.odoo.helper.CatalogHelper.CatalogProductRequestType.SEARCH_DOMAIN;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.webkit.URLUtil;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.webkul.mobikul.odoo.BuildConfig;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.CatalogProductActivity;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.constant.ApplicationConstant;
import com.webkul.mobikul.odoo.helper.CatalogHelper;
import com.webkul.mobikul.odoo.helper.OdooApplication;
import com.webkul.mobikul.odoo.model.notification.FcmAdvanceData;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Webkul Software.
 *
 * @author Webkul <support@webkul.com>
 * @package Mobikul App
 * @Category Mobikul
 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */
public class FCMMessageReceiverService extends FirebaseMessagingService {
    @SuppressWarnings("unused")
    private static final String TAG = "FCMMessageReceiverServi";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("TAG", "FCMMessageReceiverService onMessageReceived remoteMessage.getData : " + remoteMessage.getData());
        Log.d("TAG", "FCMMessageReceiverService onMessageReceived remoteMessage.getNotification : " + remoteMessage.getNotification());
        /*for safety side throwing all exception*/
        try {
            int notificationId = 0;
            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, BuildConfig.LIBRARY_PACKAGE_NAME + "id")
                    .setSmallIcon(getNotificationIcon())
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setLargeIcon(icon)
                    .setAutoCancel(true)
                    .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                    .setSound(defaultSoundUri);


            /*advance data*/
            Map<String, String> data = remoteMessage.getData();
            if (data.size() > 0) {
                Gson gson = new GsonBuilder().create();
                FcmAdvanceData fcmAdvanceData = new Gson().fromJson(gson.toJson(data), FcmAdvanceData.class);
                notificationId = fcmAdvanceData.getNotificationId();
                Intent intent = null;
                switch (fcmAdvanceData.getType()) {
                    case TYPE_CUSTOM:
                        intent = new Intent(this, CatalogProductActivity.class);
                        intent.putExtra(BUNDLE_KEY_SEARCH_DOMAIN, fcmAdvanceData.getDomain());
                        intent.putExtra(BUNDLE_KEY_CATEGORY_NAME, fcmAdvanceData.getName());
                        intent.putExtra(BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE, SEARCH_DOMAIN);
                        break;

                    case TYPE_PRODUCT:
                        intent = new Intent(this, ((OdooApplication) getApplication()).getProductActivity());
                        intent.putExtra(BUNDLE_KEY_PRODUCT_ID, fcmAdvanceData.getId());
                        intent.putExtra(BUNDLE_KEY_PRODUCT_NAME, fcmAdvanceData.getName());
                        break;

                    case TYPE_CATEGORY:
                        intent = new Intent(this, CatalogProductActivity.class);
                        intent.putExtra(BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE, CatalogHelper.CatalogProductRequestType.GENERAL_CATEGORY);
                        intent.putExtra(BUNDLE_KEY_CATEGORY_ID, fcmAdvanceData.getId());
                        intent.putExtra(BUNDLE_KEY_CATEGORY_NAME, fcmAdvanceData.getName());
                        break;
                    case TYPE_NONE:

                        break;
                }

                if (intent != null) {
                    intent.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, FCMMessageReceiverService.class.getSimpleName());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                    notificationBuilder.setContentIntent(pendingIntent);
                }


                if (URLUtil.isValidUrl(fcmAdvanceData.getImage())) {
                    try {
                        NotificationCompat.BigPictureStyle notificationBigPictureStyle = new NotificationCompat.BigPictureStyle();
                        Bitmap remote_picture = BitmapFactory.decodeStream((InputStream) new URL(fcmAdvanceData.getResizedImageUrl(this)).getContent());
                        notificationBigPictureStyle.bigPicture(remote_picture);
                        notificationBuilder.setStyle(notificationBigPictureStyle);
                    } catch (Exception e) {
                        Log.e(TAG, "onMessageReceived: " + e.getMessage());
                        Log.e(TAG, "onMessageReceived: " + fcmAdvanceData.getResizedImageUrl(this));
                    }
                }
            }

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(BuildConfig.LIBRARY_PACKAGE_NAME + "id", BuildConfig.LIBRARY_PACKAGE_NAME + "channel", NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(notificationId, notificationBuilder.build());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.ic_stat_ic_notification : R.mipmap.ic_launcher;
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        printToken();
        // Get updated InstanceID token.
        sendRegistrationToServer();
        subscribeTopics();

    }

    public static void printToken() {
        Log.d(TAG, "printToken: " + (FirebaseInstanceId.getInstance().getToken() == null ? "NO TOKEN" : FirebaseInstanceId.getInstance().getToken()));
    }

    private void sendRegistrationToServer() {
        ApiConnection.registerDeviceToken(this.getApplicationContext()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    private void subscribeTopics() {
        FirebaseMessaging pubSub = FirebaseMessaging.getInstance();
        for (String topic : ApplicationConstant.DEFAULT_GCM_TOPICS) {
            pubSub.subscribeToTopic(topic);
        }
    }
}