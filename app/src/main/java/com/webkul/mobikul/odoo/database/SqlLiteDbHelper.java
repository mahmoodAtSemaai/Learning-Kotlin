package com.webkul.mobikul.odoo.database;

import static com.webkul.mobikul.odoo.constant.ApplicationConstant.MAX_SEARCH_HISTORY_RESULT;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.webkul.mobikul.odoo.constant.DataBaseConstant;
import com.webkul.mobikul.odoo.data.entity.ProductEntity;
import com.webkul.mobikul.odoo.data.entity.SplashEntity;
import com.webkul.mobikul.odoo.model.catalog.CatalogProductResponse;
import com.webkul.mobikul.odoo.model.customer.account.SaveCustomerDetailResponse;
import com.webkul.mobikul.odoo.model.extra.SplashScreenResponse;
import com.webkul.mobikul.odoo.model.generic.ProductData;
import com.webkul.mobikul.odoo.model.home.HomePageResponse;
import com.webkul.mobikul.odoo.model.notification.NotificationMessagesResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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
public class SqlLiteDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MobikulOdoo.db";
    private static final String TABLENAME = "";

    @Inject
    public SqlLiteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SearchHistoryContract.SQL_CREATE_ENTRIES);
        db.execSQL(SaveData.CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SearchHistoryContract.SQL_DELETE_ENTRIES);
        db.execSQL(SaveData.SQL_DELETE_DATA);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /*
     * This method is used for inserting
     * Home data to the Database
     * */
    public long insertHomePageData(HomePageResponse homePageResponse) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DataBaseConstant.TABLE_NAME,
                null,
                DataBaseConstant.PAGETYPE + " = 'HomePage'",
                null/*new String[]{"HomePage"}*/, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() > 0 && cursor.getString(cursor.getColumnIndex(DataBaseConstant.PAGETYPE)).equalsIgnoreCase("HomePage")) {

                long id = upDateHomePageData(cursor.getInt(cursor.getColumnIndex(DataBaseConstant.ID)), homePageResponse);
                cursor.close();
                sqLiteDatabase.close();
                return id;
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DataBaseConstant.PAGEID, 0);
                contentValues.put(DataBaseConstant.PAGETYPE, "HomePage");
                Gson gson = new Gson();
                String jsonString = gson.toJson(homePageResponse);
                contentValues.put(DataBaseConstant.CONTENT, jsonString);
                long id = sqLiteDatabase.insert(DataBaseConstant.TABLE_NAME, null, contentValues);
                sqLiteDatabase.close();
                return id;
            }
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataBaseConstant.PAGEID, 0);
            contentValues.put(DataBaseConstant.PAGETYPE, "HomePage");
            contentValues.put(DataBaseConstant.CONTENT, homePageResponse.toString());
            long id = sqLiteDatabase.insert(DataBaseConstant.TABLE_NAME, null, contentValues);
            sqLiteDatabase.close();
            return id;
        }
    }

    /*
     * This method is used for update the home screen data.*/

    private long upDateHomePageData(int Id, HomePageResponse homePageResponse) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        Gson gson = new Gson();
        String jsonString = gson.toJson(homePageResponse);
        values.put(DataBaseConstant.CONTENT, jsonString);
        long id = db.update(DataBaseConstant.TABLE_NAME, values, DataBaseConstant.ID + " = ?",
                new String[]{Id + ""});
        db.close();
        // updating row
        return id;
    }

    /*
     * This method is used for fetching Home screenData from Database */
    public HomePageResponse getHomeScreenData() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DataBaseConstant.TABLE_NAME,
                null,
                DataBaseConstant.PAGETYPE + " = 'HomePage'",
                null/*new String[]{"HomePage"}*/, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            Gson gson = new Gson();
            HomePageResponse homePageResponse = gson.fromJson(cursor.getString(cursor.getColumnIndex(DataBaseConstant.CONTENT)), HomePageResponse.class);
            return homePageResponse;
