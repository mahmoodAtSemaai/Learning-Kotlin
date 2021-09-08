package com.webkul.mobikul.odoo.connection;

import java.util.ArrayList;
import java.util.List;

/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */
public class ApplicationSingleton {
    private static ApplicationSingleton mInstance;
    private List<List<String>> sortData = null;
    private List<List<String>> ratingStatus = null;
    private String base64LoginHeaderStr = null;

    public static ApplicationSingleton getInstance() {
        if (mInstance == null) {
            mInstance = new ApplicationSingleton();
        }
        return mInstance;
    }

    public String getBase64LoginHeaderStr() {
        if (base64LoginHeaderStr == null) {
            return "";
        }
        return base64LoginHeaderStr;
    }

    public void setBase64LoginHeaderStr(String base64LoginHeaderStr) {
        this.base64LoginHeaderStr = base64LoginHeaderStr;
    }

    public List<List<String>> getSortData() {
        if (sortData == null) {
            return new ArrayList<>();
        }
        return sortData;
    }

    public void setSortData(List<List<String>> sortData) {
        this.sortData = sortData;
    }

    public List<List<String>> getRatingStatus() {
        if (ratingStatus == null) {
            return new ArrayList<>();
        }
        return ratingStatus;
    }

    public void setRatingStatus(List<List<String>> ratingStatus) {
        this.ratingStatus = ratingStatus;
    }
}