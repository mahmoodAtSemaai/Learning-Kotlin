package com.webkul.mobikul.odoo.handler.order;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.webkul.mobikul.odoo.R;

/**
 * Webkul Software.
 * <p>
 * Android Studio version 3.0+
 * Java version 1.7+
 *
 * @author Webkul <support@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul.odoo.handler.order
 * @copyright 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */
public class OrderFragmentHandler {

    private Context mContext;


    public OrderFragmentHandler(Context context) {
        this.mContext = context;
    }


    public void onClickDownloadDeliveryOrder(String urlToDownload, String orderNumber) {
        Log.d("TAG", "OrderFragmentHandler onClickDownloadDeliveryOrder urlToDownload :-> " + urlToDownload);

        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {

            String reference = mContext.getString(R.string.orders_order_details) + "-" + orderNumber + ".pdf";
            Toast.makeText(mContext, mContext.getString(R.string.downloading), Toast.LENGTH_LONG).show();
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlToDownload));
            request.setDescription(mContext.getResources().getString(R.string.download_started));
            request.setTitle(reference);
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, reference);
            DownloadManager manager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
            request.setMimeType("application/pdf");
//        request.setMimeType("*/*");
            manager.enqueue(request);
        }

    }


}