//                HomePageResponse homePageResponse = (HomePageResponse) Class.forName( cursor.getString(cursor.getColumnIndex(DataBaseConstant.CONTENT)));
        }
        return null;
    }

    public List<String> getSearchHistoryList(String searchTerm) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(SearchHistoryContract.SearchHistoryEntry.TABLE_NAME,
                new String[]{SearchHistoryContract.SearchHistoryEntry.COLUMN_QUERY},
                SearchHistoryContract.SearchHistoryEntry.COLUMN_QUERY + " LIKE ?",
                new String[]{"%" + searchTerm + "%"},
                null,
                null,
                SearchHistoryContract.SearchHistoryEntry.COLUMN_INSERT_DATE + " DESC"
                , MAX_SEARCH_HISTORY_RESULT);

        List<String> searchQueries = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            searchQueries.add(cursor.getString(cursor.getColumnIndexOrThrow(SearchHistoryContract.SearchHistoryEntry.COLUMN_QUERY)));
        }
        return searchQueries;
    }

    /*
     * This method is used for inserting Notification screen data to the DataBase.*/
    public long insertNotificPageData(NotificationMessagesResponse notificationMessagesResponse) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DataBaseConstant.TABLE_NAME,
                null,
                DataBaseConstant.PAGETYPE + " = 'Notification'",
                null/*new String[]{"HomePage"}*/, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() > 0 && cursor.getString(cursor.getColumnIndex(DataBaseConstant.PAGETYPE)).equalsIgnoreCase("Notification")) {

                long id = upDateNotificPageData(cursor.getInt(cursor.getColumnIndex(DataBaseConstant.ID)), notificationMessagesResponse);
                cursor.close();
                sqLiteDatabase.close();
                return id;
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DataBaseConstant.PAGEID, 1);
                contentValues.put(DataBaseConstant.PAGETYPE, "Notification");
                Gson gson = new Gson();
                String jsonString = gson.toJson(notificationMessagesResponse);
                contentValues.put(DataBaseConstant.CONTENT, jsonString);
                long id = sqLiteDatabase.insert(DataBaseConstant.TABLE_NAME, null, contentValues);
                sqLiteDatabase.close();
                return id;
            }
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataBaseConstant.PAGEID, 1);
            contentValues.put(DataBaseConstant.PAGETYPE, "Notification");
            contentValues.put(DataBaseConstant.CONTENT, notificationMessagesResponse.toString());
            long id = sqLiteDatabase.insert(DataBaseConstant.TABLE_NAME, null, contentValues);
            sqLiteDatabase.close();
            return id;
        }


    }

    /*This method is used for updating the notification data to the Database.*/
    private long upDateNotificPageData(int Id, NotificationMessagesResponse notificationMessagesResponse) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Gson gson = new Gson();
        String jsonString = gson.toJson(notificationMessagesResponse);
        values.put(DataBaseConstant.CONTENT, jsonString);
        long id = db.update(DataBaseConstant.TABLE_NAME, values, DataBaseConstant.ID + " = ?",
                new String[]{Id + ""});
        db.close();
        // updating row
        return id;
    }

    /* Method for fetching data from DataBase*/
    public NotificationMessagesResponse getNotificScreenData() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DataBaseConstant.TABLE_NAME,
                null,
                DataBaseConstant.PAGETYPE + " = 'Notification'",
                null/*new String[]{"HomePage"}*/, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            Gson gson = new Gson();
            NotificationMessagesResponse notificationMessagesResponse = gson.fromJson(cursor.getString(cursor.getColumnIndex(DataBaseConstant.CONTENT)), NotificationMessagesResponse.class);
            return notificationMessagesResponse;
