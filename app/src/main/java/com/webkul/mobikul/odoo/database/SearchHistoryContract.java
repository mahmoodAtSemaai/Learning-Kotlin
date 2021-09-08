package com.webkul.mobikul.odoo.database;

import android.provider.BaseColumns;
/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */
public final class SearchHistoryContract {

    static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + SearchHistoryEntry.TABLE_NAME + " (" +
                    SearchHistoryEntry._ID + " INTEGER PRIMARY KEY," +
                    SearchHistoryEntry.COLUMN_QUERY + " TEXT NOT NULL," +
                    SearchHistoryEntry.COLUMN_INSERT_DATE + " INTEGER DEFAULT 0," +
                    "UNIQUE (" + SearchHistoryEntry.COLUMN_QUERY + ") ON CONFLICT REPLACE);";
    static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SearchHistoryEntry.TABLE_NAME;


    private SearchHistoryContract() {
    }

    /* Inner class that defines the table contents */
    public static class SearchHistoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "SEARCH_HISTORY";
        public static final String COLUMN_QUERY = "QUERY";
        public static final String COLUMN_INSERT_DATE = "INSERT_DATE";
    }

}
