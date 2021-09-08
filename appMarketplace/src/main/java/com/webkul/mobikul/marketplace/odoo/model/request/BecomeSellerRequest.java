package com.webkul.mobikul.marketplace.odoo.model.request;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Webkul Software.
 * <p>
 * Android Studio version 3.0+
 * Java version 1.7+
 *
 * @author Webkul <support@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul.marketplace.odoo.model.request
 * @copyright 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */
public class BecomeSellerRequest {
    private static final String KEY_URL_HANDLER = "url_handler";
    private static final String KEY_COUNTRY_ID = "country_id";
    private String urlHandler;
    private String countryId;

    public BecomeSellerRequest(String urlHandler, String countryId) {
        this.urlHandler = urlHandler;
        this.countryId = countryId;
    }


    public String getUrlHandler() {
        if (urlHandler == null){
            urlHandler = "";
        }
        return urlHandler;
    }

    public String getCountryId() {
        if (countryId == null){
            countryId = "";
        }
        return countryId;
    }


    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_URL_HANDLER, getUrlHandler());
            jsonObject.put(KEY_COUNTRY_ID, getCountryId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