//                HomePageResponse homePageResponse = (HomePageResponse) Class.forName( cursor.getString(cursor.getColumnIndex(DataBaseConstant.CONTENT)));
        }
        return null;
    }

    /*Method used for inserting data inserting account data to the database*/
    public long insertAccountPageData(SaveCustomerDetailResponse saveCustomerDetailResponse) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DataBaseConstant.TABLE_NAME,
                null,
                DataBaseConstant.PAGETYPE + " = 'Account'",
                null/*new String[]{"HomePage"}*/, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() > 0 && cursor.getString(cursor.getColumnIndex(DataBaseConstant.PAGETYPE)).equalsIgnoreCase("Notification")) {

                long id = upDateAccountPageData(cursor.getInt(cursor.getColumnIndex(DataBaseConstant.ID)), saveCustomerDetailResponse);
                cursor.close();
                sqLiteDatabase.close();
                return id;
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DataBaseConstant.PAGEID, 2);
                contentValues.put(DataBaseConstant.PAGETYPE, "Account");
                Gson gson = new Gson();
                String jsonString = gson.toJson(saveCustomerDetailResponse);
                contentValues.put(DataBaseConstant.CONTENT, jsonString);
                long id = sqLiteDatabase.insert(DataBaseConstant.TABLE_NAME, null, contentValues);
                sqLiteDatabase.close();
                return id;
            }
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataBaseConstant.PAGEID, 1);
            contentValues.put(DataBaseConstant.PAGETYPE, "Notification");
            contentValues.put(DataBaseConstant.CONTENT, saveCustomerDetailResponse.toString());
            long id = sqLiteDatabase.insert(DataBaseConstant.TABLE_NAME, null, contentValues);
            sqLiteDatabase.close();
            return id;
        }


    }

    /*Method used for updating the Account Screen data*/
    private long upDateAccountPageData(int Id, SaveCustomerDetailResponse saveCustomerDetailResponse) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Gson gson = new Gson();
        String jsonString = gson.toJson(saveCustomerDetailResponse);
        values.put(DataBaseConstant.CONTENT, jsonString);
        long id = db.update(DataBaseConstant.TABLE_NAME, values, DataBaseConstant.ID + " = ?",
                new String[]{Id + ""});
        db.close();
        // updating row
        return id;
    }

    /*Method used for fetching AccountScreen data from DataBase.*/
    public SaveCustomerDetailResponse getAccountScreenData() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DataBaseConstant.TABLE_NAME,
                null,
                DataBaseConstant.PAGETYPE + " = 'Account'",
                null/*new String[]{"HomePage"}*/, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            Gson gson = new Gson();
            SaveCustomerDetailResponse saveCustomerDetailResponse = gson.fromJson(cursor.getString(cursor.getColumnIndex(DataBaseConstant.CONTENT)), SaveCustomerDetailResponse.class);
            return saveCustomerDetailResponse;
