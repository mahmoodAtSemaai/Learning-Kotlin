package com.webkul.mobikul.odoo.database;

import android.app.Activity;
import android.util.Log;

import com.webkul.mobikul.odoo.constant.DataBaseConstant;
import com.webkul.mobikul.odoo.model.catalog.CatalogProductResponse;
import com.webkul.mobikul.odoo.model.extra.SplashScreenResponse;
import com.webkul.mobikul.odoo.model.generic.ProductData;
import com.webkul.mobikul.odoo.model.home.HomePageResponse;
import com.webkul.mobikul.odoo.model.notification.NotificationMessagesResponse;
/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */
public class SaveData {
    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + DataBaseConstant.TABLE_NAME + "("
                    + DataBaseConstant.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + DataBaseConstant.PAGEID + " INTEGER,"
                    + DataBaseConstant.PAGETYPE + " TEXT,"
                    + DataBaseConstant.CONTENT + " TEXT"
                    + ")";
    static final String SQL_DELETE_DATA =
            "DROP TABLE IF EXISTS " + DataBaseConstant.TABLE_NAME;


    public SaveData(Activity context, HomePageResponse homePageResponse) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(context);
                sqlLiteDbHelper.insertHomePageData(homePageResponse);
            }
        });
    }

    public SaveData(Activity context, NotificationMessagesResponse notificationMessagesResponse) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(context);
                sqlLiteDbHelper.insertNotificPageData(notificationMessagesResponse);
            }
        });
    }


    public SaveData(Activity context, ProductData productData) {
        context.runOnUiThread(() -> {
            SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(context);
            sqlLiteDbHelper.insertProductPageData(productData);
        });
    }

    public SaveData(Activity context, SplashScreenResponse splashScreenResponse) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(context);
                sqlLiteDbHelper.insertSplashPageData(splashScreenResponse);
            }
        });
    }

    public SaveData(Activity context, CatalogProductResponse catalogProductResponse, String requestType) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(context);
                sqlLiteDbHelper.insertCatalogPageData(catalogProductResponse, requestType);
            }
        });
    }





//    public SaveData(Activity context, SaveCustomerDetailResponse saveCustomerDetailResponse) {
//        context.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                SqlLiteDbHelper sqlLiteDbHelper=new SqlLiteDbHelper(context);
//                sqlLiteDbHelper.insertAccountPageData(saveCustomerDetailResponse);
//            }
//        });
//    }
}