//                HomePageResponse homePageResponse = (HomePageResponse) Class.forName( cursor.getString(cursor.getColumnIndex(DataBaseConstant.CONTENT)));
        }
        return null;
    }


    /*
     * This method is used for inserting Notification screen data to the DataBase.*/
    public long insertProductPageData(ProductData productData) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DataBaseConstant.TABLE_NAME,
                null,
                DataBaseConstant.PAGEID + " ='" + productData.getTemplateId() + "' " + "AND " + DataBaseConstant.PAGETYPE + " = 'Product'",
                null/*new String[]{"HomePage"}*/, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() > 0 && cursor.getString(cursor.getColumnIndex(DataBaseConstant.PAGETYPE)).equalsIgnoreCase("Product")) {
                sqLiteDatabase.delete(DataBaseConstant.TABLE_NAME, DataBaseConstant.PAGEID + " ='" + productData.getTemplateId() + "' " + "AND " +
                        DataBaseConstant.PAGETYPE + " = 'Product'", null);
                /*long id = upDateProductPageData(cursor.getInt(cursor.getColumnIndex(DataBaseConstant.ID)), productData);
                cursor.close();
                sqLiteDatabase.close();
                return id;*/
            } /*else {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DataBaseConstant.PAGEID, productData.getTemplateId());
                contentValues.put(DataBaseConstant.PAGETYPE, "Product");
                Gson gson = new Gson();
                String jsonString = gson.toJson(productData);
                contentValues.put(DataBaseConstant.CONTENT, jsonString);
                long id = sqLiteDatabase.insert(DataBaseConstant.TABLE_NAME, null, contentValues);
                sqLiteDatabase.close();
                return id;
            }*/
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataBaseConstant.PAGEID, productData.getTemplateId());
            contentValues.put(DataBaseConstant.PAGETYPE, "Product");
            Gson gson = new Gson();
            String jsonString = gson.toJson(productData);
            contentValues.put(DataBaseConstant.CONTENT, jsonString);
            long id = sqLiteDatabase.insert(DataBaseConstant.TABLE_NAME, null, contentValues);
            sqLiteDatabase.close();
            return id;
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataBaseConstant.PAGEID, productData.getTemplateId());
            contentValues.put(DataBaseConstant.PAGETYPE, "Product");
            contentValues.put(DataBaseConstant.CONTENT, productData.toString());
            long id = sqLiteDatabase.insert(DataBaseConstant.TABLE_NAME, null, contentValues);
            sqLiteDatabase.close();
            return id;
        }


    }

    public long insertProductEntity(ProductEntity productEntity) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DataBaseConstant.TABLE_NAME,
                null,
                DataBaseConstant.PAGEID + " ='" + productEntity.getTemplateId() + "' " + "AND " + DataBaseConstant.PAGETYPE + " = 'ProductEntity'",
                null/*new String[]{"HomePage"}*/, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() > 0 && cursor.getString(cursor.getColumnIndex(DataBaseConstant.PAGETYPE)).equalsIgnoreCase("ProductEntity")) {
                sqLiteDatabase.delete(DataBaseConstant.TABLE_NAME, DataBaseConstant.PAGEID + " ='" + productEntity.getTemplateId() + "' " + "AND " +
                        DataBaseConstant.PAGETYPE + " = 'ProductEntity'", null);

            }
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataBaseConstant.PAGEID, productEntity.getTemplateId());
            contentValues.put(DataBaseConstant.PAGETYPE, "ProductEntity");
            Gson gson = new Gson();
            String jsonString = gson.toJson(productEntity);
            contentValues.put(DataBaseConstant.CONTENT, jsonString);
            long id = sqLiteDatabase.insert(DataBaseConstant.TABLE_NAME, null, contentValues);
            sqLiteDatabase.close();
            return id;
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataBaseConstant.PAGEID, productEntity.getTemplateId());
            contentValues.put(DataBaseConstant.PAGETYPE, "ProductEntity");
            contentValues.put(DataBaseConstant.CONTENT, productEntity.toString());
            long id = sqLiteDatabase.insert(DataBaseConstant.TABLE_NAME, null, contentValues);
            sqLiteDatabase.close();
            return id;
        }


    }


    /*This method is used for updating the notification data to the Database.*/
    private long upDateProductPageData(int Id, ProductData productData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Gson gson = new Gson();
        String jsonString = gson.toJson(productData);
        values.put(DataBaseConstant.CONTENT, jsonString);
        long id = db.update(DataBaseConstant.TABLE_NAME, values, DataBaseConstant.ID + " = ?",
                new String[]{Id + ""});
        db.close();
        // updating row
        return id;
    }

    /* Method for fetching data from DataBase*/
    public ProductData getProductScreenData(String productId) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DataBaseConstant.TABLE_NAME,
                null,
                DataBaseConstant.PAGEID + " ='" + productId + "' " + "AND " + DataBaseConstant.PAGETYPE + " = 'Product'",
                null/*new String[]{"HomePage"}*/, null, null, null, null);
        System.out.println(" CURSER VALUE ====== " + cursor.getCount());
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            Gson gson = new Gson();
            ProductData productData1 = gson.fromJson(cursor.getString(cursor.getColumnIndex(DataBaseConstant.CONTENT)), ProductData.class);
            return productData1;
//                HomePageResponse homePageResponse = (HomePageResponse) Class.forName( cursor.getString(cursor.getColumnIndex(DataBaseConstant.CONTENT)));
        }
        return null;
    }

    public long insertSplashPageData(SplashScreenResponse splashScreenResponse) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DataBaseConstant.TABLE_NAME,
                null,
                DataBaseConstant.PAGETYPE + " = 'SplashPage'",
                null/*new String[]{"HomePage"}*/, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() > 0 && cursor.getString(cursor.getColumnIndex(DataBaseConstant.PAGETYPE)).equalsIgnoreCase("SplashPage")) {

                long id = upDateSplashPageData(cursor.getInt(cursor.getColumnIndex(DataBaseConstant.ID)), splashScreenResponse);
                cursor.close();
                sqLiteDatabase.close();
                return id;
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DataBaseConstant.PAGEID, 0);
                contentValues.put(DataBaseConstant.PAGETYPE, "SplashPage");
                Gson gson = new Gson();
                String jsonString = gson.toJson(splashScreenResponse);
                contentValues.put(DataBaseConstant.CONTENT, jsonString);
                long id = sqLiteDatabase.insert(DataBaseConstant.TABLE_NAME, null, contentValues);
                sqLiteDatabase.close();
                return id;
            }
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataBaseConstant.PAGEID, 0);
            contentValues.put(DataBaseConstant.PAGETYPE, "SplashPage");
            contentValues.put(DataBaseConstant.CONTENT, splashScreenResponse.toString());
            long id = sqLiteDatabase.insert(DataBaseConstant.TABLE_NAME, null, contentValues);
            sqLiteDatabase.close();
            return id;
        }
    }

    public long insertSplashPageEntity(SplashEntity splashEntity) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DataBaseConstant.TABLE_NAME,
                null,
                DataBaseConstant.PAGETYPE + " = 'SplashPageEntity'",
                null/*new String[]{"HomePage"}*/, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() > 0 && cursor.getString(cursor.getColumnIndex(DataBaseConstant.PAGETYPE)).equalsIgnoreCase("SplashPageEntity")) {

                long id = upDateSplashPageEntity(cursor.getInt(cursor.getColumnIndex(DataBaseConstant.ID)), splashEntity);
                cursor.close();
                sqLiteDatabase.close();
                return id;
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DataBaseConstant.PAGEID, 0);
                contentValues.put(DataBaseConstant.PAGETYPE, "SplashPageEntity");
                Gson gson = new Gson();
                String jsonString = gson.toJson(splashEntity);
                contentValues.put(DataBaseConstant.CONTENT, jsonString);
                long id = sqLiteDatabase.insert(DataBaseConstant.TABLE_NAME, null, contentValues);
                sqLiteDatabase.close();
                return id;
            }
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataBaseConstant.PAGEID, 0);
            contentValues.put(DataBaseConstant.PAGETYPE, "SplashPageEntity");
            contentValues.put(DataBaseConstant.CONTENT, splashEntity.toString());
            long id = sqLiteDatabase.insert(DataBaseConstant.TABLE_NAME, null, contentValues);
            sqLiteDatabase.close();
            return id;
        }
    }

    private long upDateSplashPageData(int Id, SplashScreenResponse splashScreenResponse) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        Gson gson = new Gson();
        String jsonString = gson.toJson(splashScreenResponse);
        values.put(DataBaseConstant.CONTENT, jsonString);
        long id = db.update(DataBaseConstant.TABLE_NAME, values, DataBaseConstant.ID + " = ?",
                new String[]{Id + ""});
        db.close();
        // updating row
        return id;
    }

    private long upDateSplashPageEntity(int Id, SplashEntity splashEntity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        Gson gson = new Gson();
        String jsonString = gson.toJson(splashEntity);
        values.put(DataBaseConstant.CONTENT, jsonString);
        long id = db.update(DataBaseConstant.TABLE_NAME, values, DataBaseConstant.ID + " = ?",
                new String[]{Id + ""});
        db.close();
        // updating row
        return id;
    }

    public SplashScreenResponse getSplashScreenData() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DataBaseConstant.TABLE_NAME,
                null,
                DataBaseConstant.PAGETYPE + " = 'SplashPage'",
                null/*new String[]{"HomePage"}*/, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            Gson gson = new Gson();
            SplashScreenResponse splashScreenResponse = gson.fromJson(cursor.getString(cursor.getColumnIndex(DataBaseConstant.CONTENT)), SplashScreenResponse.class);
            return splashScreenResponse;
        }
        return null;
    }

    public SplashEntity getSplashPageEntity() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DataBaseConstant.TABLE_NAME,
                null,
                DataBaseConstant.PAGETYPE + " = 'SplashPageEntity'",
                null/*new String[]{"HomePage"}*/, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            Gson gson = new Gson();
            SplashEntity splashEntity = gson.fromJson(cursor.getString(cursor.getColumnIndex(DataBaseConstant.CONTENT)), SplashEntity.class);
            return splashEntity;
        }
        return null;
    }

    public long insertCatalogPageData(CatalogProductResponse catalogProductResponse, String requestType) {
        try {

            if (requestType.contains("\'")) {
                requestType = requestType.replaceAll("\'", "");
            }
            String PageType = "CatalogPage" + requestType;

            SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.query(DataBaseConstant.TABLE_NAME,
                    null,
                    DataBaseConstant.PAGETYPE + " = '" + PageType + "'",
                    null/*new String[]{"HomePage"}*/, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0 && cursor.getString(cursor.getColumnIndex(DataBaseConstant.PAGETYPE)).equalsIgnoreCase(PageType)) {

                    long id = upDateCatalogPageData(cursor.getInt(cursor.getColumnIndex(DataBaseConstant.ID)), catalogProductResponse);
                    cursor.close();
                    sqLiteDatabase.close();
                    return id;
                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DataBaseConstant.PAGEID, 0);
                    contentValues.put(DataBaseConstant.PAGETYPE, PageType);
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(catalogProductResponse);
                    contentValues.put(DataBaseConstant.CONTENT, jsonString);
                    long id = sqLiteDatabase.insert(DataBaseConstant.TABLE_NAME, null, contentValues);
                    sqLiteDatabase.close();
                    return id;
                }
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DataBaseConstant.PAGEID, 0);
                contentValues.put(DataBaseConstant.PAGETYPE, PageType);
                contentValues.put(DataBaseConstant.CONTENT, catalogProductResponse.toString());
                long id = sqLiteDatabase.insert(DataBaseConstant.TABLE_NAME, null, contentValues);
                sqLiteDatabase.close();
                return id;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private long upDateCatalogPageData(int Id, CatalogProductResponse catalogProductResponse) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        Gson gson = new Gson();
        String jsonString = gson.toJson(catalogProductResponse);
        values.put(DataBaseConstant.CONTENT, jsonString);
        long id = db.update(DataBaseConstant.TABLE_NAME, values, DataBaseConstant.ID + " = ?",
                new String[]{Id + ""});
        db.close();
        // updating row
        return id;
    }

    public CatalogProductResponse getCatalogProductData(String requestType) {
        try {

            if (requestType.contains("\'")) {
                requestType = requestType.replaceAll("\'", "");
            }
            String PageType = "CatalogPage" + requestType;
            SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.query(DataBaseConstant.TABLE_NAME,
                    null,
                    DataBaseConstant.PAGETYPE + " = '" + PageType + "'",
                    null/*new String[]{"HomePage"}*/, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                Gson gson = new Gson();
                CatalogProductResponse catalogProductResponse = gson.fromJson(cursor.getString(cursor.getColumnIndex(DataBaseConstant.CONTENT)), CatalogProductResponse.class);
                return catalogProductResponse;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /* *************************************************************
     * ********************* RECENT VIEW ****************************
     * *************************************************************/

    /* Method for fetching data from DataBase*/
    public ArrayList<ProductData> getRecentProductList() {
        ArrayList<ProductData> listProductData = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DataBaseConstant.TABLE_NAME,
                null,
                DataBaseConstant.PAGETYPE + " = 'Product'",
                null/*new String[]{"HomePage"}*/, null, null, DataBaseConstant.ID + " desc ", "5");

        if (cursor.moveToFirst()) {
            do {
                System.out.println(" *******  " + cursor.getString(cursor.getColumnIndex(DataBaseConstant.ID)));
                Gson gson = new Gson();
                listProductData.add(gson.fromJson(cursor.getString(cursor.getColumnIndex(DataBaseConstant.CONTENT)), ProductData.class));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return listProductData;
    }

    public void deleteAllProductData() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        sqLiteDatabase.delete(DataBaseConstant.TABLE_NAME,
                DataBaseConstant.PAGETYPE + " = 'Product'", null);
        sqLiteDatabase.close();
    }

    public void deleteAllSearchData() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        sqLiteDatabase.execSQL("delete from " + SearchHistoryContract.SearchHistoryEntry.TABLE_NAME);
    }


}